package com.mjung.metric.values;

public class MyEdge {
	private Double weight;
	private Integer edgeId;

	public MyEdge(Double weight, Integer edgeId) {
		super();
		this.weight = weight;
		this.edgeId = edgeId;
	}

	public Double getWeight() {
		return weight;
	}

	public void setWeight(Double weight) {
		this.weight = weight;
	}

	public Integer getEdgeId() {
		return edgeId;
	}

	public void setEdgeId(Integer edgeId) {
		this.edgeId = edgeId;
	}

	@Override
	public String toString() {
		return "Edge:" + this.edgeId;
	}

}
