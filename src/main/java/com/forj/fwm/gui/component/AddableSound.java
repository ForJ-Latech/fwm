package com.forj.fwm.gui.component;

import java.io.File;
import java.net.MalformedURLException;

import org.apache.log4j.Logger;

import com.forj.fwm.startup.App;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.media.*;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

public class AddableSound{
	Logger log = Logger.getLogger(AddableImage.class);
	
	private static ExtensionFilter[] filts = new ExtensionFilter[]{
		new ExtensionFilter("Sound Files", "*.wav", "*.mp3")	
	};
	
	private File ourSound;
	
	public AddableSound(File sound) {
		log.debug("file:" + sound.getAbsolutePath());
		ourSound = sound;
	}

	public AddableSound() {
		ourSound = null;
	}
	
	private EventHandler<Event> onChangeHandler = null;
	
	private void changeEvent(){
		if(onChangeHandler == null){
			return;
		}
		else
		{
			onChangeHandler.handle(null);
		}
	}
	
	public void changeSound(){		
		log.debug("Mouse clicked boss");
		FileChooser fc = new FileChooser();
		fc.setTitle("Select a sound for this object!");
		fc.getExtensionFilters().addAll(filts);
		// figure out who the owner is here... 
		File selectedFile = fc.showOpenDialog(App.getMainController().getStage());
		File addedFile = null;
		if(selectedFile == null){
			// they couldn't decide what they want. 
			return; 
		}
		else
		{
			addedFile = App.worldFileUtil.addMultimedia(selectedFile);
			ourSound = addedFile;
			log.debug("sound added");
			
		}
		if(addedFile == null){
			log.error("Added file was null!");
		}
		else
		{
			changeEvent();
		}		
	}
	
	private AudioClip clip;
	
	public void stop()
	{
		if(clip != null){
			clip.stop();
		}
	}
	
	public void play(){
		stop();
	    clip = null;
		try {
			clip = new AudioClip(ourSound.toURI().toURL().toString());
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			log.debug(e);
		}
	    log.debug("Teh clip: " + clip);
		clip.play();
	}

	public String getFilename() {
		if(ourSound == null){
			return null;
		}
		else
		{
			return ourSound.getName();
		}
	}
}
