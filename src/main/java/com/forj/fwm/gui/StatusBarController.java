package com.forj.fwm.gui;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.ListView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class StatusBarController {
	
	protected static Logger log = Logger.getLogger(ShowPlayersController.class);
	
	private TextArea statusBarFull;
	
	private int stackPaneIndex;
	
	private List<Status> statuses = new ArrayList<Status>();
	
	private Label statusBarSmall;
	
	private StackPane statusStackPane;
	
	private boolean statusFullOn;
	
	private int statusMaxSize;
	
	
	public StatusBarController (StackPane stackPane, VBox statusVBoxmc){
		statusMaxSize = 50;
		statusBarFull = new TextArea();
		statusBarSmall = new Label("Ceiling cat is watching your status, but there is none");
		statusBarSmall.maxWidthProperty().bind(statusVBoxmc.widthProperty());
		statusBarSmall.setId("statusLabel");
		//statusBarSmall.setPrefWidth(statusVBoxmc.getWidth());
		statusStackPane = stackPane;
		statusFullOn = false;
		statusBarSmall.setOnMouseClicked(new EventHandler<MouseEvent>() {

			public void handle(MouseEvent event) {
				try {
					toggleStatusFull();
				} catch (Exception e) {
					log.error(e);
					e.printStackTrace();
				}
			}
		});
		statusBarFull.setOnMouseExited(new EventHandler<MouseEvent>() {

			public void handle(MouseEvent event) {
				try {
					toggleStatusFull();
				} catch (Exception e) {
					log.error(e);
					e.printStackTrace();
				}
			}
		});
	}
	
	private class Status{
		private Date date;
		private String text;
		
		public Status(Date date, String text){
			this.date = date;
			this.text = text;
		}
		
		public String getComposite(){
			DateFormat dateFormat = new SimpleDateFormat("[HH:mm:ss] - ");
			return dateFormat.format(date) + text;
		}
		
		public void setDate(Date date) {
			this.date = date;
		}

		public Date getDate() {
			return date;
		}

		public String getText() {
			return text;
		}

		public void setText(String text) {
			this.text = text;
		}
		
		public boolean equals(Status newStatus)
		{
			return newStatus.getText().equals(this.text);
		}
		
		public boolean equals(String newText)
		{
			return newText.equals(this.text);
		}
	}
	
	
	public int getStatusMaxSize() {
		return statusMaxSize;
	}

	public void setStatusMaxSize(int statusMaxSize) {
		this.statusMaxSize = statusMaxSize;
	}

	public void toggleStatusFull(){
		if (statusFullOn)
		{
			statusStackPane.getChildren().remove(stackPaneIndex);
			statusFullOn = false;
		}
		else
		{
			statusStackPane.getChildren().add(statusBarFull);
			stackPaneIndex = statusStackPane.getChildren().size() - 1;
			statusFullOn = true;
		}
	}
	
	public Label getSmallStatus(){
		return statusBarSmall;
	}
	
	public void addStatus(String text){
		Date date = new Date();
		if (statuses.isEmpty()){
			statuses.add(0, new Status(date, text));
		}
		else if(!statuses.get(0).equals(text))
		{
			statuses.add(0, new Status(date, text));
		}
		else
		{
			statuses.get(0).setDate(date);
		}
		pruneStatus();
		setStatus();
	}
	
	private void pruneStatus(){
		if (statuses.size() > statusMaxSize)
		{
			statuses.remove(statuses.size()-1);
		}
	}
	
	public String getLatestStatus(){
		return statuses.isEmpty() ? "" : statuses.get(0).getComposite();
	}
	
	private void setStatus(){
		setStatusSmall();
		setStatusFull();
	}
	
	private void setStatusSmall(){
		statusBarSmall.setText(getLatestStatus());
	}
	
	private void setStatusFull(){
		String statusText = "";
		for(Status status : statuses) {
		    statusText += status.getComposite() + "\n";
		}
		statusBarFull.setText(statusText);
	}
}
