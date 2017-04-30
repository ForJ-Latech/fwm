package com.forj.fwm.gui.component;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

import org.apache.log4j.Logger;

import com.forj.fwm.startup.App;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

public class AddableImage extends ImageView{
	private static Logger log = Logger.getLogger(AddableImage.class);
	private EventHandler<Event> onChangeHandler = null;
	private File ourImage;
	
	private static ExtensionFilter[] filts = new ExtensionFilter[]{
		new ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.bmp", "*.gif")	
	};
	
	private void setImageTwo(File f){
		ourImage = f;
		log.debug("setImageTwo called");
		try{
			Image newImage = new Image(new FileInputStream(f));
			if (!newImage.isError()){
				this.setImage(newImage);
			} else {
				log.debug("ERROR LOADING IMAGE");
			}
		}catch(Exception e){
			log.error(e);
		}
	}
	
	public void setOnImageChanged(EventHandler<Event> ev){
		onChangeHandler = ev;
	}
	
	private void changeEvent(){
		if(onChangeHandler == null){
			return;
		}
		else
		{
			onChangeHandler.handle(null);
		}
	}
	
	private void makeAddable(){
		this.setPreserveRatio(true);
		this.setFitWidth(100);
		this.setFitHeight(100);
		this.setOnMouseClicked(new EventHandler<MouseEvent>(){
			public void handle(MouseEvent event) {
				log.debug("Mouse clicked boss");
				addImage();
			}
		});
		
		
		this.setOnDragOver(new EventHandler<DragEvent>() {
            public void handle(DragEvent event) {
                Dragboard db = event.getDragboard();
                if (db.hasFiles() || db.hasHtml()) {
                    event.acceptTransferModes(TransferMode.COPY);
                } else {
                    event.consume();
                }
            }
        });
        
        // Dropping over surface
		this.setOnDragDropped(new EventHandler<DragEvent>() {
            public void handle(DragEvent event) {
                Dragboard db = event.getDragboard();
                boolean success = false;
                if (db.hasFiles()) { //try hasstring
                    success = true;
                    String filePath = null;
                    for (File file:db.getFiles()) {
                        filePath = file.getAbsolutePath();
                        System.out.println(filePath);
                        
                        String extension = "";
                        int i = filePath.lastIndexOf('.');
                        if (i >= 0) {
                            extension = filePath.substring(i+1).toLowerCase();
                        }
                        
                        if (extension.equals("png") || extension.equals("jpg") || extension.equals("jpeg") || extension.equals("bmp") || extension.equals("gif")) {
	                        File addedFile = App.worldFileUtil.addMultimedia(new File(filePath));
	                        setImageTwo(addedFile);
	            			changeEvent();
                        } else {
                        	log.debug("not an image file");
                        }
                    }
                }
                
                // snag from internet. Give it Extension if doesn't have one.
                if (db.hasHtml()) {
                	log.debug("hashtml");
                	log.debug(db.getHtml().toString());
                	
                	Pattern pattern = Pattern.compile("(?<=src=\").+?(?=\")");
                	Matcher matcher = pattern.matcher(db.getHtml().toString());
                	if (matcher.find())
                	{
                		log.debug(matcher.group());
                		
                		try {
                			URL website = new URL(matcher.group());
                    		ReadableByteChannel rbc = Channels.newChannel(website.openStream());
                    		File file1 = new File("imageimage");
                    		
                    		FileOutputStream fos = new FileOutputStream(file1);
							fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
							fos.close();
							
							File addedFile = new File("imageimage");

							addedFile = addImageExtension(addedFile);
							File addedFile2 = App.worldFileUtil.addMultimedia(addedFile);
							addedFile.delete();
							
							setImageTwo(addedFile2);
	            			changeEvent();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
                		
                	}
                }
                
                // ([a-z\-_0-9\/\:\.]*(\.(jpg|jpeg|png|gif|bmp)|(?=\?)))|(?<=src=").+?(?=")
                
                event.setDropCompleted(success);
                event.consume();
            }
        });
		
		
	}
	
	// Give files an extension (They might not have one :(  )
	public File addImageExtension(File incoming) throws IOException {
		
		String format = null;
		ImageInputStream iis = ImageIO.createImageInputStream(incoming);
		Iterator<ImageReader> imageReaders = ImageIO.getImageReaders(iis);
		while (imageReaders.hasNext()) {
		    ImageReader reader = (ImageReader) imageReaders.next();
		    format = reader.getFormatName().toLowerCase();
		    log.debug("filetype is: " + format);

		    File newfile = new File("imageimage." + format);
			if (newfile.exists()) {
				newfile.delete();    	
			}
			
		    Files.copy(incoming.toPath(), newfile.toPath());
		    incoming.delete();

		    return newfile;
		}
		return null;
		
	}
	
	
	public void addImage(){
		FileChooser fc = new FileChooser();
		fc.setTitle("Select an image for this object!");
		fc.getExtensionFilters().addAll(filts);
		// figure out who the owner is here... 
		File selectedFile = fc.showOpenDialog(App.getMainController().getStage());
		File addedFile = null;
		if(selectedFile == null){
			// they couldn't decide what they want. 
			return; 
		}
		else
		{
			addedFile = App.worldFileUtil.addMultimedia(selectedFile);
		}
		if(addedFile == null){
			log.error("Added file was null!");
		}
		else
		{
			setImageTwo(addedFile);
			changeEvent();
		}
	}
	
	public AddableImage(Image image) throws Exception{
		super(image);
		makeAddable();
		throw new Exception("DO NOT CALL THIS ONE");
	}
	
	public AddableImage(String url){
		super(new Image(url, true));
		ourImage = new File(url);
		makeAddable();
	}
	
	public AddableImage(File image){
		super(new Image("file:" + image.getAbsolutePath(), true));
		log.debug("file:" + image.getAbsolutePath());
		ourImage = image;
		makeAddable();
	}
	
	public AddableImage(){
		super(new Image(App.retGlobalResource("/src/main/ui/no_image.png").toString(), true));
		makeAddable();		
	}
	
	public String getFilename(){
		if(ourImage == null){
			return null;
		}
		else
		{
			return ourImage.getName();
		}
	}
}
