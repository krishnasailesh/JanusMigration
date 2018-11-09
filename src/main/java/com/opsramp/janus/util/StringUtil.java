package com.opsramp.janus.util;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

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
	
	public static List<Long> convertStringToLongList(String strs,String delimit) {
		List<Long> values = new ArrayList<>();
		if(strs != null) {
			for(String str : strs.split(delimit)) {
				try {
					values.add(Long.parseLong(str.trim()));
				} catch(Throwable e) {
					//ignore
				}
			}
		}
		return values;
	}
}