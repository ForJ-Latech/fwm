package com.forj.fwm.gui;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import com.forj.fwm.entity.Statblock;
import com.forj.fwm.gui.tab.Saveable;
import com.forj.fwm.gui.tab.StatBlockTabController;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TabPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class StatBlockController {
	
	private Logger log = Logger.getLogger(StatBlockController.class);
	
	@FXML
	TabPane tabPane;
	
	ArrayList<Saveable> tabControllers = new ArrayList<Saveable>();
	
	private boolean started = false;
	
	private Stage ourStage;
	
	public void start(Stage primaryStage, TabPane rootLayout){
		//TODO discover how to dispose the controllers that have been closed, unless javafx just does it. 
		
		primaryStage.setTitle("Statblock Controller");
		Scene myScene = new Scene(rootLayout);
		primaryStage.setScene(myScene);
		primaryStage.show();	       
		primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			   public void handle(WindowEvent we) {
			       log.debug("Statblock Controller is closing");
			       started = false;
			   }
		   }); 
		ourStage = primaryStage;
		started = true;
	}
	
	public static StatBlockController startStatBlockController() throws MalformedURLException, IOException{
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(StatBlockController.class.getResource("statBlockUi.fxml"));
		TabPane rootLayout = (TabPane)loader.load();
		StatBlockController cr = (StatBlockController)loader.getController();
		cr.start(new Stage(), rootLayout);
		return cr;
	}
	
	public void show(Statblock s, Saveable b) throws Exception{
		StatBlockTabController cr = StatBlockTabController.startStatBlockTabController(s, b);
		addTabController(cr);
		ourStage.requestFocus();
		
	}
	
	public void addTabController(Saveable s){
		boolean existed = false;
		for(Saveable x: tabControllers){
			if(x.getThing().equals(s.getThing())){
				existed = true;
				tabPane.getSelectionModel().select(x.getTab());
			}
		}
		
		if(!existed){
			tabPane.getTabs().add(s.getTab());
			tabPane.getSelectionModel().select(s.getTab());
			tabControllers.add(s);
		}
		else
		{
			// clean up our item, wish it was like c# where i can .dispose(); 
			s = null; 
		}
	}
	
	public void disposeTabController(Saveable s){
		for(int i = 0; i < tabControllers.size(); i++){
			if(s.getTab().equals(tabControllers.get(i).getTab())){
				tabControllers.remove(i);
			}
		}
	}
	
	public boolean getStarted(){
		return started;
	}
	
}
