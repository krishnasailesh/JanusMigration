package com.opsramp.janus.core;

import java.util.Date;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.janusgraph.core.JanusGraph;
import org.janusgraph.core.JanusGraphTransaction;
import org.janusgraph.core.SchemaViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class VerticesMigrationThread implements Runnable {
	private static Logger log = LoggerFactory.getLogger(VerticesMigrationThread.class);
	
	private JanusGraph graph;
	private long clientId;
	private CountDownLatch latch;
	
	public VerticesMigrationThread(JanusGraph graph,long clientId, CountDownLatch latch) {
		this.graph=graph;
		this.clientId=clientId;
		this.latch= latch;
	}
	
	public  void processVertices(JanusGraph graph, long clientId) throws Exception {
		JanusGraphTransaction tx = graph.newTransaction();
		try {
			if(clientId > 0 ) {
				long countTime =  new Date().getTime();
				int count = 0;
				int low = LoadProperties.getIntProperty("range.lowvalue", 1);
				int high = LoadProperties.getIntProperty("range.highvalue", 1000);
				int rangeValue = LoadProperties.getIntProperty("range.rangevalue", 1000);
				JanusGraphTransaction rangetx = null;
				GraphTraversalSource gs = null;
				while(true) {
					System.out.println("Range loop iteration lowValue : "+low+" , highValue : "+high+", clientId : "+clientId);
					rangetx = graph.newTransaction();
					try {
						gs = rangetx.traversal();
						List<Vertex> list = gs.V().has("tenantId", clientId).has("sourceType").range(low, high).toList();
						if(list.isEmpty()) {
							rangetx.close();
							break;
						}
						
						for(Vertex v : list) {
							String sourceTypeValue = v.value("sourceType").toString();
							if(!(sourceTypeValue.equalsIgnoreCase("CLIENT") || 
									sourceTypeValue.equalsIgnoreCase("CLOUD_SERVICE") ||
									sourceTypeValue.equalsIgnoreCase("APP") ||
									sourceTypeValue.equalsIgnoreCase("SERVICE"))) {
								try {
									System.out.println("Entered to iteration");
//									gs.V(v.id()).has("sourceType", v.value("sourceType").toString()).property("sourceType", "RESOURCE").iterate();
									v.property("entityType", "RESOURCE");
									v.property("resourceType", sourceTypeValue);
									count++;
								} catch (SchemaViolationException exc) {
									System.out.println("caught SchemaViolationException--- " + exc.getMessage());
									log.error("SchemaViolationException for clientId : "+clientId);
								}
							} else {
								try {
									gs.V(v.id()).property("entityType", sourceTypeValue);
									gs.V(v.id()).property("resourceType", sourceTypeValue);
									count++;
								} catch (SchemaViolationException exc) {
									System.out.println("caught SchemaViolationException--- " + exc.getMessage());
									log.error("SchemaViolationException for clientId : "+clientId);
								}
							}
						}
						log.error("Committing tx..");
						rangetx.commit();
						low = high + 1;
						high += rangeValue;
					} catch(Exception e) {
						log.error(" Migration Transaction aborted::: in range loop : lowValue : "+low+" high value: "+high+" ClientId: "+clientId);
						System.out.println("Migration Transaction aborted::: of clientId: "+clientId);
						rangetx.rollback();
					} finally {
						if(rangetx != null)
							rangetx.close();
					}

				}
				log.error("Batch size::--------"+ rangeValue);
				System.out.println("batch size::--------"+ rangeValue);
				log.error("Total time to process ::{} :  vertices in ::----- {} :sec, of ClientId : {} ",count, (new Date().getTime()-countTime)/1000, clientId);
				System.out.println("Total time to process ::"+count+ ":  vertices in ::----- "+ (new Date().getTime()-countTime)/1000+" : sec,  of clientId :"+clientId) ;
				
			}	
			log.error("update done successfully of CLIENTID : {}",clientId);
		} catch (Throwable e) {
			if (tx != null) {
				tx.rollback();
			}
			throw new Exception(e.getMessage(), e);
		} finally {
			if(latch != null) {
				latch.countDown();
			}
			if (tx != null) {
				tx.close();
			}
			
		}
	}
	
	public void run() {
		try {
			processVertices(graph, clientId);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
