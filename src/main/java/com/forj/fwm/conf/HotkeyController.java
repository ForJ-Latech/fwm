package com.forj.fwm.conf;

import java.net.URL;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import com.forj.fwm.startup.WorldFileUtil;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.ObservableListBase;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

public class HotkeyController extends Application {

	private static Logger log = Logger.getLogger(HotkeyController.class);
	
	public static WorldFileUtil worldFileUtil;
	
	private ObservableList<KeyCode> buttonsHeldDownRightNow = FXCollections.observableArrayList();
	
	@FXML
	private TextField txt;
	
	@FXML
	private Button button;
	
//	public static void main(String[] args) throws Exception {
//		launch(args);
//		
//	}
	
	private boolean buttonPushed;
	
	
	@FXML
	public void pushButton(){
		buttonPushed = !buttonPushed;
		
		log.debug("Button pushed: " + buttonPushed);
		if(buttonPushed == false){
			buttonsHeldDownRightNow.clear();
		}
	}

	private EventHandler<KeyEvent> removeThings = new EventHandler<KeyEvent>(){

		public void handle(KeyEvent event) {
			if(buttonPushed){
				// TODO Auto-generated method stub
				log.debug(event.getCode().getName());
				buttonsHeldDownRightNow.remove(event.getCode());
			}
		}
		
	};
	
	private EventHandler<KeyEvent> loadThings = new EventHandler<KeyEvent>(){

		public void handle(KeyEvent event) {
			if(buttonPushed){
				log.debug(event.getCode().getName());
				buttonsHeldDownRightNow.add(event.getCode());
			}
		}
		
	};
	
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(new URL("file:src/main/java/com/forj/fwm/conf/hotkeySettings.fxml"));
		ScrollPane rootLayout = (ScrollPane)loader.load();
		HotkeyController it = loader.getController();
		Scene myScene = new Scene(rootLayout);
		myScene.setOnKeyPressed(it.loadThings);
		myScene.setOnKeyReleased(it.removeThings);
		
		it.buttonsHeldDownRightNow.addListener(new ListChangeListener<KeyCode>(){

			public void onChanged(javafx.collections.ListChangeListener.Change<? extends KeyCode> c) {
				c.next();
				
				String txtfldtxt = "";
				for(KeyCode x : c.getList()){
					txtfldtxt += x.getName() + " ";
				}
				log.debug(txtfldtxt);
//				txt.setText(txtfldtxt);
			}
			
		});
		
		
		primaryStage.setScene(myScene);
		
		
		
		primaryStage.show();
		
	}
}
