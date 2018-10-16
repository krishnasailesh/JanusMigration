package com.opsramp.janus.core;

import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 
 * @author Murthy Chelankuri
 *
 */
public class GraphRequest {

	private final List<String> vertexHash = new ArrayList<String>();
	
	protected final JSONObject json;

	public GraphRequest(JSONObject json) {
		this.json = json;
	}

	public GraphRequest() {
		this.json = new JSONObject();
		this.json.put("vertices", new JSONArray());
		this.json.put("edges", new JSONArray());
		
	}
	
	public void addKey(String key){
		if(this.json!=null)
			this.json.put("key", key);
	}

	public int vertexRequestSize() {
		return json.getJSONArray("vertices").size();
	}
	
	public void addVertex(GraphId id, JSONObject properties) {
		//If a vertex is already added then ignoring it
		//to avoid unwanted calls to db for performance improvement
		if(!vertexHash.contains(id.toString())) {
			this.json.getJSONArray("vertices").add(VertexRequest.addVertex(id, properties).getJson());
			vertexHash.add(id.toString());
		}
	}

	public void removeVertex(GraphId id) {
		this.json.getJSONArray("vertices").add(VertexRequest.deleteVertex(id).getJson());
	}

	public void addEdge(GraphRelation label, GraphId src, GraphId dest) {
		this.json.getJSONArray("edges").add(EdgeRequest.addEdge(label, src, dest).getJson());
	}
	
	public void addEdge(GraphRelation label, GraphId src, GraphId dest, JSONObject properties) {
		this.json.getJSONArray("edges").add(EdgeRequest.addEdge(label, src, dest, properties).getJson());
	}

	public void removeEdge(GraphRelation label, GraphId src, GraphId dest) {
		this.json.getJSONArray("edges").add(EdgeRequest.deleteEdge(label, src, dest).getJson());
	}

	public void removeEdge(GraphRelation label, GraphId src, GraphId dest, JSONObject properties) {
		this.json.getJSONArray("edges").add(EdgeRequest.deleteEdge(label, src, dest,properties).getJson());
	}
	
	public JSONArray getVertices() {
		return this.json.getJSONArray("vertices");
	}

	public JSONArray getEdges() {
		return this.json.getJSONArray("edges");
	}

	public List<EdgeRequest> getEdgeList() {
		List<EdgeRequest> edges = new ArrayList<EdgeRequest>();
		JSONArray array = getEdges();
		for (Object json : array.toArray()) {
			edges.add(new EdgeRequest((JSONObject) json));
		}
		return edges;
	}

	public List<VertexRequest> getVertexList() {
		List<VertexRequest> vertices = new ArrayList<VertexRequest>();
		JSONArray array = getVertices();
		for (Object json : array.toArray()) {
			vertices.add(new VertexRequest((JSONObject) json));
		}
		return vertices;
	}

	public JSONObject getJson() {
		return json;
	}

	@Override
	public String toString() {
		return json.toString();
	}
}