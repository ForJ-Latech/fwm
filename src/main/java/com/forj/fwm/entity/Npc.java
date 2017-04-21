package com.forj.fwm.entity;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.j256.ormlite.table.*;
import com.forj.fwm.backend.dao.impl.NpcDaoImpl;
import com.forj.fwm.web.JsonHelper;
import com.j256.ormlite.field.*;

@DatabaseTable(tableName="NPC", daoClass = NpcDaoImpl.class)
public class Npc implements Searchable {
	
	@DatabaseField(generatedId = true)
	private int ID = -1;
	
	@DatabaseField(width=2000)
	private String history;
	
	@DatabaseField(width=100)
	private String imageFileName;

	@DatabaseField(width=100)
	private String soundFileName;
	
	@DatabaseField(width=2000)
	private String description;
	
	@DatabaseField(width=100)
	private String lName;
	
	@DatabaseField(width=100)
	private String fName;
	
	@DatabaseField(width=100)
	private String gender;

	@DatabaseField(width=2000)
	private String attributes;
	
	@DatabaseField(width=100)
	private String race;
	
	@DatabaseField(width=100)
	private String classType;
	
	@DatabaseField(width=100)
	private String age;
	
	@DatabaseField(foreign=true)
	private Statblock statblock;
	
	@DatabaseField(foreign=true)
	private God god;
	
	@DatabaseField
	private Date lastEdited;
	
	@DatabaseField
	private boolean shown;
	
	private List<Interaction> interactions;
	
	private List<Region> regions;
	
	private List<Event> events;
	
	private boolean full;
	
	
	public Npc() {
		interactions = new ArrayList<Interaction>();
		regions = new ArrayList<Region>();
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
		j.addAttribute("class", this.getClass().getSimpleName());
		return j.getString();
	}
	
	public int getID() {
		return ID;
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getlName() {
		return lName;
	}

	public void setlName(String lName) {
		this.lName = lName;
	}

	public String getfName() {
		return fName;
	}
	
	public String getFullName() {
		if (getlName() != null) {
			return (getfName() + " " + getlName()).trim();
		}
		else {
			return getfName();
		}
	}
	
	public String getGender() {
		return gender;
	}
	
	public void setGender(String gender){
		this.gender = gender;
	}

	public void setfName(String fName) {
		this.fName = fName;
	}

	public String getAttributes() {
		return attributes;
	}

	public void setAttributes(String attributes) {
		this.attributes = attributes;
	}

	public String getRace() {
		return race;
	}

	public void setRace(String race) {
		this.race = race;
	}

	public String getClassType() {
		return classType;
	}

	public void setClassType(String classType) {
		this.classType = classType;
	}

	public String getAge() {
		return age;
	}

	public void setAge(String age) {
		this.age = age;
	}

	public Statblock getStatblock() {
		return statblock;
	}

	public void setStatblock(Statblock statblock) {
		this.statblock = statblock;
	}

	public God getGod() {
		return god;
	}

	public void setGod(God god) {
		this.god = god;
	}

	public Date getLastEdited() {
		return lastEdited;
	}

	public void setLastEdited(Date lastEdited) {
		this.lastEdited = lastEdited;
	}

	public String getName() {
		return fName;
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
