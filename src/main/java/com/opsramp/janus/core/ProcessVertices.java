package com.opsramp.janus.core;

import java.util.Date;
import java.util.List;

import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.janusgraph.core.JanusGraph;
import org.janusgraph.core.JanusGraphTransaction;
import org.janusgraph.core.SchemaViolationException;

/**
 * @author Venkata Ramu Kandulapati
 * 
 */
public class ProcessVertices implements Runnable {
	JanusGraph rangegraph = null;
	long clientId = 0;
	int low = 0;
	int high = 0;
	
	public ProcessVertices(long clientId, int low, int high) {
		this.rangegraph = GraphFactory.getInstance().getGraph();
		this.clientId = clientId;
		this.low = low;
		this.high = high;
	}

	@Override
	public void run() {
		JanusGraphTransaction rangetx = null;
		GraphTraversalSource gs = null;
		try {
			System.out.println("Creating transaction in loop");
			rangegraph = GraphFactory.getInstance().getGraph();
			rangetx = rangegraph.newTransaction();
			gs = rangetx.traversal();
			long loadTime = new Date().getTime();
			List<Vertex> list = gs.V().has("tenantId", clientId).has("sourceType").range(low, high).toList();
			System.out.println("::: Load each iteration of load vertices :: " + (new Date().getTime()-loadTime)/1000 +" : sec");
			
			if(!(list.isEmpty())) {
				for(Vertex v : list) {
					if(!(v.value("sourceType").toString().equalsIgnoreCase("Client") || 
							v.value("sourceType").toString().equalsIgnoreCase("CloudService") ||
							v.value("sourceType").toString().equalsIgnoreCase("APP") ||
							v.value("sourceType").toString().equalsIgnoreCase("SERVICE") ||
							v.value("sourceType").toString().equalsIgnoreCase("RESOURCE"))) {
						try {
							gs.V(v.id()).has("sourceType", v.value("sourceType").toString()).property("sourceType", "RESOURCE").iterate();
						} catch (SchemaViolationException exc) {
							System.out.println("caught SchemaViolationException--- " + exc.getMessage());
						}
					}
				}
				System.out.println("Committing tx..");
				rangetx.commit();
				rangegraph.close();
				System.out.println("updation done with range:::"+ low + "----"+ high +" of clientId "+clientId);
				low = high + 1;
				high += 100;
			}
		} catch(Exception e) {
			System.out.println("Transaction aborted::: in range loop");
			rangetx.rollback();
		} finally {
			if(rangetx != null)
				rangetx.close();
			if(rangegraph != null)
				rangegraph.close();
		}

	}
		

}
