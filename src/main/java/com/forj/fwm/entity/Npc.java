package com.forj.fwm.entity;
import java.util.Date;
import com.j256.ormlite.table.*;
import com.forj.fwm.backend.dao.impl.NpcDaoImpl;
import com.j256.ormlite.field.*;

@DatabaseTable(tableName="NPC", daoClass = NpcDaoImpl.class)
public class Npc implements Searchable{
	
	@DatabaseField(generatedId = true)
	private int ID = -1;
	
	@DatabaseField
	private String history;
	
	@DatabaseField
	private String imageFileName;

	@DatabaseField
	private String soundFileName;
	
	@DatabaseField
	private String decription;
	
	@DatabaseField
	private String lName;
	
	@DatabaseField
	private String fName;
	
	@DatabaseField
	private String gender;

	@DatabaseField
	private String attributes;
	
	@DatabaseField
	private String race;
	
	@DatabaseField
	private String classType;
	
	@DatabaseField
	private int age;
	
	@DatabaseField(foreign=true)
	private Statblock statblock;
	
	@DatabaseField(foreign=true)
	private God god;
	
	@DatabaseField
	private Date lastEdited;
	
	public Npc() {
		
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

	public String getDecription() {
		return decription;
	}

	public void setDecription(String decription) {
		this.decription = decription;
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

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
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
}
