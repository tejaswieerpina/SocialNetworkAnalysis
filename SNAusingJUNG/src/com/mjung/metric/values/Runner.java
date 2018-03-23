package com.mjung.metric.values;

import java.io.File;
import java.io.FileWriter;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.collections15.Transformer;

//import com.mjung.metric.values.MetricCalculationImpl.FloydWarshall;
import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Statement;


public class Runner {

	private static LinkedList<String> distinctNodes;
	private static LinkedList<String> sourceVertices;
	private static LinkedList<String> targetVertices;
	private static LinkedList<Double> weight;
	static ArrayList<String> final_codec = new ArrayList<String>();
	static double[][] live_matrix;
	
	static int final_size=0;
	public static final int INFINITY =999 ;

	public static void main(String[] args) throws Exception {
		Class.forName("com.mysql.jdbc.Driver");
		Connection con = (Connection) DriverManager.getConnection("jdbc:mysql://localhost:3306/test", "root", "0084");
		Statement s = (Statement) con.createStatement();
		s.executeQuery("SELECT conceptname, relatednode from concepts");
		ResultSet rs = s.getResultSet();
		while (rs.next()) {
			String a = rs.getString("conceptname");
			String b = rs.getString("relatednode");
			if (!final_codec.contains(a))
				final_codec.add(rs.getString("conceptname"));
			if (!final_codec.contains(b))
				final_codec.add(rs.getString("relatednode"));
		}
		// int size_codec = codec.size();
		// int size_codec1 = codec1.size();
		System.out.println(final_codec);
		final_size = final_codec.size();
		
		int i = 0, j = 0;
		live_matrix = new double[final_size][final_size];
		i = 0;
		
		for (i = 0; i < final_size; i++) {
			for (j = 0; j < final_size; j++) {
				// exclude = check(codec.get(i-1), i);
				if (i == j) {
					live_matrix[i][j] = 0;
				} else {
					String query_string = "select frequency from concepts where conceptname='" + final_codec.get(i)
							+ "' and relatednode='" + final_codec.get(j) + "'";
					s.executeQuery(query_string);
					ResultSet rs1 = s.getResultSet();
					String frequency = "0";
					if (rs1.next()) {
						frequency = rs1.getString("frequency");
						// System.out.println(frequency);
					}
					live_matrix[i][j] = Double.parseDouble(frequency);
				}
			}
		}
		for (i = 0; i < final_size; i++) {
			for (j = 0; j < final_size; j++) {
				live_matrix[j][i] = live_matrix[i][j];
				System.out.print(live_matrix[i][j] + "\t");
			}
			System.out.println();
		}
		System.out.println("The shortest paths in graph");
		// double distancematrix[][]=Runner.live_matrix;
		  int numberofvertices=final_size;
		double[][] adjacency_matrix = new double[numberofvertices ][numberofvertices ];
      System.out.println("Enter the Weighted Matrix for the graph");
      for (int source = 0; source <numberofvertices; source++)
      {
          for (int destination = 0; destination <numberofvertices; destination++)
          {
              adjacency_matrix[source][destination] = live_matrix[source][destination];
              if (source == destination)
              {
                  adjacency_matrix[source][destination] = 0;
                  continue;
              }
              if (adjacency_matrix[source][destination] == 0)
              {
                  adjacency_matrix[source][destination] = INFINITY;
              }
          }
      }

      System.out.println("The shortest paths in graph");
      
      class FloydWarshall
  	{
  	    public double distancematrix[][]=Runner.live_matrix;
  	    public int numberofvertices=Runner.final_size;
  	    public static final int INFINITY = 999;
  	    public FloydWarshall(int numberofvertices)
  	    {
  	        distancematrix = new double[numberofvertices + 1][numberofvertices + 1];
  	        this.numberofvertices = numberofvertices;
  	    }
  	 
  	    public void floydwarshall(double adjacencymatrix[][])
  	    {
  	        for (int source = 0; source <numberofvertices; source++)
  	        {
  	            for (int destination = 0; destination <numberofvertices; destination++)
  	            {
  	                distancematrix[source][destination] = adjacencymatrix[source][destination];
  	            }
  	        }
  	 
  	        for (int intermediate = 0; intermediate <numberofvertices; intermediate++)
  	        {
  	            for (int source = 0; source < numberofvertices; source++)
  	            {
  	                for (int destination = 0; destination <numberofvertices; destination++)
  	                {
  	                    if (distancematrix[source][intermediate] + distancematrix[intermediate][destination]
  	                         < distancematrix[source][destination])
  	                        distancematrix[source][destination] = distancematrix[source][intermediate] 
  	                            + distancematrix[intermediate][destination];
  	                }
  	            }
  	        }
  	 
  	        for (int source = 0; source < numberofvertices; source++)
  	           System.out.print("\t" + source);
  	 
  	       System.out.println();
  	        for (int source = 0; source <numberofvertices; source++)
  	        {
  	            System.out.print(source + "\t");
  	            for (int destination = 0; destination <numberofvertices; destination++)
  	            {
  	                System.out.print(distancematrix[source][destination] + "\t");
  	            }
  	            System.out.println();
  	        }
  	        int reachableNodes=0;
  	        for(int i=0;i<numberofvertices;i++)
  	        {
  	        	for(int j=0;j<numberofvertices;j++)
  	        	{
  	        		if(distancematrix[i][j] >0 && distancematrix[i][j]<999)
  	        			reachableNodes++;
  	        	}
  	        }
  	        //System.out.println("reachableNodes :" +reachableNodes);
  	        double N=(numberofvertices)*(numberofvertices-1);
  	       double  Connectedness=(reachableNodes)/N;
  	       System.out.println("Connectedness " +Connectedness);
  	    }
  	}
      
  	  FloydWarshall floydwarshall = new FloydWarshall(numberofvertices);
      floydwarshall.floydwarshall(adjacency_matrix);
  	    

  
		FileWriter writer;
			File f = new File("D://output.csv");
			writer = new FileWriter(f);
		
		IMetricCalculation iMetricCalculation = new MetricCalculationImpl();
		graphData();
		UserGraph graph = iMetricCalculation.createGraph(distinctNodes,
				sourceVertices, targetVertices, weight);
		
		System.out.println("Betweenness Centrality Values:-");
		Map<String, Double> result = iMetricCalculation
				.calculateBetweennessCentrality(graph.getGraph(),
						graph.getNodes());
		Set<Entry<String, Double>> setEntry = result.entrySet();
		for (Entry<String, Double> entry : setEntry) {
			System.out.println(entry.getKey() + ": " + entry.getValue());
			CSVUtils.writeLine(writer, Arrays.asList("Betweenness Centrality", ""+entry.getKey(), ""+ entry.getValue()));
		}
		CSVUtils.writeLine(writer, Arrays.asList("\n"));
		System.out.println("\n\n\nCloseness Centrality Values:-");
		Map<String, Double> result1 = iMetricCalculation
				.calculateClosenessCentrality(graph.getGraph(),
						graph.getNodes());
		Set<Entry<String, Double>> setEntry1 = result1.entrySet();
		for (Entry<String, Double> entry : setEntry1) {
			System.out.println(entry.getKey() + ": " + entry.getValue());
			CSVUtils.writeLine(writer, Arrays.asList("Closeness Centrality", ""+entry.getKey(), ""+ entry.getValue()));
		}
		CSVUtils.writeLine(writer, Arrays.asList("\n"));
		System.out.println("\n\n\nPage Rank Values:-");
		Map<String, Double> result2 = iMetricCalculation.calculatePageRank(
				graph.getGraph(), graph.getNodes());
		Set<Entry<String, Double>> setEntry2 = result2.entrySet();
		for (Entry<String, Double> entry : setEntry2) {
			System.out.println(entry.getKey() + ": " + entry.getValue());
			CSVUtils.writeLine(writer, Arrays.asList("Page Rank ", ""+entry.getKey(), ""+ entry.getValue()));
		}
		CSVUtils.writeLine(writer, Arrays.asList("\n"));
		System.out.println("\n\n\nEigen Vector Centrality:-");
		Map<String, Double> result3 = iMetricCalculation
				.calculateEigenVectorCentrality(graph.getGraph(),
						graph.getNodes());
		Set<Entry<String, Double>> setEntry3 = result3.entrySet();
		for (Entry<String, Double> entry : setEntry3) {
			System.out.println(entry.getKey() + ": " + entry.getValue());
			CSVUtils.writeLine(writer, Arrays.asList("Eigen Vector Centrality", ""+entry.getKey(), ""+ entry.getValue()));
		}
		
		CSVUtils.writeLine(writer, Arrays.asList("\n\n"));
	//	System.out.println("Number of Concepts" +distinctNodes);
		//System.out.println("\n\n\n Subgraph: ");
		Set<Set<MyNode>> subGraphSet = iMetricCalculation
				.findDisconnectedSubgraph(graph.getGraph());
		System.out.println("\nSubgraph: "+subGraphSet.size());
		CSVUtils.writeLine(writer, Arrays.asList("Subgraphs", ""+subGraphSet.size()));
		for (Set<MyNode> set : subGraphSet) {
			System.out.println(set);
			CSVUtils.writeLine(writer, Arrays.asList("Subgraph", ""+set));
		}
		CSVUtils.writeLine(writer, Arrays.asList("\n\n"));
		
		double density = iMetricCalculation.calculateGraphDensity(graph.getGraph());
		System.out.println("\n\n\nNetwork Density: "+density);
		
		CSVUtils.writeLine(writer, Arrays.asList("Density", ""+density));
		
		Double diameter = iMetricCalculation.calculateGraphDiameter(graph.getGraph());
        System.out.println("\n\nGraph Diameter : " + diameter+"\n\n");
        CSVUtils.writeLine(writer, Arrays.asList("\nGraph Diameter", ""+diameter));
        
        CSVUtils.writeLine(writer, Arrays.asList("\n"));
        Transformer<MyNode, Double> meanDistance = iMetricCalculation.calculateGraphMeanDistance(graph.getGraph());   
        double totaldist=0;
        for(MyNode node : graph.getGraph().getVertices()){
        	System.out.println(node +" "+meanDistance.transform(node));
        	totaldist=totaldist+meanDistance.transform(node);
        	CSVUtils.writeLine(writer, Arrays.asList("Mean Distance",""+node ,""+meanDistance.transform(node)));
        }
        System.out.println("totaldist  " +totaldist);
        double meandist=0;
		meandist=(2*totaldist)/distinctNodes.size();
		System.out.println("meandist  "+meandist);
    
        Double relation = 0d; 
        for(Double freq : weight){
        	relation = relation + freq;
        }
		
        System.out.println("\n\n Number of Relations : "+relation);
        Double avgDegree = iMetricCalculation.calculateAverageDegree(graph.getGraph()); 
        System.out.println("\n\nAverage Degree : "+avgDegree);
        CSVUtils.writeLine(writer, Arrays.asList("\n\nAverage Degree : ",""+avgDegree ));
        
		writer.flush();
		writer.close();

	}

	private static void graphData() {
	/*	distinctNodes = new LinkedList<>(Arrays.asList(
				"instructional practice", "classroom", "situation", "teacher",
				"identify need", "technology implementation", "technology",
				"instructional need"));

		sourceVertices = new LinkedList<>(Arrays.asList(
				"technology implementation",
				"situation",
				"instructional practice", 
				"technology", 
				"identify need", 
				"technology", 
				"technology",
				"technology implementation"
				));
		targetVertices = new LinkedList<>(Arrays.asList(
				"technology",
				"teacher", 
				"classroom", 
				"instructional need", 
				"technology implementation", 
				"identify need",
				"instructional practice", 
				"instructional need"));
		weight = new LinkedList<>(Arrays.asList(
				8d, 
				2d,
				1d,
				4d,
				2d,
				2d,
				4d,
				4d)); */
		
		getValuesFromDatabase();
		

	}
	
	private static void getValuesFromDatabase(){
		try{
			sourceVertices = new LinkedList<>();
			targetVertices = new LinkedList<>();
			weight = new LinkedList<>();
			distinctNodes = new LinkedList<>();
			
			Class.forName("com.mysql.jdbc.Driver");
			Connection con = (Connection) DriverManager.getConnection("jdbc:mysql://localhost:3306/test", "root", "0084");
			Statement s = (Statement) con.createStatement();
			s.executeQuery("SELECT conceptname, relatednode,frequency from concepts");
			ResultSet rs = s.getResultSet();
			while (rs.next()) {
				String a = rs.getString("conceptname");
				String b = rs.getString("relatednode");
				
				if(!distinctNodes.contains(a))
					distinctNodes.add(a);
				if(!distinctNodes.contains(b))
					distinctNodes.add(b);
				
				sourceVertices.add(rs.getString("conceptname"));
				targetVertices.add(rs.getString("relatednode"));
				weight.add(Double.parseDouble( rs.getString("frequency")));
				
			}
			System.out.println("Number of Concepts: "+distinctNodes.size());
		}catch(Exception e){
			
		}
	}
}
	
