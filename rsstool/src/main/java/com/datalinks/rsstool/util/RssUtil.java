package com.datalinks.rsstool.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Logger;


public class RssUtil {

	public final static Logger LOGGER = Logger.getLogger(RssUtil.class.getName());
	static Properties prop = null;
	
	static{
		prop = new Properties();
		String propFileName = "config.properties";
		try {
			prop.load(new FileInputStream(propFileName));
		} catch (FileNotFoundException e) {
			LOGGER.severe("File "+propFileName+" not found, while initializing properties "+e.getMessage());
		} catch (IOException e) {
			LOGGER.severe("IOException for File "+propFileName+" while initializing properties "+e.getMessage());
		}
	}
	
	
	public static String getProp(String propertyName){
		return prop.getProperty(propertyName);
	}
}
