package com.forj.fwm.gui.component;

import java.util.ArrayList;

import org.apache.log4j.Logger;

import com.forj.fwm.conf.AppConfig;
import com.forj.fwm.gui.tab.InteractionTabController;
import com.forj.fwm.gui.tab.PreviouslyEditedTabController;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

/**
 * Requires a tab pane, and the tabs implementing saveable. 
 * @author jehlmann
 *
 */
public class TabControlled {
	private static Logger log = Logger.getLogger(TabControlled.class);
	
	public ArrayList<Saveable> tabControllers = new ArrayList<Saveable>();
	
	@FXML protected TabPane tabPane;
	
	public static final String DID_NOT_AUTO_UPDATE = "Auto updating disabled, manual saving on.";
	
	public void startAutoUpdateTabs(){
		tabPane.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Tab>() {
		    public void changed(ObservableValue<? extends Tab> observable, Tab oldTab, Tab newTab) {
		        if(AppConfig.getAutoUpdateTabs()){
			    	if(!newTab.equals(oldTab)){
			        	boolean tabAlreadyIndexed = false;
			        	for(Saveable s: tabControllers){
			        		if(s.getTab().equals(newTab)){
			        			tabAlreadyIndexed = true;
			        			break;
			        		}
			        	}
			        	if(tabAlreadyIndexed){
			        		findTab(newTab).autoUpdateTab();
			        	}
			        	else{
			        		//don't update it's a new tab. 
			        	}
			        }
			    }
		    }
		});
	}
	
	private EventHandler<Event> onTabClosed = new EventHandler<Event>(){
		public void handle(Event event) {
			Tab source = (Tab)event.getSource();
			int index = findTabIndex(source);
			if (index != -1)
			{
				tabControllers.remove(index);
			}
			log.debug("tab controller size after removall:" + tabControllers.size());
		}
	};
	
	public void closeTab(Tab tab){
		int index = findTabIndex(tab);
		if (index != -1)
		{
			tabControllers.remove(index);
			tabPane.getTabs().remove(tab);
		}
	}
	
	public int findTabIndex(Tab source){
		for(int i = 0; i < tabControllers.size(); i++){
			if(source.equals(tabControllers.get(i).getTab())){
				return i;
			}
		}
		return -1;
	}
	
	public void addTabController(Saveable s){
		boolean existed = false;
		s.getTab().setOnClosed(onTabClosed);
		for(Saveable x: tabControllers){
			// if they have the same class
			// and we are not trying to create a new one.
			// and they have the same id. 
			if (x instanceof InteractionTabController && s instanceof InteractionTabController) {
				existed = true;
				tabPane.getSelectionModel().select(x.getTab());
				log.debug("interaction");
				break;
			} else if (x instanceof PreviouslyEditedTabController && s instanceof PreviouslyEditedTabController) {
				existed = true;
				tabPane.getSelectionModel().select(x.getTab());
				log.debug("previouslyEdited");
				break;
			}
			
			if(x.getClass().equals(s.getClass()) 
					&& s.getThing().getID() != -1 
					&& x.getThing().getID() == s.getThing().getID()){
				existed = true;
				tabPane.getSelectionModel().select(x.getTab());
				log.debug("already open");
				break;
			}
			
			/*if (!(x instanceof InteractionTabController)){
				if(x.getClass().equals(s.getClass()) 
						&& s.getThing().getID() != -1 
						&& x.getThing().getID() == s.getThing().getID()){
					existed = true;
					tabPane.getSelectionModel().select(x.getTab());
				}
			} else {
				existed = true;
				tabPane.getSelectionModel().select(x.getTab());
			}*/
		}
		
		if(!existed){
			tabPane.getTabs().add(s.getTab());
			tabPane.getSelectionModel().select(s.getTab());
			tabControllers.add(s);
			log.debug("didn't exist");
			
		}
		else
		{
			// clean up our item, wish it was like c# where i can .dispose(); 
			s = null; 
		}
		
		log.debug("tab controller size after addition: " + tabControllers.size());
	}
	
	public void nextTab(){
		int newIndex = findTabIndex(tabPane.getSelectionModel().getSelectedItem())+1;
		if (newIndex > tabPane.getTabs().size()-1){
			tabPane.getSelectionModel().select(tabPane.getTabs().get(0));
		}
		else{
			tabPane.getSelectionModel().select(newIndex);
		}
	}
	
	public void prevTab(){
		int newIndex = findTabIndex(tabPane.getSelectionModel().getSelectedItem())-1;
		if (newIndex < 0){
			tabPane.getSelectionModel().select(tabPane.getTabs().get(tabPane.getTabs().size()-1));
		}
		else{
			tabPane.getSelectionModel().select(newIndex);
		}
	}
	
	public Saveable findTab(Tab source){
		log.debug("attempting to findTab. ");
		try{
			return tabControllers.get(findTabIndex(source));
		}catch(Exception e){
			log.error(e);
			e.printStackTrace();
			return null;
		}
	}
	
	public MainEntityTab findMainEntityTab(Tab source)
	{
		Saveable temp = tabControllers.get(findTabIndex(source));
		if(temp instanceof MainEntityTab){
			return (MainEntityTab)temp;
		}else{
			return null;
		}
	}
	
	public TabPane getTabPane() {
		return tabPane;
	}
}
