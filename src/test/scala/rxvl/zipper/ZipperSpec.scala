package rxvl.zipper

import org.specs2._
import org.specs2.matcher._

class ZipperSpec extends Specification { def is = s2"""
  Should be able to parse a Tree from a string ${tree1 and tree2}

  Should be able to move down a tree $goDown
  Should be able to move up a tree $goUp
  Should be able to move left a tree $goLeft
  Should be able to move right a tree $goRight
  Should be able to move to the nth child $goNthChild
  Should be able to move to wherever $goWherever

  Should be able to move to change subtree at a location $changeSomething

  Should be able to insert to the right $insertRight
  Should be able to insert to the left $insertLeft
  Should be able to insert below $insertDown
  Should be able to delete $delete
"""

  val tree1 = Tree.parse("1+2") should_=== Section(List(
    Item("1"),
    Item("+"),
    Item("2")))

  val aTree = Section(List(
    Item("1"),
    Item("*"),
    Section(List(
      Item("2"),
      Item("+"),
      Item("3")))))

  val tree2 = Tree.parse("1*(2+3)") should_=== aTree

  val root = Tree.parse("1*(2+3)").rootLoc
  val `at*` = Tree.parse("1*(2+3)").rootLoc.goDown.right.get.goRight.right.get

  val goDown = root.goDown should beRight(atSubtree(Item("1")))
  val goUp = `at*`.goUp should beRight(atSubtree(aTree))
  val goLeft = `at*`.goLeft should beRight(atSubtree(Item("1")))
  val goRight = `at*`.goRight should beRight(atSubtree(Tree.parse("2+3")))
  val goNthChild = root.nth(2) should beRight(atSubtree(Item("*")))
  val `at+` = root.go(Dn :: Rt :: Rt :: Dn :: Rt :: Nil)
  val goWherever = `at+` should beRight(atSubtree(Item("+")))

  val changeSomething = `at+`.right.get.change(Item("-")) should atSubtree(Item("-"))

  val insertRight = (for {
    one <- root.goDown.right
    mult <- one.goRight.right
    i1 <- mult.insertRight(Item("stuff")).right
    i2 <- i1.insertRight(Item("new")).right
  } yield i2.root) should beRight(Section(List(
    Item("1"),
    Item("*"),
    Item("new"),
    Item("stuff"),
    Section(List(
      Item("2"),
      Item("+"),
      Item("3")))))
  )

  val insertLeft = (for {
    one <- root.goDown.right
    mult <- one.goRight.right
    i1 <- mult.insertLeft(Item("new")).right
    i2 <- i1.insertLeft(Item("stuff")).right
  } yield i2.root) should beRight(Section(List(
    Item("1"),
    Item("new"),
    Item("stuff"),
    Item("*"),
    Section(List(
      Item("2"),
      Item("+"),
      Item("3")))))
  )

  val insertDown = (for {
    i1 <- root.insertDown(Item("new")).right
  } yield i1.root) should beRight(Section(List(
    Item("new"),
    Item("1"),
    Item("*"),
    Section(List(
      Item("2"),
      Item("+"),
      Item("3")))))
  )

  val delete = (for {
    one <- root.goDown.right
    d <- one.delete.right
  } yield d.root) should beRight(Section(List(
    Item("*"),
    Section(List(
      Item("2"),
      Item("+"),
      Item("3")))))
  )

  def atSubtree[A](t: Tree[A]) = {
    (_: Location[A]).subtree === t
  }
}
