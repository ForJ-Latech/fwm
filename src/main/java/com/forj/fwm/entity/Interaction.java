package com.forj.fwm.entity;

import java.util.Date;

import com.forj.fwm.backend.dao.impl.InteractionDaoImpl;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName="INTERACTION", daoClass = InteractionDaoImpl.class)
public class Interaction {
	@DatabaseField(generatedId = true)
	private int ID;
	
	@DatabaseField(defaultValue = "Party")
	private String playerCharacter;
	
	@DatabaseField
	private Date lastEdited;
	
	@DatabaseField
	private String description;

	@DatabaseField
	private Date date;
	
	public Interaction() {
		
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
}
