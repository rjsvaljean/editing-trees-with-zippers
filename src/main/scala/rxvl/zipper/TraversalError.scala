package rxvl.zipper


sealed trait TraversalError extends Product with Serializable
case object UpOfTop extends TraversalError
case object DownOfItem extends TraversalError
case object DownOfEmpty extends TraversalError
case object LeftOfTop extends TraversalError
case object LeftOfFirst extends TraversalError
case object RightOfTop extends TraversalError
case object RightOfLast extends TraversalError
case object InsertOfTop extends TraversalError
case object DeleteOfTop extends TraversalError