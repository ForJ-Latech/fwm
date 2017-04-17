package com.forj.fwm.startup;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.management.ManagementFactory;
import java.net.URL;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.apache.commons.configuration2.ex.ConfigurationException;
import org.apache.log4j.Logger;
import org.eclipse.jetty.util.log.Log;

import com.forj.fwm.conf.AppConfig;
import com.forj.fwm.conf.WorldConfig;

/**
 * This class should be used to create all of the things needed for a world file to truly work.
 * that is the properties file
 * the database location 
 * 
 * 
 * @author jehlmann
 *
 */
public class AppFileUtil {
	
	private static final String log4jFileName = "log4j.properties";
	private static final String hotkeyFileName = "hotkeys.properties";
	private static final String propertiesFileName = "app.properties";
	// private static final String loggingFileName = "FWM.log";
	private static final String propertiesLocation = "./FWM-properties/";
	private static final String copyLocations = "/src/main/resources/";
	private File idFile;
	
	private boolean appFileInitialized = false;
	
	private String errorMessage = "had somithng wrong with it. Please clear the directory";
	
	public AppFileUtil() throws Exception {
		appFileInitialized = false;
		File folder = new File(propertiesLocation);
		if(folder.exists() && !folder.isDirectory()){
			return;
		}
		else if(!folder.exists()){
			folder.mkdirs();
		}
		if (Boolean.parseBoolean(System.getenv("RUNNING_IN_ECLIPSE")) != true || AppConfig.getStartTest()) {
			for(File f: folder.listFiles()){
				if(f.getName().endsWith(".pid")){
					errorMessage = "If another instance of the application is not already running,"
							+ " go to the Properties Location and delete the file"
							+ " ending with '.pid'.";
					return;
				}
			}
			String pid = ManagementFactory.getRuntimeMXBean().getName();
			idFile = new File(folder.getAbsolutePath() + File.separator + pid + ".pid");
			idFile.createNewFile();
			idFile.deleteOnExit();
			System.out.println("Created pid file :" + idFile.getAbsolutePath());
		}
		for(String filename: new String[]{log4jFileName, hotkeyFileName, propertiesFileName}){
			String locationToCheck = folder.getAbsolutePath() + File.separator + filename;
			File out = new File(locationToCheck);
			if(!out.exists()){
				copyFile(App.retGlobalResource(copyLocations + filename).openStream(), out);
			}
			else
			{
				// don't overwrite it. 
			}
		}
		
		appFileInitialized = true;
	}
	
	public boolean success(){
		return appFileInitialized;
	}
	
	public static File copyFile(InputStream input, File outputLocation) throws IOException{
		if(!outputLocation.exists()){
			outputLocation.createNewFile();
		}
		
		FileOutputStream dest = new FileOutputStream(outputLocation);

		int read = 0;
		byte[] bytes = new byte[1024];

		while ((read = input.read(bytes)) != -1) {
			dest.write(bytes, 0, read);
		}
		
		return outputLocation;
	}
	
	public File getLog4JFile(){
		return new File(propertiesLocation + log4jFileName);
	}
	
	public File getHotkeysFile(){
		return new File(propertiesLocation + hotkeyFileName);
	}
	
	public File getAppPropFile(){
		return new File(propertiesLocation + propertiesFileName);
	}
	
	public String getErrorMessage(){
		return "Properties location: " + (new File(propertiesLocation)).getAbsolutePath().toString() + "\n\n" + errorMessage;
	}
	
	public File getIdFile(){
		return idFile;
	}
}
