package com.opsramp.janus.test;

import org.janusgraph.core.JanusGraph;
import org.janusgraph.core.schema.JanusGraphIndex;
import org.janusgraph.core.schema.JanusGraphManagement;
import org.janusgraph.core.schema.JanusGraphManagement.IndexJobFuture;
import org.janusgraph.core.schema.SchemaAction;
import org.janusgraph.core.schema.SchemaStatus;
import org.janusgraph.diskstorage.keycolumnvalue.scan.ScanMetrics;
import org.janusgraph.graphdb.database.management.ManagementSystem;

import com.opsramp.janus.core.GraphFactory;

/**
 * @author Venkata Ramu Kandulapati
 * 
 */
public class JanusDropIndex {
	
	public static void main(String... args) throws Exception {
		JanusGraph graph = GraphFactory.getInstance().getGraph();
		JanusGraphManagement graphMgmt = null;
		try {
			graphMgmt = graph.openManagement();
			JanusGraphIndex janusUniqueIndex = graphMgmt.getGraphIndex("uniqueIndex");
			graphMgmt.updateIndex(janusUniqueIndex, SchemaAction.DISABLE_INDEX).get();
			graphMgmt.commit();
			graph.tx().commit();
			
			System.out.println("Calling DISABLED Status ");
			// Block until the SchemaStatus transitions from INSTALLED to REGISTERED
			ManagementSystem.awaitGraphIndexStatus(graph, "uniqueIndex").status(SchemaStatus.DISABLED).call();
			System.out.println("Completed calling disabled status");
			
			System.out.println("Calling to Remove Index code");
			graphMgmt = graph.openManagement();
			janusUniqueIndex = graphMgmt.getGraphIndex("uniqueIndex");
			IndexJobFuture future = graphMgmt.updateIndex(janusUniqueIndex, SchemaAction.REMOVE_INDEX);
			graphMgmt.commit();
			graph.tx().commit();
			System.out.println("Completed UniqueIndex Removed ");
			
			ScanMetrics scanMetrics = future.get();
			System.out.println(scanMetrics);
			
			graphMgmt = graph.openManagement();
			janusUniqueIndex = graphMgmt.getGraphIndex("uniqueIndex");
			System.out.println("After removing uniqueIndex name ::  "+ janusUniqueIndex);
		} catch(Exception e) {
			System.out.println("Exception raised :: "+e.getMessage());
			e.printStackTrace();
			if (graphMgmt != null) {
				graphMgmt.rollback();
			}
		} finally {
			if(graph!= null)
				graph.close();
		}
		
	}

}
