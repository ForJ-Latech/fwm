package com.forj.fwm.gui;

import java.awt.Desktop;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.jetty.util.log.Log;

import com.forj.fwm.backend.Backend;
import com.forj.fwm.backend.ShowPlayersDataModel;
import com.forj.fwm.conf.HotkeyController;
import com.forj.fwm.conf.WorldConfig;
import com.forj.fwm.entity.God;
import com.forj.fwm.entity.Npc;
import com.forj.fwm.entity.Region;
import com.forj.fwm.entity.Event;
import com.forj.fwm.entity.Searchable;
import com.forj.fwm.entity.Template;
import com.forj.fwm.gui.component.Openable;
import com.forj.fwm.gui.component.TabControlled;
import com.forj.fwm.gui.tab.EventTabController;
import com.forj.fwm.gui.tab.GodTabController;
import com.forj.fwm.gui.tab.NpcTabController;
import com.forj.fwm.gui.tab.RegionTabController;
import com.forj.fwm.gui.tab.Saveable;
import com.forj.fwm.gui.tab.TemplateTabController;
import com.forj.fwm.gui.tab.WelcomeTabController;
import com.forj.fwm.startup.App;
import com.forj.fwm.startup.WorldFileUtil;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.SplitPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputControl;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.AudioClip;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.stage.WindowEvent;
import javafx.util.Callback;

/**
 * http://docs.oracle.com/javafx/2/fxml_get_started/jfxpub-fxml_get_started.htm
 * 
 * For tabbed components, it's possible look here:
 * http://stackoverflow.com/questions/19889882/javafx-tabpane-one-controller-for-each-tab#19889980
 * 
 * 
 * @author jehlmann, shibbard, rwalters
 *
 */
public class MainController extends TabControlled implements Openable {

	private ShowPlayersController spController;

	@FXML
	private MenuBar MenuBar;

	@FXML
	private Menu FileMenu, GlobalSettingsMenu, WorldSettingsMenu, HelpMenu;

	@FXML
	private Button CreateNPCButton, CreateGodButton, CreateEventButton, CreateRegionButton, showPlayersButton;
    
    @FXML
    private VBox randy, statusVBoxmc;
    
    @FXML
    private StackPane statusStackPane;
   
    private StatusBarController statusBarController;

	private static Logger log = Logger.getLogger(MainController.class);
	private static boolean started = false;
	private Stage ourStage;

	public static boolean getStarted() {
		return started;
	}

	@FXML
	private VBox searchPane;

	private SearchList sl;
	public SearchList getSearchList(){
		return sl;
	}

	
	public void startSearchList() throws Exception {
		searchPane.getChildren().clear();
		sl = SearchList.createSearchList(SearchList.EntitiesToSearch.ALL, this);
		sl.getOurRoot().minHeightProperty().bind((searchPane.heightProperty().subtract(10)));
		searchPane.getChildren().add(sl.getOurRoot());
	}

	public void start(Stage primaryStage, Pane rootLayout) throws Exception {
		primaryStage.setTitle("Fantasy World Manager");
		primaryStage.getIcons()
				.add(new Image(App.retGlobalResource("/src/main/webapp/WEB-INF/images/icons/application/64.png").openStream()));
		Scene myScene = new Scene(rootLayout);
		myScene.getStylesheets().add(App.retGlobalResource("/src/main/java/com/forj/fwm/gui/mainStylesheet.css").toString());
		primaryStage.setScene(myScene);
		primaryStage.setMinWidth(640);
		primaryStage.setMinHeight(520);
		primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			   public void handle(WindowEvent we) {
			       log.debug("Main Controller is closing");
			       started = false;
			   }
		   });
		
		primaryStage.show();
		statusBarController = new StatusBarController(statusStackPane, statusVBoxmc);
		statusVBoxmc.getChildren().add(statusBarController.getSmallStatus());
		ourStage = primaryStage;
		HotkeyController.giveGlobalHotkeys(myScene);
		HotkeyController.giveMainControllerHotkeys(myScene);
		startSearchList();
		WelcomeTabController cr = WelcomeTabController.startWelcomeTab();
		HotkeyController.giveSearchBarHotkey(sl.getSearchField());
		tabPane.getTabs().add(cr.getTab());
		tabPane.getSelectionModel().select(cr.getTab());
		tabControllers.add(cr);
		started = true;
		MenuBar.setOnKeyPressed(new EventHandler<KeyEvent>() {
			public void handle(KeyEvent key) {
			
			}
		}); 
	}

	public static MainController startMainUi() throws Exception {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(MainController.class.getResource("mainView.fxml"));
		VBox rootLayout = (VBox) loader.load();
		MainController cr = (MainController) loader.getController();
		cr.start(new Stage(), rootLayout);
		started = true;
		return cr;
	}

	@FXML
	public void CreateGod() throws Exception {
		log.debug("Create God called.");
		GodTabController cr = GodTabController.startGodTab(null);
		addTabController(cr);
	}

	@FXML
	public void CreateNPC() throws Exception {
		log.debug("Create NPC called.");
		NpcTabController cr = NpcTabController.startNpcTab(null);
		addTabController(cr);
		tabPane.getSelectionModel().select(cr.getTab());
	}

	@FXML
	public void CreateRegion() throws Exception {
		log.debug("Create Region called.");
		RegionTabController cr = RegionTabController.startRegionTab(null);
		addTabController(cr);
		tabPane.getSelectionModel().select(cr.getTab());
	}

	@FXML
	public void CreateEvent() throws Exception {
		log.debug("Create Event called.");
		EventTabController cr = EventTabController.startEventTab(null);
		addTabController(cr);
		tabPane.getSelectionModel().select(cr.getTab());
	}

	@FXML
	public void showPlayers() throws Exception {
		log.debug("attempting to show players.");
		spController = App.getShowPlayersController();
    	if (!spController.isShowing() && WorldConfig.getShowPlayersPopup()){
    		spController.showController();
    	}
		App.spdc.setUpdated(true);
    	Tab curSelected = tabPane.getSelectionModel().getSelectedItem();
    	for(Saveable s: tabControllers){		
    		if(s.getTab().equals(curSelected)){
    			App.spdc.addOne(s.getThing());
    			s.getThing().setShown(true);
    			Backend.SaveSimpleSearchable(s.getThing());
    			if (WorldConfig.getShowPlayersPopup())
    	    	{
	    			spController.playSound(s);
	    	    	spController.setObject(App.spdc.getDefault());
    	    	}
    		}
    	}
    }

	@FXML
	public void showReadme() throws Exception {
		log.debug("Readme Called");
		GenericTextController cr = GenericTextController.startGenericTextController("Readme");
		cr.setTextFromFile(App.retGlobalResource("/src/main/ui/Readme.txt").openStream());
	}

	@FXML
	public void showAbout() throws Exception {
		log.debug("About Called");
		GenericTextController cr = GenericTextController.startGenericTextController("About");
		cr.setTextFromFile(App.retGlobalResource("/src/main/ui/About.txt").openStream());
	}

	@FXML
	public void showWorldSettings() throws Exception{
		log.debug("World Settings called");
		WorldSettingsController.startWorldSettingsController();
	}
	
	@FXML
	public void showHotkeySettings() throws Exception{
		log.debug("Hotkey Settings called");
		App.getHotkeyController().showController();
	}
	
	@FXML
	public void openWebsite() throws Exception{
		log.debug("Open ForJ Website called");
		try {
			Desktop.getDesktop().browse(new URL("http://138.47.200.245/jehlmann/fwm").toURI());
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}
	
	@FXML
	public void startWebservice(){
		
		if(!JettyController.getStarted())
		{
			try {
				log.debug("starting web service. lol");
				JettyController.startJettyWindow();
			} catch (Exception e) {
				log.debug(e);
			}
		}
	}
	
	public Window getStage() {
		return ourStage;
	}

	public void addStatus(String text) {
		statusBarController.addStatus(text);
	}
	
	public void open(Searchable o) throws Exception {
		log.debug("opening tab from search");
		if (o instanceof God) {
			GodTabController tab = GodTabController.startGodTab(Backend.getGodDao().getFullGod(o.getID()));
			addTabController(tab);
		} else if (o instanceof Npc) {
			NpcTabController tab = NpcTabController.startNpcTab((Npc) o);
			addTabController(tab);
		} else if (o instanceof Region) {
			RegionTabController tab = RegionTabController.startRegionTab((Region) o);
			addTabController(tab);
		}
		else if (o instanceof Event) { 
			EventTabController tab = EventTabController.startEventTab((Event) o);
	      addTabController(tab); 
		}
		else if(o instanceof Template){
			TemplateTabController tab = TemplateTabController.startTemplateTab((Template) o);
			addTabController(tab);
		}
	}
	
	public ListView getListView(){
		// TODO Auto-generated method stub
		return null;
	}
}
