package com.opsramp.janus.core;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.janusgraph.core.JanusGraph;
import org.janusgraph.core.JanusGraphFactory;

import com.opsramp.janus.util.StringUtil;

/**
 * @author Murthy Chelankuri
 */
public class GraphFactory {

	private static GraphFactory _instance = null;

//	private static final Logger logger = LoggerFactory.getLogger(GraphFactory.class);

	private JanusGraph graph;

	private GraphFactory() {
		openGraph();
	}

	private void openGraph() {
		if (graph == null || graph.isClosed()) {
			synchronized (this) {
				if (graph == null || graph.isClosed()) {
					try {
						Properties prop1 = getProperties();
						
						//prop1.load(GraphFactory.class.getClassLoader().getResourceAsStream("db.properties"));
						Configuration conf = new PropertiesConfiguration();
						String prop[] = { "storage.backend", "storage.hostname", "storage.cassandra.keyspace:graphs",
								"storage.cassandra.astyanax.cluster-name:","ids.block-size", "storage.cassandra.astyanax.local-datacenter:",
								"storage.cassandra.read-consistency-level:LOCAL_QUORUM",
								"storage.cassandra.write-consistency-level:LOCAL_QUORUM", "cache.db-cache:false",
								"index.search.backend", "index.search.hostname", "index.search.index-name:graphs",
								"index.search.elasticsearch.client-only:true", "graph.titan-version:1.0.0"};

						for (String property : prop) {
							String tProperty = property;
							boolean hasDefault = property.contains(":");
							String defaultValue = null;
							if (hasDefault) {
								defaultValue = "";
								String ps[] = property.split(":");
								tProperty = ps[0];
								if (ps.length >= 2) {
									defaultValue = ps[1];
								}
							}
							String value = prop1.getProperty(tProperty, defaultValue);
							if (!StringUtil.isEmpty(value)) {
								conf.addProperty(tProperty, value);
							}
						}

						graph = JanusGraphFactory.open(conf);
//						logger.info("Janus graph loaded successfully.");
					} catch (Exception e) {
						throw new IllegalStateException(e);
					}
				}
			}
		}
	}

	public static GraphFactory getInstance() {
		if (_instance == null) {
			_instance = new GraphFactory();
		}
		return _instance;
	}

	public JanusGraph getGraph() {
		if(graph == null || graph.isClosed()) {
			openGraph();
		}
		return graph;
	}
	
	
	private static Properties getProperties() {
		Properties props = new Properties();
		InputStream input = null;
		try {
			String homeDir = StringUtil.getCurrentDirectory();
			File file = new File(homeDir , "db.properties");
			if (file.exists()) {
				System.out.println(String.format("Using db.properties : %s" , file.getPath()));
				input = new FileInputStream(file);
			} else {
				input = GraphFactory.class.getClassLoader().getResourceAsStream("db.properties");
				System.out.println("Using built in config.properties");
			}			
			props.load(input);
		} catch (IOException e) {
			System.out.println(e.getMessage());
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					System.out.println(e.getMessage());
				}
			}
		}
		return props;
	}
}