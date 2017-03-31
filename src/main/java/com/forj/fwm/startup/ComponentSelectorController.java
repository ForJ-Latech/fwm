package com.forj.fwm.startup;

import java.io.File;

import org.apache.log4j.Logger;

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

	public void start(Stage primaryStage, Pane myPane) throws Exception {
		this.primaryStage = primaryStage;
		log.debug("Is the primaryStage null?" + (primaryStage == null));
		primaryStage.setTitle("FWM Component Selector");
		Scene myScene = new Scene(myPane);
		primaryStage.setScene(myScene);

		String defaultLocationString = AppConfig.config.getString(AppConfig.WORLD_LOCATION);
		File defaultLocation = new File(defaultLocationString);
		File chosenLocation = null;
		log.debug("Default world location: " + defaultLocation.getAbsolutePath());
		// if we're in production, we need to start the app correctly. 
		if (App.getProd() || AppConfig.getStartTest()) {
			App.worldFileUtil = new WorldFileUtil(chosenLocation);
			while(App.worldFileUtil.success() == false){
				DirectoryChooser dirChooser = new DirectoryChooser();
				if (defaultLocation.isDirectory() && defaultLocation.exists()) {
					dirChooser.setInitialDirectory(defaultLocation);
				} else {
					log.debug("Default location from settings invalid, resorting to .");
					dirChooser.setInitialDirectory(new File("."));
				}
				dirChooser.setTitle("Select a world folder to load into FWM");
	
				File selectedDirectory = dirChooser.showDialog(primaryStage);
				if (selectedDirectory == null) {
					log.info("User decided not to select a directory.");
					System.exit(0);
				}
	
				String relativePath = new File(".").toURI().relativize(selectedDirectory.toURI()).getPath();
				AppConfig.saveDefaultWorldLocation("./" + relativePath);
				App.worldFileUtil = new WorldFileUtil(selectedDirectory);
			}
			// open us up a
			// Do nothing, wait for button presses.
			primaryStage.show();
		} else {
			// otherwise assume that we want to start both things. 
			primaryStage.show();
			
			// make certain that what we want exists!
			defaultLocation.mkdirs();
			String relativePath = new File(".").toURI().relativize(defaultLocation.toURI()).getPath();
			AppConfig.saveDefaultWorldLocation("./" + relativePath);
			
			App.worldFileUtil = new WorldFileUtil(new File(AppConfig.config.getString(AppConfig.WORLD_LOCATION)));
			
			startBoth();
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
		JettyController.startJettyWindow();
		App.setMainController(MainController.startMainUi());
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

	public static void noUiStart() throws Exception{
		AppConfig.init();
		ShowPlayersDataModel.startConnector();
		
		String defaultLocationString = AppConfig.config.getString(AppConfig.WORLD_LOCATION);
		File defaultLocation = new File(defaultLocationString);
		
		
		defaultLocation.mkdirs();
		String relativePath = new File(".").toURI().relativize(defaultLocation.toURI()).getPath();
		AppConfig.saveDefaultWorldLocation("./" + relativePath);
		
		App.worldFileUtil = new WorldFileUtil(new File(AppConfig.config.getString(AppConfig.WORLD_LOCATION)));
		
		Backend.start();
	}

}