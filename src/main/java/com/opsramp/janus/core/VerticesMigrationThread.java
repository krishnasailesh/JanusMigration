package com.opsramp.janus.core;

import java.util.Date;
import java.util.List;

import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.janusgraph.core.JanusGraph;
import org.janusgraph.core.JanusGraphTransaction;
import org.janusgraph.core.SchemaViolationException;

public class VerticesMigrationThread implements Runnable {
	
	private JanusGraph graph;
	private long clientId;
	public VerticesMigrationThread(JanusGraph graph,long clientId)
	{	
		this.graph=graph;
		this.clientId=clientId;
		
	}
	public static void processVertices(JanusGraph graph, long clientId) throws Exception{
		JanusGraphTransaction tx = graph.newTransaction();
		try {
			SourceType[] sourceTypes = SourceType.values();
			GraphTraversalSource g = tx.traversal();		
			if(clientId > 0 ) {
				/*for(SourceType st : sourceTypes) {
					g.V().has("tenantId", clientId).has("sourceType", st.name()).property("sourceType","RESOURCE").iterate();
				}*/
				System.out.println("process vertex started");
				
				long startTime = new Date().getTime();				
				
				/*List<Vertex> list = g.V().has("tenantId", clientId).range(0,300).toList();
				for(Vertex v : list) {
					g.V(v.id()).has("sourceType", "RESOURCE").property("sourceType", "LOAD_BALANCER").iterate();
				}
				tx.commit();
				graph.close();*/
				//long count = getCountOfVerticesInClient(clientId);
				long countTime =  new Date().getTime();
				
				int low = 1;
				int high = 100;
				while(true) {
					System.out.println("range loop iteration started");
					JanusGraph rangegraph = null;
					JanusGraphTransaction rangetx = null;
					GraphTraversalSource gs = null;
					try {
						System.out.println("Creating transaction in loop");
						rangegraph = GraphFactory.getInstance().getGraph();
						rangetx = rangegraph.newTransaction();
						System.out.println("New transaction created in loop");
						gs = rangetx.traversal();
						List<Vertex> list = gs.V().has("tenantId", clientId).has("sourceType").range(low, high).toList();
						if(list.isEmpty())
							break;
						for(Vertex v : list) {
							if(!(v.value("sourceType").toString().equalsIgnoreCase("Client") || 
									v.value("sourceType").toString().equalsIgnoreCase("CloudService"))) {
								try {
									gs.V(v.id()).has("sourceType", v.value("sourceType").toString()).property("sourceType", "RESOURCE").iterate();
								}
								catch (SchemaViolationException exc) {
									System.out.println("caught SchemaViolationException--- " + exc.getMessage());
								}
							}
						}
						System.out.println("Committing tx..");
						rangetx.commit();
						rangegraph.close();
						System.out.println("updation done with range:::"+ low + "----"+ high);
						low = high + 1;
						high += 100;
						
					}
					catch(Exception e) {
						System.out.println("Transaction aborted::: in range loop");
						rangetx.rollback();
					}
					finally {
						if(rangetx != null)
							rangetx.close();
						if(rangegraph != null)
							rangegraph.close();
					}
					
				}
				//System.out.println("Device Count:::::::::::::"+ count);
				System.out.println("batch size::--------"+ 100);
				System.out.println("fetched count in::--------"+ (countTime - startTime));
				System.out.println("process vertices in::--------"+ (new Date().getTime()-countTime)) ;
				
			}	
			
			System.out.println("update done successfully.");
			
			
		}
		catch (Throwable e) {
			if (tx != null) {
				tx.rollback();
			}
			throw new Exception(e.getMessage(), e);
		} finally {
			if (tx != null) {
				tx.close();
			}
		}
	}
	public void run()
	{
		try {
			processVertices(graph, clientId);
		} catch (Exception e) {
		
			e.printStackTrace();
		}
	}

}
