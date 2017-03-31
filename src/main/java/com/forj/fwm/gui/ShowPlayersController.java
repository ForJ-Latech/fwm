package com.forj.fwm.gui;

import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Enumeration;
import java.util.Scanner;
import java.util.regex.Pattern;

import org.apache.commons.configuration2.ex.ConfigurationException;
import org.apache.log4j.Category;
import org.apache.log4j.Logger;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerCollection;
import org.eclipse.jetty.webapp.WebAppContext;

import com.forj.fwm.backend.Backend;
import com.forj.fwm.backend.ShowPlayersDataModel;
import com.forj.fwm.conf.AppConfig;
import com.forj.fwm.conf.WorldConfig;
import com.forj.fwm.entity.Event;
import com.forj.fwm.entity.God;
import com.forj.fwm.entity.Npc;
import com.forj.fwm.entity.Region;
import com.forj.fwm.startup.App;
import com.forj.fwm.startup.ComponentSelectorController;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Labeled;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.awt.Desktop;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URISyntaxException;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;



public class ShowPlayersController { // NEEDS to be a space after class name or scene builder can't find it.

	private static Logger log = Logger.getLogger(ShowPlayersController.class);
	private static Boolean isOpen = false;
	
	@FXML
	private Label name, description;
	@FXML
	private Button test1, backButton, forwardButton;
	@FXML
	private ImageView image;
	
	private Object currObject;
	
	private Integer curIndex = 0;

	// Method called on wrong thread for servers to be started
	// started on GUI thread.
    public void start(Stage primaryStage, Pane rootLayout) throws Exception {
       primaryStage.setTitle("Show Players Controller");
       Scene myScene = new Scene(rootLayout);
       primaryStage.setScene(myScene);
       primaryStage.show();
       
       primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
           public void handle(WindowEvent we) {
               isOpen = false;
           }
       }); 
       
    }

	
	public static ShowPlayersController startShowPlayersWindow() throws Exception{
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(ShowPlayersController.class.getResource("showPlayersGUI.fxml"));
		AnchorPane rootLayout = (AnchorPane)loader.load();
		ShowPlayersController cr = (ShowPlayersController)loader.getController();
		cr.start(new Stage(), rootLayout);
		ShowPlayersDataModel.startConnector();
		Npc n = new Npc();
		n.setfName("Jack");
		n.setlName("Strickland");
		n.setDecription("This will be the decription.");
		n.setImageFileName("/i/fat_halfling.jpg");
		ShowPlayersDataModel.addOne(n);
		isOpen = true;
		return cr;
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
			newName = ((Npc) currObject).getfName() + " " + ((Npc) currObject).getlName();
			newDescription = ((Npc) currObject).getDecription();
			newImage = ((Npc) currObject).getImageFileName();
		} else if (currObject instanceof Event){
			newName = ((Event) currObject).getName();
			newDescription = ((Event) currObject).getDescription();
		}
		
		setName(newName);
		setDescription(newDescription);
		setImage(newImage);
	}

	@FXML
	private void back() {
		log.debug("back");
		log.debug("curIndex before: " + curIndex);
		setObject(ShowPlayersDataModel.getPrevious(curIndex));
		log.debug("curIndex after: " + curIndex);
	}
	
	@FXML
	private void forward() {
		log.debug("forward");
		log.debug("curIndex before: " + curIndex);
		setObject(ShowPlayersDataModel.getNext(curIndex));
		log.debug("curIndex after: " + curIndex);
	}
	
}