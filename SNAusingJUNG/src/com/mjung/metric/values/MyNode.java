package com.mjung.metric.values;

public class MyNode {
	private String nodeId;

	public MyNode(String nodeId) {
		super();
		this.nodeId = nodeId;
	}

	public String getNodeId() {
		return nodeId;
	}

	public void setNodeId(String nodeId) {
		this.nodeId = nodeId;
	}

	@Override
	public String toString() {
		return "Vertex:" + this.nodeId;
	}

}
