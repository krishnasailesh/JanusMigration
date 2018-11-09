package com.opsramp.janus.core;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.opsramp.janus.util.StringUtil;



/**
 * @author Venkata Ramu Kandulapati
 * 
 */
public class LoadProperties {
	
	private static Logger logger = LoggerFactory.getLogger(LoadProperties.class);
	
	private static Properties props = null;

	public static Properties getProperties() {
		if (props != null) {
			return props;
		}
		InputStream input = null;
		try {
			String homeDir = StringUtil.getCurrentDirectory();
			File file = new File(homeDir, "config.properties");
			if (file.exists()) {
				if (logger.isDebugEnabled()) {
					logger.debug(String.format("Using config.properties : %s", file.getPath()));
				}
				input = new FileInputStream(file);
			} else {
				input = LoadProperties.class.getClassLoader().getResourceAsStream("config.properties");
				if (logger.isDebugEnabled()) {
					logger.debug("Using built in config.properties");
				}
			}
			props = new Properties();
			props.load(input);
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					logger.error(e.getMessage(), e);
				}
			}
		}
		return props;
	}
	
	public static int getIntProperty(String prop, int defaut) {
		if (!StringUtil.isEmpty(getProperty(prop))) {
			return Integer.valueOf(getProperty(prop));
		}
		return defaut;
	}
	
	public static String getProperty(String prop) {
		String value = LoadProperties.getProperties().getProperty(prop);
		return value != null ? value.trim() : value;
	}
}
