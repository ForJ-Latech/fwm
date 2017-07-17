package com.forj.fwm.startup;

import java.io.File;
import java.io.IOException;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import com.forj.fwm.backend.Backend;
import com.forj.fwm.backend.ShowPlayersDataModel;
import com.forj.fwm.conf.AppConfig;
import com.forj.fwm.gui.JettyController;
import com.forj.fwm.gui.MainController;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

/**
 * http://docs.oracle.com/javafx/2/fxml_get_started/jfxpub-fxml_get_started.htm
 * 
 * @author jehlmann
 *
 */
public class ComponentSelectorController{

	private static Logger log = Logger.getLogger(ComponentSelectorController.class);
	private Stage primaryStage;
	private Pane myPane;
	private static WorldSelector worldSelector;
	public static WorldSelector getWorldSelector(){
		if(worldSelector == null){
			try{
				worldSelector = WorldSelector.startWorldSelector(new Stage(), App.getComponentSelectorController());
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		return worldSelector;
	}
	public void start(Stage primaryStage, Pane myPane) throws Exception {
		this.myPane = myPane;
		this.primaryStage = primaryStage;
		log.debug("Is the primaryStage null?" + (primaryStage == null));
		primaryStage.setTitle("FWM Component Selector");
		Scene myScene = new Scene(myPane);
		primaryStage.setScene(myScene);

		String defaultLocationString = AppConfig.getWorldsLocation() + AppConfig.getWorldLocation();
		File defaultLocation = new File(defaultLocationString);
		File chosenLocation = null;
		log.debug("Default world locations: " + defaultLocation.getAbsolutePath());
		// if we're in production, we need to start the app correctly. 
		if (App.getProd() || AppConfig.getStartTest()) {
			App.worldFileUtil = new WorldFileUtil(chosenLocation);
			worldSelector = WorldSelector.startWorldSelector(new Stage(), this);
			File selectedDirectory = null ;
		} else {
			// otherwise assume that we want to start both things. 
			primaryStage.show();
			
			// make certain that what we want exists!
			defaultLocation.mkdirs();
			
			App.worldFileUtil = new WorldFileUtil(new File(AppConfig.getWorldsLocation() + AppConfig.getWorldLocation()));
			
			startBoth();
		}
	}

	public void finish(File selectedFile) throws Exception{
		String relativePath = new File(".").toURI().relativize(selectedFile.toURI()).getPath();
		AppConfig.saveDefaultWorldLocation("./" + relativePath);
		App.worldFileUtil = new WorldFileUtil(selectedFile);
		if(App.worldFileUtil.success()){
			// skipping start both because it's sort of useless. 
			startGUI();
			primaryStage.close();
		}
		else
		{
			worldSelector = WorldSelector.startWorldSelector(primaryStage, this);
		}
	}
	
	@FXML
	public void startGUI() throws Exception {
		Backend.start();
		App.setMainController(MainController.startMainUi());
		primaryStage.close();
	}

	@FXML
	public void startBoth() throws Exception {
		Backend.start();
		App.setMainController(MainController.startMainUi());
		JettyController.startJettyWindow();
		primaryStage.close();
	}

	@FXML
	public void startWebService() throws Exception {
		Backend.start();
		JettyController.startJettyWindow();
		primaryStage.close();
	}

	@FXML
	public void startJettyOnly() throws Exception {
		Backend.start();
		JettyController.startJettyWindow();
		primaryStage.close();
	}

}