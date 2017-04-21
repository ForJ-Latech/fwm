package com.forj.fwm.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.forj.fwm.backend.dao.impl.EventDaoImpl;
import com.forj.fwm.web.JsonHelper;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName="EVENT", daoClass = EventDaoImpl.class)
public class Event implements Searchable {
	public static final String WHAT_IT_DO = "Group";
	
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
	
	@DatabaseField
	private boolean shown;
	
	@DatabaseField(foreign=true)
	private Region region;
	
	private List<God> gods;
	
	private List<Npc> npcs;
	
	private List<Statblock> statblocks;
	
	private List<Interaction> interactions;
	
	private boolean full;
	
	
	public Event(){
		gods = new ArrayList<God>();
		npcs = new ArrayList<Npc>();
		statblocks = new ArrayList<Statblock>();
		interactions = new ArrayList<Interaction>();
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
		j.addAttribute("class", this.getClass().getSimpleName());
		return j.getString();
	}
	
	public String getSoundFileName() {
		return soundFileName;
	}

	public void setSoundFileName(String soundFileName) {
		this.soundFileName = soundFileName;
	}
	
	public String getImageFileName() {
		return imageFileName;
	}

	public void setImageFileName(String imageFileName) {
		this.imageFileName = imageFileName;
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getHistory() {
		return history;
	}

	public void setHistory(String trigger) {
		this.history = trigger;
	}

	public Region getRegion() {
		return region;
	}

	public void setRegion(Region region) {
		this.region = region;
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
	
	public List<Statblock> getStatblocks() {
		return statblocks;
	}

	public void setStatblocks(List<Statblock> statblocks) {
		this.statblocks = statblocks;
	}
	
	public void addStatblock(Statblock statblock) {
		this.statblocks.add(statblock);
	}

	public void removeStatblock(Statblock statblock) {
		this.statblocks.remove(statblock);
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
	
	public String getAttributes() {
		return attributes;
	}

	public void setAttributes(String importantInfo) {
		this.attributes = importantInfo;
	}

	public Date getLastEdited() {
		return lastEdited;
	}

	public void setLastEdited(Date lastEdited) {
		this.lastEdited = lastEdited;
	}
}
