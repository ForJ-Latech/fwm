package com.forj.fwm.gui;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import com.forj.fwm.backend.Backend;
import com.forj.fwm.entity.Event;
import com.forj.fwm.entity.God;
import com.forj.fwm.entity.Npc;
import com.forj.fwm.entity.Region;
import com.forj.fwm.entity.Searchable;
import com.forj.fwm.gui.component.Openable;
import com.forj.fwm.gui.tab.RegionTabController;
import com.forj.fwm.startup.App;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.util.Callback;

public class SearchList {
	private VBox ourRoot;

	private int maxImageSize = 32;
	private Openable ourOpen;

	public static enum EntitiesToSearch {
		ALL, NPC, GOD, REGION, EVENT
	};

	private EntitiesToSearch searchEntity;

	private static Logger log = Logger.getLogger(SearchList.class);

	@FXML
	private ListView<Searchable> listView = new ListView<Searchable>();
	private ObservableList<Searchable> items = FXCollections.observableArrayList();
	@FXML
	private TextField searchField;

	private ArrayList<Searchable> searchResults = new ArrayList<Searchable>();
	private ArrayList<Integer> tree =  new ArrayList<Integer>();
	private boolean regionregion = false;

	public TextField getSearchField() {
		return searchField;
	}

	public static SearchList createSearchList(EntitiesToSearch t, Openable ol) throws IOException {
		FXMLLoader fxmlLoader = new FXMLLoader();
		fxmlLoader.setLocation(SearchList.class.getResource("searchList.fxml"));
		VBox n = (VBox) fxmlLoader.load();
		SearchList sl = fxmlLoader.getController();
		sl.start(t, ol, n);
		return sl;
	}

	public void start(EntitiesToSearch t, Openable ol, VBox n) {
		this.searchEntity = t;
		this.ourOpen = ol;
		this.ourRoot = n;
		
		if (ourOpen instanceof RelationalField){
			if (((RelationalField) ourOpen).getTabObject() instanceof Region && searchEntity == EntitiesToSearch.REGION) {
				regionregion = true;
				this.tree = createTree();
			}
		}
		
		
		updateList();

	}

	public void updateList() {
		items = FXCollections.observableArrayList(searchResults);
		listView.setItems(items);

		listView.setOnKeyPressed(new EventHandler<KeyEvent>() {
			public void handle(KeyEvent event) {
				if (event.getCode().equals(KeyCode.ENTER)) {
					try {
						ourOpen.open(listView.getSelectionModel().getSelectedItem());
					} catch (Exception e) {
						e.printStackTrace();
						// Open first item if none selected
						try {
							ourOpen.open(listView.getItems().get(0));
						} catch (Exception e1) {
							e1.printStackTrace();
						}
					}
				}
			}

		});

		listView.setCellFactory(new Callback<ListView<Searchable>, ListCell<Searchable>>() {
			public ListCell<Searchable> call(ListView<Searchable> param) {
				return new ListCell<Searchable>() {
					private void openObj() {
						try {
							ourOpen.open(this.getItem());
						} catch (Exception e) {
							e.printStackTrace();
						}
					}

					public void updateItem(Searchable obj, boolean empty) {
						super.updateItem(obj, empty);
						this.setOnMouseClicked(new EventHandler<MouseEvent>() {
							public void handle(MouseEvent event) {
								openObj();
							}
						});
						if (obj == null) {
							setText(null);
							setGraphic(null);
							setHeight(0);
							setWidth(0);
							return;
						}
						ImageView imageView = new ImageView();
						String name = "";
						if (obj != null) {
							if (obj instanceof Npc) {
								name = ((Npc) obj).getFullName();

								// Don't list region if it is a super region to
								// current region or sub region of a super
								// region
							} else if (obj instanceof Region && ourOpen instanceof RegionTabController) {
								ArrayList<Region> regs = new ArrayList<Region>();

								// Find highest super region in tree
								Region r = (((Region) ((RegionTabController) ourOpen).getThing()).getSuperRegion());
								Region s = (((Region) ((RegionTabController) ourOpen).getThing()));
								while (r != null) {
									r = r.getSuperRegion();
									if (r != null) {
										s = r;
									}
								}
								// s is the topmost super region
								regs.add(s);
								while (!regs.isEmpty()) {
									for (Region i : regs.get(0).getSubRegions()) {
										regs.add(i);
										if (((Region) obj).getID() == i.getID()) {
											return;
										}
									}
									regs.remove(0);
								}

							} else {
								name = obj.getName();
							}
							try {
								imageView.setImage(new Image(
										App.worldFileUtil.findMultimedia(obj.getImageFileName()).toURI().toString(),
										true));
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

	public void clearList() {
		listView.getItems().clear();
		searchResults.clear();
	}

	private boolean checkUnique(Searchable item) {
		boolean unique = true;
		for (Searchable i : searchResults) {
			if (i.getID() == item.getID()) {
				if (i.getClass().equals(item.getClass())) {
					unique = false;
					break;
				}
			}
		}
		if (ourOpen.getListView() != null) {
			for (Object i : ourOpen.getListView().getItems()) {
				if (((Searchable) i).getID() == item.getID()) {
					unique = false;
					break;
				}
			}
		}
		// If called from relational list, don't show the tab's own object in
		// searchlist
		if (ourOpen instanceof RelationalList) {
			if (((RelationalList) ourOpen).getTabObject().getID() == item.getID()) {
				if (((RelationalList) ourOpen).getTabObject().getClass().equals(item.getClass())) {
					unique = false;
				}
			}
		}
		
		return unique;
	}
	
	
private ArrayList<Integer> createTree() {
		
		ArrayList<Integer> tree =  new ArrayList<Integer>();
		ArrayList<Region> regs = new ArrayList<Region>();

		// Find highest super region in tree
		Region r = (((Region) ((RelationalField) ourOpen).getTabObject()).getSuperRegion());
		Region s = (((Region) ((RelationalField) ourOpen).getTabObject()));
		while (r != null) {
			r = r.getSuperRegion();
			if (r != null) {
				s = r;
			}
		}
		// s is the topmost super region
		regs.add(s);
		while (!regs.isEmpty()) {
			for (Region i : regs.get(0).getSubRegions()) {
				regs.add(i);
			}
			tree.add(regs.get(0).getID());
			regs.remove(0);
				
		}
		
		for (Integer i1 : tree){
			System.out.println(i1);
		}
		
		return tree;

	}


	private boolean checkInTree(Region r) {
		for (Integer i : tree) {
			if (r.getID() == i) {
				return true;
			}
		}
		return false;
	}



	

	public void addItem(Searchable item, boolean update) {
		if (checkUnique(item)) {
			if (regionregion){
				if (checkInTree((Region) item)){
					return;
				}
			}
			listView.getItems().add(item);
			searchResults.add(item);
			if (update) {
				updateList();
			}
			if (regionregion){
				createTree();
			}
			
		}
	}

	public ListView<Searchable> getList() {
		return listView;
	}

	@FXML
	public void searchDB() {
		clearList();
		log.info("searchdb called");
		switch (searchEntity) {
		case ALL:
			try {
				for (Npc npc : Backend.getNpcDao().queryForLike("fName", searchField.getText())) {
					addItem(npc, false);
				}
				for (God god : Backend.getGodDao().queryForLike("name", searchField.getText())) {
					addItem(god, false);
				}
				for (Region region : Backend.getRegionDao().queryForLike("name", searchField.getText())) {
					addItem(region, false);
				}
				for (Event event : Backend.getEventDao().queryForLike("name",searchField.getText())) { 
					addItem(event, false);
				}
			} catch (SQLException e) {
				log.error(e.getStackTrace());
				e.printStackTrace();
			}
			break;
		case NPC:
			try {
				for (Npc npc : Backend.getNpcDao().queryForLike("fName", searchField.getText())) {
					addItem(npc, false);
				}
			} catch (SQLException e) {
				log.error(e.getStackTrace());
			}
			break;
		case GOD:
			try {
				for (God god : Backend.getGodDao().queryForLike("name", searchField.getText())) {
					addItem(god, false);
				}
			} catch (SQLException e) {
				log.error(e.getStackTrace());
			}
			break;
		case REGION:
			try {
				for (Region region : Backend.getRegionDao().queryForLike("name", searchField.getText())) {
					addItem(region, false);
				}
			} catch (SQLException e) {
				log.error(e.getStackTrace());
			}
			break;
		case EVENT:
			try {
				for (Event event : Backend.getEventDao().queryForLike("name", searchField.getText())) {
					addItem(event, false);
				}
			} catch (SQLException e) {
				log.error(e.getStackTrace());
			}
			break;
		}
		updateList();
	}

	@FXML
	private void handleTextChanged() {
		if (searchField.getText().equals("") || searchField.getText() == null) {
			// log.info("No search input!");
			clearList();
		} else {
			// log.info("searching for " + searchField.getText());
			searchDB();
		}
	}

	public void setMaxImageSize(int newSize) {
		maxImageSize = newSize;
	}

	public void setEntitiesToSearch(EntitiesToSearch e) {
		searchEntity = e;
	}

	public VBox getOurRoot() {
		return ourRoot;
	}

}