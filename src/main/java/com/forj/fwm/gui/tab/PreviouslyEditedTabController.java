package com.forj.fwm.gui.tab;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import com.forj.fwm.backend.Backend;
import com.forj.fwm.entity.Event;
import com.forj.fwm.entity.God;
import com.forj.fwm.entity.Npc;
import com.forj.fwm.entity.Region;
import com.forj.fwm.entity.Searchable;
import com.forj.fwm.entity.Template;
import com.forj.fwm.gui.InteractionList.ListController;
import com.forj.fwm.gui.component.AddableImage;
import com.forj.fwm.gui.component.AddableSound;
import com.forj.fwm.startup.App;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.Tab;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.util.Callback;

public class PreviouslyEditedTabController implements Saveable{
	private static Logger log = Logger.getLogger(PreviouslyEditedTabController.class);
	
	@FXML private ListView<Searchable> listView;
	@FXML private Tab tabHead;
	private static boolean started = false;
	private static int maxImageSize = 32;
	
	private void updateTab() {
		
	}
	
	public void start(Tab rootLayout) throws Exception {
		log.debug("start previously edited tab controller called");
		
		tabHead.setText("Previously Edited");
		
		App.getMainController().getTabPane().getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Tab>() {
		    public void changed(ObservableValue<? extends Tab> observable, Tab oldTab, Tab newTab) {
		        if(newTab == getTab()) {
		        	updateTab();
		        }
		    }
		});
		updateList();
		started = true;
	}
	
	public static PreviouslyEditedTabController startPreviouslyEditedTab() throws Exception {
		log.debug("static startPreviouslyEditedTab called.");
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(PreviouslyEditedTabController.class.getResource("PreviouslyEditedTab.fxml"));
		Tab rootLayout = (Tab)loader.load();
		PreviouslyEditedTabController cr = (PreviouslyEditedTabController)loader.getController();
		cr.start(rootLayout);
		started = true;
		return cr;
	}
	
	private List<Searchable> getPreviouslyEditedList() throws SQLException {
		List<Searchable> list = new ArrayList<Searchable>();
		List<Event> events = Backend.getEventDao().queryBuilder().limit((long) 100).orderBy("LASTEDITED", false).query();
		List<God> gods = Backend.getGodDao().queryBuilder().limit((long) 100).orderBy("LASTEDITED", false).query();
		List<Npc> npcs = Backend.getNpcDao().queryBuilder().limit((long) 100).orderBy("LASTEDITED", false).query();
		List<Region> regions = Backend.getRegionDao().queryBuilder().limit((long) 100).orderBy("LASTEDITED", false).query();
		List<Template> templates = Backend.getTemplateDao().queryBuilder().limit((long) 100).orderBy("LASTEDITED", false).query();
		
		// This could maybe be optimized to not have to just auto add 100 of each to ensure the correct number, but eh
		for (Event s : events) {
			if (s.getLastEdited() != null && s.getShownName() != null) {
				list.add(s);
			}
		}
		for (God s : gods) {
			if (s.getLastEdited() != null && s.getShownName() != null) {
				list.add(s);
			}
		}
		for (Npc s : npcs) {
			if (s.getLastEdited() != null && s.getShownName() != null) {
				list.add(s);
			}
		}
		for (Region s : regions) {
			if (s.getLastEdited() != null && s.getShownName() != null) {
				list.add(s);
			}
		}
		for (Template s : templates) {
			if (s.getLastEdited() != null && s.getShownName() != null) {
				list.add(s);
			}
		}
		
		Collections.sort(list, new Comparator<Searchable>() {
		    public int compare(Searchable left, Searchable right) {
		        // -1 - less than, 1 - greater than, 0 - equal, all inversed for descending
		    	return compareLR(left,right);
		    }
		});
		
		if (list.size() > 99) {
			list = list.subList(0,99);
		} else {
			list = list.subList(0,list.size());
		}
		
		return list;		
	}
	
    private int compareLR(Searchable left, Searchable right) {
    	Date d1 = left.getLastEdited();
    	Date d2 = right.getLastEdited();

    	if (d1.after(d2)) {
    		return -1;
    	} else if (d1.before(d2)) {
    		return 1;
    	} else {
    		return 0;
    	}
    }
	
	public void updateList() throws SQLException {
		
		ObservableList<Searchable> items = FXCollections.observableArrayList(getPreviouslyEditedList());
		listView.setItems(items);
		
		listView.setOnKeyPressed(new EventHandler<KeyEvent>() {
			public void handle(KeyEvent event) {
				if (event.getCode().equals(KeyCode.ENTER)) {
					try {
						App.getMainController().open(grabObject((Searchable) listView.getSelectionModel().getSelectedItem()));
					} catch (Exception e) {
						e.printStackTrace();
						// Open first item if none selected
						try {
							App.getMainController().open(grabObject((Searchable) listView.getItems().get(0)));
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
					private void openObj(Searchable obj2) {
						try {
							App.getMainController().open(grabObject(this.getItem()));
						} catch (Exception e) {
							e.printStackTrace();
						}
					}

					public void updateItem(final Searchable obj, boolean empty) {
						super.updateItem(obj, empty);
						this.setOnMouseClicked(new EventHandler<MouseEvent>() {
							public void handle(MouseEvent event) {
								if (obj != null) {
									Searchable intobj = null;
									try {
										intobj = grabObject(obj);
									} catch (SQLException e) {
										e.printStackTrace();
									}
									openObj(intobj);
								}
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
							Searchable intobj = null;
							try {
								intobj = grabObject(obj);
							} catch (SQLException e1) {
								e1.printStackTrace();
							}
							
							if (intobj instanceof Event) {
								name = " [Group] " + obj.getShownName() + " : " + obj.getLastEdited();
							} else if (intobj instanceof God) {
								name = " [God] " + obj.getShownName() + " : " + obj.getLastEdited();
							} else if (intobj instanceof Npc) {
								name = " [NPC] " + obj.getShownName() + " : " + obj.getLastEdited();
							} else if (intobj instanceof Region) {
								name = " [Region] " + obj.getShownName() + " : " + obj.getLastEdited();
							} else if (intobj instanceof Template) {
								name = " [Template] " + obj.getShownName() + " : " + obj.getLastEdited();
							}
							
							try {
								imageView.setImage(new Image(
										App.worldFileUtil.findMultimedia(intobj.getImageFileName()).toURI().toString(),
										true));
							} catch (NullPointerException e) {
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
	
	private Searchable grabObject(Searchable searchable) throws SQLException {
		Searchable intobj = null;
		
		if (searchable instanceof Event) {
			intobj = Backend.getEventDao().getFullEvent(searchable.getID());
		} else if (searchable instanceof God) {
			intobj = Backend.getGodDao().getFullGod(searchable.getID());
		} else if (searchable instanceof Npc) {
			intobj = Backend.getNpcDao().getFullNpc(searchable.getID());
		} else if (searchable instanceof Region) {
			intobj = Backend.getRegionDao().getFullRegion(searchable.getID());
		} else if (searchable instanceof Template) {
			intobj = Backend.getTemplateDao().getFullTemplate(searchable.getID());
		}
		
		return intobj;
	}
	
	public Tab getTab() {
		return tabHead;
	}
	
	public static boolean getStarted() {
		return started;
	}

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

	public void nameFocus(){
		
	}
	public AddableSound getAddableSound(){
		return null;
	}
}
