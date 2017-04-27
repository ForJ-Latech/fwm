package com.forj.fwm.gui.tab;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.forj.fwm.backend.Backend;
import com.forj.fwm.conf.WorldConfig;
import com.forj.fwm.entity.Event;
import com.forj.fwm.entity.God;
import com.forj.fwm.entity.Interaction;
import com.forj.fwm.entity.Npc;
import com.forj.fwm.entity.Region;
import com.forj.fwm.entity.Searchable;
import com.forj.fwm.gui.RelationalField;
import com.forj.fwm.gui.RelationalList;
import com.forj.fwm.gui.SearchList;
import com.forj.fwm.gui.InteractionList.ListController;
import com.forj.fwm.gui.component.AddableImage;
import com.forj.fwm.gui.component.AddableSound;
import com.forj.fwm.startup.App;
import com.sun.javafx.scene.control.skin.TextAreaSkin;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Accordion;
import javafx.scene.control.Tab;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputControl;
import javafx.scene.control.TitledPane;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class EventTabController implements Saveable {
	private static Logger log = Logger.getLogger(EventTabController.class);
	private Event event;
	private ListController interactionController;
	private AddableImage image;
    private AddableSound sound;
    private TextInputControl[] thingsThatCanChange;
	private RelationalList npcRelation, godRelation;
	private SearchList.EntitiesToSearch tabType = SearchList.EntitiesToSearch.EVENT;
	private RelationalField regionRelation;
	private List<Region> myRegion = new ArrayList<Region>();
	
	@FXML private StackPane regionPane;
    @FXML private TextField name;
	@FXML private TextArea description, attributes, history;
	@FXML private VBox interactionContainer, rhsVbox;
	@FXML private Tab tabHead;
	@FXML private Accordion accordion;
	
	private ChangeListener<String> nameListener = new ChangeListener<String>(){
		public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
			tabHead.setText(newValue);
		}		
	};
	
	private EventHandler<javafx.event.Event> saveEvent = new EventHandler<javafx.event.Event>() {
		public void handle(javafx.event.Event event) {
			log.debug("Save event firing!");
			if(!WorldConfig.getManualSaveOnly()){
				simpleSave();
			}
		}
	};
	
	public void startRelationalList() throws Exception {
		accordion.getPanes().clear();
		
		npcRelation = RelationalList.createRelationalList(this, App.toListSearchable(event.getNpcs()), "NPCs", true, true, tabType, SearchList.EntitiesToSearch.NPC);
		accordion.getPanes().add((TitledPane) npcRelation.getOurRoot());
		
		godRelation = RelationalList.createRelationalList(this, App.toListSearchable(event.getGods()), "Gods", true, true, tabType, SearchList.EntitiesToSearch.GOD);
		accordion.getPanes().add((TitledPane) godRelation.getOurRoot());
		
		myRegion.clear();
		if (event.getRegion() != null){
			event.getRegion().setName(Backend.getRegionDao().queryForEq("ID", event.getRegion().getID()).get(0).getName());
			event.getRegion().setImageFileName(Backend.getRegionDao().queryForEq("ID", event.getRegion().getID()).get(0).getImageFileName());
			myRegion.add(event.getRegion());
		}
		regionRelation = RelationalField.createRelationalList(this, App.toListSearchable(myRegion), "Region", true, true, tabType, SearchList.EntitiesToSearch.REGION);
		regionPane.getChildren().add(regionRelation.getOurRoot());

	}
	
	public void updateTab(){
//		try {
//			setAllTexts(region);
//			Backend.getRegionDao().update(region);
//			Backend.getRegionDao().refresh(region);
//			godRelation.clearList();
//			godRelation.populateList();
//			npcRelation.clearList();
//			npcRelation.populateList();
//			eventRelation.clearList();
//			eventRelation.populateList();
//			regionRelation.clearList();
//			regionRelation.populateList();
//			
//			/*
//			
//			if (region.getSuperRegion() != null) System.out.println("super = " + region.getSuperRegion().getName() + " with ID " +  region.getSuperRegion().getID());
//			System.out.println("this = " + region.getName() + " with ID " +  region.getID());
//			
//			if (region.getSubRegions() != null)
//			for(Region c: region.getSubRegions()){
//				System.out.println("sub = " + c.getName() + " with ID " +  c.getID());
//			}
//			
//			for (OMRegionRegion omreg : Backend.getOmRegionRegionDao().queryForLike("superRegion_id", region.getID())) {
//				System.out.println("!!!!!!!!!!" +omreg.getSubRegion().getName() + "  -  " + omreg.getSuperRegion().getName());
//			}
//			
//			for (OMRegionRegion omreg : Backend.getOmRegionRegionDao().queryForLike("superRegion_id", region.getSuperRegion().getID())) {
//				System.out.println("!!!!!!!!!!" +omreg.getSubRegion().getName() + "  -  " + omreg.getSuperRegion().getName());
//			}
//			
//			for (OMRegionRegion omreg : Backend.getOmRegionRegionDao().queryForLike("superRegion_id", region.getSubRegions().get(0).getID())) {
//				System.out.println("!!!!!!!!!!" +omreg.getSubRegion().getName() + "  -  " + omreg.getSuperRegion().getName());
//			}
//			
//			*/
//			
//			
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
	}
	
	public void getAllRelations(){
//		log.debug("in get all relations.");
//		for(Npc n: (List<Npc>)(List<?>)npcRelation.getList()){
//			log.debug("\t" + n.getID());
//		}
		event.setInteractions(new ArrayList<Interaction>((List<Interaction>)(List<?>)interactionController.getAllInteractions()));
		event.setGods(new ArrayList<God>((List<God>)(List<?>)godRelation.getList()));
		event.setNpcs(new ArrayList<Npc>((List<Npc>)(List<?>)npcRelation.getList()));
//		event.setRegion(new Region());	
		
		if (!regionRelation.getList().isEmpty()){
			Region newRegion = new ArrayList<Region>((List<Region>)(List<?>)regionRelation.getList()).get(0);
			event.setRegion(newRegion);
		}
	}
	
	@FXML
	public void fullSave(){
		getAllTexts();
		getAllRelations();
		if(tabHead.getText()!=null && !tabHead.getText().equals(""))
		{
			// pass
		}
		else {
			log.debug("can't save, no name");
			App.getMainController().addStatus("Unable to save without a name.");
			return;
		}
		try{
			Backend.getEventDao().saveFullEvent(event);
			Backend.getEventDao().refresh(event);
			log.debug("Save successfull!");
			log.debug("event id: " + event.getID());
			App.getMainController().addStatus("Successfully saved full " + Event.WHAT_IT_DO + " " + event.getName() + " ID: " + event.getID());
		}catch(SQLException e){
			log.error(e);
			e.printStackTrace();
		}
	}
	
	public void simpleSave() {
		getAllTexts();
		if(tabHead.getText()!=null && !tabHead.getText().equals(""))
		{
			// pass
		}
		else {
			log.debug("can't save, no name");
			App.getMainController().addStatus("Unable to save without a name.");
			return;
		}
		try{
			Backend.SaveSimpleSearchable(event);
			log.debug("Save successfull!");
			log.debug("event id: " + event.getID());
			App.getMainController().addStatus("Successfully saved base " + Event.WHAT_IT_DO + " " + event.getName() + " ID: " + event.getID());
		}catch(SQLException e){
			log.error(e);
			e.printStackTrace();
		}
	}

	public void relationalSave() {
		getAllRelations();
		if(tabHead.getText()!=null && !tabHead.getText().equals(""))
		{
			// pass
		}
		else {
			log.debug("can't save, no name");
			App.getMainController().addStatus("Unable to save without a name.");
			return;
		}
		try{
			Backend.getEventDao().saveRelationalEvent(event);
			log.debug("Save successfull!");
			log.debug("event id: " + event.getID());
			App.getMainController().addStatus("Successfully saved " + Event.WHAT_IT_DO + " relations " + event.getName() + " ID: " + event.getID());
		}catch(SQLException e){
			log.error(e);
			e.printStackTrace();
		}
	}
	
	
	public void start(Tab rootLayout, Event event) throws Exception {
		this.event = event;
		interactionController = ListController.startInteractionList(event.getInteractions(), this);
		interactionContainer.getChildren().add(interactionController.getInteractionListContainer());
		
		if(App.worldFileUtil.findMultimedia(event.getSoundFileName()) != null)
		{
			sound = new AddableSound(App.worldFileUtil.findMultimedia(event.getSoundFileName()));
		}
		else
		{
			sound = new AddableSound();
		}
		
		if(App.worldFileUtil.findMultimedia(event.getImageFileName()) != null)
		{
			image = new AddableImage(App.worldFileUtil.findMultimedia(event.getImageFileName()));
		}
		else
		{
			image = new AddableImage();
		}
		image.setOnImageChanged(new EventHandler<javafx.event.Event>(){
			public void handle(javafx.event.Event event) {
				if(!WorldConfig.getManualSaveOnly()){	
					fullSave();
				}
			}
		});
		image.setVisible(true);
		rhsVbox.getChildren().add(0, image);
		
		
		thingsThatCanChange = new TextInputControl[] {description, attributes, name, history};
		name.textProperty().addListener(nameListener);
		log.debug("start event tab controller called");
		setAllTexts(event);
		
		for(TextInputControl c: thingsThatCanChange){
			if (c.getClass() == TextArea.class){
				c.addEventFilter(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
					public void handle(KeyEvent event) {
					    if (event.getCode().equals(KeyCode.TAB)) {
					        Node node = (Node) event.getSource();
					        if (node instanceof TextField) {
					            TextAreaSkin skin = (TextAreaSkin) ((TextField)node).getSkin();
					            if (event.isShiftDown()) {
					                skin.getBehavior().traversePrevious();
					            }
					            else {
					                skin.getBehavior().traverseNext();
					            }               
					        }
					        else if (node instanceof TextArea) {
					            TextAreaSkin skin = (TextAreaSkin) ((TextArea)node).getSkin();
					            if (event.isShiftDown()) {
					                skin.getBehavior().traversePrevious();
					            }
					            else {
					                skin.getBehavior().traverseNext();
					            }
					        }

					        event.consume();
					    }
					}
			    });
			}
			c.setOnKeyReleased(saveEvent);
		}
		
		startRelationalList();
		
		App.getMainController().getTabPane().getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Tab>() {
		    public void changed(ObservableValue<? extends Tab> observable, Tab oldTab, Tab newTab) {
		        if(newTab == getTab()) {
		        	updateTab();
		        }
		    }
		});
		
		
		Platform.runLater(new Runnable() {
			public void run() {
				name.requestFocus();
			}
		});
		
		tabHead.setOnSelectionChanged(new EventHandler(){
			public void handle(javafx.event.Event arg0) {
				sound.stop();
			}
		});
		
		started = true;
	}

	private void setAllTexts(Event event){
		tabHead.setText(event.getName());
		description.setText(event.getDescription());
		attributes.setText(event.getAttributes());
		name.setText(event.getName());
		history.setText(event.getHistory());
	}
	
	private void getAllTexts()
	{
		event.setImageFileName(image.getFilename());
		event.setSoundFileName(sound.getFilename());
		event.setDescription(description.getText());
		event.setHistory(history.getText());
		event.setName(name.getText());
		event.setAttributes(attributes.getText());
	}
	
	private static boolean started = false;

	public static boolean getStarted() {
		return started;
	}


	public static EventTabController startEventTab(Event event) throws Exception {
		log.debug("static startRegionTab called.");

		Event ourE = event;
		if(event != null){
			log.debug("ourR got filled from backend");
			ourE = Backend.getEventDao().getFullEvent(event.getID());
		}else
		{
			ourE = new Event();
			
		}
		
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(EventTabController.class.getResource("eventTab.fxml"));
		Tab rootLayout = (Tab)loader.load();
		EventTabController cr = (EventTabController)loader.getController();
		cr.start(rootLayout, ourE);
		started = true;
		return cr;
		
	}
	
	
	
	@FXML
	public void changeSound() throws Exception{
		sound.changeSound();
		if(!WorldConfig.getManualSaveOnly()){
			fullSave();
		}
		sound.play();
	}
	
	@FXML 
	public void playSound() throws Exception{
		if(sound != null)
		{
			sound.play();
		}
	}
	
	public Tab getTab(){
		return tabHead;
	}

	public Searchable getThing() {
		return event;
	}

	public AddableImage getAddableImage() {
		return image;
	}
	public ListController getListController(){
		return interactionController;
	}
	public void nameFocus(){
		name.requestFocus();
	}
}
