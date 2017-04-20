package rxvl.zipper.memo

import rxvl.zipper.{DownOfItem, UpOfTop}

sealed trait MemoTree[+A]
case class Item[+A](item: A) extends MemoTree[A]
case class Siblings[+A](
  l: List[MemoTree[A]],
  up: MemoTree[A],
  r: List[MemoTree[A]]
) extends MemoTree[A]

sealed trait MemoPath[+A]
case object Top extends MemoPath[Nothing]
case class Node[+A](
  l: List[MemoTree[A]],
  up: MemoPath[A],
  r: List[MemoTree[A]]
) extends MemoPath[A]

case class MemoLocation[+A](t: MemoTree[A], p: MemoPath[A]) {
  def goUpMemo = p match {
    case Top => Left(UpOfTop)
    case Node(left, _p, right) =>
      Right(MemoLocation(Siblings(left, t, right), _p))
  }

  def goDownMemo = t match {
    case Item(_) => Left(DownOfItem)
    case Siblings(left, _t, right) =>
      Right(MemoLocation(_t, Node(left, p, right)))
  }
}