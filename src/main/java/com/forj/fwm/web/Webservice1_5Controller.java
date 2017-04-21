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

@Controller
public class Webservice1_5Controller {
	private static Logger log = Logger.getLogger(Webservice1_5Controller.class);

	private enum gameObject {
		Npc, Event, God, Region;
	}
	
	public static class JsonSearchable{
		private Searchable thing;
		private String type;
		public JsonSearchable(Searchable s){
			this.type = s.getClass().getSimpleName();
			this.thing = s;
		}
		public Searchable getS() {
			return thing;
		}
		public String getType() {
			return type;
		}
		public void setS(Searchable s) {
			this.thing = s;
		}
		public void setType(String type) {
			this.type = type;
		}
	}
	
	
	// All views should be created with "HttpServletRequest request" as a second
	// parameter for getting client IP
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
	
	@RequestMapping("/loadNpc/{id}")
	public ModelAndView loadNpc(ModelMap modelMap, @PathVariable("id") int id, HttpServletRequest request) {
		// colName, Object (search)
		try {
			Npc q = Backend.getNpcDao().getNpc(id);
			modelMap.addAttribute("npc", q);
		} catch (SQLException e) {
			log.error(e);
		}

		return new ModelAndView("webservice1_5");
	}

	@RequestMapping("jay_test_shit")
	public ModelAndView jayTestShit(ModelMap modelMap, HttpServletRequest request) {
		List<Npc> st = new ArrayList<Npc>();
		for (int i = 0; i < 10; i++) {
			Npc cur = new Npc();
			cur.setfName("NPC " + i);
			st.add(cur);
		}
		modelMap.addAttribute("st", st);
		return new ModelAndView("jays");
	}
	
	@RequestMapping("searchAll/{text}")
	public @ResponseBody ResponseEntity<String> search(@PathVariable("text") String text, HttpServletRequest request, HttpServletResponse response){
		String json;
		try{
			List<Searchable> curList = Backend.searchAllByLike(text);
			List<JsonSearchable> results = new ArrayList<JsonSearchable>();
			StringBuilder s = new StringBuilder();
			s.append("[");
			for(Searchable cur : curList){
				s.append(cur.toOneFiveJsonString() + ",");
			}
			s.append("]");
			json = s.toString();
//			json = g.toJson(results.toArray(new JsonSearchable[0]));
		}
		catch(Exception e){
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
			God g = Backend.getGodDao().getFullGod(id);
			json = g.toOneFiveJsonString();
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
			Npc g = Backend.getNpcDao().getFullNpc(id);
			json = g.toOneFiveJsonString();
		}
		catch(Exception e){
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
			Region g = Backend.getRegionDao().getFullRegion(id);
			json = g.toOneFiveJsonString();
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
			Event g = Backend.getEventDao().getFullEvent(id);
			json = g.toOneFiveJsonString();
		}
		catch(Exception e){
			json = "{\"Message\":\"" + e.getLocalizedMessage() + "\"}";
		}
		HttpHeaders headers = new HttpHeaders();
		headers.set("Content-Type", "application/json");
		
    	return new ResponseEntity<String>(json, headers, HttpStatus.CREATED);
	}
}
