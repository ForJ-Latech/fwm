package com.forj.fwm.gui.InteractionList;

import org.apache.log4j.Logger;

import com.forj.fwm.conf.AppConfig;
import com.forj.fwm.conf.WorldConfig;
import com.forj.fwm.entity.Interaction;
import com.forj.fwm.entity.Searchable;
import com.forj.fwm.gui.component.AddableImage;
import com.forj.fwm.gui.component.AddableSound;
import com.forj.fwm.gui.component.Saveable;
import com.forj.fwm.startup.App;
import com.sun.javafx.scene.control.skin.TextAreaSkin;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Tab;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputControl;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;


public class InteractionController implements Saveable {
	private static Logger log = Logger.getLogger(InteractionController.class);
	private Interaction interaction;
	private TextInputControl[] thingsThatCanChange;
	private Saveable s;
	private static boolean started = false;
	
	@FXML private TextField partyMember;
	@FXML private TextArea description;
	@FXML private HBox singleInteraction;
	
	private EventHandler<Event> saveEvent = new EventHandler<Event>(){
		public void handle(Event event){
			log.debug("Save event firing!");
			getAllTexts();
			if(!AppConfig.getManualSaveOnly()){	
				fullSave();
			}
		}
	};
	
	public void fullSave(){
			s.relationalSave();
			App.getMainController().addStatus("Successfully saved Interaction ID: " + interaction.getID());
			log.debug("Save successfull!");
			log.debug("Interaction id: " + interaction.getID());
	}
	
	public void start(HBox rootLayout, Interaction interaction, Saveable s) throws Exception {
		this.interaction = interaction;
		thingsThatCanChange = new TextInputControl[] {partyMember, description};
		log.debug("start interaction controller called");
		setAllTexts(interaction);
		this.s = s;
		
		for(TextInputControl c: thingsThatCanChange){
			c.setOnKeyReleased(saveEvent);
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
		}
		
		started = true;
	}

	private void setAllTexts(Interaction interaction){
		partyMember.setText(interaction.getPlayerCharacter());
		description.setText(interaction.getDescription());
	}
	
	private void getAllTexts()
	{
		interaction.setPlayerCharacter(partyMember.getText());
		interaction.setDescription(description.getText());
	}
	
	public static InteractionController startInteraction(Interaction interaction, Saveable s) throws Exception {
		log.debug("static startInteraction called.");
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(InteractionController.class.getResource("interaction.fxml"));
		HBox rootLayout = (HBox)loader.load();
		InteractionController cr = (InteractionController)loader.getController();
		cr.start(rootLayout, interaction, s);
		started = true;
		return cr;
	}
	
	public HBox getSingleInteraction(){
		return singleInteraction;
	}
	
	public static boolean getStarted() {
		return started;
	}
	
	public TextField getPartyMember(){
		return partyMember;
	}

	public Searchable getThing() {
		return interaction;
	}
	
	public Interaction getInteraction(){
		return interaction;
	}

	public Tab getTab() {
		// dnn
		return null;
	}

	public void simpleSave() {
		fullSave();
	}

	public void relationalSave() {
		fullSave();
	}

	public void autoUpdateTab() {
		// dnn
	}

	public void manualUpdateTab() {
		// dnn
	}
}

