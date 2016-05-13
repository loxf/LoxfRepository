/**
 * PropertiesUtil.java
 * luohj - 下午2:49:32
 * 
 */
package org.loxf.core.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @author luohj
 *
 */
public class PropertiesUtil {
	public static Properties init(String path) throws IOException {
		InputStream inputStream = null;
		ClassLoader cl = PropertiesUtil.class.getClassLoader();
		if(cl!=null){
			inputStream = cl.getResourceAsStream(path);
		}
		if(inputStream == null) {
			inputStream = cl.getResourceAsStream(path);
		}
		if(inputStream == null) {
			inputStream = ClassLoader.getSystemResourceAsStream(path);
		}
		Properties dbProps = new Properties();
		dbProps.load(inputStream);
		inputStream.close();
		return dbProps;
	}
	
	public static Properties init(InputStream inputStream) throws IOException {
		Properties dbProps = new Properties();
		dbProps.load(inputStream);
		inputStream.close();
		return dbProps;
	}
}
