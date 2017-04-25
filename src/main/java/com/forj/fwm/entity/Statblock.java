package com.forj.fwm.entity;

import com.forj.fwm.backend.dao.impl.StatblockDaoImpl;
import com.forj.fwm.web.JsonHelper;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName="STATBLOCK", daoClass = StatblockDaoImpl.class)
public class Statblock implements Searchable{ 
	@DatabaseField(generatedId = true)
	private int ID = -1;
	
	@DatabaseField(width=100)
	private String name;
	
	@DatabaseField(columnDefinition="VARCHAR_IGNORECASE", width=500)
	private String ignoreCaseName;
	
	@DatabaseField(width=5000)
	private String description;
	
	public Statblock() {
		
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
	
	public Statblock(String name, String description) {
		this.name = name;
		this.description = description;
	}
	
	public int getID() {
		return ID;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
		this.ignoreCaseName = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getImageFileName() {
		return "";
	}
	

	public boolean isShown() {
		return false;
	}

	public void setShown(boolean shown) {
		return;
	}
}
