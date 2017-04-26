package com.forj.fwm.gui.tab;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.forj.fwm.backend.Backend;
import com.forj.fwm.conf.WorldConfig;
import com.forj.fwm.entity.God;
import com.forj.fwm.entity.Interaction;
import com.forj.fwm.entity.Npc;
import com.forj.fwm.entity.Region;
import com.forj.fwm.entity.Statblock;
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
import javafx.event.Event;
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

public class NpcTabController implements Saveable {
	private static Logger log = Logger.getLogger(NpcTabController.class);
	private Npc npc;
	private ListController interactionController;
	private AddableImage image;
    private AddableSound sound;
	private TextInputControl[] thingsThatCanChange; 
	private List<Npc> fam = new ArrayList<Npc>();
	private List<God> myGod = new ArrayList<God>();
	private RelationalList npcRelation, eventRelation, regionRelation;
	private RelationalField godRelation;
	private SearchList.EntitiesToSearch tabType = SearchList.EntitiesToSearch.NPC;
	
	@FXML private Tab tabHead;
    @FXML private TextField fName, race, lName, classType, gender, age;
    @FXML private TextArea description, history, attributes;
    @FXML private VBox interactionContainer, rhsVbox;
    @FXML private Accordion accordion;
    @FXML private StackPane godPane;
	
	private ChangeListener<String> nameListener = new ChangeListener<String>(){
		public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
			tabHead.setText(newValue);
		}
	};
	
	private EventHandler<Event> saveEvent = new EventHandler<Event>(){
		public void handle(Event event){
			log.debug("Save event firing!");
			if(!WorldConfig.getManualSaveOnly()){	
				simpleSave();
			}
		}
	};
	private EventHandler<Event> lnameEvent = new EventHandler<Event>(){
		public void handle(Event event){
			updateFamily(true);
		}
	};

	public void startRelationalList() throws Exception {
		
		fam.clear();
		fam.addAll(Backend.getNpcDao().getFamily(npc));

		accordion.getPanes().clear();
		npcRelation = RelationalList.createRelationalList(this, App.toListSearchable(fam), "Family", false, false, tabType, SearchList.EntitiesToSearch.NPC);
		accordion.getPanes().add((TitledPane) npcRelation.getOurRoot());
			
		eventRelation = RelationalList.createRelationalList(this, App.toListSearchable(npc.getEvents()), com.forj.fwm.entity.Event.WHAT_IT_DO + "s", true, true, tabType, SearchList.EntitiesToSearch.EVENT);
		accordion.getPanes().add((TitledPane) eventRelation.getOurRoot());
		
		regionRelation = RelationalList.createRelationalList(this, App.toListSearchable(npc.getRegions()), "Regions", true, true, tabType, SearchList.EntitiesToSearch.REGION);
		accordion.getPanes().add((TitledPane) regionRelation.getOurRoot());
		
		myGod.clear();
		if (npc.getGod() != null){
			npc.getGod().setName(Backend.getGodDao().queryForEq("ID", npc.getGod().getID()).get(0).getName());
			npc.getGod().setImageFileName(Backend.getGodDao().queryForEq("ID", npc.getGod().getID()).get(0).getImageFileName());
			myGod.add(npc.getGod());
		}
		godRelation = RelationalField.createRelationalList(this, App.toListSearchable(myGod), "God", true, true, tabType, SearchList.EntitiesToSearch.GOD);
		godPane.getChildren().add(godRelation.getOurRoot());

	}
	
	private void updateFamily(boolean save){
		if(tabHead.getText()!=null && save)
		{
			if(!WorldConfig.getManualSaveOnly()){
				simpleSave();
			}
		}
		fam.clear();
		fam.addAll(Backend.getNpcDao().getFamily(npc));
		npcRelation.clearList();
		npcRelation.populateList(App.toListSearchable(fam));
	}
	
	
	private void updateTab(){
//		try {
//			setAllTexts(npc);
//			Backend.getNpcDao().update(npc);
//			Backend.getNpcDao().refresh(npc);
//			godRelation.clearList();
//			godRelation.populateList();
//			npcRelation.clearList();
//			npcRelation.populateList();
//			eventRelation.clearList();
//			eventRelation.populateList();
//			regionRelation.clearList();
//			regionRelation.populateList();
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}

	}
	
	public void getAllRelations(){
		log.debug("in get all relations.");
//		for(Interaction n:  interactionController.getAllInteractions()){
//			log.debug("\t" + n.getID());
//		}
		npc.setInteractions(new ArrayList<Interaction>((List<Interaction>)(List<?>)interactionController.getAllInteractions()));
		npc.setRegions(new ArrayList<Region>((List<Region>)(List<?>)regionRelation.getList()));
		npc.setEvents(new ArrayList<com.forj.fwm.entity.Event>((List<com.forj.fwm.entity.Event>)(List<?>)eventRelation.getList()));	
		
		if (!godRelation.getList().isEmpty()){
			God newGod = new ArrayList<God>((List<God>)(List<?>)godRelation.getList()).get(0);
			npc.setGod(newGod);
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
			Backend.getNpcDao().saveFullNpc(npc);
			Backend.getNpcDao().refresh(npc);
			log.debug("Save successfull!");
			log.debug("npc id: " + npc.getID());
			App.getMainController().addStatus("Successfully saved full NPC " + npc.getFullName() + " ID: " + npc.getID());
		}catch(SQLException e){
			log.error(e);
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
			Backend.SaveSimpleSearchable(npc);
			log.debug("Save successfull!");
			log.debug("npc id: " + npc.getID());
			App.getMainController().addStatus("Successfully saved base Npc " + npc.getName() + " ID: " + npc.getID());
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
			Backend.getNpcDao().saveRelationalNpc(npc);
			log.debug("Save successfull!");
			log.debug("Npc id: " + npc.getID());
			App.getMainController().addStatus("Successfully saved Npc relations" + npc.getName() + " ID: " + npc.getID());
			
		
		}catch(SQLException e){
			log.error(e);
			e.printStackTrace();
		}
	}
	
	public void start(Tab rootLayout, Npc npc) throws Exception {
		this.npc = npc;
		
		interactionController = ListController.startInteractionList(npc.getInteractions(), this);
		interactionContainer.getChildren().add(interactionController.getInteractionListContainer());
		
		if(App.worldFileUtil.findMultimedia(npc.getSoundFileName()) != null)
		{
			sound = new AddableSound(App.worldFileUtil.findMultimedia(npc.getSoundFileName()));
		}
		else
		{
			sound = new AddableSound();
		}
		if(App.worldFileUtil.findMultimedia(npc.getImageFileName()) != null)
		{
			image = new AddableImage(App.worldFileUtil.findMultimedia(npc.getImageFileName()));
		}
		else
		{
			image = new AddableImage();
		}
		image.setOnImageChanged(new EventHandler<Event>(){
			public void handle(Event event) {
				if(!WorldConfig.getManualSaveOnly()){
					fullSave();
				}
			}
		});
		image.setVisible(true);
		rhsVbox.getChildren().add(0, image);
		
		thingsThatCanChange = new TextInputControl[] {history , description, lName ,fName ,gender ,attributes ,race ,classType, age};
		fName.textProperty().addListener(nameListener);
		log.debug("start npc tab controller called");
		setAllTexts(npc);
		
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
		
		thingsThatCanChange[2].setOnKeyReleased(lnameEvent);
		updateFamily(false);
		
		App.getMainController().getTabPane().getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Tab>() {
		    public void changed(ObservableValue<? extends Tab> observable, Tab oldTab, Tab newTab) {
		        if(newTab == getTab()) {
		        	updateTab();
		        }
		    }
		});
		
		Platform.runLater(new Runnable() {
			public void run() {
				fName.requestFocus();
			}
		});
		
		tabHead.setOnSelectionChanged(new EventHandler(){
			public void handle(Event arg0) {
				sound.stop();
			}
		});
		
		started = true;
	}

	private void setAllTexts(Npc npc){
		if(npc.getStatblock() != null)
		{
			try {
				npc.setStatblock(Backend.getStatblockDao().queryForSameId(npc.getStatblock()));
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				log.error(e);
			}
		}
		tabHead.setText(npc.getfName());
		history.setText(npc.getHistory());
		description.setText(npc.getDescription());
		attributes.setText(npc.getAttributes());
		classType.setText(npc.getClassType());
		race.setText(npc.getRace());
		gender.setText(npc.getGender());
		fName.setText(npc.getfName());
		lName.setText(npc.getlName());
		age.setText(npc.getAge());
	}
	
	private void getAllTexts()
	{
		npc.setHistory(history.getText());
		npc.setDescription(description.getText());
		npc.setGender(gender.getText());
		npc.setClassType(classType.getText());
		npc.setRace(race.getText());
		npc.setAttributes(attributes.getText());
		npc.setfName(fName.getText());
		npc.setlName(lName.getText());
		npc.setAge(age.getText());
		npc.setImageFileName(image.getFilename());
		npc.setSoundFileName(sound.getFilename());
	}
	
	private static boolean started = false;

	public static boolean getStarted() {
		return started;
	}

	public static NpcTabController startNpcTab(Npc npc) throws Exception {
		log.debug("static startNpcTab called.");

		Npc ourN = npc;
		if(npc != null){
			if(npc.getID() == -1){
				log.debug("Did not fill from backend, created by template.");
				ourN = npc;
			}
			else
			{
				log.debug("ourN got filled from backend");
				ourN = Backend.getNpcDao().getFullNpc(npc.getID());
			}
		}else
		{
			ourN = new Npc();
			
		}
		
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(NpcTabController.class.getResource("npcTab.fxml"));
		Tab rootLayout = (Tab)loader.load();
		NpcTabController cr = (NpcTabController)loader.getController();
		cr.start(rootLayout, ourN);
		started = true;
		return cr;
	}
	
	@FXML
	public void showStatBlock() throws Exception{
		if(npc.getID() != -1){
			log.debug("statblock is being brought up.");
			if (npc.getStatblock() == null)
			{
				log.debug("statblock is null.");
				npc.setStatblock(new Statblock());
				npc.getStatblock().setDescription("");
			}	
		App.getStatBlockController().show(npc.getStatblock(), this);
		}
		else{
			App.getMainController().addStatus("Error: Cannot open statblock of unsaved object.");
		}
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

	public Npc getThing() {
		return npc;
	}
	public AddableImage getAddableImage() {
		return image;
	}
	public ListController getListController(){
		return interactionController;
	}
	public void nameFocus(){
		fName.requestFocus();
	}
}
