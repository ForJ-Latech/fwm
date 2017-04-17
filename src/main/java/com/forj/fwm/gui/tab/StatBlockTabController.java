package com.forj.fwm.gui.tab;

import java.net.URL;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import com.forj.fwm.backend.Backend;
import com.forj.fwm.conf.WorldConfig;
import com.forj.fwm.entity.God;
import com.forj.fwm.entity.Searchable;
import com.forj.fwm.entity.Statblock;
import com.forj.fwm.gui.MainController;
import com.forj.fwm.gui.InteractionList.ListController;
import com.forj.fwm.gui.component.AddableImage;
import com.forj.fwm.startup.App;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Tab;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputControl;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class StatBlockTabController implements Saveable {
	
	private ChangeListener<String> nameListener = new ChangeListener<String>(){
		public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
			tabHead.setText(newValue);
		}
	};
	
	private EventHandler<Event> saveEvent = new EventHandler<Event>(){
		public void handle(Event event){
			log.debug("Save event firing!");
			if(!WorldConfig.getManualSaveOnly()){
				fullSave();
			}
		}
	};
	
	@FXML
	VBox mainVbox;
	
	@FXML
	Tab tabHead;
	
	@FXML
	HBox nameArea;
	
	@FXML
	TextField nameText;
	
	@FXML
	TextArea statBlockText;
	
	private static Logger log = Logger.getLogger(StatBlockTabController.class);
	
	private void getAllTexts(){
		stat.setName(nameText.getText());
		stat.setDescription(statBlockText.getText());
		log.debug("getting Description to save");
		log.debug("stat.getDesc: " + stat.getDescription());
		log.debug("statBlockText.get: " +statBlockText.getText());
	}
	
	public void fullSave(){
		getAllTexts();
		try{
			Backend.getStatblockDao().createIfNotExists(stat);
			Backend.getStatblockDao().update(stat);
			Backend.getStatblockDao().refresh(stat);
			log.debug("Save successfull!");
			log.debug("stat id: " + stat.getID());
			log.debug("truth: " + stat.getDescription());
			App.getMainController().addStatus("Successfully saved Statblock " + stat.getName() + " ID: " + stat.getID());
			beSaved.simpleSave();
		}catch(SQLException e){
			log.error(e.getStackTrace());
		}
	}
	
	private Statblock stat;
	
	private Saveable beSaved;
	
	private TextInputControl[] thingsThatCanChange; 
	public void start(Tab rootLayout, Statblock s, Saveable beSaved) throws Exception {
		log.debug("statBlockTab.start called");
		stat = s;
		this.beSaved = beSaved ;
		this.tabHead.setText(((Searchable)beSaved.getThing()).getName());
		setAllTexts(stat);
		
		thingsThatCanChange = new TextInputControl[] {nameText, statBlockText};
		nameText.textProperty().addListener(nameListener);
		for(TextInputControl c: thingsThatCanChange){
			c.setOnKeyReleased(saveEvent);
		}
		
	}
	
	private void setAllTexts(Statblock s){
		if(stat.getName() == null || stat.getName().equals("")){
			System.out.println("1");
			nameArea.setVisible(false);
			mainVbox.getChildren().remove(nameArea);
		}
		else
		{
			System.out.println("2");
			if(!mainVbox.getChildren().contains(nameArea)){
				System.out.println("3");
				mainVbox.getChildren().add(0, nameArea);
			}
			System.out.println("4");
			nameArea.setVisible(true);
			nameText.setText(stat.getName());
		}
		System.out.println("5");
		statBlockText.setText(stat.getDescription());
		log.debug(stat.getDescription());
	}
	
	public static StatBlockTabController startStatBlockTabController(Statblock s, Saveable beSaved) throws Exception {
		log.debug("static startStatBlockTabController called.");
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(StatBlockTabController.class.getResource("statBlockTab.fxml"));
		Tab rootLayout = (Tab)loader.load();
		StatBlockTabController cr = (StatBlockTabController)loader.getController();
		cr.start(rootLayout, s, beSaved);
		return cr;
	}

	public Searchable getThing() {
		return stat;
	}

	public Tab getTab() {
		return tabHead;
	}
	
	public AddableImage getAddableImage() {
		return null;
	}
	
	public ListController getListController(){
		return null;
	}

	public void simpleSave() {
		fullSave();
		
	}

	public void relationalSave() {
		fullSave();
	}
}
