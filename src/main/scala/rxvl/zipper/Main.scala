package rxvl.zipper

import java.io.FileWriter
import sys.process._

object Main {
  def main(args: Array[String]): Unit = drawTree(Tree.parse(args(0)))

  def drawTree(t: Tree[String]): Unit = {
    val writer = new FileWriter("tree-data.js")

    writer.write("const root = " + Tree.toJson(t).spaces2)
    writer.flush()
    writer.close()
    "open index.html" !
  }

  def main1(args: Array[String]): Unit = {

    println(Location.toJson(Tree.parse(args(0)).rootLoc).spaces2)
  }
}

