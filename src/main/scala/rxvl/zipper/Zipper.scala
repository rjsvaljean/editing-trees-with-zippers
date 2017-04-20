package rxvl.zipper

sealed trait Path[+A]
case object Top extends Path[Nothing]
case class Node[+A](
  left: List[Tree[A]],
  path: Path[A],
  right: List[Tree[A]]
) extends Path[A]

case class Location[+A](subtree: Tree[A], path: Path[A]) {
  def goLeft: Either[TraversalError, Location[A]] = path match {
    case Top => Left(LeftOfTop)
    case Node(l :: left, up, right) =>
      Right(Location(l, Node(left, up, subtree :: right)))
    case Node(Nil, up, right) =>
      Left(LeftOfFirst)
  }

  def goRight: Either[TraversalError, Location[A]] = path match {
    case Top => Left(RightOfTop)
    case Node(left, up, r :: right) =>
      Right(Location(r, Node(subtree :: left, up, right)))
    case _ =>
      Left(RightOfLast)
  }

  def goUp: Either[TraversalError, Location[A]] = path match {
    case Top => Left(RightOfTop)
    case Node(left, up, right) =>
      Right(Location(Section(left.reverse ++ (subtree :: right)), up))
  }

  def goDown: Either[TraversalError, Location[A]] = subtree match {
    case Item(item) => Left(DownOfItem)
    case Section(t1 :: trees) => Right(Location(t1, Node(Nil, path, trees)))
    case Section(Nil) => Left(DownOfEmpty)
  }

  def go(directions: Seq[Direction]) = directions.foldLeft(Right(this): Either[TraversalError, Location[A]]) {
    case (Right(l), Up) => l.goUp
    case (Right(l), Dn) => l.goDown
    case (Right(l), Lf) => l.goLeft
    case (Right(l), Rt) => l.goRight
    case (Right(l), N(n)) => l.nth(n)
    case (e @ Left(_), _) => e
  }

  def root = {
    def go(l: Location[A]): Either[TraversalError, Location[A]]  =
      l.goUp.right.map(j => go(j).right.getOrElse(j))
    go(this).right.get.subtree
  }

  def nth(n: Int): Either[TraversalError, Location[A]] =
    toOne(n)(this.goDown)(_.right.flatMap(_.goRight))

  def change[AA >: A](newtree: Tree[AA]) = copy(subtree = newtree)

  def insertRight[AA >: A](r: Tree[AA]) = path match {
    case Top => Left(InsertOfTop)
    case Node(left, up, right) =>
      Right(Location(subtree, Node(left, up, r :: right)))
  }

  def insertLeft[AA >: A](l: Tree[AA]) = path match {
    case Top => Left(InsertOfTop)
    case Node(left, up, right) =>
      Right(Location(subtree, Node(l :: left, up, right)))
  }

  def insertDown[AA >: A](t1: Tree[AA]) = subtree match {
    case Item(item) => Left(DownOfItem)
    case Section(children) => Right(Location(t1, Node(Nil, path, children)))
  }

  def delete = path match {
    case Top => Left(DeleteOfTop)
    case Node(left, up, r :: right) => Right(Location(r, Node(left, up, right)))
    case Node(l :: left, up, Nil) => Right(Location(l, Node(left, up, Nil)))
    case Node(Nil, up, Nil) => Right(Location(Section(Nil), up))
  }

  private def toOne[B](n: Int)(start: B)(step: B => B): B = n match {
    case 1 => start
    case i => step(toOne(n - 1)(start)(step))
  }

  override def toString = "At:\n" + subtree.toString
}