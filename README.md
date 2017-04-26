# editing-trees-with-zippers
Implementation of the Huet paper on Zippers and other stuff for https://www.meetup.com/ny-scala/events/239188121/

# Index

- [Zipper Implementation](https://github.com/rjsvaljean/editing-trees-with-zippers/blob/master/src/main/scala/rxvl/zipper/Zipper.scala#L14)
- [Traversal](https://github.com/rjsvaljean/editing-trees-with-zippers/blob/master/src/main/scala/rxvl/zipper/Zipper.scala#L15-L50)
- [Mutation](https://github.com/rjsvaljean/editing-trees-with-zippers/blob/master/src/main/scala/rxvl/zipper/Zipper.scala#L61-L85)
- [Memoizing traversal](https://github.com/rjsvaljean/editing-trees-with-zippers/blob/master/src/main/scala/rxvl/zipper/memo/MemoTree.scala)
- [Binary Tree Zipper from the Huet paper](https://github.com/rjsvaljean/editing-trees-with-zippers/blob/master/src/main/scala/rxvl/zipper/firstOrderTerm/BinaryTree.scala)
- [Binary Tree Zipper from the McBride Paper](https://github.com/rjsvaljean/editing-trees-with-zippers/blob/master/src/main/scala/rxvl/zipper/generalize/BTreeZipper.scala)

# Running

Want to draw a tree for some reason: `sbt 'run 1+2+3+5'`
`sbt test` just runs (ZipperSpec)[https://github.com/rjsvaljean/editing-trees-with-zippers/blob/master/src/test/scala/rxvl/zipper/ZipperSpec.scala]
