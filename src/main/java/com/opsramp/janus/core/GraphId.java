package com.opsramp.janus.core;

import net.sf.json.JSONObject;

/**
 * 
 * @author Murthy Chelankuri
 *
 */
public class GraphId {

	protected final JSONObject json;

	public GraphId(JSONObject json) {
		this.json = json;
	}

	public GraphId(long id, String sourceType) {
		this.json = new JSONObject();
		this.json.put("id", id);
		this.json.put("sourceType", sourceType);
	}
	
	public long getId() {
		return json.getLong("id");
	}

	public void setId(long id) {
		json.put("id", id);
	}

	public String getSourceType() {
		return json.getString("sourceType");
	}
	
	public JSONObject getJson() {
		return json;
	}

	@Override
	public String toString() {
		return json.toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((getSourceType() == null) ? 0 : getSourceType().hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		GraphId another = (GraphId)obj;
		return this.getId() == another.getId() && this.getSourceType().equals(another.getSourceType());
	}
}