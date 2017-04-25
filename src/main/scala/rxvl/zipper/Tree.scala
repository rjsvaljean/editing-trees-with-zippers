package rxvl.zipper

import fastparse.all._
import argonaut.{CodecJson, Json}
import argonaut.Argonaut.ToJsonIdentity

sealed trait Tree[+A] {
  def fold[B](l: A => B, n: List[B] => B): B =
    this match {
      case Item(i) => l(i)
      case Section(children) => n(children.map(i => i.fold(l, n)))
    }

  def rootLoc: Location[A] = Location(this, Top)

  override def toString: String = "\n" + Tree.show(this)
}

case class Item[A](item: A) extends Tree[A]

case class Section[A](children: List[Tree[A]]) extends Tree[A]

object Tree {

  def show(t: Tree[Any]): String = {
    val lines: Tree[Any] => List[String] =
      _.fold[List[String]](
        i => List(i.toString),
        children => children.flatMap(i => "`-- " :: i.map("|   " + _))
      )
    lines(t).mkString("\n")
  }

  private case class JSONTree(name: String, children: Seq[JSONTree])
  implicit private val treeCodec: CodecJson[JSONTree] = {
    def rec(a: JSONTree): Json = Json("name" -> a.name.asJson, "children" -> a.children.map(rec).asJson)
    CodecJson(rec, (decoder) => ???)
  }
  def toJson(t: Tree[Any]): Json = {
    t.fold[JSONTree](
      {
        case "*" => JSONTree("X", Nil)
        case "/" => JSONTree("÷", Nil)
        case a => JSONTree(a.toString, Nil)
      },
      bs => JSONTree("", bs)
    ).asJson
  }

  def parse(str: String): Tree[String] = {
    expr.parse(str).get.value
  }

  private lazy val number: P[Tree[String]] =
    P( CharIn('0'to'9').rep(1).!.map(Item[String]) )
  private lazy val parens: P[Tree[String]] =
    P( "(" ~/ addSub ~ ")" )
  private lazy val factor: P[Tree[String]] =
    P( number | parens )
  private lazy val divMul: P[Tree[String]] =
    P( factor ~ (CharIn("*/").! ~/ factor).rep ).map(eval)
  private lazy val addSub: P[Tree[String]] =
    P( divMul ~ (CharIn("+-").! ~/ divMul).rep ).map(eval)
  private lazy val expr: P[Tree[String]]   =
    P( addSub ~ End )

  private def eval(tree: (Tree[String], Seq[(String, Tree[String])])): Tree[String] = {
    val (base, ops) = tree
    ops.foldLeft(base){ case (left, (op, right)) =>
      Section(List(left, Item(op), right))
    }
  }

}