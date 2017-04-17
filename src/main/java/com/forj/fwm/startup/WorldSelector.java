package com.forj.fwm.startup;

import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.apache.log4j.Logger;

import com.forj.fwm.conf.AppConfig;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class WorldSelector {
	
	private static Logger log = Logger.getLogger(WorldSelector.class);
	
	@FXML
	Button createNewWorldButton;
	
	@FXML
	VBox worlds;
	
	@FXML
	TextField newWorldName;
	
	private Stage primaryStage;
	
	public void closeWindow(){
		//TODO close this window. 
		started = false;
		try{
			ds.finish(this.selectedFile);
			primaryStage.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	private boolean started;
	
	private File selectedFile;
	
	public File getSelectedFile(){
		return selectedFile;
	}
	
	private ComponentSelectorController ds;
	
	private static Font bFont = new Font(20);
	
	public void start(Stage primaryStage, VBox v, ComponentSelectorController ds) {
		started = true;
		this.primaryStage = primaryStage;
		Scene myScene = new Scene(v);
		primaryStage.setScene(myScene);
		this.ds = ds;
		try{
			File f = new File(AppConfig.getWorldsLocation());
			if(!f.exists()){
				f.mkdirs();
			}
			DirectoryStream<Path> stream = Files.newDirectoryStream(f.toPath());
			List<String> files = new ArrayList<String>();
			for(Path p: stream){
				try{
					files.add(p.getFileName().toString());
				}catch(NumberFormatException e){
					log.error(e);
				}
			}
			
			for(final String filename: files){
				Button cur = new Button(filename);
				cur.setOnAction(new EventHandler<ActionEvent>(){
					public void handle(ActionEvent event) {
						selectedFile = new File(AppConfig.getWorldsLocation() + filename);
						closeWindow();
					}
				});
				cur.setFont(bFont);
				cur.minWidthProperty().bind(worlds.widthProperty().subtract(10));
				worlds.getChildren().add(cur);
			}
		}catch(IOException ex){
			log.error(ex);
		}
		primaryStage.show();
	}
	
	public static WorldSelector startWorldSelector(Stage primaryStage, ComponentSelectorController ds) throws IOException{
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(WorldSelector.class.getResource("worldSelector.fxml"));
		
		VBox v = (VBox)loader.load();
		
		WorldSelector cr = (WorldSelector)loader.getController();
		cr.start(primaryStage, v, ds);
		return cr;
	}
	
	@FXML
	public void addNewWorld(){
		final File f = new File(AppConfig.getWorldsLocation() + newWorldName.getText());
		f.mkdirs();
		
		Button cur = new Button(f.getName());
		cur.setOnAction(new EventHandler<ActionEvent>(){
			public void handle(ActionEvent event) {
				selectedFile = new File(AppConfig.getWorldsLocation() + f.getName());
				closeWindow();
			}
		});
		cur.setFont(bFont);
		cur.minWidthProperty().bind(worlds.widthProperty().subtract(10));
		worlds.getChildren().add(cur);
	}
}
