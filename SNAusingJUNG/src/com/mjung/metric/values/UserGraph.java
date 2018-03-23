package com.mjung.metric.values;

import java.util.LinkedList;

import edu.uci.ics.jung.graph.Graph;

public class UserGraph {

	private Graph<MyNode, MyEdge> graph;
	private LinkedList<MyNode> nodes;

	public UserGraph(Graph<MyNode, MyEdge> graph, LinkedList<MyNode> nodes) {
		super();
		this.graph = graph;
		this.nodes = nodes;
	}

	public Graph<MyNode, MyEdge> getGraph() {
		return graph;
	}

	public void setGraph(Graph<MyNode, MyEdge> graph) {
		this.graph = graph;
	}

	public LinkedList<MyNode> getNodes() {
		return nodes;
	}

	public void setNodes(LinkedList<MyNode> nodes) {
		this.nodes = nodes;
	}

}
