package com.forj.fwm.gui;

import java.io.File;
import java.net.MalformedURLException;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import com.forj.fwm.backend.Backend;
import com.forj.fwm.backend.ShowPlayersDataModel;
import com.forj.fwm.conf.HotkeyController;
import com.forj.fwm.conf.WorldConfig;
import com.forj.fwm.entity.Event;
import com.forj.fwm.entity.God;
import com.forj.fwm.entity.Npc;
import com.forj.fwm.entity.Region;
import com.forj.fwm.entity.Searchable;
import com.forj.fwm.gui.tab.Saveable;
import com.forj.fwm.startup.App;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.media.AudioClip;
import javafx.stage.Stage;

public class ShowPlayersController { // NEEDS to be a space after class name or scene builder can't find it.
	private static Logger log = Logger.getLogger(ShowPlayersController.class);
	private static Boolean isOpen = false;
	
	@FXML
	private Label name, description;
	@FXML
	private Button test1, backButton, forwardButton;
	@FXML
	private ImageView image;
	@FXML
	private HBox imageViewHBox;
	
	private Searchable curShowing;
	
	private Integer curIndex = 0;

	private Stage ourStage;
	
	private static Scene theScene;
	private static boolean started = false;
	
	AudioClip sound = null;
	
	// Method called on wrong thread for servers to be started
	// started on GUI thread.
    public void start(Stage primaryStage, ScrollPane rootLayout) throws Exception {
    	primaryStage.setTitle("Player View - " + App.worldFileUtil.getWorldName());
	   	Scene myScene = new Scene(rootLayout);
	   	myScene.getStylesheets().add(App.retGlobalResource("/src/main/ui/showPlayersStylesheet.css").toString());
	
	   	primaryStage.getIcons()
		.add(new Image(App.retGlobalResource("/src/main/webapp/WEB-INF/images/icons/player/64.png").openStream()));
	   	primaryStage.setScene(myScene);
	   	primaryStage.setMinWidth(640);
	   	primaryStage.setMinHeight(520);
	   
	   	image.fitWidthProperty().bind(imageViewHBox.widthProperty().subtract(10));
	   	image.fitHeightProperty().bind(imageViewHBox.heightProperty().subtract(10));
	   
	   	ourStage = primaryStage;
	   	ourStage.setOnCloseRequest(new EventHandler(){
			public void handle(javafx.event.Event event) {
				closeWindow();
			}
	   	});
	   	HotkeyController.giveGlobalHotkeys(myScene);
	   	setScene(myScene);
	   	if(WorldConfig.getDarkMode())
		{
	   		setDark(true);
		}
	   	started = true;
    }

	
	public static ShowPlayersController startShowPlayersWindow() throws Exception{
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(ShowPlayersController.class.getResource("showPlayersGUI.fxml"));
		ScrollPane rootLayout = (ScrollPane)loader.load();
		ShowPlayersController cr = (ShowPlayersController)loader.getController();
		cr.start(new Stage(), rootLayout);
		return cr;
	}
	
	public void showController(){
		if (!ourStage.isShowing()){
			ourStage.show();
		}
		ourStage.requestFocus();
	}
	
	public boolean isShowing(){
		return ourStage.isShowing();
	}
	
	public void closeWindow(){
		started = false;
		stopSound();
		ourStage.close();
	}
	
	@FXML
	public void back() {
		log.debug("back");
		log.debug("curIndex before: " + curIndex);	
		changeShown(App.spdc.getPrevious(curIndex), false);
		log.debug("curIndex after: " + curIndex);
		
	}
	
	@FXML
	public void forward() {
		log.debug("forward");
		log.debug("curIndex before: " + curIndex);
		changeShown(App.spdc.getNext(curIndex), false);
		log.debug("curIndex after: " + curIndex);
	}
	
	public void stopSound(){
		if(sound != null)
		{
			sound.stop();
		}
	}
	
	public void playSound(Searchable s)
	{
		stopSound();
		if(s.getID() == -1){
			return;
		}
		try{
			if(s.equals(Region.class) && s.getID() != -1)
			{
				Region r = (Region)s;
				if(r.getSoundFileName() != null){
					sound = new AudioClip(App.worldFileUtil.findMultimedia(r.getSoundFileName()).toURI().toURL().toString());
					sound.play();
				}
			}
			else if(s.getClass().equals(Npc.class) )
			{
				Npc r = (Npc)s;
				if(r.getSoundFileName() != null){
					sound = new AudioClip(App.worldFileUtil.findMultimedia(r.getSoundFileName()).toURI().toURL().toString());
					sound.play();
				}
				 				
			}
			else if(s.getClass().equals(God.class))
			{
				God r = (God)s;
				if(r.getSoundFileName() != null){
					sound = new AudioClip(App.worldFileUtil.findMultimedia(r.getSoundFileName()).toURI().toURL().toString());
					sound.play();
				}	   				
			}
			else if(s.getClass().equals(Event.class))
			{
				Event r = (Event)s;
				if(r.getSoundFileName() != null){
					sound = new AudioClip(App.worldFileUtil.findMultimedia(r.getSoundFileName()).toURI().toURL().toString());
					sound.play();
				}				
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} 
	}
	
	public static Boolean getOpen(){
		return isOpen;
	}
	
	public void setName(String newName) {
		name.setText(newName);
		App.getMainController().changeShowLabel(name.getText());
	}
	
	public void setDescription(String newDesription) {
		description.setText(newDesription);
	}
	
	private void setImage(String newImageFileName) {
		if (newImageFileName == null || "".equals(newImageFileName)){
			image.setImage(new Image(App.retGlobalResource("/src/main/ui/no_image.png").toString()));
		} 
		else
		{
			File newImageFile = App.worldFileUtil.findMultimedia(newImageFileName);
			Image newImage = new Image(newImageFile.toURI().toString());
			image.setImage(newImage);
		}
	}
	
	public static void setScene(Scene myScene)
	{
		theScene = myScene;
	}
	
	public static Scene getScene()
	{
		return theScene;
	}

	public static void setDark(boolean dark) {
		if(dark)
		{
			getScene().getStylesheets().add(App.retGlobalResource("/src/main/ui/darkShowStylesheet.css").toString());
		}
		else
		{
			getScene().getStylesheets().remove(1);
		}		
	}
	
	public void changeShown(ShowPlayersDataModel.ShowData obj, boolean playSound) {
		stopSound();
		curShowing = (Searchable)obj.getObject();
		curIndex = obj.getNewIndex();
		setName(curShowing.getShownName());
		setDescription(curShowing.getDescription());
		setImage(curShowing.getImageFileName());
		if(playSound){
			playSound(curShowing);
		}
	}


	public static boolean getStarted() {
		return started;
	}
}
