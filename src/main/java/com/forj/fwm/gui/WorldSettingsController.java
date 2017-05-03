package com.forj.fwm.gui;

import org.apache.commons.configuration2.ex.ConfigurationException;
import org.apache.log4j.Logger;

import com.forj.fwm.conf.WorldConfig;
import com.forj.fwm.startup.App;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class WorldSettingsController {
	protected static Logger log = Logger.getLogger(ShowPlayersController.class);
	protected static Boolean isOpen = false;
	
	@FXML private RadioButton popUpOn, popUpOff, manualSaveOn, manualSaveOff, darkModeOn, darkModeOff;
	
	@FXML private Label manualSaveLabel, darkModeLabel;
	private Stage ourStage;
	final ToggleGroup popUp = new ToggleGroup();
	final ToggleGroup manualSaveGroup = new ToggleGroup();
	final ToggleGroup darkModeGroup = new ToggleGroup();
	
	// Method called on wrong thread for servers to be started
	// started on GUI thread.
    public void start(Stage primaryStage, Pane rootLayout, String title) throws Exception {
       ourStage = primaryStage;
    	primaryStage.setTitle(title);
       Scene myScene = new Scene(rootLayout);
       primaryStage.setScene(myScene);
       primaryStage.show();
       
       popUpOn.setToggleGroup(popUp);
       popUpOff.setToggleGroup(popUp);
       if(WorldConfig.getShowPlayersPopup()) {
    	   popUpOn.setSelected(true);
       }
       else {
    	   popUpOff.setSelected(true);
       }
       
       manualSaveOn.setToggleGroup(manualSaveGroup);
       manualSaveOff.setToggleGroup(manualSaveGroup);
       if(WorldConfig.getManualSaveOnly()){
    	   manualSaveOn.setSelected(true);
       }else{
    	   manualSaveOff.setSelected(true);
       }
       
       darkModeOn.setToggleGroup(darkModeGroup);
       darkModeOff.setToggleGroup(darkModeGroup);
       if(WorldConfig.getDarkMode()){
    	   darkModeOn.setSelected(true);
       }else{
    	   darkModeOff.setSelected(true);
       }
       
       primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
           public void handle(WindowEvent we) {
               isOpen = false;
           }
       });
    }

	public static WorldSettingsController startWorldSettingsController() throws Exception{
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(WorldSettingsController.class.getResource("worldSettings.fxml"));
		VBox rootLayout = (VBox)loader.load();
		WorldSettingsController cr = (WorldSettingsController)loader.getController();
		cr.start(new Stage(), rootLayout, "World Settings");
		isOpen = true;
		return cr;
	}
	
	public void setPopUp() throws ConfigurationException{
		WorldConfig.setShowPlayersPopup(popUpOn.isSelected());
	}
	
	public void setManualSave() throws ConfigurationException{
		WorldConfig.setManualSaveOnly(manualSaveOn.isSelected());
	}
	
	public void setDarkMode() throws ConfigurationException{
		WorldConfig.setDarkMode(darkModeOn.isSelected());
		App.getMainController().setDark(darkModeOn.isSelected());
		if(JettyController.getStarted())
			JettyController.setDark(darkModeOn.isSelected());
		if(ShowPlayersController.getStarted())
			ShowPlayersController.setDark(darkModeOn.isSelected());
		
	}
	
	public static Boolean getOpen(){
		return isOpen;
	}
	public void closeWindow(){
		ourStage.close();
	}
}
