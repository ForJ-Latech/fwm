package com.forj.fwm.gui.tab;

import com.forj.fwm.entity.Searchable;
import com.forj.fwm.gui.InteractionList.ListController;
import com.forj.fwm.gui.component.AddableImage;
import com.forj.fwm.gui.component.AddableSound;
import com.forj.fwm.startup.App;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Tab;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

public class WelcomeTabController implements Saveable {
	@FXML private VBox midVBox;
    @FXML private Tab tabHead;

	public static WelcomeTabController startWelcomeTab() throws Exception {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(WelcomeTabController.class.getResource("WelcomeTab.fxml"));
		Tab rootLayout = (Tab)loader.load();
		WelcomeTabController cr = (WelcomeTabController)loader.getController();
		cr.start(rootLayout);
		return cr;
		
	}
	
    private void start(Tab rootLayout) {
    	tabHead.setText("Welcome!");
    	ImageView mImage  = new ImageView(App.retGlobalResource("/src/main/ui/WelcomeTabv12.png").toString());
    	mImage.setVisible(true);
    	midVBox.getChildren().add(0, mImage);
    	mImage.fitWidthProperty().bind(midVBox.widthProperty().subtract(4));
    	mImage.fitHeightProperty().bind(midVBox.heightProperty().subtract(4));
	}

	public Tab getTab() {
		return tabHead;
	}

	public void fullSave() {
		
	}

	public Searchable getThing() {
		return null;
	}
    
	public AddableImage getAddableImage() {
		return null;
	}
	
	public ListController getListController(){
		return null;
	}

	public void simpleSave() {
		
	}

	public void relationalSave() {
		
	}
	public void nameFocus(){
		
	}
	public AddableSound getAddableSound(){
		return null;
	}
}
