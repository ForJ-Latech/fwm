package com.forj.fwm.entity;

import java.util.Date;

import com.forj.fwm.backend.dao.impl.InteractionDaoImpl;
import com.forj.fwm.web.JsonHelper;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName="INTERACTION", daoClass = InteractionDaoImpl.class)
public class Interaction implements Searchable{
	@DatabaseField(generatedId = true)
	private int ID;
	
	@DatabaseField(defaultValue = "Party", width=100)
	private String playerCharacter;
	
	@DatabaseField
	private Date lastEdited;
	
	@DatabaseField(width=2000)
	private String description;

	@DatabaseField
	private Date date;
	
	public Interaction() {
		
	}

	public String toOneFiveJsonString(){
		JsonHelper j = new JsonHelper();
		j.addAttribute("id", getID());
		j.addAttribute("name", getShownName());
		j.addAttribute("description", getDescription());
		j.addAttribute("class", this.getClass().getSimpleName());
		return j.getString();
	}
	
	public int getID() {
		return ID;
	}

	public String getPlayerCharacter() {
		return playerCharacter;
	}

	public void setPlayerCharacter(String playerCharacter) {
		this.playerCharacter = playerCharacter;
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

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}
	
	public String getShownName(){
		return this.getPlayerCharacter() + ": " + this.getDescription();
	}

	public String getImageFileName() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public boolean isShown() {
		return false;
	}

	public void setShown(boolean shown) {
		return;
	}
}
