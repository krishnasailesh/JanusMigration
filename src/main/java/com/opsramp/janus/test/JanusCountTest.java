package com.opsramp.janus.test;

import java.util.Date;

import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.janusgraph.core.JanusGraph;
import org.janusgraph.core.JanusGraphTransaction;

import com.opsramp.janus.core.GraphFactory;

/**
 * @author Venkata Ramu Kandulapati
 * 
 */
public class JanusCountTest {
	public static void main(String... args) {
		JanusGraph graph = GraphFactory.getInstance().getGraph();
		int[] clientIds= {};
		
		System.out.println("eneterd into main----------------------------------------------------------");
		if(clientIds.length>0)
		{
			for(int clientId:clientIds)
			{	long startTime= new Date().getTime();
				System.out.println("----------------------------------------------------------");
				System.out.println("total count of vertices for client id"+clientId+"is   "+getVerticesCount(graph,clientId)+".....in time  "+(new Date().getTime()-startTime)/1000+"secs");
			}
		}
		else
		{	long startTime= new Date().getTime();
			System.out.println("total count of vertices : "+getVerticesCount(graph,-1)+".....in time  "+(new Date().getTime()-startTime)/1000+"secs");
		}
	}
	
	public static long getVerticesCount(JanusGraph graph,int clientId)
	{
		JanusGraphTransaction tx = graph.newTransaction();
		try {
			System.out.println("-++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++---");
			GraphTraversalSource g = tx.traversal();
			if((clientId>0))
			{
				return g.V().has("tenantId", clientId).count().next();
			}
			else
			{	System.out.println("-***********************************************---");
				return g.V().count().next();
			}
		}
		catch(Exception e)
		{	System.out.println("exceptionnnnnnnnnnnnnnnnnn");
			System.out.println("exception occured:::::::::::::::::::::::::::  "+e);
			return 0;
		}
		finally
		{		System.out.println("finalllllllllllllllllllllllllllllllllllllllllly");
			if(tx!=null)
			{  
				tx.close();
			}
		}
	}
}
