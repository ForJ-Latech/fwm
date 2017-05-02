package com.forj.fwm.gui.tab;

import java.sql.SQLException;

import org.apache.log4j.Logger;

import com.forj.fwm.backend.Backend;
import com.forj.fwm.conf.WorldConfig;
import com.forj.fwm.entity.Searchable;
import com.forj.fwm.entity.Statblock;
import com.forj.fwm.gui.MainController;
import com.forj.fwm.gui.InteractionList.ListController;
import com.forj.fwm.gui.component.AddableImage;
import com.forj.fwm.gui.component.AddableSound;
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
	private static Logger log = Logger.getLogger(StatBlockTabController.class);
	private TextInputControl[] thingsThatCanChange; 
	private Statblock stat;
	private Saveable beSaved;
	private boolean wasNamed = false;
	
	@FXML VBox mainVbox;
	@FXML public Tab tabHead;
	@FXML HBox nameArea;
	@FXML TextField nameText;
	@FXML TextArea statBlockText;
	
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
	
	private void getAllTexts(){
		if(wasNamed){
			stat.setName("$" + nameText.getText());
		}else{
			stat.setName("");
		}
		stat.setDescription(statBlockText.getText());
		log.debug("getting Description to save");
		log.debug("stat.getDesc: " + stat.getDescription());
		log.debug("statBlockText.get: " +statBlockText.getText());
	}
	
	public void fullSave(){
		getAllTexts();
		// it needs to be named well. 
		if(wasNamed && stat.getName().length() < 2){
			log.info("Can't save no name");
			App.getMainController().addStatus("Cannot save Statblock with ID: " + stat.getID() + " no name!");
			return;
		}
		try{
			Backend.getStatblockDao().createOrUpdate(stat);
			Backend.getStatblockDao().refresh(stat);
			log.debug("Save successfull!");
			log.debug("stat id: " + stat.getID());
			log.debug("description: " + stat.getDescription());
			App.getMainController().addStatus("Successfully saved Statblock " + 
			("".equals(stat.getShownName()) ? "without name": stat.getShownName()) + 
			" ID: " + stat.getID());
			beSaved.simpleSave();
		}catch(SQLException e){
			log.error(e.getStackTrace());
		}
	}
	
	public void start(Tab rootLayout, Statblock s, Saveable beSaved) throws Exception {
		log.debug("statBlockTab.start called");
		stat = s;
		this.beSaved = beSaved ;
		log.debug("Stat ID:" + s.getID());
		setAllTexts(stat);
		
		thingsThatCanChange = new TextInputControl[] {nameText, statBlockText};
		nameText.textProperty().addListener(nameListener);
		for(TextInputControl c: thingsThatCanChange){
			c.setOnKeyReleased(saveEvent);
		}
	}
	
	private void setAllTexts(Statblock s){
		if(stat.getName() == null || "".equals(stat.getName())) {
			nameArea.setVisible(false);
			mainVbox.getChildren().remove(nameArea);
			tabHead.setText(beSaved.getThing().getShownName());
		}
		else {
			wasNamed = true;
			if(!mainVbox.getChildren().contains(nameArea)){
				mainVbox.getChildren().add(0, nameArea);
			}
			nameArea.setVisible(true);
			// we ignore the first char, so that we can tell if something had been named in the past. 
			nameText.setText(stat.getName().substring(1));
			tabHead.setText(stat.getName().substring(1));
		}
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

	public void nameFocus(){
		log.debug("Focus from statblock");
		App.getMainController().getStage().requestFocus();
	}
	
	public Searchable getThing() {
		return stat;
	}

	public Tab getTab() {
		return tabHead;
	}
	
	public void simpleSave() {
		fullSave();
	}

	public void relationalSave() {
		fullSave();
	}
	
	public AddableImage getAddableImage() {
		return null;
	}
	
	public ListController getListController(){
		return null;
	}
	
	public AddableSound getAddableSound(){
		return null;
	}
}
