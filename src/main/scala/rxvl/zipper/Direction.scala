package rxvl.zipper

sealed trait Direction
case object Up extends Direction
case object Dn extends Direction
case object Lf extends Direction
case object Rt extends Direction
case class N(n: Int) extends Direction