package com.opsramp.janus.core;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sf.json.JSONObject;

import org.apache.tinkerpop.gremlin.structure.Edge;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.apache.tinkerpop.gremlin.structure.VertexProperty;

public final class Schema {

	public static final String ID = "id";
	public static final String SOURCE_TYPE = "sourceType";
	public static final String NAME = "name";
	public static final String PARTNER_ID = "partnerId";
	public static final String TENANT_ID = "tenantId";
	public static final String ROOT_VISIBLE = "rootVisibility";

	
	public static List<JSONObject> toEdgeJSON(List<Edge> edges) {
		List<JSONObject> list = new ArrayList<JSONObject>();
		for (Edge e : edges) {
			list.add(toJSON(e));
		}
		return list;
	}
	
	public static List<JSONObject> toJSON(List<Vertex> vertices) {
		List<JSONObject> list = new ArrayList<JSONObject>();
		for (Vertex v : vertices) {
			JSONObject json = toJSON(v);
			if(json != null) {
				list.add(json);
			}
		}
		return list;
	}

	public static int idHashCode(Vertex vertex) {
		StringBuffer buffer = new StringBuffer();
		buffer.append(vertex.property(ID).value());
		buffer.append(vertex.property(SOURCE_TYPE).value());
		return buffer.toString().hashCode();
	}
	
	public static JSONObject toJSON(Edge edge) {
		JSONObject json = new JSONObject();
		json.put("dependency", edge.label());
		return json;
	}

	public static JSONObject toJSON(Vertex vertex) {
		JSONObject json = new JSONObject();

		JSONObject key = new JSONObject();
		JSONObject properties = new JSONObject();

		Iterator<VertexProperty<Object>> vprops = vertex.properties();
		while (vprops.hasNext()) {     
			VertexProperty<Object> vprop = vprops.next();
			String vkey = vprop.key();
			Object val = vprop.value();
			if (ID.equals(vkey) || SOURCE_TYPE.equals(vkey)) { 
				key.put(vkey, val);
			} else {
				properties.put(vkey, val);
			}
		}
		json.put("key", key);
		json.put("properties", properties);
		return (key.containsKey(ID)) && key.containsKey(SOURCE_TYPE) ? json : null;
	}
	
	public static JSONObject toExternalJSON(Vertex vertex) {  
		JSONObject json = new JSONObject();

		JSONObject properties = new JSONObject();  

		Iterator<VertexProperty<Object>> vprops = vertex.properties();
		while (vprops.hasNext()) {
			VertexProperty<Object> vprop = vprops.next();
			String vkey = vprop.key();
			if (ID.equals(vkey)) { continue; }
			Object val = vprop.value();
			properties.put(vkey, val);
		}
		json.put("properties", properties);
		return json;
	}

	public static JSONObject toJSON(Vertex parent, Edge edge, Vertex child) { 
		JSONObject json = new JSONObject();

		JSONObject parentKey = new JSONObject();
		parentKey.put(ID, parent.property(ID).value());
		parentKey.put(SOURCE_TYPE, parent.property(SOURCE_TYPE).value());

		JSONObject childKey = new JSONObject();
		childKey.put(ID, child.property(ID).value());
		childKey.put(SOURCE_TYPE, child.property(SOURCE_TYPE).value());

		json.put("src", parentKey);
		json.put("dest", childKey);
		json.put("label", edge.label());
		
		if(edge.keys() != null && !edge.keys().isEmpty()) { 
			JSONObject properties = new JSONObject();
			for(String key : edge.keys()) {
				properties.put(key, edge.value(key));
			}
			json.put("props", properties);  
		}
        
		return json;
	}

	public static JSONObject toJSON(Vertex parent, String edgeLebel, Vertex child) {
		JSONObject json = new JSONObject();

		JSONObject parentKey = new JSONObject();
		parentKey.put(ID, parent.property(ID).value());
		parentKey.put(SOURCE_TYPE, parent.property(SOURCE_TYPE).value());

		JSONObject childKey = new JSONObject();
		childKey.put(ID, child.property(ID).value());
		childKey.put(SOURCE_TYPE, child.property(SOURCE_TYPE).value());

		json.put("src", parentKey);
		json.put("dest", childKey);
		json.put("label", edgeLebel);

		return json;
	}
	
	public static JSONObject toExternalJSON(Vertex parent, Edge edge, Vertex child) { 
		JSONObject json = new JSONObject();
		json.put("label", edge.label());
		
		if(edge.keys() != null && !edge.keys().isEmpty()) { 
			JSONObject properties = new JSONObject();
			for(String key : edge.keys()) {
				properties.put(key, edge.value(key));
			}
			json.put("edgeProps", properties);  
		}
		return json;
	}
}