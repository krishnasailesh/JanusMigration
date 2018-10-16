package com.opsramp.janus.core;

/**
 * 
 * @author Murthy Chelankuri
 *
 */
public enum GraphRelation {
	
	REL_MANAGES("manages"),REL_MANAGED_BY("managed by"),
	REL_CONTAINS("contains"),
	REL_PROVIDES("provides"),
	REL_INITIATES("initiates"),
	REL_POWERS("powers"),
	REL_POSTDATA("post data"),REL_RECEIVE_DATA("receive data"),
	REL_USES("uses"), REL_USED_BY("used by"),
	REL_HOSTS("hosts"),
	REL_CONSUMES("consumes"),
	REL_IMPACTS("impacts"),
	REL_DEFINES("defines"),
	REL_RUNS_ON("runs on"),
	REL_HOSTED_ON("hosted on"),
	REL_SUPPORTED_BY("supported by"),
	REL_DEPENDS("depends"),
	REL_COMSUMES("consumes"),
	REL_REGISTERED_ON("registered on"),
	REL_IMPACT("impacts"),
	REL_DEPS("deps"),
	REL_TOPOLOGY("topology");

	private String name;
	
	private GraphRelation(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public static GraphRelation lookup(String name) {
		for(GraphRelation rel : values()) {
			if(rel.getName().equalsIgnoreCase(name)) {
				return rel;
			}
		}
		return null;
	}
}