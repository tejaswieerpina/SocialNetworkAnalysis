package com.mjung.metric.values;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.collections15.Transformer;

import edu.uci.ics.jung.algorithms.cluster.WeakComponentClusterer;
import edu.uci.ics.jung.algorithms.filters.KNeighborhoodFilter;
import edu.uci.ics.jung.algorithms.scoring.BetweennessCentrality;
import edu.uci.ics.jung.algorithms.scoring.ClosenessCentrality;
import edu.uci.ics.jung.algorithms.scoring.DegreeScorer;
import edu.uci.ics.jung.algorithms.scoring.EigenvectorCentrality;
import edu.uci.ics.jung.algorithms.scoring.PageRank;
import edu.uci.ics.jung.algorithms.shortestpath.DijkstraShortestPath;
import edu.uci.ics.jung.algorithms.shortestpath.Distance;
import edu.uci.ics.jung.algorithms.shortestpath.DistanceStatistics;
import edu.uci.ics.jung.algorithms.shortestpath.UnweightedShortestPath;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.UndirectedSparseGraph;
import edu.uci.ics.jung.graph.util.EdgeType;


public class MetricCalculationImpl implements IMetricCalculation {

	private final Double JUMPFACTOR = 0.15;
	private final Double TOLERANCE = 0.000001;
	private final Integer MAXITERATIONSS = 2000;

	Transformer<MyEdge, Double> transformer = new Transformer<MyEdge, Double>() {
		@Override
		public Double transform(MyEdge me) {
			return me.getWeight();
		}
	};

	@Override
	public UserGraph createGraph(LinkedList<String> distinctNodes,
			LinkedList<String> sourceVertices,
			LinkedList<String> tragetVertices, LinkedList<Double> edgeWeight) {
		Graph<MyNode, MyEdge> graph = new UndirectedSparseGraph<>();
		Map<String, MyNode> nodeMap = new HashMap<String, MyNode>();
		LinkedList<MyNode> sourceNodes = new LinkedList<>();
		LinkedList<MyNode> targetNode = new LinkedList<>();
		LinkedList<MyNode> nodesOnly = new LinkedList<>();
		String node_name = "";
		for (String node : distinctNodes) {
			node_name = node.toString();
			MyNode myNode = new MyNode(node_name);
			nodesOnly.add(myNode);
			nodeMap.put(node_name, myNode);
		}

		for (int i = 0; i < sourceVertices.size(); i++) {
			sourceNodes.add(nodeMap.get(sourceVertices.get(i)));
			targetNode.add(nodeMap.get(tragetVertices.get(i)));
		}

		for (int i = 0; i < edgeWeight.size(); i++) {

			graph.addEdge(new MyEdge(edgeWeight.get(i), i + 1),
					sourceNodes.get(i), targetNode.get(i), EdgeType.UNDIRECTED);
		}

		return new UserGraph(graph, nodesOnly);
	}

	@Override
	public Map<String, Double> calculateBetweennessCentrality(
			Graph<MyNode, MyEdge> graph, LinkedList<MyNode> nodes) {
		Map<String, Double> betweennessScoreNodes = new HashMap<>();
		BetweennessCentrality<MyNode, MyEdge> betweennessCentrality = new BetweennessCentrality<>(
				graph, transformer);
                
		for (int i = 0; i < nodes.size(); i++) {
			betweennessScoreNodes.put(nodes.get(i).getNodeId(),
					betweennessCentrality.getVertexScore(nodes.get(i)));
		}
		return betweennessScoreNodes;
	}
        
	@Override
	public Map<String, Double> calculateClosenessCentrality(
			Graph<MyNode, MyEdge> graph, LinkedList<MyNode> nodes) {
		Map<String, Double> closenessScoreNodes = new HashMap<>();
		ClosenessCentrality<MyNode, MyEdge> closenessCentrality = new ClosenessCentrality<>(
				graph, transformer);
		for (int i = 0; i < nodes.size(); i++) {
			closenessScoreNodes.put(nodes.get(i).getNodeId(),
					closenessCentrality.getVertexScore(nodes.get(i)));
		}
		return closenessScoreNodes;
	}

	@Override
	public Map<String, Double> calculateEigenVectorCentrality(
			Graph<MyNode, MyEdge> graph, LinkedList<MyNode> nodes) {
		Map<String, Double> eigenScoreNodes = new HashMap<>();
		EigenvectorCentrality<MyNode, MyEdge> eigenCentrality = new EigenvectorCentrality<>(
				graph, transformer);
		eigenCentrality.acceptDisconnectedGraph(true);
		eigenCentrality.evaluate();
		for (int i = 0; i < nodes.size(); i++) {
			eigenScoreNodes.put(nodes.get(i).getNodeId(),
					eigenCentrality.getVertexScore(nodes.get(i)));
		}
		return eigenScoreNodes;
	}

	@Override
	public Map<String, Double> calculatePageRank(Graph<MyNode, MyEdge> graph,
			LinkedList<MyNode> nodes) {
		Map<String, Double> pageRankNodes = new HashMap<>();
		PageRank<MyNode, MyEdge> pageRank = new PageRank<>(graph, JUMPFACTOR);
		pageRank.initialize();
		pageRank.setTolerance(TOLERANCE);
		pageRank.setMaxIterations(MAXITERATIONSS);
		pageRank.evaluate();
		for (MyNode myNode : nodes) {
			pageRankNodes.put(myNode.getNodeId(),
					pageRank.getVertexScore(myNode));
		}
		return pageRankNodes;
	}

	@Override
	public Set<Set<MyNode>> findDisconnectedSubgraph(Graph<MyNode, MyEdge> graph) {

		WeakComponentClusterer<MyNode, MyEdge> cluster = new WeakComponentClusterer<>();
		Set<Set<MyNode>> subGraphSet = cluster.transform(graph);
		return subGraphSet;
	}

	@Override
	public Double calculateGraphDensity(Graph<MyNode, MyEdge> graph) {
		Double actualConnections=(double) graph.getEdgeCount();
		int vertexCnt=graph.getVertexCount();
		Double potentialConnections=(double) (vertexCnt*(vertexCnt-1)/2);
		return (actualConnections/potentialConnections);
	}
	
        @Override
        public Double calculateGraphDiameter(Graph<MyNode, MyEdge> graph){
             Double diameter = DistanceStatistics.diameter(graph, new DijkstraShortestPath<MyNode, MyEdge>(graph) ,true);   
            return diameter;
        }
        
        @Override
        public Transformer<MyNode, Double> calculateGraphMeanDistance(Graph<MyNode, MyEdge> graph){
            Transformer<MyNode, Double> transformer = DistanceStatistics.averageDistances(graph, new DijkstraShortestPath<MyNode, MyEdge>(graph) );
            return transformer;
        }
        
        @Override
        public Double calculateAverageDegree(Graph<MyNode, MyEdge> graph){
            DegreeScorer<MyNode> degObj = new DegreeScorer<>(graph);
            Double degree = new Double(0);
            int count = 0;
            for(MyNode node : graph.getVertices()){
            	degree = degree + degObj.getVertexScore(node);
            	count++;
            }
            return degree/count;
        }
      
        
        
}
