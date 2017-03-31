package com.forj.fwm.gui;

import java.io.File;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.eclipse.jetty.util.log.Log;

import com.forj.fwm.backend.Backend;
import com.forj.fwm.backend.ShowPlayersDataModel;
import com.forj.fwm.entity.God;
import com.forj.fwm.entity.Npc;
import com.forj.fwm.entity.Region;
import com.forj.fwm.entity.Searchable;
import com.forj.fwm.gui.tab.GodTabController;
import com.forj.fwm.gui.tab.NpcTabController;
import com.forj.fwm.gui.tab.RegionTabController;
import com.forj.fwm.gui.tab.Saveable;
import com.forj.fwm.startup.App;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
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
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.Window;
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
public class MainController {
	
	//TODO fix the tab controller jay...
	//TODO fix the tab controllers so that they dispose of themselves.
	private ArrayList<Saveable> tabControllers = new ArrayList<Saveable>();
	private ShowPlayersController spController;

    @FXML
    private MenuBar MenuBar;

    @FXML
    private Menu FileMenu;

    @FXML
    private Menu GlobalSettingsMenu;

    @FXML
    private Menu WorldSettingsMenu;

    @FXML
    private Menu HelpMenu;

    @FXML
    private TextField SearchArea;

    @FXML
    private ListView<Searchable> listView;

    @FXML
    private Button CreateNPCButton;

    @FXML
    private Button CreateGodButton;

    @FXML
    private Button CreateEventButton;

    @FXML
    private Button CreateRegionButton;

    @FXML
    private TabPane TabPane;

    @FXML
    private Button showPlayersButton;

	private static Logger log = Logger.getLogger(MainController.class);

	private static boolean started = false;

	private Stage ourStage;
	
	public static boolean getStarted() {
		return started;
	}

	private EventHandler<Event> searchEvent = new EventHandler<Event>(){
		public void handle(Event event){
			log.debug("Search event firing!");
			SearchDB();
		}
	};

	static class SearchCell extends ListCell<Searchable> {
		@Override
		public void updateItem(Searchable item, boolean empty) {
			super.updateItem(item, empty);
			if (empty || item == null) {
				setText(null);
			}
			else
			{
				setText(item.getName());
			}
		}
	}

	public void start(Stage primaryStage, Pane rootLayout) throws Exception {
		listView.setCellFactory(new Callback<ListView<Searchable>, 
				ListCell<Searchable>>() {
			public ListCell<Searchable> call(ListView<Searchable> arg0) {				
				return new SearchCell();
			}
		});
		primaryStage.setTitle("Fantasy World Manager");
		primaryStage.getIcons().add(new Image(App.retGlobalResource("/src/main/webapp/WEB-INF/images/FWM-icon.png").openStream()));
		Scene myScene = new Scene(rootLayout);
		primaryStage.setScene(myScene);
		primaryStage.show();
		TextInputControl[] thingsThatCanChange = new TextInputControl[] {SearchArea};
		for(TextInputControl c: thingsThatCanChange){
			c.setOnKeyTyped(searchEvent);
		}

		listView.setOnMouseClicked(new EventHandler<MouseEvent>() {

			public void handle(MouseEvent event) {
				System.out.println("clicked on " + listView.getSelectionModel().getSelectedItem());
				try {
					OpenTab(listView.getSelectionModel().getSelectedItem());
				} catch (Exception e) {
					log.error(e);
					e.printStackTrace();
				}
			}
		});
		ourStage = primaryStage;
		started = true;
	}

	public static MainController startMainUi() throws Exception {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(MainController.class.getResource("mainView.fxml"));
		VBox rootLayout = (VBox)loader.load();
		MainController cr = (MainController)loader.getController();
		cr.start(new Stage(), rootLayout);
		started = true;
		return cr;
	}

	@FXML
	void SearchDB(){
		listView.getItems().clear();
		try {
			for (Npc npc : Backend.getNpcDao().queryForLike("fName",SearchArea.getText())) {
				listView.getItems().add(npc);
			}
			for (God god : Backend.getGodDao().queryForLike("name",SearchArea.getText())) {
				listView.getItems().add(god);
			}
			for (Region region : Backend.getRegionDao().queryForLike("name",SearchArea.getText())) {
				listView.getItems().add(region);
			}
			/*
			for (Event event : Backend.getEventDao().queryForLike("Name",SearchArea.getText())) {
				listView.getItems().add(event);
			}
			 */
		} catch (SQLException e) {
			log.error(e.getStackTrace());
		}
	}

	@FXML
	void CreateGod() throws Exception {
		// pray to god that this works. 
		log.debug("Create God called.");
		God g = new God();
		g.setName("Satan");
		GodTabController cr = GodTabController.startGodTab(g);
		addTabController(cr);
	}

	@FXML
	void CreateNPC() throws Exception {
		log.debug("Create NPC called.");
		Npc n = new Npc();
		n.setfName("Joe");
		NpcTabController cr = NpcTabController.startNpcTab(n);
		addTabController(cr);
	}

	@FXML
	private void CreateRegion() throws Exception {
		log.debug("Create Region called.");
		Region r = new Region();
		r.setName("Hyrule");
		RegionTabController cr = RegionTabController.startRegionTab(r);
		addTabController(cr);
	}

	@FXML
	void CreateEvent() {
		System.out.println("Event");
	}

	@FXML
	void OpenTab(Object o) throws Exception{
		log.debug("opening tab from search");
		if (o instanceof God)
		{
			GodTabController tab = GodTabController.startGodTab((God) o);
			addTabController(tab);
		}
		else if (o instanceof Npc)
		{
			NpcTabController tab = NpcTabController.startNpcTab((Npc) o);
			addTabController(tab);
		}
		else if (o instanceof Region)
		{
			RegionTabController tab = RegionTabController.startRegionTab((Region) o);
			addTabController(tab);
		}
		/*
    	else if (o instanceof Event)
    	{
    		EventTabController tab = EventTabController.startEventTab((Event) o, this);
    	}
		 */

	}
	
	@FXML
    public void showPlayers() throws Exception{
		log.debug("attempting to show players.");
    	if (!ShowPlayersController.getOpen()){
    		spController = ShowPlayersController.startShowPlayersWindow();
    	}
    	Tab curSelected = TabPane.getSelectionModel().getSelectedItem();
    	for(Saveable s: tabControllers){
    		if(s.getTab().equals(curSelected)){
    			ShowPlayersDataModel.addOne(s.getThing());
    		}
    	}
    	spController.setObject(ShowPlayersDataModel.getDefault());
    }
	
	
	public void addTabController(Saveable s){
		boolean existed = false;
		for(Saveable x: tabControllers){
			if(x.getThing().equals(s.getThing())){
				existed = true;
				TabPane.getSelectionModel().select(x.getTab());
			}
		}
		
		if(!existed){
			TabPane.getTabs().add(s.getTab());
			TabPane.getSelectionModel().select(s.getTab());
			tabControllers.add(s);
		}
		else
		{
			// clean up our item, wish it was like c# where i can .dispose(); 
			s = null; 
		}
	}
	
	public void disposeTabController(Saveable s){
		for(int i = 0; i < tabControllers.size(); i++){
			if(s.getTab().equals(tabControllers.get(i).getTab())){
				tabControllers.remove(i);
			}
		}
	}

	@FXML
	public void showReadme() throws Exception{
		log.debug("Readme Called");
		GenericTextController cr = GenericTextController.startGenericTextController("Readme");
		cr.setTextFromFile(new File(getClass().getResource("/src/main/ui/Readme.txt").toString()));
	}

	public Window getStage() {
		return ourStage;
	}
}
