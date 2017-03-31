package com.forj.fwm.entity;
import com.j256.ormlite.table.*;

import java.util.Date;

import org.eclipse.jetty.util.log.Log;

import com.forj.fwm.backend.dao.impl.GodDaoImpl;
import com.j256.ormlite.field.*;

@DatabaseTable(tableName="GOD", daoClass = GodDaoImpl.class)
public class God  implements Searchable{
	@DatabaseField(generatedId = true)
	private int ID = -1;
	
	@DatabaseField
	private String name;
	
	@DatabaseField
	private String pantheon;
	
	@DatabaseField
	private String history;
	
	@DatabaseField
	private String imageFileName;
	
	@DatabaseField
	private String soundFileName;
	
	@DatabaseField
	private String gender;
	
	@DatabaseField
	private String race;
	
	@DatabaseField
	private String class_;
	
	@DatabaseField(foreign=true)
	private Statblock statblock;
	
	@DatabaseField
	private String attributes;
	
	@DatabaseField
	private Date lastEdited;
	
	@DatabaseField
	private String description;
	
	public God() {
		
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
		System.out.println("setStatBlock Called " + statblock.getID());
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
}
