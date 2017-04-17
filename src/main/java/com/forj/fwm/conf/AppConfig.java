package com.forj.fwm.conf;

import java.io.File;

import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.FileBasedConfiguration;
import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.builder.FileBasedConfigurationBuilder;
import org.apache.commons.configuration2.builder.fluent.Configurations;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.apache.log4j.Logger;

import com.forj.fwm.backend.Backend;
import com.forj.fwm.startup.App;

/**
 * https://commons.apache.org/proper/commons-configuration/userguide/quick_start.html
 * 
 * If something was 'optional' go and grab it. 
 * https://mvnrepository.com/artifact/org.apache.commons/commons-configuration2/2.0
 * 
 * @author jehlmann
 *
 */
public class AppConfig {
	
	private static Logger log = Logger.getLogger(AppConfig.class);
	
	public static Configuration config;
	private static Configurations configManager;
	private static FileBasedConfigurationBuilder<FileBasedConfiguration> builder;
	
	public static void init() throws ConfigurationException {
		configManager = new Configurations();
		builder = new FileBasedConfigurationBuilder<FileBasedConfiguration>(PropertiesConfiguration.class)
				.configure(configManager.getParameters().fileBased().setFile(App.appFileUtil.getAppPropFile()));
		
		config = builder.getConfiguration();		
	}
	
	public static final String WEB_CONTEXT = "webcontext";
	public static final String PROD = "prod";
	public static final String PORT = "port";
	public static final String WORLD_LOCATION = "worldlocation";
	public static final String WORLDS_LOCATION = "worldsLocation";
	public static final String START_TEST="start-test";
	
	public static void saveDefaultWorldLocation(String worldloc) throws ConfigurationException{
		log.debug("saveDefaultWorldLocation: " + worldloc);
		config.setProperty(WORLD_LOCATION, worldloc);
		config = builder.getConfiguration();
		config.setProperty(WORLD_LOCATION, worldloc);
		builder.save();
	}
	
	public static String getWorldLocation(){
		return config.getString(WORLD_LOCATION);
	}
	
	public static boolean getStartTest(){
		return config.containsKey(START_TEST);
	}

	public static boolean getProd(){
		return config.getBoolean(PROD);
	}
	
	public static String getWorldsLocation(){
		return config.getString(WORLDS_LOCATION);
	}
	
	private static String defaultFileName = "src/main/resources/app.properties";
	public static void firstInit() throws Exception{
		configManager = new Configurations();
		
		builder = new FileBasedConfigurationBuilder<FileBasedConfiguration>(PropertiesConfiguration.class)
				.configure(configManager.getParameters().fileBased().setFileName("src/main/resources/app.properties"));
		
		config = builder.getConfiguration();
	}
	
}
