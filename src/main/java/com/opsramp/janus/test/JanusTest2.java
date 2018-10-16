package com.opsramp.janus.test;

import java.time.Duration;
import java.util.Set;

import org.janusgraph.core.JanusGraph;
import org.janusgraph.core.schema.JanusGraphManagement;

import com.opsramp.janus.core.GraphFactory;

/**
 * 
 * @author Murthy Chelankuri
 *
 */
public class JanusTest2 {

	public static void main(String... args) {
		JanusGraph graph = GraphFactory.getInstance().getGraph();
		JanusGraphManagement mgmt = null;
		try {
			mgmt = graph.openManagement();
			/*
			mgmt.get("cache.db-cache");
			// Prints the current config setting
			mgmt.set("cache.db-cache", true);
			// Changes option
			mgmt.get("cache.db-cache");
			// Prints 'true'
			mgmt.commit();
			*/

		
			System.out.println("Instances ---------------------");

			Set<String> openInstances = mgmt.getOpenInstances();
		
			System.out.println("Total No of Instances found " + openInstances.size());
			
			
			for (String ins : openInstances) {
				try {
					//mgmt = graph.openManagement();
					System.out.println("Instance " + ins);
					//mgmt.forceCloseInstance(ins);
					//mgmt.commit();
				} catch(Exception e) {
					
				}
			}
			System.out.println("-------------------------Completed-------------------------");
			/*
			//
			// mgmt = graph.openManagement();

			// Set<String> graphs = JanusGraphFactory.getGraphNames();
			//
			//
			// System.out.println("Graphs ---------------------");
			// for(String gra : graphs) {
			// System.out.println(gra);
			// }
*/
			//mgmt.set("storage.lock.wait-time", Duration.ofMillis(300));
			mgmt.commit();
			
			System.out.println("DONE ---------------------");
		} catch (Throwable e) {
			System.out.println("Failed ---------------------");
			e.printStackTrace();
		} finally {
			System.exit(0);
			
		}
	}
}