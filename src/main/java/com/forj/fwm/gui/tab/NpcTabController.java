package com.forj.fwm.gui.tab;

import java.awt.event.TextEvent;
import java.net.URL;
import java.sql.SQLException;

import javax.swing.event.DocumentEvent.EventType;
import javax.swing.text.TextAction;

import org.apache.log4j.Logger;

import com.forj.fwm.backend.Backend;
import com.forj.fwm.backend.dao.NpcDao;
import com.forj.fwm.entity.Npc;
import com.forj.fwm.entity.Statblock;
import com.forj.fwm.gui.AddableImage;
import com.forj.fwm.gui.MainController;
import com.forj.fwm.startup.App;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Tab;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.control.TextInputControl;

public class NpcTabController implements Saveable {
	private static Logger log = Logger.getLogger(NpcTabController.class);
	private Npc npc;
	
	private ChangeListener<String> nameListener = new ChangeListener<String>(){
		public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
			tabHead.setText(newValue);
		}
	};
	
	private EventHandler<Event> saveEvent = new EventHandler<Event>(){
		public void handle(Event event){
			log.debug("Save event firing!");
			save();
		}
	};
	
	@FXML
    private Tab tabHead;

    @FXML
    private TextField fName;

    @FXML
    private TextField race;

    @FXML
    private TextArea description;

    @FXML
    private TextField lName;

    @FXML
    private TextField classType;

    @FXML
    private TextArea history;

    @FXML
    private TextField gender;

    @FXML
    private TextField god;

    @FXML
    private TextArea attributes;

    @FXML
    private VBox rhsVbox;
	
    private AddableImage image;
    
	private TextInputControl[] thingsThatCanChange; 
	
	@FXML
	public void save(){
		getAllTexts();
		try{
			Backend.getNpcDao().createIfNotExists(npc);
			Backend.getNpcDao().update(npc);
			Backend.getNpcDao().refresh(npc);
			log.debug("Save successfull!");
			log.debug("npc id: " + npc.getID());
		}catch(SQLException e){
			log.error(e);
		}
	}
	
	public void start(Tab rootLayout, Npc npc) throws Exception {
		this.npc = npc;
		
		if(App.worldFileUtil.findMultimedia(npc.getImageFileName()) != null)
		{
			image = new AddableImage(App.worldFileUtil.findMultimedia(npc.getImageFileName()));
		}
		else
		{
			image = new AddableImage();
		}
		image.setOnImageChanged(new EventHandler<Event>(){
			public void handle(Event event) {
				save();
			}
		});
		image.setVisible(true);
		rhsVbox.getChildren().add(0, image);
		
		
		thingsThatCanChange = new TextInputControl[] {history , description, lName ,fName ,gender ,attributes ,race ,classType};
		fName.textProperty().addListener(nameListener);
		log.debug("start npc tab controller called");
		setAllTexts(npc);
		
		for(TextInputControl c: thingsThatCanChange){
			c.setOnKeyTyped(saveEvent);
		}
		
		started = true;
		
	}

	private void setAllTexts(Npc npc){
		if(npc.getStatblock() != null)
		{
			try {
				npc.setStatblock(Backend.getStatblockDao().queryForSameId(npc.getStatblock()));
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				log.error(e);
			}
		}
		tabHead.setText(npc.getfName());
		history.setText(npc.getHistory());
		description.setText(npc.getDecription());
		attributes.setText(npc.getAttributes());
		classType.setText(npc.getClassType());
		race.setText(npc.getRace());
		gender.setText(npc.getGender());
		fName.setText(npc.getfName());
		lName.setText(npc.getlName());
	}
	
	private void getAllTexts()
	{
		npc.setHistory(history.getText());
		npc.setDecription(description.getText());
		npc.setGender(gender.getText());
		npc.setClassType(classType.getText());
		npc.setRace(race.getText());
		npc.setAttributes(attributes.getText());
		npc.setfName(fName.getText());
		npc.setlName(lName.getText());
		npc.setImageFileName(image.getFilename());
	}
	
	private static boolean started = false;

	public static boolean getStarted() {
		return started;
	}


	public static NpcTabController startNpcTab(Npc npc) throws Exception {
		log.debug("static startNpcTab called.");
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(NpcTabController.class.getResource("npcTab.fxml"));
		Tab rootLayout = (Tab)loader.load();
		NpcTabController cr = (NpcTabController)loader.getController();
		cr.start(rootLayout, npc);
		started = true;
		return cr;
	}
	
	@FXML
	public void showStatBlock() throws Exception{
		if(npc.getID() != -1){
			log.debug("statblock is being brought up.");
			if (npc.getStatblock() == null)
			{
				log.debug("statblock is null.");
				npc.setStatblock(new Statblock());
				npc.getStatblock().setDescription("Lmao descriptions eyyy lmao. ");
			}	
		App.getStatBlockController().show(npc.getStatblock(), this);
		}
	}
	
	public Tab getTab(){
		return tabHead;
	}

	public Object getThing() {
		return npc;
	}
	
	
}
