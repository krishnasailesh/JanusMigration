package com.opsramp.janus.util;

import java.io.File;
import java.net.URL;

/**
 * 
 * @author Murthy Chelankuri
 *
 */
public final class StringUtil {
	
	private StringUtil() {
		
	}

	public static boolean isEmpty(String str) {
		return str == null || str.trim().equals("");
	}
	
	public static String getCurrentDirectory() {
		URL location = StringUtil.class.getProtectionDomain().getCodeSource().getLocation();
		String jarPath = location.getFile();
		File file = new File(jarPath);
		return file.getParent();
	}
}