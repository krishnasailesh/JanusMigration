package com.opsramp.janus.test;

import java.util.List;
import java.util.concurrent.CountDownLatch;

import org.janusgraph.core.JanusGraph;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.opsramp.janus.core.GraphFactory;
import com.opsramp.janus.core.LoadProperties;
import com.opsramp.janus.core.VerticesMigrationThread;
import com.opsramp.janus.util.StringUtil;
import com.opsramp.janus.util.ThreadPool;

/**
 * 
 * @author Murthy Chelankuri
 *
 */
public class JanusTest {
	private static Logger log = LoggerFactory.getLogger(JanusTest.class);

	public static void main(String... args) {
		JanusGraph graph = GraphFactory.getInstance().getGraph();
		System.setProperty("logfilename: ", StringUtil.getCurrentDirectory() + "\\logfile");
		System.out.println("CurrentDirectory : "+StringUtil.getCurrentDirectory() + "\\logfile");
		String clientIds = LoadProperties.getProperty("migration.clientids");
		List<Long> clientIdList = StringUtil.convertStringToLongList(clientIds, ",");
		log.error("Migrated ClientIds List : {} ", clientIdList);
		ThreadPool pool=ThreadPool.getInstance();
		try {
			System.out.println("Migration started for clientIds : "+clientIds);
			System.out.println("+++++++++++++++++++++++++++++++++++++++++++++");
			//calling thread pool
			CountDownLatch latch = new CountDownLatch(clientIdList.size());
			for(long clientId: clientIdList) {
				log.error("Started migration for client with clientid: {} ", clientId);
				pool.submit(new VerticesMigrationThread(graph, clientId, latch));
				log.error("Ended migration for client with clientid: {} ", clientId);
			}
			latch.await();
			log.error("Completed migration for all clients with clientids:"+clientIdList);
			System.out.println("Completed migration for all clients with clientids:"+ clientIdList);
			System.out.println("***************************************************");
			
		} catch (Throwable e) {
			log.error(" Migration Failed for clientIds ----- :: {} ", clientIds);
			System.out.println("Failed to Migrate ClientIds : "+ clientIdList+"  :: reason :"+e.getMessage());
			e.printStackTrace();
		} finally {
			System.out.println("Reached Finally in main Method");
			System.out.println("*******************************************");
			pool.shutDown();
		}
	}
	
	
}
