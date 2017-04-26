package com.forj.fwm.gui.tab;

import java.sql.SQLException;

import org.apache.log4j.Logger;

import com.forj.fwm.backend.Backend;
import com.forj.fwm.conf.WorldConfig;
import com.forj.fwm.entity.Npc;
import com.forj.fwm.entity.Template;
import com.forj.fwm.gui.InteractionList.ListController;
import com.forj.fwm.gui.component.AddableImage;
import com.forj.fwm.gui.component.AddableSound;
import com.forj.fwm.gui.component.Openable;
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
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class TemplateTabController implements Saveable{
	private static Logger log = Logger.getLogger(TemplateTabController.class);
	
	@FXML private Tab tabHead;
    @FXML private TextField name;
    @FXML private TextArea fName, race, lName, classType, gender, age, description, history, attributes;
    @FXML private VBox interactionContainer, rhsVbox;
    @FXML private Accordion accordion;
    @FXML private StackPane godPane;
    
	private Openable open;
    
    private TextInputControl[] thingsThatCanChange; 
	
    private Template template;
    
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
			Backend.getTemplateDao().saveFullTemplate(template);
			log.debug("Save successfull!");
			log.debug("template id: " + template.getID());
			App.getMainController().addStatus("Successfully saved base Template " + template.getName() + " ID: " + template.getID());
		}catch(SQLException e){
			log.error(e);
			e.printStackTrace();
		}
	}
    
    private void setAllTexts(Template template){
		if(template.getStatblock() != null)
		{
			try {
				template.setStatblock(Backend.getStatblockDao().queryForSameId(template.getStatblock()));
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				log.error(e);
			}
		}
		name.setText(template.getName());
		tabHead.setText(template.getName());
		history.setText(template.getHistory());
		description.setText(template.getDescription());
		attributes.setText(template.getAttributes());
		classType.setText(template.getClassType());
		race.setText(template.getRace());
		gender.setText(template.getGender());
		fName.setText(template.getfName());
		lName.setText(template.getlName());
		age.setText(template.getAge());
	}
	
	private void getAllTexts()
	{
		template.setName(name.getText());
		template.setHistory(history.getText());
		template.setDescription(description.getText());
		template.setGender(gender.getText());
		template.setClassType(classType.getText());
		template.setRace(race.getText());
		template.setAttributes(attributes.getText());
		template.setfName(fName.getText());
		template.setlName(lName.getText());
		template.setAge(age.getText());
	}
	public void start(Tab rootLayout, Template template, Openable open) throws Exception {
		this.template = template;
		thingsThatCanChange = new TextInputControl[] {history , description, lName ,fName ,gender ,attributes ,race ,classType};
		name.textProperty().addListener(nameListener);
		log.debug("start npc tab controller called");
		setAllTexts(template);
		this.open = open;
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
	}

	public static TemplateTabController startTemplateTab(Template template, Openable open) throws Exception {
		log.debug("static startNpcTab called.");

		Template ourT = template;
		if(template != null){
			log.debug("ourN got filled from backend");
			template = Backend.getTemplateDao().getFullTemplate(template.getID());
		}else
		{
			ourT = new Template();
			
		}
		
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(TemplateTabController.class.getResource("templateTab.fxml"));
		Tab rootLayout = (Tab)loader.load();
		TemplateTabController cr = (TemplateTabController)loader.getController();
		cr.start(rootLayout, ourT, open);
		return cr;
	}
	
	@FXML
	public void newFromTemplate() throws Exception{
		open.open(template.newFromTemplate());
	}
	
	public Tab getTab(){
		return tabHead;
	}

	public Template getThing() {
		return template;
	}

	public void fullSave() {
		simpleSave();
	}

	public void relationalSave() {
		simpleSave();
	}

	public AddableImage getAddableImage() {
		return null;
	}

	public ListController getListController() {
		return null;
	}
}
