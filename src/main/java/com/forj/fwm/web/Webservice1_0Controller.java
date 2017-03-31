package com.forj.fwm.web;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.eclipse.jetty.util.log.Log;
import org.springframework.http.MediaType;
import org.springframework.stereotype.*;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.*;

import com.forj.fwm.backend.ShowPlayersDataModel;
import com.forj.fwm.entity.*;
import com.forj.fwm.gui.JettyController;
import com.forj.fwm.startup.App;
import com.forj.fwm.startup.WorldFileUtil;


@Controller
public class Webservice1_0Controller{
	
	private static Logger log = Logger.getLogger(Webservice1_0Controller.class);

	
	private enum gameObject {
		Npc, Event, God, Region;
	}
	
	private ModelMap showThis(ModelMap modelMap, int index , Object obj) {
		modelMap.addAttribute("currentIndex", index);
		String className = obj.getClass().getSimpleName();
		//App.worldFileUtil.findMultimedia()
		
		switch (gameObject.valueOf(className)) {
			
			case Npc:
				Npc n = (Npc) obj;
				String nImage = n.getImageFileName();
				//App.worldFileUtil.findMultimedia()
				
				modelMap.addAttribute("Name", n.getfName()+ " " + n.getlName());   	
		    	modelMap.addAttribute("Description", n.getDecription());
		    	modelMap.addAttribute("ImageFileName", nImage);
				break;
				
			case Event:
				Event e = (Event) obj;
				
				modelMap.addAttribute("Name", e.getName());
				modelMap.addAttribute("Description", e.getDescription());
				break;
				
			case God:
				God g = (God) obj;
				String gImage = g.getImageFileName();

				modelMap.addAttribute("Name", g.getName());
				modelMap.addAttribute("Description", g.getDescription());
		    	modelMap.addAttribute("ImageFileName", gImage);

				break;
			
			case Region:
				Region r = (Region) obj; 
				String rImage = r.getImageFileName();
				
				modelMap.addAttribute("Name", r.getName());
				modelMap.addAttribute("Description", r.getDescription());
		    	modelMap.addAttribute("ImageFileName", rImage);

				break;
		}
		
		log.debug(modelMap.get("ImageFileName"));
		return modelMap;
	}
	
	/**
	 * This is the page for webservice 1.0
	 * It will take the most recent item from ShowPlayersDataModel
	 * and display it on Webservice1_0.jsp
	 * @param modelMap
	 * @return
	 */
	@RequestMapping("/webservice1_0")
    public ModelAndView getWebservicePage(ModelMap modelMap, HttpServletRequest request) {
		
		ShowPlayersDataModel.ShowData sd = ShowPlayersDataModel.getDefault();
		Object obj = sd.getObject();
		int newIndex = sd.getNewIndex();
		modelMap = showThis(modelMap, newIndex, obj);
        return new ModelAndView("webservice1_0");
    }
	
	/**
	 * It will take the next object from ShowPlayersDataModel
	 * and display it on Webservice1_0.jsp
	 * @param modelMap
	 * @return
	 */
	@RequestMapping("/webservice1_0/getNext/{index}")
    public ModelAndView getNextObject(ModelMap modelMap, @PathVariable("index") int index, HttpServletRequest request) {
		
		ShowPlayersDataModel.ShowData sd = ShowPlayersDataModel.getNext(index);
		Object obj = sd.getObject();
		int newIndex = sd.getNewIndex();
		modelMap = showThis(modelMap, newIndex, obj);
        return new ModelAndView("webservice1_0");
    }
	
	/**
	 * It will take the previous object from ShowPlayersDataModel
	 * and display it on Webservice1_0.jsp
	 * @param modelMap
	 * @return
	 */
	@RequestMapping("/webservice1_0/getPrev/{index}")
    public ModelAndView getPrevObject(ModelMap modelMap, @PathVariable("index") int index, HttpServletRequest request) {
		
		ShowPlayersDataModel.ShowData sd = ShowPlayersDataModel.getPrevious(index);
		Object obj = sd.getObject();
		int newIndex = sd.getNewIndex();
		modelMap = showThis(modelMap, newIndex, obj);
        return new ModelAndView("webservice1_0");
    }
	
	@RequestMapping(value="/multimedia/{imageFile}.{ender}", method = RequestMethod.GET)
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
	
}