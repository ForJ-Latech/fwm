package com.forj.fwm.gui.InteractionList;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.forj.fwm.entity.Interaction;
import com.forj.fwm.gui.InteractionList.InteractionController;
import com.forj.fwm.startup.ComponentSelectorController;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class ListController extends Application{
	private static Logger log = Logger.getLogger(ListController.class);

	public void start(VBox rootLayout, List<Interaction> listA) throws Exception {
		log.debug("start interaction list controller called");
		started = true;
		
		//For loop that passes the list of existing interactions into the interaction list
		for (int i = 0; i < listA.size(); i++){
			InteractionController currentInteraction = InteractionController.startInteraction(listA.get(i));
			list.getChildren().add(0,currentInteraction.getSingleInteraction());
		}
		
	}
	
//	public static void main(String[] args) throws Exception {
//		ComponentSelectorController.noUiStart();
//		launch(args);
//	}
	
	private static boolean started = false;

	public static boolean getStarted() {
		return started;
	}
	
	@FXML
	private VBox interactionListContainer;
	
	public VBox getInteractionListContainer(){
		return interactionListContainer;
	}
	
	@FXML
	private VBox list;
	
	@FXML
	private Button newInteraction;
	
	@FXML
	private void addNewInteraction() throws Exception{
		
		log.debug("add new interaction called");
		Interaction i = new Interaction();
		i.setPlayerCharacter("Party");
		InteractionController currentInteraction = InteractionController.startInteraction(i);
		list.getChildren().add(0,currentInteraction.getSingleInteraction());
		
	}
	
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(ListController.class.getResource("interactionList.fxml"));
		VBox rootLayout = (VBox)loader.load();
		ListController cr = (ListController)loader.getController();
		
		Scene myScene = new Scene(rootLayout);
		primaryStage.setScene(myScene);		
		primaryStage.show();
		
		Interaction i1 = new Interaction();
		i1.setPlayerCharacter("Bob");
		i1.setDescription("prayed to Satan");
		Interaction i2 = new Interaction();
		i2.setPlayerCharacter("Bob");
		i2.setDescription("talked to Joe");
		Interaction i3 = new Interaction();
		i3.setPlayerCharacter("Dan");
		i3.setDescription("bought some food from Joe");
		
		List<Interaction> listA = new ArrayList<Interaction>();
		listA.add(i1);
		listA.add(i2);
		listA.add(i3);
		
		cr.start(rootLayout, listA);
			
		
	}
}
