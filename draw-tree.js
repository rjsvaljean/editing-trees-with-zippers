

// ************** Generate the tree diagram	 *****************
const margin = {top: 20, right: 120, bottom: 20, left: 120};
const width = 960 - margin.right - margin.left;
const height = 500 - margin.top - margin.bottom;


var tree = d3.layout.tree()
	.size([height, width]);

var diagonal = d3.svg.diagonal()
	.projection(function(d) { return [d.y, d.x]; });

var svg = d3.select("body").append("svg")
	.attr("width", width + margin.right + margin.left)
	.attr("height", height + margin.top + margin.bottom)
    .append("g")
	.attr("transform", "translate(" + margin.left + "," + margin.top + ")");


// Compute the new tree layout.
const nodes = tree.nodes(root).reverse();
const links = tree.links(nodes);

// Normalize for fixed-depth.
nodes.forEach(function(d) {
  d.y = d.depth * 80;
  d.x = d.x * 0.7
});

// Declare the nodes…
let i = 0;
const node = svg.selectAll("g.node")
  .data(nodes, function(d) { return d.id || (d.id = ++i); });

// Enter the nodes.
const nodeEnter = node.enter().append("g")
  .attr("class", "node")
  .attr("transform", function(d) {
    return "translate(" + d.y + "," + d.x + ")";
  });

nodeEnter.append("circle")
  .attr("r", 15)
  .style("fill", "#fff");

nodeEnter.append("text")
  .attr("dy", ".35em")
  .attr("text-anchor", function(d) { return "middle"; })
  .text(function(d) { return d.name; })

const link = svg.selectAll("path.link")
  .data(links, function(d) { return d.target.id; });

link.enter().insert("path", "g")
  .attr("class", "link")
  .attr("d", diagonal);


tree.append("circle").attr("r", 100)
    .style("stroke", "#000")
    .style("fill", "#fff")
    .style("depth", "0")
    .attr("transform", "translate(" + tree.x + ", " + tree.y + ")")