package com.forj.fwm.gui;

import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.jar.Manifest;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;
import javax.swing.event.HyperlinkEvent.EventType;

import org.apache.commons.configuration2.ex.ConfigurationException;
import org.apache.log4j.Logger;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerCollection;
import org.eclipse.jetty.webapp.WebAppClassLoader;
import org.eclipse.jetty.webapp.WebAppContext;

import com.forj.fwm.backend.Backend;
import com.forj.fwm.backend.ShowPlayersDataModel;
import com.forj.fwm.conf.AppConfig;
import com.forj.fwm.conf.WorldConfig;
import com.forj.fwm.entity.Npc;
import com.forj.fwm.startup.App;
import com.forj.fwm.startup.ComponentSelectorController;
import com.forj.fwm.web.HelloController;

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

import java.awt.AWTException;
import java.awt.Desktop;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.net.InetAddress;
import java.net.JarURLConnection;
import java.net.MalformedURLException;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URISyntaxException;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;

/**
 * http://docs.oracle.com/javafx/2/fxml_get_started/jfxpub-fxml_get_started.htm
 * 
 * If you're reading this Ryan, then you're in the right place.
 * 
 * jettyUi in /src/main/ui can be scene-built.
 * 
 */

public class JettyController{ // NEEDS to be a space after class name or scene builder can't find it.

	private static Logger log = Logger.getLogger(JettyController.class);
	private static Server server;
	
	public static final String FILENAME = "password";
	private Boolean passwordVisibility = false;
	private static String[] loggedArray = new String[100]; // will there ever be more than 10 co-DMs?
	private static int numLogged = 0;
	private static String loadedPassword;
	
	private boolean firstTime;
    private TrayIcon trayIcon;
	
    private Stage ourStage;
    
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

	private EventHandler<WindowEvent> closeEventHandler = new EventHandler<WindowEvent>(){
		public void handle(WindowEvent e){
			closeWindow(e);
		}
	};
	
	private void closeWindow(WindowEvent e){
		log.debug("Jetty Controller is closing");
        try {
        	Platform.setImplicitExit(true);
     	   started = false;
     	   server.stop();
     	   server.join();
     	   ourStage.close();
	   } catch (Exception e1) {
		   e1.printStackTrace();
	   }
	}
	
	// Method called on wrong thread for servers to be started
	// started on GUI thread.
    public void start(Stage primaryStage, Pane rootLayout) throws Exception {
    	ourStage = primaryStage;
    	createTrayIcon(primaryStage);
        firstTime = true;
        Platform.setImplicitExit(false);
        primaryStage.setTitle("WebService Controller");
        primaryStage.getIcons().add(new javafx.scene.image.Image(App.retGlobalResource("/src/main/webapp/WEB-INF/images/icons/server/64.png").openStream()));
        Scene myScene = new Scene(rootLayout);
        myScene.getStylesheets().add(App.retGlobalResource("/src/main/java/com/forj/fwm/gui/jettyWindowStylesheet.css").toString());
        primaryStage.setScene(myScene);
        primaryStage.show();
        started = true;
        showAddress();
        showPassword();
        radio10.setSelected(WorldConfig.getRad10());
        radio15.setSelected(WorldConfig.getRad15());
        radio20.setSelected(WorldConfig.getRad20());
        
        
       
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
		return cr;
	}
	
	private static void startServer() throws Exception {
		try{
			port = AppConfig.config.getInt(AppConfig.PORT);
			
			ctx = new WebAppContext();
			ctx.setContextPath("/");
			
			ctx.setWar(App.retGlobalResource("/src/main/webapp").toString());			
			
			if(App.getProd()){
				ctx.preConfigure();
				ClassLoader contextClassLoader = ctx.getClassLoader();
				ClassLoader parentLoader = ctx.getClassLoader().getParent();
				//a1kmm is a GOD.
				//a1kmm is literally a god, holy fucking shit it works. 
				if (contextClassLoader instanceof WebAppClassLoader &&
				        parentLoader instanceof URLClassLoader) {
				      LinkedList<URL> allURLs =
				          new LinkedList<URL>(Arrays.asList(((URLClassLoader)parentLoader).getURLs()));
				      for (URL url : ((LinkedList<URL>)allURLs.clone())) {
				        try {
				          URLConnection conn = new URL("jar:" + url.toString() + "!/").openConnection();
				          if (!(conn instanceof JarURLConnection))
				            continue;
				          JarURLConnection jconn = (JarURLConnection)conn;
				          Manifest jarManifest = jconn.getManifest();
				          String[] classPath = ((String)jarManifest.getMainAttributes().getValue("Class-Path")).split(" ");

				          for (String cpurl : classPath)
				            allURLs.add(new URL(url, cpurl));
				        } catch (IOException e) {} catch (NullPointerException e) {}
				      }

				      ctx.setClassLoader(
				          new WebAppClassLoader(
				              new URLClassLoader(allURLs.toArray(new URL[]{}), parentLoader),
				              ((WebAppClassLoader)contextClassLoader).getContext()));
				    }
				}
			
			HandlerCollection hc = new HandlerCollection();
			
			hc.setHandlers(new Handler[] {ctx});
			server = new Server(port);
			server.addBean(App.spdc);
			server.setHandler(hc);
			server.setStopAtShutdown(true);
			server.start();
		}catch(Exception e){
			e.printStackTrace();
		}
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
				serverStatus.setTextFill(Color.web("#0e007c"));
				started = true;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
	}
	
	private EventHandler<WindowEvent> hideHandler = new EventHandler<WindowEvent>() {
        public void handle(WindowEvent t) {
            hide(ourStage);
        }
    };
	
	public void createTrayIcon(final Stage stage) {
		 if(App.getProd() && !System.getProperty("os.name").contains("Mac")){
	        if (SystemTray.isSupported()) {
	            final SystemTray tray = SystemTray.getSystemTray();
	            Image image = null;
	            try {
	            	image = ImageIO.read(App.retGlobalResource("/src/main/webapp/WEB-INF/images/icons/server/64.png"));
	            } catch (IOException ex) {
	                log.debug(ex);
	            }
	
	
	            stage.setOnCloseRequest(hideHandler);
	            
	            final ActionListener closeListener = new ActionListener() {
	                public void actionPerformed(java.awt.event.ActionEvent e) {
	                	Platform.runLater(new Runnable(){
	                		public void run(){
	                			log.debug("requesting that the stage close...");
	                			stage.removeEventHandler(WindowEvent.ANY, hideHandler);
	                			stage.setOnCloseRequest(closeEventHandler);
	                			stage.fireEvent(new WindowEvent(stage,WindowEvent.WINDOW_CLOSE_REQUEST));
	                			tray.remove(trayIcon);
	                		}
	                	});
	                }
	            };
	
	            ActionListener showListener = new ActionListener() {
	                public void actionPerformed(java.awt.event.ActionEvent e) {
	                    Platform.runLater(new Runnable() {
	                        public void run() {
	                            stage.show();
	                        }
	                    });
	                }
	            };
	            PopupMenu popup = new PopupMenu();
	
	            MenuItem showItem = new MenuItem("Open Server Window");
	            showItem.addActionListener(showListener);
	            popup.add(showItem);
	
	            MenuItem closeItem = new MenuItem("Exit");
	            closeItem.addActionListener(closeListener);
	            popup.add(closeItem);
	
	            trayIcon = new TrayIcon(image, "Fantasy World Manager", popup);
	            trayIcon.addActionListener(showListener);
	            trayIcon.setImageAutoSize(true);
	
	            try {
	                tray.add(trayIcon);
	            } catch (AWTException e) {
	            	e.printStackTrace();
	            }
	        } else{
	        	stage.setOnCloseRequest(closeEventHandler); 
	        }
		}
		else{
			stage.setOnCloseRequest(closeEventHandler); 
		}
    }

	
	
    public void showMinimizedMessage() {
        if (firstTime) {
            trayIcon.displayMessage("Fanstasy World Manager",
                    "Server controls are minimized to system tray",
                    TrayIcon.MessageType.INFO);
            firstTime = false;
        }
    }

    private void hide(final Stage stage) {
        Platform.runLater(new Runnable() {
            public void run() {
                if (SystemTray.isSupported()) {
                    stage.hide();
                    showMinimizedMessage();
                }
            }
        });
    }


	
}