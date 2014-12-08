function drawGraph(graph, divId) {
  console.log(graph);
  var width = 960,
      height = 500;

  var color = d3.scale.category20();

  var force = d3.layout.force()
    .charge(-1000)
    .linkDistance(100)
    .size([width, height]);

  var drag = d3.behavior.drag()
      .origin(function(d) { return d; })
      .on("dragstart", dragstarted)
      .on("drag", dragged)
      .on("dragend", dragended);

  var svg = d3.select("#"+divId).append("svg")
    .attr("width", width)
    .attr("height", height);
  var vis = svg
    .append('svg:g')
    .call(d3.behavior.zoom()
       .scaleExtent([0.1,10])
       .on("zoom", rescale)
    );

  vis.append('svg:rect')
    .attr('width', width)
    .attr('height', height)
    .style('opacity', '0.0');

  function rescale() {
    trans=d3.event.translate;
    scale=d3.event.scale;

    vis.attr("transform",
      "translate(" + trans + ")"
      + " scale(" + scale + ")");
  }
  force
      .nodes(graph.nodes)
      .links(graph.edges)
      .start();

  var link = vis.selectAll(".link")
      .data(graph.edges)
      .enter().append("line")
        .attr("class", "link")
        .style("stroke-width", function(d) { return Math.sqrt(10*d.value); })
        .style("stroke", function(d) { return color(d.group); })
        .attr("title", function(d) { return d.name; });

  var node = vis.selectAll("circle.node")
        .data(graph.nodes)
        .enter().append("g")
        .attr("class", "node")
        .call(drag);
      //CIRCLE
      node.append("svg:circle")
        .attr("r", function(d) { return 10*d.value; })
        .attr("fill", function(d) { return color(d.group); });

      //TEXT
      node.append("text")
        .text(function(d, i) { return d.name; })
        .attr("x",    function(d) { return 10*d.value + 5; })
        .attr("y",            function(d) { return 10*d.value + 5; })
        .attr("font-family",  "Bree Serif")
        .attr("font-size",    function(d) {  return  "1em"; })
        .attr("text-anchor",  function(d) { return  "beginning";});

  force.on("tick", function(e) {
    node.attr("transform", function(d, i) {
          return "translate(" + d.x + "," + d.y + ")";
      });
     link.attr("x1", function(d)   { return d.source.x; })
         .attr("y1", function(d)   { return d.source.y; })
         .attr("x2", function(d)   { return d.target.x; })
         .attr("y2", function(d)   { return d.target.y; })
  });

  force.start();

  function dragstarted(d) {
    d3.event.sourceEvent.stopPropagation();
    d3.select(this).classed("dragging", true);
    force.start();
  }

  function dragged(d) {
    d3.select(this).attr("x", d.x = d3.event.x).attr("y", d.y = d3.event.y);
  }

  function dragended(d) {
    d3.select(this).classed("dragging", false);
  }
}

function animate(divId, maxFrames) {
  // add buttons
  $('#'+divId).append('<button type="button" id="' + divId + 'prev" class="btn btn-default" style="width:50%;">&lt; Previous</span></button>')
  $('#'+divId).append('<button type="button" id="' + divId + 'next" class="btn btn-default" style="width:50%;">Next &gt;</span></button>')
  var currentSelection = 0;
  $('#'+divId + ' #frame0').removeClass('hide');

  $('#'+divId + 'prev').on('click', function () {
    if(currentSelection > 0) {
      currentSelection -= 1;
      changeFrame(currentSelection + 1, currentSelection);
    } else {
      currentSelection = maxFrames - 1;
      changeFrame(0, currentSelection);
    }
  })
  $('#'+divId + 'next').on('click', function () {
    if(currentSelection < maxFrames - 1) {
      currentSelection += 1;
      changeFrame(currentSelection - 1, currentSelection);
    } else {
      currentSelection = 0;
      changeFrame(maxFrames-1, currentSelection);
    }
  })

  function changeFrame(oldSel, newSel) {
    $('#'+divId + ' #frame' + oldSel).addClass('hide');
    $('#'+divId + ' #frame' + newSel).removeClass('hide');
  }
}