package com.opsramp.janus.core;

import net.sf.json.JSONObject;

public class VertexRequest {

	protected final JSONObject json;

	public VertexRequest(JSONObject json) {
		this.json = json;
	}

	private VertexRequest(final Operation ops, final GraphId graphId, final JSONObject properties) {
		this.json = new JSONObject();
		json.put("ops", ops.name());
		json.put("graphId", graphId.getJson());
		json.put("properties", properties);
	}

	public static VertexRequest addVertex(final GraphId graphId, final JSONObject properties) {
		return new VertexRequest(Operation.ADD, graphId, properties);
	}

	public static VertexRequest deleteVertex(final GraphId graphId) {
		return new VertexRequest(Operation.DELETE, graphId, null);
	}

	public Operation getOps() {
		return Operation.valueOf(json.getString("ops"));
	}

	public GraphId getGraphId() {
		return new GraphId(json.getJSONObject("graphId"));
	}

	public JSONObject getProperties() {
		return json.getJSONObject("properties");
	}
	
	public JSONObject getJson() {
		return json;
	}

	@Override
	public String toString() {
		return json.toString();
	}
}