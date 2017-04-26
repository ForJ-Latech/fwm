package com.forj.fwm.gui.InteractionList;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.forj.fwm.entity.Interaction;
import com.forj.fwm.gui.tab.Saveable;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ListController extends Application{
	private static Logger log = Logger.getLogger(ListController.class);
	private Saveable s;
	private List<InteractionController> interactionControllers = new ArrayList<InteractionController>();
	private static boolean started = false;
	
	@FXML private VBox interactionListContainer;
	@FXML private VBox list;
	@FXML private Button newInteraction;
	
	public void start(VBox rootLayout , List<Interaction> listA, Saveable s) throws Exception {
		log.debug("start interaction list controller called");
		started = true;
		this.s = s;
		
		//For loop that passes the list of existing interactions into the interaction list
		for (int i = 0; i < listA.size(); i++){
			InteractionController currentInteraction = InteractionController.startInteraction(listA.get(i), s);
			currentInteraction.getSingleInteraction().setManaged(true);
			list.getChildren().add(0,currentInteraction.getSingleInteraction());
			interactionControllers.add(currentInteraction);
			
		}
		
	}
	
	@FXML
	public void addNewInteraction() throws Exception{
		
		log.debug("add new interaction called");
		Interaction i = new Interaction();
		i.setPlayerCharacter("Party");
		InteractionController currentInteraction = InteractionController.startInteraction(i, s);
		currentInteraction.getSingleInteraction().setManaged(true);
		list.setManaged(true);
		list.setFillWidth(true);
		list.getChildren().add(0,currentInteraction.getSingleInteraction());
		interactionControllers.add(currentInteraction);
		currentInteraction.getPartyMember().requestFocus();
	}
	
	public static ListController startInteractionList(List<Interaction> listA , Saveable s) throws Exception {
		log.debug("static startInteractionList called.");
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(ListController.class.getResource("interactionList.fxml"));
		VBox rootLayout = (VBox)loader.load();
		ListController cr = (ListController)loader.getController();
		cr.start(rootLayout, listA, s);
		started = true;
		return cr;
	}
	
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(ListController.class.getResource("interactionList.fxml"));
		
	}
	
	public List<Interaction> getAllInteractions()
	{
		List<Interaction> interactions = new ArrayList<Interaction>();
		for (int i = 0; i < interactionControllers.size(); i++){
					interactions.add(interactionControllers.get(i).getInteraction());	
		}
		return interactions;
	}
	
	public static boolean getStarted() {
		return started;
	}
	
	public VBox getInteractionListContainer(){
		return interactionListContainer;
	}
}
