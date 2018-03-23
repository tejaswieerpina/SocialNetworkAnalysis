package com.mjung.metric.values;

import java.util.LinkedList;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import edu.uci.ics.jung.graph.Graph;
import org.apache.commons.collections15.Transformer;



public interface IMetricCalculation {
	
	Map<String, Double> calculateBetweennessCentrality(
			Graph<MyNode, MyEdge> graph, LinkedList<MyNode> nodes);

	Map<String, Double> calculateClosenessCentrality(
			Graph<MyNode, MyEdge> graph, LinkedList<MyNode> nodes);

	Map<String, Double> calculateEigenVectorCentrality(
			Graph<MyNode, MyEdge> graph, LinkedList<MyNode> nodes);

	Map<String, Double> calculatePageRank(Graph<MyNode, MyEdge> graph,
			LinkedList<MyNode> nodes);

	Set<Set<MyNode>> findDisconnectedSubgraph(Graph<MyNode, MyEdge> graph);

	Double calculateGraphDensity(Graph<MyNode, MyEdge> graph);
        
        Double calculateGraphDiameter(Graph<MyNode, MyEdge> graph);
        
        Transformer<MyNode, Double> calculateGraphMeanDistance(Graph<MyNode, MyEdge> graph);

	UserGraph createGraph(LinkedList<String> distinctNodes,
			LinkedList<String> sourceVertices,
			LinkedList<String> tragetVertices, LinkedList<Double> edgeWeight);
	
	Double calculateAverageDegree(Graph<MyNode, MyEdge> graph);

}
