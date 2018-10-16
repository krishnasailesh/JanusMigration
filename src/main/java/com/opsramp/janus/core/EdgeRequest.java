package com.opsramp.janus.core;

import net.sf.json.JSONObject;

/**
 * 
 * @author Murthy Chelankuri
 *
 */
public class EdgeRequest {
	
	protected final JSONObject json;
	
	public static EdgeRequest addEdge(GraphRelation label, GraphId src, GraphId dest) {
		return new EdgeRequest(Operation.ADD, label, src, dest);
	}
	
	public static EdgeRequest addEdge(GraphRelation label, GraphId src, GraphId dest, JSONObject properties) {
		return new EdgeRequest(Operation.ADD, label, src, dest, properties);
	}

	public static EdgeRequest deleteEdge(GraphRelation label, GraphId src, GraphId dest) {
		return new EdgeRequest(Operation.DELETE, label, src, dest);
	}
	
	public static EdgeRequest deleteEdge(GraphRelation label, GraphId src, GraphId dest, JSONObject properties) {
		return new EdgeRequest(Operation.DELETE, label, src, dest, properties);
	}
	
	public EdgeRequest(JSONObject json) {
		this.json = json;
	}

	private EdgeRequest(Operation ops, GraphRelation label, GraphId src, GraphId dest) {
		this.json = new JSONObject();
		json.put("ops", ops.name());
		json.put("label", label.name());
		json.put("src", src.getJson());
		json.put("dest", dest.getJson());
	}
	
	private EdgeRequest(Operation ops, GraphRelation label, GraphId src, GraphId dest, JSONObject properties) {
		this.json = new JSONObject();
		json.put("ops", ops.name());
		json.put("label", label.name());
		json.put("src", src.getJson());
		json.put("dest", dest.getJson());
		json.put("properties", properties);
	}

	public Operation getOps() {
		return Operation.valueOf(json.getString("ops"));
	}

	public GraphId getSrc() {
		return new GraphId(json.getJSONObject("src"));
	}

	public GraphId getDest() {
		return new GraphId(json.getJSONObject("dest"));
	}

	public GraphRelation getLabel() {
		return GraphRelation.valueOf(json.getString("label"));
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