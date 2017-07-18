package com.forj.fwm.gui.component;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import com.forj.fwm.gui.tab.EventTabController;
import com.forj.fwm.gui.tab.GodTabController;
import com.forj.fwm.gui.tab.NpcTabController;
import com.forj.fwm.gui.tab.RegionTabController;
import com.forj.fwm.startup.App;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.media.*;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

public class AddableSound extends Button{
	private static Logger log = Logger.getLogger(AddableImage.class);
	private File ourSound;
	private EventHandler<Event> onChangeHandler = null;
	private AudioClip clip;
	private Saveable caller;
	
	private static ExtensionFilter[] filts = new ExtensionFilter[]{
		new ExtensionFilter("Sound Files", "*.wav", "*.mp3")	
	};
	
	public AddableSound(Saveable s, File sound) {
		super();
		this.caller = s;
		log.debug("file:" + sound.getAbsolutePath());
		ourSound = sound;
		start();
	}

	public AddableSound(Saveable s) {
		super();
		this.caller = s;
		ourSound = null;
		start();
	}
	
	private void setButtonText(String s){
		this.setText(s);
	}
	
	
	private void start() {
		
		this.setFont(new Font(10));
		this.setWidth(65);
		this.setHeight(20);
		
		if (ourSound == null) {
			setButtonText("Add Sound");
			setPlayButtonEnabled(false);
		} else {
			setButtonText("Change Sound");
			setPlayButtonEnabled(true);
		}
		
		
		this.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                changeSound();
            }
        });
		
		this.setOnDragOver(new EventHandler<DragEvent>() {
            public void handle(DragEvent event) {
                Dragboard db = event.getDragboard();
                if (db.hasFiles()) {
                    event.acceptTransferModes(TransferMode.COPY);
                } else {
                    event.consume();
                }
            }
        });
        
        // Dropping over surface
		this.setOnDragDropped(new EventHandler<DragEvent>() {
            public void handle(DragEvent event) {
                Dragboard db = event.getDragboard();
                boolean success = false;
                if (db.hasFiles()) {
                    success = true;
                    String filePath = null;
                    for (File file:db.getFiles()) {
                        filePath = file.getAbsolutePath();
                        System.out.println(filePath);
                        
                        String extension = "";
                        int i = filePath.lastIndexOf('.');
                        if (i >= 0) {
                            extension = filePath.substring(i+1).toLowerCase();
                        }
                        
                        if (extension.equals("wav") || extension.equals("mp3")) {
	                        File addedFile = App.worldFileUtil.addMultimedia(new File(filePath));
	                        ourSound = addedFile;
	            			changeEvent();
	            			stop();
	            			caller.fullSave();
	            			setButtonText("Change Sound");
	            			setPlayButtonEnabled(true);
                        } else {
                        	log.debug("not an audio file");
                        }
                    }
                }
                
                event.setDropCompleted(success);
                event.consume();
            }
        });
	}
	
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
			caller.fullSave();
			setButtonText("Change Sound");
			setPlayButtonEnabled(true);
			stop();
			
		}
		if(addedFile == null){
			log.error("Added file was null!");
		}
		else
		{
			changeEvent();
		}		
	}
	
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
			if (ourSound != null)
				clip = new AudioClip(ourSound.toURI().toURL().toString());
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			log.debug(e);
		}
	    log.debug("Teh clip: " + clip);
		clip.play();
	}
	
	public boolean isPlaying() {
		if (clip != null){
			return clip.isPlaying();
		}
		return false;
	}
	
	public boolean hasSound(){
		if (ourSound != null) {
			return true;
		}
		return false;
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
	
	private void setPlayButtonEnabled(boolean bool){
		Button b = null;
		if (caller instanceof GodTabController){
			b = ((GodTabController) caller).getPlayButton();
		} else if (caller instanceof NpcTabController){
			b = ((NpcTabController) caller).getPlayButton();
		} else if (caller instanceof RegionTabController){
			b = ((RegionTabController) caller).getPlayButton();
		} else if (caller instanceof EventTabController){
			b = ((EventTabController) caller).getPlayButton();
		}
		if (bool) {
			b.setDisable(false);
		} else {
			b.setDisable(true);
		}
		
	}

}
