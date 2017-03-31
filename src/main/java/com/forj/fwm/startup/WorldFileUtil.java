package com.forj.fwm.startup;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.apache.commons.configuration2.ex.ConfigurationException;
import org.apache.log4j.Logger;

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
public class WorldFileUtil {

	private static Logger log = Logger.getLogger(WorldFileUtil.class);

	private String propertiesLocation;
	private String dbLocation;
	private String multimediaLocation;
	
	private static final String propertiesFileName = "/world.properties";
	private static final String dbFileName = "/world";
	private static final String multimediaFileName = "/multimedia/";
	
	private boolean worldFileInitialized = false;
	
	/**
	 * @param the desired world folder
	 * @return check instance by using success() if true, then use this copy else fail.
	 */
	public WorldFileUtil(File worldFolder) throws Exception {
		worldFileInitialized = false;
		// TODO Auto-generated method stub
		if(worldFolder == null){
			return;
		}
		else if(worldFolder.exists() && !worldFolder.isDirectory()){
			return;
		}
		else if(!worldFolder.exists()){
			worldFolder.mkdirs();
		}
		
		log.debug("world file util has good directory: " + worldFolder.isDirectory());
		File worldPropertiesFile = new File(worldFolder.getAbsolutePath() + propertiesFileName);
		boolean worldPropertiesFileInitializationWorked = true;
		if(worldPropertiesFile.exists()){
			
		}
		else
		{
			try{
				copyDefaultWorldProperties(worldPropertiesFile);
			}catch(IOException e){
				log.error(e);
			}
		}
		try{
			// TODO do this right. 
			WorldConfig.init(worldPropertiesFile.getAbsolutePath());
		}catch(ConfigurationException e){
			log.error(e);
			worldPropertiesFileInitializationWorked = false;
		}
		log.info("World Properties File Initialization Status Success: " + worldPropertiesFileInitializationWorked);
		
		dbLocation = worldFolder.getAbsolutePath() + dbFileName;
		propertiesLocation = worldPropertiesFile.getAbsolutePath();
		multimediaLocation = worldFolder.getAbsolutePath() + multimediaFileName;
		File multimedia = new File(multimediaLocation);
		if(!multimedia.exists()){
			multimedia.mkdirs();
		}
		else
		{
			if(multimedia.isFile()){
				log.error("Multimedia will not work as a folder called multimedia already exists");
				throw new Exception("Multimedia will not work as a folder called multimedia already exists!");
			}
		}
		worldFileInitialized = true;
	}
	
	public boolean success(){
		return worldFileInitialized;
	}
	
	private static void copyDefaultWorldProperties(File outputLocation) throws IOException{
		copyFile(new File("src/main/resources/world.properties"), outputLocation);
	}
	
	private static File copyFile(File input, File outputLocation) throws IOException{
		if(!outputLocation.exists()){
			outputLocation.createNewFile();
		}
		FileInputStream source = new FileInputStream(input);
		FileOutputStream dest = new FileOutputStream(outputLocation);
		dest.getChannel().transferFrom(source.getChannel(), 0, source.getChannel().size());
		dest.close();
		source.close();
		return outputLocation;
	}
	
	public File addMultimedia(File incoming){
		try{
			DirectoryStream<Path> stream = Files.newDirectoryStream(new File(multimediaLocation).toPath());
			List<Integer> files = new ArrayList<Integer>();
			for(Path f: stream){
				try{
					files.add(Integer.parseInt(f.getFileName().toString().substring(0, f.getFileName().toString().lastIndexOf("."))));
				}catch(NumberFormatException e){
					log.error(e);
				}
			}
			// sort our list so that we can put it back. 
			files.sort(new Comparator<Integer>(){
				public int compare(Integer o1, Integer o2) {
					return o1.compareTo(o2);
				}
			});
			Integer lastThing = null;
			if(!files.isEmpty()){
				lastThing = files.get(files.size() - 1);
				lastThing += 1;
			}
			else
			{
				lastThing = 0;
			}
			log.debug("Incomin file name on add multimedia: " + incoming.getName());
			String incomingFileEnd = incoming.getName().substring(incoming.getName().lastIndexOf("."));
			File saved = null;
			saved = copyFile(incoming, new File(multimediaLocation + lastThing.toString() + incomingFileEnd));
			return saved;
		}catch(IOException ex){
			log.error(ex);
			return null;
		}
	}
	
	public File findMultimedia(String filename){
		if(filename == null || filename.equals("")){
			return null;
		}
		return new File(multimediaLocation + filename);
	}
	
	
	public String getPropertiesLocation() {
		return propertiesLocation;
	}

	public String getDbLocation() {
		return dbLocation;
	}

	public String getMultimediaLocation() {
		return multimediaLocation;
	}
}
