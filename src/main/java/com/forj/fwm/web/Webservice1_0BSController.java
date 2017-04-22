package com.forj.fwm.web;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.*;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.*;

import com.forj.fwm.backend.ShowPlayersDataModel;
import com.forj.fwm.entity.*;
import com.forj.fwm.startup.App;
import com.google.gson.Gson;


@Controller
public class Webservice1_0BSController{
	
	private static Logger log = Logger.getLogger(Webservice1_0Controller.class);
	private static ShowPlayersDataModel.ShowData showdata;
	
	private enum gameObject {
		Npc, Event, God, Region;
	}
	
	
	/**
	 * This is the page for webservice 1.0.
	 * It will take the most recent item from ShowPlayersDataModel
	 * and display it on Webservice1_0.jsp
	 * @param modelMap
	 * @return
	 */
	@RequestMapping("/webservice1_0bs")
    public ModelAndView getWebservicePage(ModelMap modelMap, HttpServletRequest request) {
		
		ShowPlayersDataModel.ShowData sd = App.spdc.getDefault();
		
		if (sd == null) {
			Npc nullguy = new Npc();
			nullguy.setfName("NO");
			nullguy.setlName("OBJECTS");
			nullguy.setDescription("The DM has not shown any objects");
			App.spdc.addOne(nullguy);
			sd = App.spdc.getDefault();
			Object obj = sd.getObject();
			int newIndex = sd.getNewIndex();
			modelMap = showThis(modelMap, newIndex, obj);
		} else {
			Object obj = sd.getObject();
			int newIndex = sd.getNewIndex();
			modelMap = showThis(modelMap, newIndex, obj);
		}
		
        return new ModelAndView("webservice1_0bs");
    }
	
	/**
	 * It will take the next object from ShowPlayersDataModel
	 * and display it on Webservice1_0.jsp
	 * @param modelMap
	 * @return
	 */
	@RequestMapping("/webservice1_0bs/getNext/{index}")
    public ModelAndView getNextObject(ModelMap modelMap, @PathVariable("index") int index, HttpServletRequest request) {
		
		ShowPlayersDataModel.ShowData sd = App.spdc.getNext(index);
		Object obj = sd.getObject();
		int newIndex = sd.getNewIndex();
		log.debug("getNext newIndex: " + newIndex);
		modelMap = showThis(modelMap, newIndex, obj);
        return new ModelAndView("webservice1_0bs");
    }
	
	/**
	 * It will take the previous object from ShowPlayersDataModel
	 * and display it on Webservice1_0.jsp
	 * @param modelMap
	 * @return
	 */
	@RequestMapping("/webservice1_0bs/getPrev/{index}")
    public ModelAndView getPrevObject(ModelMap modelMap, @PathVariable("index") int index, HttpServletRequest request) {
		
		ShowPlayersDataModel.ShowData sd = App.spdc.getPrevious(index);
		Object obj = sd.getObject();
		int newIndex = sd.getNewIndex();
		log.debug("getPrev newIndex: " + newIndex);
		modelMap = showThis(modelMap, newIndex, obj);
        return new ModelAndView("webservice1_0bs");
    }
	
	@RequestMapping(value="/webservice1_0bs/multimediaImage/{imageFile}.{ender}", method = RequestMethod.GET)
	@ResponseBody
	public void getImage(@PathVariable("imageFile") String imageFile, @PathVariable("ender") String end, HttpServletResponse response) {
		try {
			ByteArrayOutputStream jpegOutputStream = new ByteArrayOutputStream();
		 	File img = App.worldFileUtil.findMultimedia(imageFile + "." + end);
			log.debug(img.getAbsolutePath());
			log.debug(img.exists());
			BufferedImage image = ImageIO.read(img);
			ImageIO.write(image, end, jpegOutputStream);
		   
			byte[] imgByte = jpegOutputStream.toByteArray();
	
			response.setHeader("Cache-Control", "no-store");
			response.setHeader("Pragma", "no-cache");
		    response.setDateHeader("Expires", 0);
		    response.setContentType("image/jpeg");
		    ServletOutputStream responseOutputStream = response.getOutputStream();
		    responseOutputStream.write(imgByte);
		    responseOutputStream.flush();
		    responseOutputStream.close();		    
		} catch (IOException e1) {
			log.error(e1);		
			try {
				response.sendError(HttpServletResponse.SC_NOT_FOUND);
			} catch (Exception e) {
				log.error(e);
			}
		}
	}
	
	@RequestMapping(value="/webservice1_0bs/multimediaSound/{soundFile}.{ender}", method = RequestMethod.GET)
	@ResponseBody
	public void getSound(@PathVariable("soundFile") String soundFile, @PathVariable("ender") String end, HttpServletResponse response) {
		File audio = App.worldFileUtil.findMultimedia(soundFile + "." + end);
		log.debug(audio.getAbsolutePath());
		log.debug(audio.exists());
		
		FileInputStream fis;
        byte[] buffer=null;
        try {
            fis = new FileInputStream(audio);
            buffer= new byte[fis.available()];
            fis.read(buffer);
            fis.close();
        } catch (FileNotFoundException e) {
            log.error(e);
        } catch (IOException e) {
            log.error(e);
        }        

        response.setContentType("audio/mp3");
	    try {                
	        response.getOutputStream().write(buffer);              
	    } catch (IOException e) {
	        e.printStackTrace();
	    }       
		
	}
	
	
	
	/**
	 * This is returns the id and className of new object getting shown
	 * to the webservice 1.0 jsp view. 
	 * 
	 * @param modelMap
	 * @return some shit
	 */
	@RequestMapping(value="/webservice1_0bs/objectIdCheck/{currentIndex}", method = RequestMethod.GET, produces ="application/json", headers="Accept=application/json")
	public @ResponseBody ResponseEntity<String> checkObjectId(@PathVariable("currentIndex") int currentIndex, HttpServletRequest request, HttpServletResponse response) {
		
		Gson gson = new Gson();
		showdata = App.spdc.getByIndex(currentIndex);
		String json;
		if(showdata != null){
			Object obj = showdata.getObject();
			int newId = getObjectId(obj);
			
			
			String className = obj.getClass().getSimpleName();
			String[] info = {String.valueOf(newId), className}; 
			json = gson.toJson(info);
		}
		else
		{
			json = "[null, null]";
		}
		HttpHeaders headers = new HttpHeaders();
	    headers.set("Content-Type", "application/json");
	
	    return new ResponseEntity<String>(json, headers, HttpStatus.CREATED);
    }
	
	
	
	/**
	 * This will evaluate the object
	 * and determine its type
	 * while adding attributes to the modelMap
	 * 
	 * @param Object
	 * @return
	 */
	private ModelMap showThis(ModelMap modelMap, int index , Object obj) {
		String className = obj.getClass().getSimpleName();
		modelMap.addAttribute("CurrentIndex", index);
		modelMap.addAttribute("ObjectClass", className);
		
		switch (gameObject.valueOf(className)) {
			
			case Npc:
				Npc n = (Npc) obj;
				String nImage = n.getImageFileName();
				
				modelMap.addAttribute("Name", n.getFullName());   	
		    	modelMap.addAttribute("Description", n.getDescription());
		    	modelMap.addAttribute("ImageFileName", nImage);
		    	modelMap.addAttribute("ObjectId", n.getID());
		    	
		    	if (n.getSoundFileName() != null) {
		    		modelMap.addAttribute("SoundFileName", n.getSoundFileName());
		    		log.debug("the sound file name is " + n.getSoundFileName());
		    	}
				break;
				
			case Event:
				Event e = (Event) obj;
				
				modelMap.addAttribute("Name", e.getName());
				modelMap.addAttribute("Description", e.getDescription());
				modelMap.addAttribute("ObjectId", e.getID());
				modelMap.addAttribute("ImageFileName", e.getImageFileName());
				if(e.getSoundFileName() != null){
					modelMap.addAttribute("SoundFileName", e.getSoundFileName());
					log.debug("the sound file name is " + e.getSoundFileName());
				}
				break;
				
			case God:
				God g = (God) obj;
				String gImage = g.getImageFileName();

				modelMap.addAttribute("Name", g.getName());
				modelMap.addAttribute("Description", g.getDescription());
		    	modelMap.addAttribute("ImageFileName", gImage);
		    	modelMap.addAttribute("ObjectId", g.getID());
		    	
		    	if (g.getSoundFileName() != null) {
		    		modelMap.addAttribute("SoundFileName", g.getSoundFileName());
		    		log.debug("the sound file name is " + g.getSoundFileName());
		    	}
				break;
			
			case Region:
				Region r = (Region) obj; 
				String rImage = r.getImageFileName();
				
				modelMap.addAttribute("Name", r.getName());
				modelMap.addAttribute("Description", r.getDescription());
		    	modelMap.addAttribute("ImageFileName", rImage);
		    	modelMap.addAttribute("ObjectId", r.getID());
		    	
		    	if (r.getSoundFileName() != null) {
		    		modelMap.addAttribute("SoundFileName", r.getSoundFileName());
		    		log.debug("the sound file name is " + r.getSoundFileName());
		    	}
				break;
		}
		
		log.debug(modelMap.get("ImageFileName"));
		log.debug(modelMap.get("SoundFileName"));
		log.debug("ShowThis method current index: " + modelMap.get("CurrentIndex"));
		
		
		return modelMap;
	}
	
	private int getObjectId(Object obj) {
		String className = obj.getClass().getSimpleName();
		switch (gameObject.valueOf(className)) {
			case Event:
				Event e = (Event) obj;
				return e.getID();
			case God:
				God g = (God) obj;
				return g.getID();
			case Npc:
				Npc n = (Npc) obj;
				return n.getID();
			case Region:
				Region r = (Region) obj;
				return r.getID();
			default:
				return (-1);
		}
		
	}
	
	
}