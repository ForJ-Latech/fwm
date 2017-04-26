package com.forj.fwm.gui.tab;

import org.apache.log4j.Logger;

import com.forj.fwm.entity.Searchable;
import com.forj.fwm.gui.InteractionList.ListController;
import com.forj.fwm.gui.component.AddableImage;

import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.Tab;
import javafx.scene.layout.VBox;

public class PreviouslyEditedTabController implements Saveable{
	private static Logger log = Logger.getLogger(PreviouslyEditedTabController.class);
	
	@FXML private Tab tabHead;
	@FXML private ListView<Searchable> listView = new ListView<Searchable>();
	@FXML private VBox vbox;

	public void fullSave() {
		// TODO Auto-generated method stub
		
	}

	public void simpleSave() {
		// TODO Auto-generated method stub
		
	}

	public void relationalSave() {
		// TODO Auto-generated method stub
		
	}

	public AddableImage getAddableImage() {
		// TODO Auto-generated method stub
		return null;
	}

	public Searchable getThing() {
		// TODO Auto-generated method stub
		return null;
	}

	public ListController getListController() {
		// TODO Auto-generated method stub
		return null;
	}

	public Tab getTab() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public void nameFocus(){
		
	}
}
