package com.forj.fwm.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.forj.fwm.backend.dao.impl.RegionDaoImpl;
import com.forj.fwm.web.JsonHelper;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName="REGION", daoClass = RegionDaoImpl.class)
public class Region implements Searchable {
	@DatabaseField(generatedId = true)
	private int ID = -1;
	
	@DatabaseField(width=100)
	private String name;
	
	@DatabaseField(width=100)
	private String imageFileName;
	
	@DatabaseField(width=100)
	private String soundFileName;
	
	@DatabaseField(width=2000)
	private String description;
	
	@DatabaseField(width=2000)
	private String history;
	
	@DatabaseField(width=2000)
	private String attributes;
	
	@DatabaseField
	private Date lastEdited;
	
	@DatabaseField(foreign=true)
	private Statblock statblock;
	
	@DatabaseField
	private boolean shown;
	
	private List<Npc> npcs;
	
	private List<God> gods;
	
	private List<Interaction> interactions;
	
	private List<Region> subRegions;
	
	private List<Event> events;
	
	private Region superRegion;
	
	private boolean full;
		
	public Region() {
		npcs = new ArrayList<Npc>();
		gods = new ArrayList<God>();
		interactions = new ArrayList<Interaction>();
		subRegions = new ArrayList<Region>();
		events = new ArrayList<Event>();
		superRegion = null;
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getHistory() {
		return history;
	}

	public void setHistory(String history) {
		this.history = history;
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
	
	public Statblock getStatblock() {
		return statblock;
	}

	public void setStatblock(Statblock statblock) {
		this.statblock = statblock;
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
	
	public List<God> getGods() {
		return gods;
	}

	public void setGods(List<God> gods) {
		this.gods = gods;
	}
	
	public void addGod(God god) {
		this.gods.add(god);
	}

	public void removeGod(God god) {
		this.gods.remove(god);
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
	
	public List<Region> getSubRegions() {
		return subRegions;
	}

	public void setSubRegions(List<Region> subRegions) {
		this.subRegions = subRegions;
	}
	
	public void addSubRegion(Region subRegion) {
		this.subRegions.add(subRegion);
	}

	public void removeSubRegion(Region subRegion) {
		this.subRegions.remove(subRegion);
	}

	public Region getSuperRegion() {
		return superRegion;
	}

	public void setSuperRegion(Region superRegion) {
		this.superRegion = superRegion;
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
