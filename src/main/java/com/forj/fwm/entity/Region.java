package com.forj.fwm.entity;

import java.util.Date;

import com.forj.fwm.backend.dao.impl.RegionDaoImpl;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName="REGION", daoClass = RegionDaoImpl.class)
public class Region implements Searchable{
	@DatabaseField(generatedId = true)
	private int ID = -1;
	
	@DatabaseField
	private String name;
	
	@DatabaseField
	private String imageFileName;
	
	@DatabaseField
	private String soundFileName;
	
	@DatabaseField
	private String description;
	
	@DatabaseField
	private String history;
	
	@DatabaseField
	private String attributes;
	
	@DatabaseField
	private Date lastEdited;
	
	public Region() {
		
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
}
