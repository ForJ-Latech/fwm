package com.forj.fwm.startup;

import java.net.URI;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import com.forj.fwm.conf.AppConfig;
import com.forj.fwm.entity.Interaction;
import com.forj.fwm.gui.MainController;
import com.forj.fwm.gui.StatBlockController;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * http://docs.oracle.com/javafx/2/fxml_get_started/jfxpub-fxml_get_started.htm
 * 
 * @author jehlmann
 *
 */
public class App extends Application {

	private static Logger log = Logger.getLogger(App.class);
	
	public static WorldFileUtil worldFileUtil;
	
	public static AppFileUtil appFileUtil;
	
	private static StatBlockController statBlockController;
	
	private static MainController mc;
	
	public static StatBlockController getStatBlockController(){
		try{
			if(statBlockController == null || statBlockController.getStarted() == false){
				statBlockController = null;
				statBlockController = StatBlockController.startStatBlockController();
			}
		} catch(Exception e){
			// log.error(e.getStackTrace());
			e.printStackTrace();
			return null;
		}
		return statBlockController;
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
	
	public static void main(String[] args) throws Exception {
		// PRODUCTION mode needs to be determined before we get here... 
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
		
		ComponentSelectorController cr = (ComponentSelectorController)loader.getController();
		cr.start(primaryStage, rootLayout);
	}
	

	public static MainController getMainController(){
		return mc;
	}
	
	public static void setMainController(MainController newMc){
		mc = newMc;
	}
	
}