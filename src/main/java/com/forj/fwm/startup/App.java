package com.forj.fwm.startup;

import java.io.File;
import java.net.URI;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import com.forj.fwm.backend.Backend;
import com.forj.fwm.backend.ShowPlayersDataModel;
import com.forj.fwm.conf.AppConfig;
import com.forj.fwm.conf.HotkeyController;
import com.forj.fwm.entity.Interaction;
import com.forj.fwm.entity.Searchable;
import com.forj.fwm.gui.MainController;
import com.forj.fwm.gui.ShowPlayersController;
import com.forj.fwm.gui.StatBlockController;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import com.forj.fwm.web.*;

/**
 * http://docs.oracle.com/javafx/2/fxml_get_started/jfxpub-fxml_get_started.htm
 * 
 * @author jehlmann
 *
 */
public class App extends Application {
	
	public static ShowPlayersDataModel spdc;
	
	private static Logger log = Logger.getLogger(App.class);
	
	public static WorldFileUtil worldFileUtil;
	
	public static AppFileUtil appFileUtil;
	
	private static StatBlockController statBlockController;
	
	private static MainController mc;
	
	private static ComponentSelectorController componentSelectorController;
	
	public static ComponentSelectorController getComponentSelectorController(){
		return componentSelectorController;
	}
	
	private static ShowPlayersController showPlayersController;
	
	public static ShowPlayersController getShowPlayersController(){
		try{
			if(showPlayersController == null){
				showPlayersController = null;
				showPlayersController = ShowPlayersController.startShowPlayersWindow();
			}
		} catch(Exception e){
			// log.error(e.getStackTrace());
			e.printStackTrace();
			return null;
		}
		return showPlayersController;
	}
	public static StatBlockController getStatBlockController(){
		try{
			if(statBlockController == null || statBlockController.getStarted() == false){
				statBlockController = null;
				statBlockController = StatBlockController.startStatBlockController();
			}else{
				statBlockController.getStage().show();
			}
		} catch(Exception e){
			// log.error(e.getStackTrace());
			e.printStackTrace();
			return null;
		}
		return statBlockController;
	}
	
	private static HotkeyController hotkeyController;
	
	public static HotkeyController getHotkeyController(){
		try{
			if(hotkeyController == null || hotkeyController.isStarted() == false){
				hotkeyController = null;
				hotkeyController = HotkeyController.startHotkeyController();
			}
		} catch (Exception e){
			e.printStackTrace();
			return null;
		}
		return hotkeyController;
	}
	
	
	public static URL retGlobalResource(String location){
		if(prod){
			return App.class.getResource(location);
		}
		else
		{
			try{
				return new URL("file:" + location.substring(1));
			}catch(Exception e){
				log.error(e);
				return null;
			}
		}
	}
	
	public static List<Searchable> toListSearchable(Object s){
		return (List<Searchable>)(List<?>)s;
	}
	
	public static void main(String[] args) throws Exception {		
		// PRODUCTION mode needs to be determined before we get here... 
		AppConfig.firstInit();
		prod = AppConfig.getProd();
		appFileUtil = new AppFileUtil();
		if(!appFileUtil.success()){
			System.err.println(appFileUtil.getErrorMessage());
			Platform.runLater(new Runnable(){
				public void run(){
					Alert al = new Alert(AlertType.ERROR);
					al.setResizable(true);
					for(Node n : al.getDialogPane().getChildren()){
						if(n instanceof Label){
							((Label)n).setMinHeight(Region.USE_PREF_SIZE);
						}
					}
					al.setContentText(appFileUtil.getErrorMessage());
					al.setHeaderText("FWM Startup Error");
					al.showAndWait();
					System.exit(-1);
				}
			});
			return;
		}
		// ignore everything else because this means that we're in a jar file, so the app won't work
		// if it doesn't think that we're prod. 
		PropertyConfigurator.configure(appFileUtil.getLog4JFile().getAbsolutePath());
		AppConfig.init();
		HotkeyController.init();
		log.debug("Currently prod? "  + prod);
		log.debug(retGlobalResource("/src/main/webapp/WEB-INF/images/FWM-icon.png").getFile());
		launch(args);
	}
	
	private static boolean prod;

	public static boolean getProd() {
		return prod;
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("startup.fxml"));
		AnchorPane rootLayout = (AnchorPane)loader.load();
		
		componentSelectorController = (ComponentSelectorController)loader.getController();
		componentSelectorController.start(primaryStage, rootLayout);
	}

	public static MainController getMainController(){
		return mc;
	}
	
	public static void setMainController(MainController newMc){
		mc = newMc;
	}
	
	public static void noUiStart() throws Exception{
		AppConfig.firstInit();
		prod = AppConfig.getProd();
		appFileUtil = new AppFileUtil();
		if(!appFileUtil.success()){
			System.err.println(appFileUtil.getErrorMessage());
			System.exit(-1);
		}
		// ignore everything else because this means that we're in a jar file, so the app won't work
		// if it doesn't think that we're prod. 
		PropertyConfigurator.configure(appFileUtil.getLog4JFile().getAbsolutePath());
		AppConfig.init();
		
		String defaultLocationString = AppConfig.config.getString(AppConfig.WORLD_LOCATION);
		File defaultLocation = new File(defaultLocationString);
		
		
		defaultLocation.mkdirs();
		String relativePath = new File(".").toURI().relativize(defaultLocation.toURI()).getPath();
		AppConfig.saveDefaultWorldLocation("./" + relativePath);
		
		App.worldFileUtil = new WorldFileUtil(new File(AppConfig.config.getString(AppConfig.WORLD_LOCATION)));
		
		Backend.start();
	}
}