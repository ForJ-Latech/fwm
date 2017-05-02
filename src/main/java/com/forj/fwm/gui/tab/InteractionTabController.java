package com.forj.fwm.gui.tab;

import java.sql.SQLException;
import java.util.List;

import org.apache.log4j.Logger;

import com.forj.fwm.backend.Backend;
import com.forj.fwm.entity.God;
import com.forj.fwm.entity.Interaction;
import com.forj.fwm.entity.Npc;
import com.forj.fwm.entity.OMEventInteraction;
import com.forj.fwm.entity.OMGodInteraction;
import com.forj.fwm.entity.OMNpcInteraction;
import com.forj.fwm.entity.OMRegionInteraction;
import com.forj.fwm.entity.Region;
import com.forj.fwm.entity.Searchable;
import com.forj.fwm.gui.InteractionList.ListController;
import com.forj.fwm.gui.component.AddableImage;
import com.forj.fwm.gui.component.AddableSound;
import com.forj.fwm.startup.App;
import com.j256.ormlite.stmt.QueryBuilder;

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

public class InteractionTabController implements Saveable {
	private static Logger log = Logger.getLogger(InteractionTabController.class);
	
	@FXML private ListView<Interaction> listView;
	@FXML private Tab tabHead;
	private static boolean started = false;
	private static int maxImageSize = 32;
	
	// NOTE: Probably much more important than just using for pantheon. Updates objects in other tabs
	private void updateTab() {
		
	}
	
	public void start(Tab rootLayout) throws Exception {
		log.debug("start interaction tab controller called");
		
		tabHead.setText("Recent Interactions");
		
		
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

	public static InteractionTabController startInteractionTab() throws Exception {
		log.debug("static startGodTab called.");
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(InteractionTabController.class.getResource("interactionTab.fxml"));
		Tab rootLayout = (Tab)loader.load();
		InteractionTabController cr = (InteractionTabController)loader.getController();
		cr.start(rootLayout);
		started = true;
		return cr;
	}
	
	private List<Interaction> getInteractionList() throws SQLException {
		QueryBuilder<Interaction, String> builder = Backend.getInteractionDao().queryBuilder();
		builder.limit((long) 100); // how many to return
		builder.orderBy("LASTEDITED", false);  // true for ascending, false for descending
		List<Interaction> list = Backend.getInteractionDao().query(builder.prepare()); 
		return list;		
	}
	
	public void updateList() throws SQLException {
		ObservableList<Interaction> items = FXCollections.observableArrayList(getInteractionList());
		listView.setItems(items);
		
		listView.setOnKeyPressed(new EventHandler<KeyEvent>() {
			public void handle(KeyEvent event) {
				if (event.getCode().equals(KeyCode.ENTER)) {
					try {
						App.getMainController().open(grabObject((Interaction) listView.getSelectionModel().getSelectedItem()));
					} catch (Exception e) {
						e.printStackTrace();
						// Open first item if none selected
						try {
							App.getMainController().open(grabObject((Interaction) listView.getItems().get(0)));
						} catch (Exception e1) {
							e1.printStackTrace();
						}
					}
				}
			}

		});

		listView.setCellFactory(new Callback<ListView<Interaction>, ListCell<Interaction>>() {
			public ListCell<Interaction> call(ListView<Interaction> param) {
				return new ListCell<Interaction>() {
					private void openObj(Searchable obj2) {
						try {
							App.getMainController().open(grabObject(this.getItem()));
						} catch (Exception e) {
							e.printStackTrace();
						}
					}

					public void updateItem(final Interaction obj, boolean empty) {
						super.updateItem(obj, empty);
						this.setOnMouseClicked(new EventHandler<MouseEvent>() {
							public void handle(MouseEvent event) {
								if (obj != null) {
									Searchable intobj = null;
									try {
										intobj = grabObject(obj);
									} catch (SQLException e) {
										// TODO Auto-generated catch block
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
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
							
							//could have timestamp if wanted. looked kinda ugly though.
							String date = null;
							try {
								date = Backend.getInteractionDao().queryForEq("ID", obj.getID()).get(0).getLastEdited().toString();
							} catch (SQLException e1) {
								log.debug(e1);
							}
							//name = intobj.getName() + " [" + date + "] " + obj.getPlayerCharacter() + " : " + obj.getDescription();
							name = " [" + intobj.getShownName() + "] "  + obj.getPlayerCharacter() + " : " + obj.getDescription();

							
							try {
								imageView.setImage(new Image(
										App.worldFileUtil.findMultimedia(intobj.getImageFileName()).toURI().toString(),
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
	
	private Searchable grabObject(Interaction interaction) throws SQLException {
		Searchable intobj = null;
		
		for (OMGodInteraction omg : Backend.getOmGodInteractionDao().queryForEq("INTERACTION_ID", interaction.getID())) {
			intobj = omg.getGod();
			God g = Backend.getGodDao().queryForEq("ID", omg.getGod().getID()).get(0);
			((God) intobj).setImageFileName(g.getImageFileName());
			((God) intobj).setName(g.getName());
		}
		for (OMNpcInteraction omn : Backend.getOmNpcInteractionDao().queryForEq("INTERACTION_ID", interaction.getID())) {
			intobj = omn.getNpc();
			Npc n = Backend.getNpcDao().queryForEq("ID", omn.getNpc().getID()).get(0);
			((Npc) intobj).setImageFileName(n.getImageFileName());
			((Npc) intobj).setfName(n.getFullName());
		}
		for (OMEventInteraction ome : Backend.getOmEventInteractionDao().queryForEq("INTERACTION_ID", interaction.getID())) {
			intobj = ome.getEvent();
			com.forj.fwm.entity.Event e = Backend.getEventDao().queryForEq("ID", ome.getEvent().getID()).get(0);
			((com.forj.fwm.entity.Event)intobj).setName(e.getName());
			((com.forj.fwm.entity.Event)intobj).setImageFileName(e.getImageFileName());
		}
		for (OMRegionInteraction omr : Backend.getOmRegionInteractionDao().queryForEq("INTERACTION_ID", interaction.getID())) {
			intobj = omr.getRegion();
			Region r = Backend.getRegionDao().queryForEq("ID", omr.getRegion().getID()).get(0);
			((Region) intobj).setName(r.getName());
			((Region) intobj).setImageFileName(r.getImageFileName());
		}
		
		return intobj;
		
	}
	
	public static boolean getStarted() {
		return started;
	}
	
	public Tab getTab(){
		return tabHead;
	}

	public void fullSave() {
		
	}

	public void simpleSave() {
		
	}

	public void relationalSave() {

	}

	public AddableImage getAddableImage() {
		return null;
	}

	public Searchable getThing() {
		return null;
	}

	public void nameFocus() {
		
	}

	public ListController getListController() {
		return null;
	}
	public AddableSound getAddableSound(){
		return null;
	}
}
