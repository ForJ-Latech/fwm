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
	
	private static String defaultFileName = "src/main/resources/app.properties";
	public static void firstInit() throws Exception{
		configManager = new Configurations();
		
		builder = new FileBasedConfigurationBuilder<FileBasedConfiguration>(PropertiesConfiguration.class)
				.configure(configManager.getParameters().fileBased().setFileName("src/main/resources/app.properties"));
		
		config = builder.getConfiguration();
	}
	
	public static final String WEB_CONTEXT = "webcontext";
	public static final String PROD = "prod";
	public static final String PORT = "port";
	public static final String WORLD_LOCATION = "worldlocation";
	public static final String WORLDS_LOCATION = "worldsLocation";
	public static final String START_TEST="start-test";
	
	public static final String MANUAL_SAVE_ONLY = "manualSavingOnly";
	public static final String DARKMODE = "darkMode";
	public static final String AUTO_UPDATE_TABS = "autoUpdateTabs";
	
	
	
	public static void saveDefaultWorldLocation(String worldloc) throws ConfigurationException{
		log.debug("saveDefaultWorldLocation: " + worldloc);
		config.setProperty(WORLD_LOCATION, worldloc);
		config = builder.getConfiguration();
		config.setProperty(WORLD_LOCATION, worldloc);
		builder.save();
	}
	
	public static void setManualSaveOnly(boolean popup) throws ConfigurationException {
		log.debug("save manualSaveOnly");
		config=builder.getConfiguration();
		config.setProperty(MANUAL_SAVE_ONLY, popup);
		builder.save();
		log.debug("manualSaveOnly: " + config.getBoolean(MANUAL_SAVE_ONLY));
	}
	
	public static void setDarkMode(boolean popup) throws ConfigurationException {
		log.debug("dark mode toggle");
		config=builder.getConfiguration();
		config.setProperty(DARKMODE, popup);
		builder.save();
		log.debug("darkMode: " + config.getBoolean(DARKMODE));
	}
	
	public static void setAutoUpdateTabs(boolean autoUpdateTabs) throws ConfigurationException{
		log.debug("auto update tabs toggle");
		config=builder.getConfiguration();
		config.setProperty(AUTO_UPDATE_TABS, autoUpdateTabs);
		builder.save();
		log.debug("autoUpdateTabs: " + config.getBoolean(AUTO_UPDATE_TABS));
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
	
	public static boolean getManualSaveOnly(){
		return config.getBoolean(MANUAL_SAVE_ONLY);
	}
	
	public static boolean getDarkMode(){
		return config.getBoolean(DARKMODE);
	}
	
	public static boolean getAutoUpdateTabs(){
		return config.getBoolean(AUTO_UPDATE_TABS);
	}
	
	
}
