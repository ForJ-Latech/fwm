package com.forj.fwm.gui;

import java.io.File;
import java.io.FileInputStream;
import java.net.URL;

import org.apache.log4j.Logger;

import com.forj.fwm.backend.ShowPlayersDataModel;
import com.forj.fwm.entity.Event;
import com.forj.fwm.entity.God;
import com.forj.fwm.entity.Npc;
import com.forj.fwm.entity.Region;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class GenericTextController { // NEEDS to be a space after class name or scene builder can't find it.

	protected static Logger log = Logger.getLogger(ShowPlayersController.class);
	protected static Boolean isOpen = false;

	@FXML
	private TextArea textArea;
	
	// Method called on wrong thread for servers to be started
	// started on GUI thread.
    public void start(Stage primaryStage, Pane rootLayout, String title) throws Exception {
       primaryStage.setTitle(title);
       Scene myScene = new Scene(rootLayout);
       primaryStage.setScene(myScene);
       primaryStage.show();
       
       primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
           public void handle(WindowEvent we) {
               isOpen = false;
           }
       });
       textArea.setWrapText(true);
    }

	
	public static GenericTextController startGenericTextController(String title) throws Exception{
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(GenericTextController.class.getResource("readme.fxml"));
		VBox rootLayout = (VBox)loader.load();
		GenericTextController cr = (GenericTextController)loader.getController();
		cr.start(new Stage(), rootLayout, title);
		isOpen = true;
		return cr;
	}
	
	public static Boolean getOpen(){
		return isOpen;
	}
	
	public void setText(String text){
		textArea.setText(text);
	}
	
	public void setTextFromFile(File f){
		try{
			FileInputStream fi = new FileInputStream(f);
		    java.util.Scanner s = new java.util.Scanner(fi).useDelimiter("\\A");
		    textArea.setText(s.hasNext() ? s.next() : "");
		} catch (Exception e){
			log.error(e);
		}
	}
}