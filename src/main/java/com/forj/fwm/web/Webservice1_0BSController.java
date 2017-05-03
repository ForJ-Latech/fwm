package com.forj.fwm.web;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

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
import com.forj.fwm.conf.WorldConfig;
import com.forj.fwm.entity.*;
import com.forj.fwm.startup.App;
import com.google.gson.Gson;

@Controller
public class Webservice1_0BSController {

	private static Logger log = Logger.getLogger(Webservice1_0BSController.class);
	private static ShowPlayersDataModel.ShowData showdata;

	private enum gameObject {
		Npc, Event, God, Region;
	}
	
	private static ModelAndView validateWS1_0(String dest) {
		log.debug(WorldConfig.getRad10());
		if (WorldConfig.getRad10() && dest != ""){
			return new ModelAndView(dest);
		} else {
			return new ModelAndView("/views/error.html");
		}
	}

	/**
	 * This is the page for webservice 1.0. It will take the most recent item
	 * from ShowPlayersDataModel and display it on Webservice1_0.jsp
	 * 
	 * @param modelMap
	 * @return
	 */
	@RequestMapping("/webservice1_0bs/main/{shit}")
	public ModelAndView getWebservicePage(ModelMap modelMap, HttpServletRequest request, @PathVariable("shit") String shit) {
//		return new ModelAndView("views/webservice1_0bs.html");
		log.debug("getting webservice1_0bs");
		return validateWS1_0("/views/webservice1_0bs.html");
	}
	
	@RequestMapping("/webservice1_0bs/getCurrent")
	public ResponseEntity<String> getCurrentObject(HttpServletRequest request) {
		String json = showThis(App.spdc.getDefault());
		HttpHeaders headers = new HttpHeaders();
		headers.set("Content-Type", "application/json");

		return new ResponseEntity<String>(json, headers, HttpStatus.CREATED);
	}
		
	
	/**
	 * It will take the next object from ShowPlayersDataModel and display it on
	 * Webservice1_0.jsp
	 * 
	 * @param modelMap
	 * @return
	 */
	@RequestMapping("/webservice1_0bs/getNext/{index}")
	public ResponseEntity<String> getNextObject(@PathVariable("index") int index, HttpServletRequest request) {
		
		if (WorldConfig.getRad10()) {
			String json = showThis(App.spdc.getNext(index));
			HttpHeaders headers = new HttpHeaders();
			headers.set("Content-Type", "application/json");
			return new ResponseEntity<String>(json, headers, HttpStatus.CREATED);
		} else {
			//creates a dummy when functionality is disabled. Could make it a global json but need to push quick
			JsonHelper js = new JsonHelper();
			js.addAttribute("Name", "No Web Service Functionality");
			js.addAttribute("Description", "The DM has disabled this web service functionality.");
			js.addAttribute("ImageFileName", null);
			js.addAttribute("ObjectId", -1);
			js.addAttribute("ObjectClass", null);
			js.addAttribute("CurrentIndex", null);
			String json = js.getString();
			HttpHeaders headers = new HttpHeaders();
			headers.set("Content-Type", "application/json");
			return new ResponseEntity<String>(json, headers, HttpStatus.CREATED);
		}
	}

	/**
	 * It will take the previous object from ShowPlayersDataModel and display it
	 * on Webservice1_0.jsp
	 * 
	 * @param modelMap
	 * @return
	 */
	@RequestMapping("/webservice1_0bs/getPrev/{index}")
	public ResponseEntity<String> getPrevObject(@PathVariable("index") int index, HttpServletRequest request) {

		if (WorldConfig.getRad10()) {
			String json = showThis(App.spdc.getPrevious(index));
			HttpHeaders headers = new HttpHeaders();
			headers.set("Content-Type", "application/json");
			return new ResponseEntity<String>(json, headers, HttpStatus.CREATED);
		} else {
			//creates a dummy when functionality is disabled. Could make it a global json but need to push quick
			JsonHelper js = new JsonHelper();
			js.addAttribute("Name", "No Web Service Functionality");
			js.addAttribute("Description", "The DM has disabled this web service functionality.");
			js.addAttribute("ImageFileName", null);
			js.addAttribute("ObjectId", -1);
			js.addAttribute("ObjectClass", null);
			js.addAttribute("CurrentIndex", null);
			String json = js.getString();
			HttpHeaders headers = new HttpHeaders();
			headers.set("Content-Type", "application/json");
			return new ResponseEntity<String>(json, headers, HttpStatus.CREATED);

		}

	}

	@RequestMapping(value = "/webservice1_0bs/multimediaImage/null", method = RequestMethod.GET)
	@ResponseBody
	public void getNullImage(HttpServletResponse response) {
		try {
			byte[] b = new byte[0];
			InputStream i = App.retGlobalResource("/src/main/ui/no_image_icon.png").openStream();
			response.setHeader("Cache-Control", "no-store");
			response.setHeader("Pragma", "no-cache");
			response.setDateHeader("Expires", 0);
			response.setContentType("image/" + "png");
			ServletOutputStream responseOutputStream = response.getOutputStream();
			byte[] bytes = new byte[1024];
			int read = 0;
			while ((read = i.read(bytes)) != -1) {
				responseOutputStream.write(bytes, 0, read);
			}

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

	@RequestMapping(value = "/webservice1_0bs/multimediaImage/{imageFile}.{ender}", method = RequestMethod.GET)
	@ResponseBody
	public void getImage(@PathVariable("imageFile") String imageFile, @PathVariable("ender") String end,
			HttpServletResponse response) {

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
			response.setContentType("image/" + end);
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

	@RequestMapping(value = "/webservice1_0bs/multimediaSound/{soundFile}.{ender}", method = RequestMethod.GET)
	@ResponseBody
	public void getSound(@PathVariable("soundFile") String soundFile, @PathVariable("ender") String end,
			HttpServletResponse response) {
		File audio = App.worldFileUtil.findMultimedia(soundFile + "." + end);
		log.debug(audio.getAbsolutePath());
		log.debug(audio.exists());

		FileInputStream fis;
		byte[] buffer = null;
		try {
			fis = new FileInputStream(audio);
			buffer = new byte[fis.available()];
			fis.read(buffer);
			fis.close();
		} catch (FileNotFoundException e) {
			log.error(e);
		} catch (IOException e) {
			log.error(e);
		}

		response.setContentType("audio" + end);
		try {
			response.getOutputStream().write(buffer);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * This is returns the id and className of new object getting shown to the
	 * webservice 1.0 jsp view.
	 * 
	 * @param modelMap
	 * @return some shit
	 */
	@Deprecated
	@RequestMapping(value = "/webservice1_0bs/objectIdCheck/{currentIndex}", method = RequestMethod.GET, produces = "application/json", headers = "Accept=application/json")
	public @ResponseBody ResponseEntity<String> checkObjectId(@PathVariable("currentIndex") int currentIndex,
			HttpServletRequest request, HttpServletResponse response) {

		Gson gson = new Gson();
		showdata = App.spdc.getByIndex(currentIndex);
		String json;
		if (showdata != null) {
			Object obj = showdata.getObject();
			int newId = showdata.getNewIndex();
			String className = obj.getClass().getSimpleName();
			String[] info = { String.valueOf(newId), className };
			json = gson.toJson(info);
		} else {
			json = "[null, null]";
		}
		HttpHeaders headers = new HttpHeaders();
		headers.set("Content-Type", "application/json");

		return new ResponseEntity<String>(json, headers, HttpStatus.CREATED);
	}

	/**
	 * This will evaluate the object and determine its type while adding
	 * attributes to the modelMap
	 * 
	 * @param index, and object. 
	 * @return a json of the object that you found. 
	 */
	private String showThis(ShowPlayersDataModel.ShowData sd) {
		JsonHelper js = new JsonHelper();
		
		if(sd == null){
			js.addAttribute("Name", "No objects shown yet");
			js.addAttribute("Description", "");
			js.addAttribute("ImageFileName", null);
			js.addAttribute("ObjectId", -1);
			js.addAttribute("ObjectClass", null);
			js.addAttribute("CurrentIndex", null);
			return js.getString();
		}
		Object obj = sd.getObject();
		int index = sd.getNewIndex();
		
		String className = obj.getClass().getSimpleName();
		js.addAttribute("CurrentIndex", index);
		js.addAttribute("ObjectClass", className);

		switch (gameObject.valueOf(className)) {
		
		case Npc:
			Npc n = (Npc) obj;
			String nImage = n.getImageFileName();

			js.addAttribute("Name", n.getFullName());
			js.addAttribute("Description", n.getDescription());
			js.addAttribute("ImageFileName", nImage);
			js.addAttribute("ObjectId", n.getID());

			if (n.getSoundFileName() != null) {
				js.addAttribute("SoundFileName", n.getSoundFileName());
				log.debug("the sound file name is " + n.getSoundFileName());
			}
			break;

		case Event:
			Event e = (Event) obj;
			js.addAttribute("Name", e.getName());
			js.addAttribute("Description", e.getDescription());
			js.addAttribute("ObjectId", e.getID());
			js.addAttribute("ImageFileName", e.getImageFileName());
			if (e.getSoundFileName() != null) {
				js.addAttribute("SoundFileName", e.getSoundFileName());
				log.debug("the sound file name is " + e.getSoundFileName());
			}
			break;
		case God:
			God g = (God) obj;
			String gImage = g.getImageFileName();

			js.addAttribute("Name", g.getName());
			js.addAttribute("Description", g.getDescription());
			js.addAttribute("ImageFileName", gImage);
			js.addAttribute("ObjectId", g.getID());

			if (g.getSoundFileName() != null) {
				js.addAttribute("SoundFileName", g.getSoundFileName());
				log.debug("the sound file name is " + g.getSoundFileName());
			}
			break;

		case Region:
			Region r = (Region) obj;
			String rImage = r.getImageFileName();

			js.addAttribute("Name", r.getName());
			js.addAttribute("Description", r.getDescription());
			js.addAttribute("ImageFileName", rImage);
			js.addAttribute("ObjectId", r.getID());

			if (r.getSoundFileName() != null) {
				js.addAttribute("SoundFileName", r.getSoundFileName());
				log.debug("the sound file name is " + r.getSoundFileName());
			}
			break;
		}
		return js.getString();
	}

}