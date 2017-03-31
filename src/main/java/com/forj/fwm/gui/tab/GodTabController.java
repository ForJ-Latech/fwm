package com.forj.fwm.gui.tab;

import java.awt.event.TextEvent;
import java.io.File;
import java.net.URI;
import java.net.URL;
import java.sql.SQLException;

import javax.swing.event.DocumentEvent.EventType;
import javax.swing.text.TextAction;

import org.apache.log4j.Logger;

import com.forj.fwm.backend.Backend;
import com.forj.fwm.backend.dao.GodDao;
import com.forj.fwm.entity.God;
import com.forj.fwm.entity.Statblock;
import com.forj.fwm.gui.AddableImage;
import com.forj.fwm.gui.MainController;
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
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.control.TextInputControl;

public class GodTabController implements Saveable  {
	private static Logger log = Logger.getLogger(GodTabController.class);
	private God god;
	
	private ChangeListener<String> nameListener = new ChangeListener<String>(){
		public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
			tabHead.setText(newValue);
		}
	};
	
	private EventHandler<Event> saveEvent = new EventHandler<Event>(){
		public void handle(Event event){
			log.debug("Save event firing!");
			save();
		}
	};
	
	@FXML
	private VBox rhsVbox;
	
	private AddableImage image;
	
	@FXML
	private TextField name;
	
	@FXML
	private TextField pantheon;
	
	@FXML
	private TextField gender;
	
	@FXML
	private TextField race;
	
	@FXML
	private TextField godClass;
	
	@FXML
	private TextArea attributes;
	
	@FXML
	private TextArea description;
	
	@FXML
	private TextArea history;
	
	
	@FXML
	private Tab tabHead;
	
	private TextInputControl[] thingsThatCanChange; 
	
	@FXML
	public void save(){
		getAllTexts();
		
		try{
			Backend.getGodDao().createIfNotExists(god);
			Backend.getGodDao().update(god);
			Backend.getGodDao().refresh(god);
			log.debug("Save successfull!");
			log.debug("god id: " + god.getID());
		}catch(SQLException e){
			log.error(e);
		}
	}
	
	public void start(Tab rootLayout, God god) throws Exception {
		this.god = god;
		if(App.worldFileUtil.findMultimedia(god.getImageFileName()) != null)
		{
			image = new AddableImage(App.worldFileUtil.findMultimedia(god.getImageFileName()));
		}
		else
		{
			image = new AddableImage();
		}
		image.setOnImageChanged(new EventHandler<Event>(){
			public void handle(Event event) {
				save();
			}
		});
		image.setVisible(true);
		rhsVbox.getChildren().add(0, image);
		thingsThatCanChange = new TextInputControl[] {history, description, attributes, godClass, race, gender, pantheon, name};
		name.textProperty().addListener(nameListener);
		log.debug("start god tab controller called");
		setAllTexts(god);
		
		for(TextInputControl c: thingsThatCanChange){
			c.setOnKeyTyped(saveEvent);
		}
		
		started = true;
		
	}

	private void setAllTexts(God god){
		if(god.getStatblock() != null)
		{
			try {
				god.setStatblock(Backend.getStatblockDao().queryForSameId(god.getStatblock()));
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				log.error(e);
			}
		}
		tabHead.setText(god.getName());
		history.setText(god.getHistory());
		description.setText(god.getDescription());
		attributes.setText(god.getAttributes());
		godClass.setText(god.getClass_());
		race.setText(god.getRace());
		gender.setText(god.getGender());
		name.setText(god.getName());
		pantheon.setText(god.getPantheon());
	}
	
	private void getAllTexts()
	{
		god.setImageFileName(image.getFilename());
		god.setHistory(history.getText());
		god.setDescription(description.getText());
		god.setGender(gender.getText());
		god.setClass_(godClass.getText());
		god.setRace(race.getText());
		god.setAttributes(attributes.getText());
		god.setName(name.getText());
		god.setPantheon(pantheon.getText());
	}
	
	private static boolean started = false;

	public static boolean getStarted() {
		return started;
	}


	public static GodTabController startGodTab(God god) throws Exception {
		log.debug("static startGodTab called.");
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(GodTabController.class.getResource("godTab.fxml"));
		Tab rootLayout = (Tab)loader.load();
		GodTabController cr = (GodTabController)loader.getController();
		cr.start(rootLayout, god);
		started = true;
		return cr;
	}
	
	@FXML
	public void showStatBlock() throws Exception{
		if(god.getID() != -1){
			log.debug("statblock is being brought up.");
			if (god.getStatblock() == null)
			{
				log.debug("statblock is null.");
				god.setStatblock(new Statblock());
				god.getStatblock().setDescription("Lmao descriptions eyyy lmao. ");
			}	
		App.getStatBlockController().show(god.getStatblock(), this);
		}
	}
	
	public Tab getTab(){
		return tabHead;
	}

	public Object getThing() {
		return god;
	}
	
	
}
