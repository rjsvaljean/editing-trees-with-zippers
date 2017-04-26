package rxvl.zipper.generalize

import rxvl.zipper.{DownOfEmpty, UpOfTop, LeftOfFirst, LeftOfTop, RightOfLast, RightOfTop}


sealed trait BTree[+A] {
  def loc: Location[A] = Location(Path(Nil), this)
}
case object Leaf extends BTree[Nothing]
case class Branch[+A](a: A, l: BTree[A], r: BTree[A]) extends BTree[A]
object BTree {
  def apply[A: Ordering](a: A*): BTree[A] = {
    if (a.length >= 3) {
      val (h, t) = a.splitAt(a.length / 2)
      Branch[A](t.head, apply(h:_*), apply(t.tail:_*))
    } else if (a.length == 2) {
      val (h, t) = (a(0), a(1))
      Branch[A](t, Branch(h, Leaf, Leaf), Leaf)
    } else if (a.length == 1) {
      Branch[A](a.head, Leaf, Leaf)
    } else {
      Leaf
    }
  }
}

case class Path[+A](contextList: List[(A, Two, BTree[A])])
sealed trait Two
case object LeftB extends Two
case object RightB extends Two
case class Location[+A](path: Path[A], t: BTree[A]) {
  def goLeft = path match {
    case Path(Nil) => Left(LeftOfTop)
    case Path((_, LeftB, _) :: _) => Left(LeftOfFirst)
    case Path((r, RightB, left) :: rest) =>
      Right(Location(Path((r, LeftB, t) :: rest), left))
  }

  def goRight = path match {
    case Path(Nil) => Left(RightOfTop)
    case Path((_, RightB, _) :: _) => Left(RightOfLast)
    case Path((r, LeftB, right) :: rest) =>
      Right(Location(Path((r, RightB, t) :: rest), right))
  }

  def goUp = path match {
    case Path(Nil) => Left(UpOfTop)
    case Path((r, LeftB, right) :: rest) => Right(Location(Path(rest), Branch(r, t, right)))
    case Path((r, RightB, left) :: rest) => Right(Location(Path(rest), Branch(r, left, t)))
  }

  def goFirst = t match {
    case Leaf => Left(DownOfEmpty)
    case Branch(a, l, r) => Right(Location(Path((a, LeftB, r) :: path.contextList), l))
  }

  def goSecond = t match {
    case Leaf => Left(DownOfEmpty)
    case Branch(a, l, r) => Right(Location(Path((a, RightB, l) :: path.contextList), r))
  }

}
