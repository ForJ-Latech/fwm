package com.forj.fwm.entity;

import java.util.Date;

import com.forj.fwm.backend.dao.impl.EventDaoImpl;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName="EVENT", daoClass = EventDaoImpl.class)
public class Event implements Searchable{
	@DatabaseField(generatedId = true)
	private int ID = -1;
	
	@DatabaseField
	private String name;
	
	@DatabaseField
	private String description;
	
	@DatabaseField
	private String trigger;
	
	@DatabaseField
	private Date lastEdited;
	
	@DatabaseField
	private Date date;
	
	@DatabaseField(foreign=true)
	private Region region;
	
	public Event(){
		
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

	public String getTrigger() {
		return trigger;
	}

	public void setTrigger(String trigger) {
		this.trigger = trigger;
	}

	public Date getLastEdited() {
		return lastEdited;
	}

	public void setLastEdited(Date lastEdited) {
		this.lastEdited = lastEdited;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Region getRegion() {
		return region;
	}

	public void setRegion(Region region) {
		this.region = region;
	}
}
