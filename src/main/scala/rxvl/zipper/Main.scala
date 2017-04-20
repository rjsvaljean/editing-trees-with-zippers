package rxvl.zipper

import scala.scalajs.js
import scala.scalajs.js.JSApp
import org.scalajs.dom
import org.scalajs.dom.document
import org.singlespaced.d3js.d3
import org.singlespaced.d3js.svg.diagonalModule.{Node => dNode}
import org.singlespaced.d3js.Ops._

object Main extends JSApp{
  @scala.scalajs.js.annotation.JSExport
  def main(): Unit = {
//    val svg = d3.select("body").append("svg").attr("width", "100%").attr("height", "450px")
//    val sel = svg.selectAll("rect").data(js.Array(1))
//    sel.enter()

    val treeData = Tree.parse("1+2*3")

    val margin = Margin(20, 120, 20, 120)
    val widthTotal = 980
    val heightTotal = 500
    val width = widthTotal - margin.right - margin.left
    val height = heightTotal - margin.top - margin.bottom
    val tree = d3.layout.tree().size(js.Tuple2(height, width))
    val diagonal =
      d3.svg.diagonal().projection((d: dNode, _: Double) => js.Tuple2(d.y, d.x))
    val svg = d3.select("body").append("svg")
      .attr("width", widthTotal)
      .attr("height", heightTotal)
      .append("g")
      .attr("transform", s"translate(${margin.left},${margin.top})")
//    tree.nodes(treeData)
  }

  case class D3Tree(name: String, parent: Option[String], children: Seq[D3Tree])
//  def toD3Json[Any](t: Tree[Any]): D3Tree = {
//    def withParent(parent: Option[String], _t: Tree[Any]) = _t match {
//      case Item(item) => D3T
//      case Section(children) =>
//    }
//  }

}

case class Margin(top: Int, right: Int, bottom: Int, left: Int)
