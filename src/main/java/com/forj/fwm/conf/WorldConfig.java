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

/**
 * https://commons.apache.org/proper/commons-configuration/userguide/quick_start.html
 * 
 * If something was 'optional' go and grab it. 
 * https://mvnrepository.com/artifact/org.apache.commons/commons-configuration2/2.0
 * 
 * @author jehlmann
 *
 */
public class WorldConfig {
	
	private static Logger log = Logger.getLogger(AppConfig.class);
	
	public static Configuration config;
	private static Configurations configManager;
	private static FileBasedConfigurationBuilder<FileBasedConfiguration> builder;
	private static String defaultWorldPropertiesLocation = "src/main/resources/world.properties";
	
	public static void init(String worldLocation) throws ConfigurationException {
		log.debug("World Location for properties: " + worldLocation);
		configManager = new Configurations();
		if(worldLocation == null){
			builder = new FileBasedConfigurationBuilder<FileBasedConfiguration>(PropertiesConfiguration.class)
				.configure(configManager.getParameters().fileBased().setFileName(defaultWorldPropertiesLocation));
		}else
		{
			builder = new FileBasedConfigurationBuilder<FileBasedConfiguration>(PropertiesConfiguration.class)
					.configure(configManager.getParameters().fileBased().setFileName(worldLocation));
		}
		config = builder.getConfiguration();		
	}
	
	public static final String PASSWORD = "password";
	public static final String RADIO10 = "radio10";
	public static final String RADIO15 = "radio15";
	public static final String SHOWPLAYERSPOPUP = "showPlayersPopup";
	public static final String MANUAL_SAVE_ONLY = "manualSavingOnly";
	
	
	public static String getPassword(){
		return config.getString(PASSWORD);
	}
	
	public static void savePassword(String password) throws ConfigurationException{
		log.debug("saveDefaultWorldLocation: " + password);
		config.setProperty(PASSWORD, password);
		config = builder.getConfiguration();
		config.setProperty(PASSWORD, password);
		builder.save();
	}
	
	public static void saveRadios(boolean r10, boolean r15) throws ConfigurationException {
		log.debug("saveJettyRadios");
		config=builder.getConfiguration();
		config.setProperty(RADIO10, r10);
		config.setProperty(RADIO15, r15);
		builder.save();
		log.debug("radio 1.0: " + config.getBoolean(RADIO10) + 
				"\nradio 1.5: " + config.getBoolean(RADIO15));
		
	}

	public static void setShowPlayersPopup(boolean popup) throws ConfigurationException {
		log.debug("save showplayers");
		config=builder.getConfiguration();
		config.setProperty(SHOWPLAYERSPOPUP, popup);
		builder.save();
		log.debug("ShowPlayersPopup: " + config.getBoolean(SHOWPLAYERSPOPUP));
	}
	
	public static void setManualSaveOnly(boolean popup) throws ConfigurationException {
		log.debug("save manualSaveOnly");
		config=builder.getConfiguration();
		config.setProperty(MANUAL_SAVE_ONLY, popup);
		builder.save();
		log.debug("manualSaveOnly: " + config.getBoolean(SHOWPLAYERSPOPUP));
	}
	
	public static boolean getRad10() {
		return config.getBoolean(RADIO10);
	}
	public static boolean getRad15() {
		return config.getBoolean(RADIO15);
	}
	
	public static boolean getShowPlayersPopup(){
		return config.getBoolean(SHOWPLAYERSPOPUP);
	}
	
	public static boolean getManualSaveOnly(){
		return config.getBoolean(MANUAL_SAVE_ONLY);
	}
}
