package com.forj.fwm.gui;

import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Enumeration;
import java.util.Scanner;
import java.util.regex.Pattern;

import org.apache.commons.configuration2.ex.ConfigurationException;
import org.apache.log4j.Logger;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerCollection;
import org.eclipse.jetty.webapp.WebAppContext;

import com.forj.fwm.backend.Backend;
import com.forj.fwm.backend.ShowPlayersDataModel;
import com.forj.fwm.conf.AppConfig;
import com.forj.fwm.conf.WorldConfig;
import com.forj.fwm.entity.Npc;
import com.forj.fwm.startup.App;
import com.forj.fwm.startup.ComponentSelectorController;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Labeled;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.awt.Desktop;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URISyntaxException;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;

/**
 * http://docs.oracle.com/javafx/2/fxml_get_started/jfxpub-fxml_get_started.htm
 * 
 * If you're reading this Ryan, then you're in the right place.
 * 
 * jettyUi in /src/main/ui can be scene-built.
 * 
 */

public class JettyController { // NEEDS to be a space after class name or scene builder can't find it.

	private static Logger log = Logger.getLogger(JettyController.class);
	private static Server server;
	
	public static final String FILENAME = "password";
	private Boolean passwordVisibility = false;
	private static String[] loggedArray = new String[100]; // will there ever be more than 10 co-DMs?
	private static int numLogged = 0;
	private static String loadedPassword;
	
	@FXML
	private Label serverStatus;
	
	@FXML
    private TextField addrVar;
	
	@FXML
    private Button copyButton, openButton, applyButton, toggleServerButton;
	
	@FXML 
	private RadioButton showPasswordButton, radio10, radio15, radio20;
	
	@FXML
    private PasswordField passwordVar;
	
	@FXML
    private TextField visPasswordVar;

	// Method called on wrong thread for servers to be started
	// started on GUI thread.
    public void start(Stage primaryStage, Pane rootLayout) throws Exception {
       primaryStage.setTitle("WebService Controller");
       Scene myScene = new Scene(rootLayout);
       primaryStage.setScene(myScene);
       primaryStage.show();
       started = true;
       showAddress();
       showPassword();
       radio10.setSelected(WorldConfig.getRad10());
       radio15.setSelected(WorldConfig.getRad15());
       radio20.setSelected(WorldConfig.getRad20());
       
       primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
           public void handle(WindowEvent we) {
               System.out.println("Stage is closing");
               try {
            	   started = false;
            	   server.stop();
           	   } catch (Exception e1) {
           		   e1.printStackTrace();
           	   }
           }
       }); 
       
       visPasswordVar.toBack();
    }
	
	private static boolean started = false;
	public static boolean getStarted()
	{
		return started;
	}
	
	
	private static WebAppContext ctx;
	private static int port;
	
	public static JettyController startJettyWindow() throws Exception{
		startServer();
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(JettyController.class.getResource("jettyUi.fxml"));
		VBox rootLayout = (VBox)loader.load();
		JettyController cr = (JettyController)loader.getController();
		cr.start(new Stage(), rootLayout);
		started = true;
		Npc n = new Npc();
		n.setfName("Jack");
		n.setlName("Strickland");
		n.setDecription("This will be the decription.");
		//n.setImageFileName("/i/fat_halfling.jpg");
		ShowPlayersDataModel.addOne(n);
		return cr;
	}
	
	private static void startServer() throws Exception {
		port = AppConfig.config.getInt(AppConfig.PORT);
		
		ctx = new WebAppContext();
		ctx.setContextPath("/");
		// JettyController.class.getResource("/src/main/webapp/WEB-INF").toString()
		ctx.setWar(App.retGlobalResource("/src/main/webapp").toString());
		HandlerCollection hc = new HandlerCollection();
		hc.setHandlers(new Handler[] {ctx});
		
		server = new Server(port);
		server.setHandler(hc);
		server.setStopAtShutdown(true);
		server.start();
		//stage.setOnCloseRequest(we -> onClosed());
	}
	

	public static String getAddress() throws Exception {
	    InetAddress addr = InetAddress.getLocalHost();
	    return addr.getHostAddress() + ":" + port;
	    
	    // Ugly as sin method to determine Linux local IP. Maybe. Still working on it.
	    /*
	    NetworkInterface iface = null;
		String ethr;
		String myip = "";
		String regex = "^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +	"([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." + "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." + "([01]?\\d\\d?|2[0-4]\\d|25[0-5])$";
		try {
			for(Enumeration ifaces = NetworkInterface.getNetworkInterfaces();ifaces.hasMoreElements();) {
				iface = (NetworkInterface)ifaces.nextElement();
				ethr = iface.getDisplayName();

				if ((Pattern.matches("eth[0-9]", ethr)) || (Pattern.matches("wlan[0-9]", ethr))) {
					System.out.println("Interface:" + ethr);
					InetAddress ia = null;
					for(Enumeration ips = iface.getInetAddresses();ips.hasMoreElements();) {
						ia = (InetAddress)ips.nextElement();
						if (Pattern.matches(regex, ia.getCanonicalHostName())) {
							myip = ia.getCanonicalHostName();
							return myip;
						}
					}
				}
				System.out.println("Interface:" + ethr);
			}
		} catch (SocketException e){ System.out.println("Interface:"); }
		return myip;
		*/
	}
	
	
	@FXML
	public void showAddress() {
	    try {
	    	addrVar.setText(getAddress());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@FXML
	public void copyAddress(ActionEvent event) {
        try {
        	ClipboardContent content = new ClipboardContent();
			content.putString(getAddress());
			Clipboard.getSystemClipboard().setContent(content);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@FXML
	public void openServerInBrowser() {
		try {
			Desktop.getDesktop().browse(new URL("http://" + getAddress()).toURI());
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/*
	public void setPassword() {
		BufferedWriter bw = null;
		FileWriter fw = null;
		
		try {
			String content = passwordVar.getText();
			if (passwordVisibility){
				content = visPasswordVar.getText();
			}
			fw = new FileWriter(FILENAME);
			bw = new BufferedWriter(fw);
			bw.write(content);
			loadedPassword = content;
			log.debug("Done");
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (bw != null) bw.close();
				if (fw != null) fw.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		
		showPassword();
	}
	*/
	
	
	public void setPassword() {
		
		String content = passwordVar.getText();
		if (passwordVisibility){
			content = visPasswordVar.getText();
		}
		
		try {
			WorldConfig.savePassword(content);
		} catch (ConfigurationException e) {
			e.printStackTrace();
		}
		
		loadedPassword = content;
		log.debug("Done");
		showPassword();
	}
	
	/*
	@FXML
	public String getPassword() {
		try {
			String newpass = new String(Files.readAllBytes(Paths.get(FILENAME)));
			loadedPassword = newpass;
			return newpass;
		} catch (IOException e) {
			return "NO PASSWORD SET";
		}
	}
	*/
	
	@FXML
	public String getPassword() {
		loadedPassword = WorldConfig.getPassword();
		return loadedPassword;
	}

	
	@FXML
	public void toggleShowPassword() {
		if (showPasswordButton.isSelected()){
			visPasswordVar.toFront();
			visPasswordVar.setText(passwordVar.getText());
			passwordVisibility = true;
		} else {
			visPasswordVar.toBack();
			passwordVar.setText(visPasswordVar.getText());
			passwordVisibility = false;
		}
	}
	
	@FXML
	public void toggleRadios() throws ConfigurationException{
		WorldConfig.saveRadios(radio10.isSelected(), radio15.isSelected(), radio20.isSelected());	
		//radio1.0 show content
		//radio1.5 allow players to view/search gods/npcs/regions/events and search
		//radio2.0 allow co-dm to edit
	}
	
	@FXML
	public void showPassword() {
		String password = getPassword();
		passwordVar.setText(password);
		visPasswordVar.setText(password);
	}
	
	
	public static Boolean getIsLoggedIn(String ip) {
		for (int i = 0; i < numLogged; i++){
			if (loggedArray[i].equals(ip)) return true;
		}
		return false;
	}
	
	
	public static void logIn(String password, String ip){
		if (numLogged >= 100){
			System.out.println(ip + " couldn't log in. Too many people already logged in");
		}
		if (password.equals(loadedPassword)){
			loggedArray[numLogged] = ip;
			numLogged += 1;
			System.out.println(ip + " successfully logged in");
		} else {
			System.out.println(ip + " entered invalid password");
		}
	}
	
	@FXML
	public void toggleServer(){
		if (started){
			try {
				server.stop();
				toggleServerButton.setText("Start Server");
				serverStatus.setText("Server is stopped");
				serverStatus.setTextFill(Color.web("#ff0000"));
				started = false;
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			try {
				startServer();
				toggleServerButton.setText("Stop Server");
				serverStatus.setText("Server is running");
				serverStatus.setTextFill(Color.web("#00cc00"));
				started = true;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
	}

	
}