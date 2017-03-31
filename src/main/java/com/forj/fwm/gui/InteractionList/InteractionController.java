package com.forj.fwm.gui.InteractionList;

import java.net.URL;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import com.forj.fwm.backend.Backend;
import com.forj.fwm.entity.Interaction;

import javafx.application.Application;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputControl;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;


public class InteractionController extends Application{
	private static Logger log = Logger.getLogger(InteractionController.class);
	private Interaction interaction = new Interaction();
	private EventHandler<Event> saveEvent = new EventHandler<Event>(){
		public void handle(Event event){
			log.debug("Save event firing!");
			save();
		}
	};
		
	
	@FXML
	private HBox singleInteraction;
	
	public HBox getSingleInteraction(){
		return singleInteraction;
	}
	
	@FXML
	private TextField partyMember;
	
	@FXML
	private TextField description;
	
	
	
	private TextInputControl[] thingsThatCanChange;
	
	@FXML
	public void save(){
		getAllTexts();
		
		try{
			Backend.getInteractionDao().createIfNotExists(interaction);
			Backend.getInteractionDao().update(interaction);
			Backend.getInteractionDao().refresh(interaction);
			log.debug("Save successfull!");
			log.debug("Interaction id: " + interaction.getID());
		}catch(SQLException e){
			log.error(e);
		}
	}
	
	public void start(HBox rootLayout, Interaction interaction) throws Exception {
		this.interaction = interaction;
		thingsThatCanChange = new TextInputControl[] {partyMember, description};
		log.debug("start interaction controller called");
		setAllTexts(interaction);
		
		for(TextInputControl c: thingsThatCanChange){
			c.setOnKeyTyped(saveEvent);
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
	
	private static boolean started = false;

	public static boolean getStarted() {
		return started;
	}


	public static InteractionController startInteraction(Interaction interaction) throws Exception {
		log.debug("static startInteraction called.");
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(InteractionController.class.getResource("interaction.fxml"));
		HBox rootLayout = (HBox)loader.load();
		InteractionController cr = (InteractionController)loader.getController();
		cr.start(rootLayout, interaction);
		started = true;
		return cr;
	}
	

	public Object getThing() {
		return interaction;
	}
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(new URL("file:src/main/java/com/forj/fwm/gui/InteractionList/interaction.fxml"));
		HBox rootLayout = (HBox)loader.load();
		InteractionController cr = (InteractionController)loader.getController();
		cr.start(rootLayout, interaction);
		Scene myScene = new Scene(rootLayout);
		primaryStage.setScene(myScene);		
		primaryStage.show();
		
		
	}
}

