package com.forj.fwm.gui;

import java.io.IOException;
import java.net.MalformedURLException;

import org.apache.log4j.Logger;

import com.forj.fwm.entity.Statblock;
import com.forj.fwm.gui.component.TabControlled;
import com.forj.fwm.gui.tab.Saveable;
import com.forj.fwm.gui.tab.StatBlockTabController;
import com.forj.fwm.startup.App;

import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class StatBlockController extends TabControlled {
	
	private static Logger log = Logger.getLogger(StatBlockController.class);
	
	private boolean started = false;
	
	private Stage ourStage;
	
	private TabPane tabPane;
	
	public void start(Stage primaryStage, TabPane rootLayout){
		tabPane = rootLayout;
		primaryStage.setTitle("Statblock Controller");
		Scene myScene = new Scene(rootLayout);
		App.getHotkeyController().giveGlobalHotkeys(myScene);
		App.getHotkeyController().giveStatblockHotkeys(myScene);
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
		if(s.getID() == MainController.NPCstat.getID()){
			cr.tabHead.setText("Default NPC Statblock");
		}
		else if(s.getID() == MainController.GodStat.getID()){
			cr.tabHead.setText("Default God Statblock");
		}
		else if(s.getID() == MainController.RegionStat.getID()){
			cr.tabHead.setText("Default Region Statblock");
		}
		else if(s.getID() == MainController.GroupStat.getID()){
			cr.tabHead.setText("Default Group Statblock");
		}
		addTabController(cr);
		ourStage.requestFocus();
	}
	
	public boolean getStarted(){
		return started;
	}
	
	public Stage getStage(){
		return ourStage;
	}
}
