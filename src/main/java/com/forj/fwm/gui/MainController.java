package com.forj.fwm.gui;

import java.awt.Desktop;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.log4j.Logger;

import com.forj.fwm.backend.Backend;
import com.forj.fwm.backend.DefaultStatblockBackend;
import com.forj.fwm.conf.AppConfig;
import com.forj.fwm.conf.HotkeyController;
import com.forj.fwm.conf.WorldConfig;
import com.forj.fwm.entity.Event;
import com.forj.fwm.entity.God;
import com.forj.fwm.entity.Npc;
import com.forj.fwm.entity.Region;
import com.forj.fwm.entity.Searchable;
import com.forj.fwm.entity.Statblock;
import com.forj.fwm.entity.Template;
import com.forj.fwm.gui.component.MainEntityTab;
import com.forj.fwm.gui.component.Openable;
import com.forj.fwm.gui.component.Saveable;
import com.forj.fwm.gui.component.TabControlled;
import com.forj.fwm.gui.tab.EventTabController;
import com.forj.fwm.gui.tab.GodTabController;
import com.forj.fwm.gui.tab.InteractionTabController;
import com.forj.fwm.gui.tab.NpcTabController;
import com.forj.fwm.gui.tab.PreviouslyEditedTabController;
import com.forj.fwm.gui.tab.RegionTabController;
import com.forj.fwm.gui.tab.StatBlockTabController;
import com.forj.fwm.gui.tab.TemplateTabController;
import com.forj.fwm.gui.tab.WelcomeTabController;
import com.forj.fwm.startup.App;
import com.forj.fwm.startup.ComponentSelectorController;
import com.forj.fwm.startup.WorldSelector;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.Tab;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.stage.WindowEvent;

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
	private static Logger log = Logger.getLogger(MainController.class);
	
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
    
    @FXML
    private Label showLabel;
   
    private StatusBarController statusBarController;
    private WorldSettingsController ws;
    private Scene theScene;
    
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
		primaryStage.setTitle("Fantasy World Manager - " + App.worldFileUtil.getWorldName());
		primaryStage.getIcons()
				.add(new Image(App.retGlobalResource("/src/main/webapp/WEB-INF/images/icons/application/64.png").openStream()));
		Scene myScene = new Scene(rootLayout);
		myScene.getStylesheets().add(App.retGlobalResource("/src/main/ui/mainStylesheet.css").toString());
		primaryStage.setScene(myScene);
		primaryStage.setMinWidth(640);
		primaryStage.setMinHeight(520);
		primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			   public void handle(WindowEvent we) {
			       log.debug("Main Controller is closing");
			       started = false;
			   }
		   });
		
		setScene(myScene);
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
		startAutoUpdateTabs();
		MenuBar.setOnKeyPressed(new EventHandler<KeyEvent>() {
			public void handle(KeyEvent key) {
			//TODO figure out if this is needed here.
			}
		});
		if(AppConfig.getDarkMode())
		{
			setDark(true);
		}
		started = true;
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
	public void CreateTemplate() throws Exception{
		log.debug("create template called.");
		TemplateTabController cr = TemplateTabController.startTemplateTab(null, this);
		addTabController(cr);
		tabPane.getSelectionModel().select(cr.getTab());
	}
	
	@FXML
	public void CreateNamedStatblock() throws Exception{
		log.debug("create named statblock called");
		Statblock s = new Statblock();
		s.setName("$");
		StatBlockTabController cr = StatBlockTabController.startStatBlockTabController(s, DefaultStatblockBackend.spawnEmptyStatSaveable());
		App.getStatBlockController().addTabController(cr);
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
	    	    	spController.changeShown(App.spdc.getDefault(), true);
    	    	}
    		}
    	}
    }

	@FXML
	public void exitApplication() throws Exception {
		System.exit(0);
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
	public void showCrashCourse() throws Exception {
		log.debug("Crash Course Called");
		GenericTextController cr = GenericTextController.startGenericTextController("CrashCourse");
		cr.setTextFromFile(App.retGlobalResource("/src/main/ui/CrashCourse.txt").openStream());
	}

	@FXML
	public void showWorldSettings() throws Exception{
		log.debug("World Settings called");
		ws = WorldSettingsController.startWorldSettingsController();
	}
	
	@FXML
	public void editNPCStatblock() throws Exception{
		log.debug("Edit NPC Statblock called");
		App.getStatBlockController().show(DefaultStatblockBackend.getNpcStat(), DefaultStatblockBackend.getNpcDefaultSaveable());
	}
	
	@FXML
	public void editGodStatblock() throws Exception{
		log.debug("Edit God Statblock called");
		App.getStatBlockController().show(DefaultStatblockBackend.getGodStat(),  DefaultStatblockBackend.getGodDefaultSaveable());
	}
	
	@FXML
	public void editRegionStatblock() throws Exception{
		log.debug("Edit Region Statblock called");
		App.getStatBlockController().show(DefaultStatblockBackend.getRegionStat(),  DefaultStatblockBackend.getRegionDefaultSaveable());
	}
	
	@FXML
	public void editGroupStatblock() throws Exception{
		log.debug("Edit Group Statblock called");
		App.getStatBlockController().show(DefaultStatblockBackend.getGroupStat(),  DefaultStatblockBackend.getGroupDefaultSaveable());
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
			Desktop.getDesktop().browse(new URL("https://github.com/ForJ-Latech/fwm").toURI());
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}
	
	@FXML
	public void startWebservice(){
		App.getJettyController().getStage().requestFocus();
	}
	
	@FXML public void saveCurrentlySelected(){
		try {
			findTab(tabPane.getSelectionModel().getSelectedItem()).fullSave();
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
		}
	}
	
	@FXML public void saveAll(){
		try {
			for(Saveable s: tabControllers){
				s.fullSave();							
			}
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
		}
	}
	
	@FXML public void refreshAll(){
		for(Saveable s: tabControllers){
			if(s instanceof MainEntityTab){
				s.manualUpdateTab();
			}
		}
		log.debug("refresh successful.");
		addStatus("Refresh Successful");
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
			TemplateTabController tab = TemplateTabController.startTemplateTab((Template) o, this);
			addTabController(tab);
		}
		else if(o instanceof Statblock){
			StatBlockTabController tab = StatBlockTabController.startStatBlockTabController((Statblock)o, DefaultStatblockBackend.spawnEmptyStatSaveable());
			App.getStatBlockController().addTabController(tab);
			App.getStatBlockController().getStage().requestFocus();
		}
	}
	
	public ListView getListView(){
		// TODO Auto-generated method stub
		return null;
	}
	
	public Window getStage() {
		return ourStage;
	}

	public void addStatus(String text) {
		statusBarController.addStatus(text);
	}
	
	public void changeShowLabel(String text) {
		showLabel.setText("Showing: " + text);
	}
	
	public void setScene(Scene myScene)
	{
		theScene = myScene;
	}
	
	public Scene getScene()
	{
		return theScene;
	}
	
	public void setDark(boolean dark)
	{
		if(dark)
		{
			getScene().getStylesheets().add(App.retGlobalResource("/src/main/ui/darkStylesheet.css").toString());
		}
		else
		{
			getScene().getStylesheets().remove(1);
		}
	}
	
	@FXML
	public void openPreviouslyEdited() {
		try {
			PreviouslyEditedTabController tab = PreviouslyEditedTabController.startPreviouslyEditedTab();
			addTabController(tab);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@FXML
	public void openInteractions() {
		try {
			InteractionTabController tab = InteractionTabController.startInteractionTab();
			addTabController(tab);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	@FXML
	public void selectWorld(){
		ComponentSelectorController.getWorldSelector().selectWorld();
		ourStage.close();
		JettyController.close();
		App.getShowPlayersController().closeWindow();
		App.getStatBlockController().closeWindow();
		if(ws != null){
			ws.closeWindow();
		}
	}

}
