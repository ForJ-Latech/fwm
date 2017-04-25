package com.forj.fwm.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Category;
import org.apache.log4j.Logger;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.*;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.*;

import com.forj.fwm.backend.Backend;
import com.forj.fwm.backend.ShowPlayersDataModel;
import com.forj.fwm.entity.Event;
import com.forj.fwm.entity.God;
import com.forj.fwm.entity.Npc;
import com.forj.fwm.entity.Region;
import com.forj.fwm.entity.Searchable;
import com.forj.fwm.gui.JettyController;
import com.forj.fwm.startup.App;
import com.google.gson.Gson;
import com.j256.ormlite.stmt.SelectArg;

@Controller
public class Webservice1_5Controller {
	private static Logger log = Logger.getLogger(Webservice1_5Controller.class);

	private enum gameObject {
		Npc, Event, God, Region;
	}
	
	@RequestMapping("/webservice1_5")
	public ModelAndView startWS1_5(ModelMap modelMap, HttpServletRequest request) throws Exception {
		return new ModelAndView("webservice1_5");
	}

	@RequestMapping("/webservice1_5/{search}")
	public ModelAndView search(ModelMap modelMap, @PathVariable("search") String Npcname, HttpServletRequest request) {
		// colName, Object (search)
		try {
			List<Npc> q = Backend.getNpcDao().queryForLike("fname", Npcname);
			modelMap.addAttribute("found", q);
		} catch (SQLException e) {
			log.error(e);
		}

		return new ModelAndView("webservice1_5");
	}
	
	@RequestMapping("searchAll/{text}")
	public @ResponseBody ResponseEntity<String> search(@PathVariable("text") String text, HttpServletRequest request, HttpServletResponse response){
		String json;
		log.debug("inside searchall/" + text);
		try{
			List<Searchable> curList = Backend.searchAllByLike(text);
			StringBuilder s = new StringBuilder();
			log.debug("curList size: " + curList.size());
			s.append("[");
			int cntr = 0;
			for(Searchable cur : curList){
				// they should only show up if they have been seen in the list. 
				if(showLogic(cur)){
					if(cntr++ > 0){
						s.append(",");
					}
					s.append(cur.toOneFiveJsonString());
				}
			}
			s.append("]");
			json = s.toString();
			log.debug(json);
		}
		catch(Exception e){
			e.printStackTrace();
			json = "{\"Message\":\"" + e.getLocalizedMessage() + "\"}";
		}
		HttpHeaders headers = new HttpHeaders();
		headers.set("Content-Type", "application/json");
		
    	return new ResponseEntity<String>(json, headers, HttpStatus.CREATED);
	}
	
	
	@RequestMapping("getGod/{id}")
	public @ResponseBody ResponseEntity<String> getGod(@PathVariable("id") int id, HttpServletRequest request, HttpServletResponse response){
		String json;
		try{
			God god = Backend.getGodDao().getFullGod(id);
			if(showLogic(god) && god != null){
				JsonHelper help = new JsonHelper(god.toOneFiveJsonString());
				// should in theory work for every type of searchable list...
				addListToHelper("npcs", App.toListSearchable(god.getNpcs()), help);
				addListToHelper("events", App.toListSearchable(god.getEvents()), help);
				addListToHelper("regions", App.toListSearchable(god.getRegions()), help);
				addListToHelper("pantheon", 
						App.toListSearchable(Backend.getGodDao().getPantheon(god)), 
						help);
				json = help.getString();
			}else
			{
				throw new Exception("This npc is not showable.");
			}
		}
		catch(Exception e){
			e.printStackTrace();
			json = "{\"Message\":\"" + e.getLocalizedMessage() + "\"}";
		}
		HttpHeaders headers = new HttpHeaders();
		headers.set("Content-Type", "application/json");
		
    	return new ResponseEntity<String>(json, headers, HttpStatus.CREATED);
	}
	
	@RequestMapping("getNpc/{id}")
	public @ResponseBody ResponseEntity<String> getNpc(@PathVariable("id") int id, HttpServletRequest request, HttpServletResponse response){
		String json;
		try{
			Npc npc = Backend.getNpcDao().getFullNpc(id);
			if(showLogic(npc) && npc != null){
				JsonHelper help = new JsonHelper(npc.toOneFiveJsonString());
				God curG = null;
				if(npc.getGod() != null){
					curG = Backend.getGodDao().getGod(npc.getGod().getID()); 
				}
				// add our god
				if(showLogic(curG)){
					help.addRawString("god", curG.toOneFiveJsonString());
				}else
				{
					help.addAttribute("god", null);
				}
				// should in theory work for every type of searchable list... 
				addListToHelper("events", App.toListSearchable(npc.getEvents()), help);
				addListToHelper("regions", App.toListSearchable(npc.getRegions()), help);
				addListToHelper("family", 
						App.toListSearchable(Backend.getNpcDao().getFamily(npc)), 
						help);
				json = help.getString();
			}else
			{
				throw new Exception("This npc is not showable.");
			}
		}
		catch(Exception e){
			e.printStackTrace();
			json = "{\"Message\":\"" + e.getLocalizedMessage() + "\"}";
		}
		HttpHeaders headers = new HttpHeaders();
		headers.set("Content-Type", "application/json");
		
    	return new ResponseEntity<String>(json, headers, HttpStatus.CREATED);
	}
	
	@RequestMapping("getRegion/{id}")
	public @ResponseBody ResponseEntity<String> getRegion(@PathVariable("id") int id, HttpServletRequest request, HttpServletResponse response){
		String json;
		try{
			Region reg = Backend.getRegionDao().getFullRegion(id);
			if(showLogic(reg) && reg != null){
				JsonHelper help = new JsonHelper(reg.toOneFiveJsonString());
				Region curReg = null;
				if(reg.getSuperRegion() != null){
					curReg = Backend.getRegionDao().getRegion(reg.getSuperRegion().getID()); 
				}
				// add our super region
				if(showLogic(curReg)){
					help.addRawString("superRegion", curReg.toOneFiveJsonString());
				}else
				{
					help.addAttribute("superRegion", null);
				}
				// should in theory work for every type of searchable list... 
				addListToHelper("npcs", App.toListSearchable(reg.getNpcs()), help);
				addListToHelper("gods", App.toListSearchable(reg.getGods()), help);
				addListToHelper("regions", App.toListSearchable(reg.getSubRegions()), help);
				json = help.getString();
			}else
			{
				throw new Exception("This " + Event.WHAT_IT_DO +  " is not showable.");
			}
		}
		catch(Exception e){
			json = "{\"Message\":\"" + e.getLocalizedMessage() + "\"}";
		}
		HttpHeaders headers = new HttpHeaders();
		headers.set("Content-Type", "application/json");
		
    	return new ResponseEntity<String>(json, headers, HttpStatus.CREATED);
	}
	
	@RequestMapping("getEvent/{id}")
	public @ResponseBody ResponseEntity<String> getEvent(@PathVariable("id") int id, HttpServletRequest request, HttpServletResponse response){
		String json;
		try{
			Event ev = Backend.getEventDao().getFullEvent(id);
			if(showLogic(ev) && ev != null){
				JsonHelper help = new JsonHelper(ev.toOneFiveJsonString());
				Region curReg = null;
				if(ev.getRegion() != null){
					curReg = Backend.getRegionDao().getRegion(ev.getRegion().getID()); 
				}
				// add our god
				if(showLogic(curReg)){
					help.addRawString("region", curReg.toOneFiveJsonString());
				}else
				{
					help.addAttribute("region", null);
				}
				// should in theory work for every type of searchable list... 
				addListToHelper("npcs", App.toListSearchable(ev.getNpcs()), help);
				addListToHelper("gods", App.toListSearchable(ev.getGods()), help);
				json = help.getString();
			}else
			{
				throw new Exception("This " + Event.WHAT_IT_DO +  " is not showable.");
			}
		}
		catch(Exception e){
			json = "{\"Message\":\"" + e.getLocalizedMessage() + "\"}";
		}
		HttpHeaders headers = new HttpHeaders();
		headers.set("Content-Type", "application/json");
		
    	return new ResponseEntity<String>(json, headers, HttpStatus.CREATED);
	}
	
	public static boolean showLogic(Searchable s){
		// because null will always be fine to show, lmao. 
		if(s == null){
			return false;
		}
		return s.isShown();
	}
	
	public static void addListToHelper(String key, List<Searchable> things, JsonHelper help){
		
		StringBuilder events = new StringBuilder("[");
		if(things != null){
			int cntr = 0;
			for(Searchable e : things){
				if(showLogic(e)){
					if(cntr++ > 0){
						events.append(",");
					}
					events.append(e.toOneFiveJsonString());
				}
			}
		}
		events.append("]");
		help.addRawString(key, events.toString());
		
	}
}
