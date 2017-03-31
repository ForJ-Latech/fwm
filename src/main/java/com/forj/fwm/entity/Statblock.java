package com.forj.fwm.entity;

import com.j256.ormlite.table.*;
import com.forj.fwm.backend.dao.impl.StatblockDaoImpl;
import com.forj.fwm.gui.tab.Saveable;
import com.j256.ormlite.field.*;

@DatabaseTable(tableName="STATBLOCK", daoClass = StatblockDaoImpl.class)
public class Statblock implements Searchable{ 
	@DatabaseField(generatedId = true)
	private int ID = -1;
	
	@DatabaseField
	private String name;
	
	@DatabaseField
	private String description;
	
	public Statblock() {
		
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
}
