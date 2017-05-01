package com.forj.fwm.conf;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.servlet.jsp.jstl.core.Config;

import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.FileBasedConfiguration;
import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.builder.FileBasedConfigurationBuilder;
import org.apache.commons.configuration2.builder.fluent.Configurations;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.apache.log4j.Logger;

import com.forj.fwm.gui.GenericTextController;
import com.forj.fwm.gui.InteractionList.ListController;
import com.forj.fwm.gui.component.AddableImage;
import com.forj.fwm.gui.component.AddableSound;
import com.forj.fwm.gui.tab.EventTabController;
import com.forj.fwm.gui.tab.GodTabController;
import com.forj.fwm.gui.tab.NpcTabController;
import com.forj.fwm.gui.tab.RegionTabController;
import com.forj.fwm.gui.tab.Saveable;
import com.forj.fwm.startup.App;
import com.forj.fwm.startup.WorldFileUtil;
import com.sun.javafx.scene.control.skin.TextAreaSkin;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.ObservableListBase;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Accordion;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class HotkeyController {

	private static Logger log = Logger.getLogger(HotkeyController.class);

	public static WorldFileUtil worldFileUtil;
	private static HotkeyController hotkeyController;
	private static Configuration config;
	private static Configurations configManager;
	private static FileBasedConfigurationBuilder<FileBasedConfiguration> builder;
	private static String defaultHotkeyPropertiesLocation = "src/main/resources/hotkeys.properties";

	@FXML
	private VBox hotkeyVBox;

	private static boolean changeHotkey;
	private static String changeHotkeyFunction;
	private Stage ourStage;
	private static boolean started;

	public static class Hotkey {
		private String hotkeyName;
		private HBox hbox;
		private Button changeButton = new Button("Change");
		private Button unbindButton = new Button("Unbind");
		private Label label = new Label();
		private TextField textField = new TextField();
		private String keyCode;

		public Hotkey(String name, String keyCode) {
			hotkeyName = name;
			setButtons();
			setHotkey(keyCode);
			setAlignment();
		}

		public Hotkey(String name, KeyCodeCombination keyCode) {
			hotkeyName = name;
			setButtons();
			setHotkey(keyCode.getName());
			setAlignment();
		}

		private void setButtons() {
			changeButton.setOnAction(new EventHandler<ActionEvent>() {
				public void handle(ActionEvent e) {
					HotkeyController.changeHotkey = true;
					HotkeyController.changeHotkeyFunction = hotkeyName;
				}
			});
			unbindButton.setOnAction(new EventHandler<ActionEvent>() {
				public void handle(ActionEvent e) {
					setHotkey(null);
					try {
						HotkeyController.saveHotkeys();
					} catch (ConfigurationException e1) {
						log.debug(e1);
					}
				}
			});
		}

		private void setAlignment() {
			label.setPrefWidth(200);
			changeButton.setMinWidth(71);
			unbindButton.setMinWidth(71);
			hbox = new HBox(label, textField, changeButton, unbindButton);
			hbox.setAlignment(Pos.CENTER_LEFT);
			hbox.setPrefHeight(31);
			label.setText(getPrettyName());
			label.setAlignment(Pos.CENTER_LEFT);
			Insets labelInset = new Insets(0, 0, 0, 10);
			Insets buttonInset = new Insets(0, 10, 0, 0);
			unbindButton.setMinHeight(31);
			hbox.setMargin(label, labelInset);
			hbox.setMargin(unbindButton, buttonInset);
			textField.setEditable(false);
			hbox.setHgrow(textField, Priority.ALWAYS);
		}

		public String getPrettyName() {
			return hotkeyName.replace("_", " ");
		}

		public String getName() {
			return hotkeyName;
		}

		public String getHotkeyText() {
			if (keyCode == null) {
				return "null";
			}
			return keyCode;
		}

		public boolean match(KeyEvent keyEvent) {
			if (keyCode == null) {
				return false;
			}
			return ((KeyCodeCombination) KeyCodeCombination.keyCombination(keyCode)).match(keyEvent);
		}

		public void setHotkey(String keyCode) {
			this.keyCode = keyCode;
			if (keyCode == null) {
				textField.setText("");
			} else {
				textField.setText(keyCode);
			}
		}

		public HBox getHotkeyHBox() {
			return hbox;
		}
	}

	public static final String FORWARD_SHOW_HOTKEY = "Show_Players_Forward_One";
	public static final String BACKWARD_SHOW_HOTKEY = "Show_Players_Back_One";
	public static final String NPC_HOTKEY = "Create_NPC";
	public static final String GOD_HOTKEY = "Create_God";
	public static final String REGION_HOTKEY = "Create_Region";
	public static final String TEMPLATE_HOTKEY = "Create_Template";
	public static final String SHOW_HOTKEY = "Show_Players";
	public static final String IMAGE_HOTKEY = "Select_Image";
	public static final String INTERACTION_HOTKEY = "Create_Interaction";
	public static final String SEARCH_HOTKEY = "Search";
	public static final String TAB_FORWARD_HOTKEY = "Next_Tab";
	public static final String TAB_BACKWARD_HOTKEY = "Previous_Tab";
	public static final String MANUAL_SAVE_CURRENT = "Manual_Save_Current_Tab";
	public static final String MANUAL_SAVE_ALL = "Manual_Save_All_Tabs";
	public static final String GROUP_HOTKEY = "Create_Group";
	public static final String STATBLOCK_HOTKEY = "Open_Statblock";
	public static final String FOCUS_HOTKEY = "Focus_On_Entity_Name";
	public static final String SOUND_HOTKEY = "Change_Sound";
	public static final String ACCORDION_ONE = "First_Accordion";
	public static final String[] HOTKEYS = { FORWARD_SHOW_HOTKEY, BACKWARD_SHOW_HOTKEY, NPC_HOTKEY, GOD_HOTKEY,
			REGION_HOTKEY, GROUP_HOTKEY, INTERACTION_HOTKEY, STATBLOCK_HOTKEY, TEMPLATE_HOTKEY, SHOW_HOTKEY, IMAGE_HOTKEY, SOUND_HOTKEY, SEARCH_HOTKEY,
			TAB_FORWARD_HOTKEY, TAB_BACKWARD_HOTKEY, FOCUS_HOTKEY, MANUAL_SAVE_CURRENT, MANUAL_SAVE_ALL, ACCORDION_ONE};

	private static HashMap<String, Hotkey> hotkeys = new HashMap<String, Hotkey>();

	public static void init() throws ConfigurationException {
		configManager = new Configurations();
		builder = new FileBasedConfigurationBuilder<FileBasedConfiguration>(PropertiesConfiguration.class)
				.configure(configManager.getParameters().fileBased().setFile(App.appFileUtil.getHotkeysFile()));
		config = builder.getConfiguration();
		loadHotkeys();
	}

	public static void loadHotkeys() {
		for (String key : HOTKEYS) {
			String value = null;
			try {
				value = config.getString(key);
				if (value.equals("null")) {
					value = null;
				}
				hotkeys.put(key, new Hotkey(key, value));
			} catch (Exception e) {
				log.info(key + " had no set hotkey");
				hotkeys.put(key, new Hotkey(key, value));
				log.debug(e);
			}

		}
	}

	public static void saveHotkeys() throws ConfigurationException {
		log.debug("Saving Hotkeys");
		for (String key : hotkeys.keySet()) {
			config.setProperty(key, hotkeys.get(key).getHotkeyText());
			log.debug(hotkeys.get(key).getHotkeyText());
		}
		builder.save();
	}

	public void start(Stage primaryStage, ScrollPane rootLayout) throws Exception {
		primaryStage.setTitle("Edit Hotkeys");
		Scene myScene = new Scene(rootLayout);
		ourStage = primaryStage;
		myScene.addEventFilter(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
			public void handle(KeyEvent event) {
				if (changeHotkey) {
					if (addHotkey(event, changeHotkeyFunction)) {
						changeHotkey = false;
					}
				}
			}
		});
		for (String key : HOTKEYS) {
			hotkeyVBox.getChildren().add(hotkeys.get(key).getHotkeyHBox());
		}
		primaryStage.setScene(myScene);
	}

	public static HotkeyController startHotkeyController() throws Exception {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(HotkeyController.class.getResource("hotkeySettings.fxml"));
		ScrollPane rootLayout = (ScrollPane) loader.load();
		hotkeyController = (HotkeyController) loader.getController();
		hotkeyController.start(new Stage(), rootLayout);
		started = true;
		return hotkeyController;
	}

	public void showController() {
		if (!ourStage.isShowing()) {
			ourStage.show();
		}
		ourStage.requestFocus();
	}

	public static Hotkey getHotkey(String hotkeyFunction) {
		return hotkeys.get(hotkeyFunction);
	}

	public static void giveSearchBarHotkey(TextField scene){
		scene.addEventFilter(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
			public void handle(final KeyEvent keyEvent) {
				if (KeyCode.ENTER == keyEvent.getCode()) {
					try {
						App.getMainController().open(App.getMainController().getSearchList().getList().getItems().get(0));
						keyEvent.consume();
					} catch (Exception e) {
						
					}
				}
			}
		});
	}
	public static void giveStatblockHotkeys(Scene scene){
		scene.addEventFilter(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
			public void handle(final KeyEvent keyEvent) {
				if (HotkeyController.getHotkey(TAB_BACKWARD_HOTKEY).match(keyEvent)) {
					try {
						App.getStatBlockController().prevTab();
					} catch (Exception e) {
						log.debug(e);
					}
				}
				if (HotkeyController.getHotkey(TAB_FORWARD_HOTKEY).match(keyEvent)) {
					try {
						App.getStatBlockController().nextTab();
					} catch (Exception e) {
						log.debug(e);
					}
				}
				if (HotkeyController.getHotkey(MANUAL_SAVE_CURRENT).match(keyEvent)) {
					try {
						App.getStatBlockController().findTab(App.getStatBlockController().getTabPane().getSelectionModel().getSelectedItem()).fullSave();
					} catch (Exception e) {
						log.debug(e);
					}
				}
				if (HotkeyController.getHotkey(MANUAL_SAVE_ALL).match(keyEvent)) {
					try {
						for(Saveable s: App.getStatBlockController().tabControllers){
							s.fullSave();							
						}
					} catch (Exception e) {
						log.debug(e);
					}
				}
			}
		});
	}
	
	public static void giveMainControllerHotkeys(Scene scene) {
		scene.addEventFilter(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
			public void handle(final KeyEvent keyEvent) {
				if (HotkeyController.getHotkey(TAB_BACKWARD_HOTKEY).match(keyEvent)) {
					try {
						App.getMainController().prevTab();
						keyEvent.consume();
					} catch (Exception e) {
						log.debug(e);
					}
				}
				if (HotkeyController.getHotkey(TAB_FORWARD_HOTKEY).match(keyEvent)) {
					try {
						App.getMainController().nextTab();
						keyEvent.consume();
					} catch (Exception e) {
						log.debug(e);
					}
				}
				if (HotkeyController.getHotkey(MANUAL_SAVE_CURRENT).match(keyEvent)) {
					try {
						App.getMainController().findTab(App.getMainController().getTabPane().getSelectionModel().getSelectedItem()).fullSave();} catch (Exception e) {
						log.debug(e);
					}
				}
				if (HotkeyController.getHotkey(MANUAL_SAVE_ALL).match(keyEvent)) {
					try {
						for(Saveable s: App.getMainController().tabControllers){
							s.fullSave();							
						}
					} catch (Exception e) {
						log.debug(e);
					}
				}
			}
		});
	}
	public static void giveGlobalHotkeys(Scene scene) {
		scene.addEventFilter(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
			public void handle(final KeyEvent keyEvent) {
				String character = keyEvent.getCode().getName();
				int number = 0;
				try {
					number = Integer.parseInt(character);
					number--;
					if (number < 0){
						number = 10;
					}
				}
				catch (Exception e){
					number = -1;
				}
				final int pane = number; 
				if (HotkeyController.getHotkey(BACKWARD_SHOW_HOTKEY).match(keyEvent)) {
					try {
						App.getShowPlayersController().back();
					} catch (Exception e) {
						log.error(e);
					}
				}
				if (HotkeyController.getHotkey(GROUP_HOTKEY).match(keyEvent)) {
					try {
						App.getMainController().CreateEvent();
						App.getMainController().getStage().requestFocus();
					} catch (Exception e) {
						log.error(e);
					}
				}
				if (HotkeyController.getHotkey(FORWARD_SHOW_HOTKEY).match(keyEvent)) {
					try {
						App.getShowPlayersController().forward();
					} catch (Exception e) {
						log.error(e);
					}
				}
				if (HotkeyController.getHotkey(FORWARD_SHOW_HOTKEY).match(keyEvent)) {
					try {
						App.getShowPlayersController().forward();
					} catch (Exception e) {
						log.error(e);
					}
				}
				if (HotkeyController.getHotkey(STATBLOCK_HOTKEY).match(keyEvent)) {
					try {
						Saveable tab = App.getMainController()
						.findTab(App.getMainController().getTabPane().getSelectionModel().getSelectedItem());
						if(tab instanceof GodTabController)
						{
							((GodTabController) tab).showStatBlock();
						}
						else if(tab instanceof RegionTabController)
						{
							((RegionTabController) tab).showStatBlock();
						}
						else if(tab instanceof EventTabController)
						{
							
						}
						else if(tab instanceof NpcTabController)
						{
							((NpcTabController) tab).showStatBlock();
						}
					} catch (Exception e) {
						log.error(e);
					}
				}
				if (HotkeyController.getHotkey(FOCUS_HOTKEY).match(keyEvent)) {
					try {
						Saveable tab = App.getMainController()
						.findTab(App.getMainController().getTabPane().getSelectionModel().getSelectedItem());
						tab.nameFocus();
					} catch (Exception e) {
						log.error(e);
					}
				}
				if (HotkeyController.getHotkey(NPC_HOTKEY).match(keyEvent)) {
					try {
						App.getMainController().CreateNPC();
						App.getMainController().getStage().requestFocus();
					} catch (Exception e) {
						log.error(e);
					}
				}
				if (HotkeyController.getHotkey(GOD_HOTKEY).match(keyEvent)) {
					try {
						App.getMainController().CreateGod();
						App.getMainController().getStage().requestFocus();
					} catch (Exception e) {
						log.error(e);
					}
				}
				if (HotkeyController.getHotkey(REGION_HOTKEY).match(keyEvent)) {
					try {
						App.getMainController().CreateRegion();
						App.getMainController().getStage().requestFocus();
					} catch (Exception e) {
						log.error(e);
					}
				}
				if(HotkeyController.getHotkey(TEMPLATE_HOTKEY).match(keyEvent)){
					try{
						App.getMainController().CreateTemplate();
						App.getMainController().getStage().requestFocus();
					} catch (Exception e) {
						log.error(e);
					}
				}
				if (HotkeyController.getHotkey(SHOW_HOTKEY).match(keyEvent)) {
					try {
						App.getMainController().showPlayers();
					} catch (Exception e) {
						log.error(e);
					}
				}
				if (HotkeyController.getHotkey(IMAGE_HOTKEY).match(keyEvent)) {
					try {
						AddableImage image = App.getMainController()
								.findTab(App.getMainController().getTabPane().getSelectionModel().getSelectedItem())
								.getAddableImage();
						if (image != null){
							image.addImage();
						}
					} catch (Exception e) {
						log.error(e);
					}
				}
				if (HotkeyController.getHotkey(SOUND_HOTKEY).match(keyEvent)) {
					try {
						AddableSound sound = App.getMainController()
								.findTab(App.getMainController().getTabPane().getSelectionModel().getSelectedItem()).getAddableSound();
						if (sound != null){
							sound.changeSound();
						}
					} catch (Exception e) {
						log.error(e);
					}
				}
				if (keyEvent.isAltDown() && number != -1){
					try {
						log.debug(number);
						Accordion accordion = null;
						final Saveable tab = App.getMainController()
								.findTab(App.getMainController().getTabPane().getSelectionModel().getSelectedItem());
								if(tab instanceof GodTabController)
								{
									accordion = ((GodTabController) tab).getAccordion();
									accordion.getPanes().get(number).setExpanded(true);
									Platform.runLater(new Runnable() {
								        public void run() {
								        	if (pane == 0)
								        	{
								        		((GodTabController) tab).getNpcRelation().getListView().requestFocus();
									        	((GodTabController) tab).getNpcRelation().getListView().getFocusModel().focus(0);
									        	((GodTabController) tab).getNpcRelation().getListView().getSelectionModel().select(0);
									        	if (keyEvent.isShiftDown())
									        	{
									        		((GodTabController) tab).getNpcRelation().handleAddButton();
									        	}
								        	
								        	}
								        	else if (pane == 1)
								        	{
								        		((GodTabController) tab).getGodRelation().getListView().requestFocus();
									        	((GodTabController) tab).getGodRelation().getListView().getFocusModel().focus(0);
									        	((GodTabController) tab).getGodRelation().getListView().getSelectionModel().select(0);
								        	}
								        	else if (pane == 2)
								        	{
								        		((GodTabController) tab).getEventRelation().getListView().requestFocus();
									        	((GodTabController) tab).getEventRelation().getListView().getFocusModel().focus(0);
									        	((GodTabController) tab).getEventRelation().getListView().getSelectionModel().select(0);
									        	if (keyEvent.isShiftDown())
									        	{
									        		((GodTabController) tab).getEventRelation().handleAddButton();
									        	}
								        	}
								        	else if (pane == 3)
								        	{
									        	((GodTabController) tab).getRegionRelation().getListView().requestFocus();
									        	((GodTabController) tab).getRegionRelation().getListView().getFocusModel().focus(0);
									        	((GodTabController) tab).getRegionRelation().getListView().getSelectionModel().select(0);
									        	if (keyEvent.isShiftDown())
									        	{
									        		((GodTabController) tab).getRegionRelation().handleAddButton();
									        	}
								        	}
								        }
								    });
								}
								else if(tab instanceof RegionTabController)
								{
									accordion = ((RegionTabController) tab).getAccordion();
									accordion.getPanes().get(number).setExpanded(true);
									Platform.runLater(new Runnable() {
								        public void run() {
								        	if (pane == 0)
								        	{
									        	((RegionTabController) tab).getRegionRelation().getListView().requestFocus();
									        	((RegionTabController) tab).getRegionRelation().getListView().getFocusModel().focus(0);
									        	((RegionTabController) tab).getRegionRelation().getListView().getSelectionModel().select(0);
									        	if (keyEvent.isShiftDown())
									        	{
									        		((RegionTabController) tab).getRegionRelation().handleAddButton();
									        	}
								        	}
								        	else if (pane == 1)
								        	{
								        		((RegionTabController) tab).getNpcRelation().getListView().requestFocus();
									        	((RegionTabController) tab).getNpcRelation().getListView().getFocusModel().focus(0);
									        	((RegionTabController) tab).getNpcRelation().getListView().getSelectionModel().select(0);
									        	if (keyEvent.isShiftDown())
									        	{
									        		((RegionTabController) tab).getNpcRelation().handleAddButton();
									        	}
								        	}
								        	else if (pane == 2)
								        	{
								        		((RegionTabController) tab).getGodRelation().getListView().requestFocus();
									        	((RegionTabController) tab).getGodRelation().getListView().getFocusModel().focus(0);
									        	((RegionTabController) tab).getGodRelation().getListView().getSelectionModel().select(0);
									        	if (keyEvent.isShiftDown())
									        	{
									        		((RegionTabController) tab).getGodRelation().handleAddButton();
									        	}
								        	}
								        	else if (pane == 3)
								        	{
								        		((RegionTabController) tab).getEventRelation().getListView().requestFocus();
									        	((RegionTabController) tab).getEventRelation().getListView().getFocusModel().focus(0);
									        	((RegionTabController) tab).getEventRelation().getListView().getSelectionModel().select(0);
									        	if (keyEvent.isShiftDown())
									        	{
									        		((RegionTabController) tab).getEventRelation().handleAddButton();
									        	}
								        	}
								        }
								    });
								}
								else if(tab instanceof EventTabController)
								{
									accordion = ((EventTabController) tab).getAccordion();
									accordion.getPanes().get(number).setExpanded(true);
									Platform.runLater(new Runnable() {
								        public void run() {
								        	if (pane == 0)
								        	{
									        	((EventTabController) tab).getNpcRelation().getListView().requestFocus();
									        	((EventTabController) tab).getNpcRelation().getListView().getFocusModel().focus(0);
									        	((EventTabController) tab).getNpcRelation().getListView().getSelectionModel().select(0);
									        	if (keyEvent.isShiftDown())
									        	{
									        		((EventTabController) tab).getNpcRelation().handleAddButton();
									        	}
								        	}
								        	else if (pane == 1)
								        	{
								        		((EventTabController) tab).getGodRelation().getListView().requestFocus();
									        	((EventTabController) tab).getGodRelation().getListView().getFocusModel().focus(0);
									        	((EventTabController) tab).getGodRelation().getListView().getSelectionModel().select(0);
									        	if (keyEvent.isShiftDown())
									        	{
									        		((EventTabController) tab).getGodRelation().handleAddButton();
									        	}
								        	}
								        }
								    });
								}
								else if(tab instanceof NpcTabController)
								{
									accordion = ((NpcTabController) tab).getAccordion();
									accordion.getPanes().get(number).setExpanded(true);
									Platform.runLater(new Runnable() {
								        public void run() {
								        	if (pane == 0)
								        	{
									        	((NpcTabController) tab).getNpcRelation().getListView().requestFocus();
									        	((NpcTabController) tab).getNpcRelation().getListView().getFocusModel().focus(0);
									        	((NpcTabController) tab).getNpcRelation().getListView().getSelectionModel().select(0);
								        	}
								        	else if (pane == 1)
								        	{
								        		((NpcTabController) tab).getEventRelation().getListView().requestFocus();
									        	((NpcTabController) tab).getEventRelation().getListView().getFocusModel().focus(0);
									        	((NpcTabController) tab).getEventRelation().getListView().getSelectionModel().select(0);
									        	if (keyEvent.isShiftDown())
									        	{
									        		((NpcTabController) tab).getEventRelation().handleAddButton();
									        	}
								        	}
								        	else if (pane == 2)
								        	{
								        		((NpcTabController) tab).getRegionRelation().getListView().requestFocus();
									        	((NpcTabController) tab).getRegionRelation().getListView().getFocusModel().focus(0);
									        	((NpcTabController) tab).getRegionRelation().getListView().getSelectionModel().select(0);	
									        	if (keyEvent.isShiftDown())
									        	{
									        		((NpcTabController) tab).getRegionRelation().handleAddButton();
									        	}
								        	}
								        }
								    });
								}
								if (accordion != null)
								{
									
								}
					} catch (Exception e) {
						log.error(e);
					}
				}
				if (HotkeyController.getHotkey(INTERACTION_HOTKEY).match(keyEvent)) {
					try {
						ListController listController = App.getMainController()
								.findTab(App.getMainController().getTabPane().getSelectionModel().getSelectedItem())
								.getListController();
						if (listController != null){
							App.getMainController().getStage().requestFocus();
							listController.addNewInteraction();
						}
						} catch (Exception e) {
						log.error(e);
					}
				}
				if (HotkeyController.getHotkey(SEARCH_HOTKEY).match(keyEvent)) {
					try {
						App.getMainController().getStage().requestFocus();
						App.getMainController().getSearchList().getSearchField().requestFocus();
					} catch (Exception e) {
						log.error(e);
					}
				}
				
				for (String hotkey : HOTKEYS){
					if (HotkeyController.getHotkey(hotkey).match(keyEvent)){
						keyEvent.consume();
					}
				}
				if (keyEvent.isAltDown()){
					keyEvent.consume();
				}
			}
		});
	}

	public static boolean isStarted() {
		return started;
	}

	private static boolean addHotkey(KeyEvent event, String function) {
		String keyCombString = "";
		boolean modified = false;
		if (event.isShiftDown()) {
			modified = true;
			keyCombString += "Shift+";
		}
		if (event.isAltDown()) {
			modified = true;
			keyCombString += "Alt+";
		}
		if (event.isShortcutDown()) {
			modified = true;
			keyCombString += "Shortcut+";
		}
		keyCombString += event.getCode().getName();
		if (!event.getCode().isModifierKey() && modified) {
			hotkeys.get(function).setHotkey(keyCombString);
			try {
				saveHotkeys();
			} catch (ConfigurationException e) {
				e.printStackTrace();
			}
			return true;
		}
		return false;
	}
}
