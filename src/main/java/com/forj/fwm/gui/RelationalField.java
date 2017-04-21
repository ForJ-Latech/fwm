package com.forj.fwm.gui;

import java.awt.MouseInfo;
import java.awt.Point;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.forj.fwm.backend.Backend;
import com.forj.fwm.conf.WorldConfig;
import com.forj.fwm.entity.Event;
import com.forj.fwm.entity.God;
import com.forj.fwm.entity.MMRegionGod;
import com.forj.fwm.entity.Npc;
import com.forj.fwm.entity.OMRegionRegion;
import com.forj.fwm.entity.Region;
import com.forj.fwm.entity.Searchable;
import com.forj.fwm.gui.SearchList.EntitiesToSearch;
import com.forj.fwm.gui.component.Openable;
import com.forj.fwm.gui.tab.GodTabController;
import com.forj.fwm.gui.tab.NpcTabController;
import com.forj.fwm.gui.tab.RegionTabController;
import com.forj.fwm.gui.tab.Saveable;
import com.forj.fwm.startup.App;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.control.Accordion;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tab;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Popup;
import javafx.stage.Stage;
import javafx.util.Callback;

public class RelationalField implements Openable{
	private Node ourRoot;

	private static int maxImageSize = 16;
	
	private EntitiesToSearch tabType;
	private EntitiesToSearch relationType;
	
	private static Logger log = Logger.getLogger(RelationalField.class);
	
	private ObservableList<Searchable> items = FXCollections.observableArrayList();
	
	// @Jay how do you feel about @FXML on the same line for variables? 
	// I was getting eye cancer from full screens of FXML variables
	@FXML private ListView<Searchable> listView = new ListView<Searchable>();
	@FXML private Button addButton;
	@FXML private StackPane stackPane, listPane;
	private boolean page1 = true;

	private ArrayList<Searchable> searchResults = new ArrayList<Searchable>();
	private SearchList sl;

	private Saveable caller;
	private Searchable tabObject;
	
	private boolean relationsRemovable;
	Popup popup = new Popup();
	
	public ListView<Searchable> getListView(){
		return listView;
	}

	public static RelationalField createRelationalList(Saveable caller, List<Searchable> ourItems, String title, boolean useButton, boolean relationsRemovable, EntitiesToSearch tabType, EntitiesToSearch relationType) throws IOException {
		FXMLLoader fxmlLoader = new FXMLLoader();
		fxmlLoader.setLocation(RelationalField.class.getResource("relationalField.fxml"));
		Node n = (Node) fxmlLoader.load();
		RelationalField rl = fxmlLoader.getController();
		rl.start(tabType, relationType, n, caller, ourItems, useButton, relationsRemovable, title);
		return rl;
	}

	public void start(EntitiesToSearch t, EntitiesToSearch r, Node n, Saveable caller, List<Searchable> ourItems, boolean useButton, boolean removable, String title) {
		this.tabType = t;
		this.relationType = r;
		this.ourRoot = n;
		this.caller = caller;
		this.relationsRemovable = removable;
		addButton.setContentDisplay(ContentDisplay.RIGHT);
		
		// set reference to the object owned by the tab
		tabObject = caller.getThing();
		populateList(ourItems);
			
			
		updateList();
		setTitle(title);

	}
	
	public void updateList() {
		items = FXCollections.observableArrayList(searchResults);
		listView.setItems(items);
		
		listView.setOnKeyPressed(new EventHandler<KeyEvent>(){
			public void handle(KeyEvent event) {
				if(event.getCode().equals(KeyCode.ENTER)){
					try{
						App.getMainController().open(listView.getSelectionModel().getSelectedItem());
					}catch(Exception e){
						e.printStackTrace();
					}
				}
				if(event.getCode().equals(KeyCode.DELETE)){
					removeItem(listView.getSelectionModel().getSelectedItem());
				}
			}
		});
		// JavaFX cancer
		listView.setCellFactory(new Callback<ListView<Searchable>, ListCell<Searchable>>() {
			public ListCell<Searchable> call(ListView<Searchable> param) {
				return new ListCell<Searchable>() {
					private void openObj(){
						try{
							App.getMainController().open(this.getItem());	
						}catch(Exception e)
						{
							e.printStackTrace();
						}
					}
					public void updateItem(final Searchable obj, boolean empty) {
						super.updateItem(obj, empty);
						
						final ContextMenu contextMenu = new ContextMenu();
				        MenuItem deleteMenuItem = new MenuItem("Remove Relation");
				        
				        deleteMenuItem.setOnAction(new EventHandler<ActionEvent>() {
				            public void handle(ActionEvent e) {
				                removeItem(obj);
				                log.debug("removed relation");
				            }
				        });
				        
				        contextMenu.getItems().addAll(deleteMenuItem);
				        this.setContextMenu(contextMenu);
				        final ListCell<Searchable> thing = this;
				        
						this.setOnMouseClicked(new EventHandler<MouseEvent>(){
							public void handle(MouseEvent event) {
								if (event.getButton() ==  MouseButton.SECONDARY && relationsRemovable){
									contextMenu.show(thing, event.getScreenX(), event.getScreenY());
								} else {
									openObj();
								}
							}
						} );
						
						if(obj == null){
							setText(null);
							setGraphic(null);
							setHeight(0);
							setWidth(0);
							return;
						}
						ImageView imageView = new ImageView();
						String name = "";
						if (obj != null) {
							// TODO: Region should have hierarchy??
							if (obj instanceof Npc) {
								name = ((Npc) obj).getFullName();
							} else {
								name = obj.getName();
								log.debug(name);
							}
							try {
								
								imageView.setImage(new Image(
										App.worldFileUtil.findMultimedia(obj.getImageFileName()).toURI().toString(), true));
							} catch (NullPointerException e) {
								// log.info("No Image Set for object -> " +
								// name);
								imageView.setImage(
										new Image(App.retGlobalResource("/src/main/ui/no_image_icon.png").toString()));
							}
						}
						if (empty) {
							setText(null);
							setGraphic(null);
						} else {
							setText(name);
							imageView.setFitHeight(maxImageSize);
							imageView.setFitWidth(maxImageSize);
							imageView.setPreserveRatio(true);
							setGraphic(imageView);
						}
					}
				};
			}
		});
	}
	
	@FXML
	public void escape() {
		if (!page1){
			handleAddButton();
		}
	}
	
	public void clearList() {
		listView.getItems().clear();
		searchResults.clear();
	}
	
	public Searchable getTabObject() {
		return tabObject;
	}

	private boolean checkUnique(Searchable item) {
		boolean unique = true;
		if (item == null) {
			return false;
		}
		for (Searchable i : searchResults) {
			if (i != null) {
				if (i.getID() == item.getID()) {
					if (i.getClass().equals(item.getClass())) {
						unique = false;
						break;
					}
					
				}
			}
		}
		return unique;
	}

	public void addItem(Searchable item, boolean update) {
		clearList();
		if (checkUnique(item)) {
			listView.getItems().add(item);
			searchResults.add(item);
			if (update) {
				updateList();
			}
		}
		updateList();
		
	}
	
	public void removeItem(Searchable item){
		listView.getItems().remove(item);
		searchResults.remove(item);
		log.debug("Removing an item, and attempting to save changes to tab.");
		
		// make npc heathenous
		if (item instanceof God && tabObject instanceof Npc) {
			((Npc) tabObject).setGod(null);
		}
		
		
		if(!WorldConfig.getManualSaveOnly()){
			caller.fullSave();
		}
		updateList();
	}

	public List<Searchable> getList() {
		return listView.getItems();
	}
	

	public void setMaxImageSize(int newSize) {
		maxImageSize = newSize;
	}

	public void setEntitiesToSearch(EntitiesToSearch e) {
		tabType = e;
	}

	public Node getOurRoot() {
		return ourRoot;
	}
	
	public void setButtonText(String newText) {
		addButton.setText(newText);
	}
	
	@FXML
	private void handleAddButton() {
		
			
	        popup.setAutoHide( true );
	        popup.setHideOnEscape( true );
	        popup.setAutoFix( true );

			try {
				sl = SearchList.createSearchList(relationType, this);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			
			popup.getContent().addAll(sl.getOurRoot());

			Point p = MouseInfo.getPointerInfo().getLocation();
		    popup.show( getOurRoot(), p.getX(), p.getY());
			
			sl.setMaxImageSize(16);
			sl.getSearchField().requestFocus();
			

	}
	
	public void setTitle(String newText) {
		//titledPane.setText(newText);
	}
	
	public void populateList(List<Searchable> ourItems) {
		if (ourItems != null)
		for(Searchable s: ourItems){
			addItem(s, false);
		}
		updateList();
	}
	
	public void open(Searchable o) throws Exception {
		log.debug("opening tab from search");
		
		clearList();
		
		searchResults.add(o);
		populateList(searchResults);
		updateList();
		if(!WorldConfig.getManualSaveOnly()){
			caller.fullSave();
		}
		popup.hide();
		
		// research on left hand side? ... 
		// App.getMainController().getSearchList().searchDB();
		
	}


}