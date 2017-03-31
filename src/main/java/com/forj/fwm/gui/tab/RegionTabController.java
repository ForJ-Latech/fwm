package com.forj.fwm.gui.tab;

import java.net.URL;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import com.forj.fwm.backend.Backend;
import com.forj.fwm.backend.dao.RegionDao;
import com.forj.fwm.entity.Region;
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
import javafx.scene.control.TextInputControl;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;


public class RegionTabController implements Saveable {
	private static Logger log = Logger.getLogger(RegionTabController.class);
	private Region region;
	private MainController mc;
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
	private TextField name;
	
	@FXML
	private TextArea attributes;
	
	@FXML
	private TextArea description;
	
	@FXML
	private TextArea history;
	
	private AddableImage image;
	
	@FXML
	private Tab tabHead;
	
	private TextInputControl[] thingsThatCanChange;
	
	@FXML
	VBox rhsVbox;
	
	@FXML
	public void save(){
		getAllTexts();
		try{
			Backend.getRegionDao().createIfNotExists(region);
			Backend.getRegionDao().update(region);
			Backend.getRegionDao().refresh(region);
			log.debug("Save successfull!");
			log.debug("region id: " + region.getID());
		}catch(SQLException e){
			log.error(e);
		}
	}
	
	public void start(Tab rootLayout, Region region) throws Exception {
		this.region = region;
		if(App.worldFileUtil.findMultimedia(region.getImageFileName()) != null)
		{
			image = new AddableImage(App.worldFileUtil.findMultimedia(region.getImageFileName()));
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
		
		
		thingsThatCanChange = new TextInputControl[] {history, description, attributes, name};
		name.textProperty().addListener(nameListener);
		log.debug("start region tab controller called");
		setAllTexts(region);
		
		for(TextInputControl c: thingsThatCanChange){
			c.setOnKeyTyped(saveEvent);
		}
		
		started = true;
	}

	private void setAllTexts(Region region){
		tabHead.setText(region.getName());
		history.setText(region.getHistory());
		description.setText(region.getDescription());
		attributes.setText(region.getAttributes());
		name.setText(region.getName());
	}
	
	private void getAllTexts()
	{
		region.setImageFileName(image.getFilename());
		region.setHistory(history.getText());
		region.setDescription(description.getText());
		region.setAttributes(attributes.getText());
		region.setName(name.getText());
	}
	
	private static boolean started = false;

	public static boolean getStarted() {
		return started;
	}


	public static RegionTabController startRegionTab(Region region) throws Exception {
		log.debug("static startRegionTab called.");
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(RegionTabController.class.getResource("regionTab.fxml"));
		Tab rootLayout = (Tab)loader.load();
		RegionTabController cr = (RegionTabController)loader.getController();
		cr.start(rootLayout, region);
		started = true;
		return cr;
	}
	
	public Tab getTab(){
		return tabHead;
	}

	public Object getThing() {
		return region;
	}
	
}
