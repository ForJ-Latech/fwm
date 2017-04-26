package com.forj.fwm.gui.component;

import java.io.File;
import java.io.FileInputStream;

import org.apache.log4j.Logger;

import com.forj.fwm.startup.App;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

public class AddableImage extends ImageView{
	private static Logger log = Logger.getLogger(AddableImage.class);
	private EventHandler<Event> onChangeHandler = null;
	private File ourImage;
	
	private static ExtensionFilter[] filts = new ExtensionFilter[]{
		new ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif", "*.jpeg")	
	};
	
	private void setImageTwo(File f){
		ourImage = f;
		log.debug("setImageTwo called");
		try{
			this.setImage(new Image(new FileInputStream(f)));
		}catch(Exception e){
			log.error(e);
		}
	}
	
	public void setOnImageChanged(EventHandler<Event> ev){
		onChangeHandler = ev;
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
	
	private void makeAddable(){
		this.setFitWidth(100);
		this.setFitHeight(100);
		this.setOnMouseClicked(new EventHandler<MouseEvent>(){
			public void handle(MouseEvent event) {
				log.debug("Mouse clicked boss");
				addImage();
			}
		});
	}
	
	public void addImage(){
		FileChooser fc = new FileChooser();
		fc.setTitle("Select an image for this object!");
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
		}
		if(addedFile == null){
			log.error("Added file was null!");
		}
		else
		{
			setImageTwo(addedFile);
			changeEvent();
		}
	}
	
	public AddableImage(Image image) throws Exception{
		super(image);
		makeAddable();
		throw new Exception("DO NOT CALL THIS ONE");
	}
	
	public AddableImage(String url){
		super(new Image(url, true));
		ourImage = new File(url);
		makeAddable();
	}
	
	public AddableImage(File image){
		super(new Image("file:" + image.getAbsolutePath(), true));
		log.debug("file:" + image.getAbsolutePath());
		ourImage = image;
		makeAddable();
	}
	
	public AddableImage(){
		super(new Image(App.retGlobalResource("/src/main/ui/no_image.png").toString(), true));
		makeAddable();		
	}
	
	public String getFilename(){
		if(ourImage == null){
			return null;
		}
		else
		{
			return ourImage.getName();
		}
	}
}
