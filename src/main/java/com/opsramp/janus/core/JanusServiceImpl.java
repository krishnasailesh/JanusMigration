package com.opsramp.janus.core;

import org.janusgraph.core.JanusGraph;
import org.janusgraph.core.JanusGraphTransaction;
import org.janusgraph.core.schema.JanusGraphManagement;

public class JanusServiceImpl {
	public static void main(String... args) {
		JanusGraph graph = GraphFactory.getInstance().getGraph();
		JanusGraphManagement mgmt = null;
		try {
			JanusGraphTransaction tx = graph.newTransaction();
			
			
			
		} catch (Throwable e) {
			System.out.println("Failed ---------------------");
			e.printStackTrace();
		}
	}
}
