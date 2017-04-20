package rxvl.zipper.firstOrderTerm

import rxvl.zipper.{DownOfEmpty, UpOfTop, LeftOfFirst, LeftOfTop, RightOfLast, RightOfTop}

sealed trait BinaryTree
case object Nil extends BinaryTree
case class Cons(l: BinaryTree, r: BinaryTree) extends BinaryTree

sealed trait BinaryPath
case object Top extends BinaryPath
case class BLeft(p: BinaryPath, t: BinaryTree) extends BinaryPath
case class BRight(t: BinaryTree, p: BinaryPath) extends BinaryPath

case class BLocation(t: BinaryTree, p: BinaryPath) {
  def change(newTree: BinaryTree) = this.copy(t = newTree)

  def goLeft = p match {
    case Top => Left(LeftOfTop)
    case BLeft(father, right) => Left(LeftOfFirst)
    case BRight(left, father) =>
      Right(BLocation(left, BLeft(father, t)))
  }

  def goRight = p match {
    case Top => Left(RightOfTop)
    case BLeft(father, right) =>
      Right(BLocation(right, BRight(t, father)))
    case BRight(left, father) => Left(RightOfLast)
  }

  def goUp = p match {
    case Top => Left(UpOfTop)
    case BLeft(father, right) => Right(BLocation(Cons(t, right), father))
    case BRight(left, father) => Right(BLocation(Cons(left, t), father))
  }

  def goFirst = t match {
    case Nil => Left(DownOfEmpty)
    case Cons(l, r) => Right(BLocation(l, BLeft(p, r)))
  }

  def goSecond = t match {
    case Nil => Left(DownOfEmpty)
    case Cons(l, r) => Right(BLocation(r, BRight(l, p)))
  }
}


