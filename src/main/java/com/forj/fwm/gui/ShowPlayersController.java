package com.forj.fwm.gui;

import java.io.File;
import java.net.MalformedURLException;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import com.forj.fwm.backend.Backend;
import com.forj.fwm.backend.ShowPlayersDataModel;
import com.forj.fwm.conf.HotkeyController;
import com.forj.fwm.entity.Event;
import com.forj.fwm.entity.God;
import com.forj.fwm.entity.Npc;
import com.forj.fwm.entity.Region;
import com.forj.fwm.gui.tab.Saveable;
import com.forj.fwm.startup.App;

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
	
	private Object currObject;
	
	private Integer curIndex = 0;

	private Stage ourStage;
	
	AudioClip sound = null;
	
	// Method called on wrong thread for servers to be started
	// started on GUI thread.
    public void start(Stage primaryStage, ScrollPane rootLayout) throws Exception {
       primaryStage.setTitle("Show Players Controller");
       Scene myScene = new Scene(rootLayout);
       myScene.getStylesheets().add(App.retGlobalResource("/src/main/java/com/forj/fwm/gui/showPlayersStylesheet.css").toString());

       primaryStage.getIcons()
		.add(new Image(App.retGlobalResource("/src/main/webapp/WEB-INF/images/icons/player/64.png").openStream()));

       primaryStage.setScene(myScene);
       image.fitWidthProperty().bind(imageViewHBox.widthProperty().subtract(20));

       image.fitHeightProperty().bind(imageViewHBox.heightProperty().subtract(20));
       
       ourStage = primaryStage;
       HotkeyController.giveGlobalHotkeys(myScene);
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
		ourStage.close();
	}
	
	@FXML
	public void back() {
		log.debug("back");
		log.debug("curIndex before: " + curIndex);
		setObject(App.spdc.getPrevious(curIndex));
		log.debug("curIndex after: " + curIndex);
	}
	
	@FXML
	public void forward() {
		log.debug("forward");
		log.debug("curIndex before: " + curIndex);
		setObject(App.spdc.getNext(curIndex));
		log.debug("curIndex after: " + curIndex);
	}
	
	public void playSound(Saveable s)
	{
		if(sound != null)
		{
			sound.stop();
		}
		if(s.getThing().getClass().equals(Region.class) && s.getThing().getID() != -1)
		{
			try {
				if(Backend.getRegionDao().getRegion(s.getThing().getID()).getSoundFileName() != null)
				{
					sound = new AudioClip(App.worldFileUtil.findMultimedia(Backend.getRegionDao().getRegion(s.getThing().getID()).getSoundFileName()).toURI().toURL().toString());
					sound.play();
				}
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (SQLException e) {
				e.printStackTrace();
			}   				
		}
		
		else if(s.getThing().getClass().equals(Npc.class) && s.getThing().getID() != -1)
		{
			try {
				if(Backend.getNpcDao().getNpc(s.getThing().getID()).getSoundFileName() != null)
				{
					sound = new AudioClip(App.worldFileUtil.findMultimedia(Backend.getNpcDao().getNpc(s.getThing().getID()).getSoundFileName()).toURI().toURL().toString());
					sound.play();
				}
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (SQLException e) {
				e.printStackTrace();
			}   				
		}
		
		else if(s.getThing().getClass().equals(God.class) && s.getThing().getID() != -1)
		{
			try {
				if(Backend.getGodDao().getGod(s.getThing().getID()).getSoundFileName() != null)
				{
					sound = new AudioClip(App.worldFileUtil.findMultimedia(Backend.getGodDao().getGod(s.getThing().getID()).getSoundFileName()).toURI().toURL().toString());
					sound.play();
				}
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (SQLException e) {
				e.printStackTrace();
			}   				
		}
	}
	
	public static Boolean getOpen(){
		return isOpen;
	}
	
	public void setName(String newName) {
		name.setText(newName);
	}
	
	public void setDescription(String newDesription) {
		description.setText(newDesription);
	}
	
	private void setImage(String newImageFileName) {
		if (newImageFileName == "" || newImageFileName == null) return;
		File newImageFile = App.worldFileUtil.findMultimedia(newImageFileName);
		Image newImage = new Image(newImageFile.toURI().toString());
		image.setImage(newImage);
	}
	
	public void setObject(ShowPlayersDataModel.ShowData obj) {
		currObject = obj.getObject();
		curIndex = obj.getNewIndex();
		String newName = "";
		String newDescription = "";
		String newImage = "";
		// Add more stuff to show here
		
		if (currObject instanceof God){
			newName = ((God) currObject).getName();
			newDescription = ((God) currObject).getDescription();
			newImage = ((God) currObject).getImageFileName();
		} else if (currObject instanceof Region){
			newName = ((Region) currObject).getName();
			newDescription = ((Region) currObject).getDescription();
			newImage = ((Region) currObject).getImageFileName();
		} else if (currObject instanceof Npc){
			if(((Npc) currObject).getlName() != null)
			{
				newName = ((Npc) currObject).getfName() + " " + ((Npc) currObject).getlName();
			}	
			else
			{
				newName = ((Npc) currObject).getfName();
			}
			newDescription = ((Npc) currObject).getDescription();
			newImage = ((Npc) currObject).getImageFileName();
		} else if (currObject instanceof Event){
			newName = ((Event) currObject).getName();
			newDescription = ((Event) currObject).getDescription();
		}
		
		setName(newName);
		setDescription(newDescription);
		setImage(newImage);
	}
}
