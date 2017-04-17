package com.forj.fwm.entity;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.forj.fwm.backend.dao.impl.GodDaoImpl;
import com.forj.fwm.web.JsonHelper;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName="GOD", daoClass = GodDaoImpl.class)
public class God  implements Searchable{
	@DatabaseField(generatedId = true)
	private int ID = -1;
	
	@DatabaseField(width=100)
	private String name;
	
	@DatabaseField(width=100)
	private String pantheon;
	
	@DatabaseField(width=2000)
	private String history;
	
	@DatabaseField(width=100)
	private String imageFileName;
	
	@DatabaseField(width=100)
	private String soundFileName;
	
	@DatabaseField(width=100)
	private String gender;
	
	@DatabaseField(width=100)
	private String race;
	
	@DatabaseField(width=100)
	private String class_;
	
	@DatabaseField(foreign=true)
	private Statblock statblock;
	
	@DatabaseField(width=2000)
	private String attributes;
	
	@DatabaseField
	private Date lastEdited;
	
	@DatabaseField(width=2000)
	private String description;
	
	@DatabaseField
	private boolean shown;
	
	
	private List<Interaction> interactions;
	
	private List<Region> regions;
	
	private List<Npc> npcs;
	
	private List<Event> events;
	
	private boolean full;
	
	
	public God() {
		interactions = new ArrayList<Interaction>();
		regions = new ArrayList<Region>();
		npcs = new ArrayList<Npc>();
		events = new ArrayList<Event>();
		full = false;
		shown = false;
	}
	
	public String toOneFiveJsonString(){
		JsonHelper j = new JsonHelper();
		j.addAttribute("id", getID());
		j.addAttribute("name", getName());
		j.addAttribute("description", getDescription());
		j.addAttribute("imageFileName", getImageFileName());
		j.addAttribute("name", getName());
		return j.getString();
	}
	
	public int getID() {
		return ID;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPantheon() {
		return pantheon;
	}

	public void setPantheon(String pantheon) {
		this.pantheon = pantheon;
	}

	public String getHistory() {
		return history;
	}

	public void setHistory(String history) {
		this.history = history;
	}

	public String getImageFileName() {
		return imageFileName;
	}

	public void setImageFileName(String imageFileName) {
		this.imageFileName = imageFileName;
	}

	public String getSoundFileName() {
		return soundFileName;
	}

	public void setSoundFileName(String soundFileName) {
		this.soundFileName = soundFileName;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getRace() {
		return race;
	}

	public void setRace(String race) {
		this.race = race;
	}

	public String getClass_() {
		return class_;
	}

	public void setClass_(String class_) {
		this.class_ = class_;
	}

	public Statblock getStatblock() {
		return statblock;
	}

	public void setStatblock(Statblock statblock) {
		this.statblock = statblock;
	}

	public String getAttributes() {
		return attributes;
	}

	public void setAttributes(String attributes) {
		this.attributes = attributes;
	}

	public Date getLastEdited() {
		return lastEdited;
	}

	public void setLastEdited(Date lastEdited) {
		this.lastEdited = lastEdited;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	public List<Interaction> getInteractions() {
		return interactions;
	}

	public void setInteractions(List<Interaction> interactions) {
		this.interactions = interactions;
	}
	
	public void addInteraction(Interaction interaction) {
		this.interactions.add(interaction);
	}

	public void removeInteraction(Interaction interaction) {
		this.interactions.remove(interaction);
	}
	
	public List<Region> getRegions() {
		return regions;
	}

	public void setRegions(List<Region> regions) {
		this.regions = regions;
	}
	
	public void addRegion(Region region) {
		this.regions.add(region);
	}

	public void removeRegion(Region region) {
		this.regions.remove(region);
	}
	
	public List<Npc> getNpcs() {
		return npcs;
	}

	public void setNpcs(List<Npc> npcs) {
		this.npcs = npcs;
	}
	
	public void addNpc(Npc npc) {
		this.npcs.add(npc);
	}

	public void removeNpc(Npc npc) {
		this.npcs.remove(npc);
	}
	
	public List<Event> getEvents() {
		return events;
	}

	public void setEvents(List<Event> events) {
		this.events = events;
	}
	
	public void addEvent(Event event) {
		this.events.add(event);
	}

	public void removeEvent(Event event) {
		this.events.remove(event);
	}

	public boolean isFull() {
		return full;
	}

	public void setFull(boolean isFull) {
		this.full = isFull;
	}

	public boolean isShown() {
		return shown;
	}

	public void setShown(boolean shown) {
		this.shown = shown;
	}
}
