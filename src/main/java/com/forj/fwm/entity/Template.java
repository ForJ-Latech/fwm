package com.forj.fwm.entity;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import com.forj.fwm.backend.dao.impl.TemplateDaoImpl;
import com.forj.fwm.web.JsonHelper;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "TEMPLATE", daoClass = TemplateDaoImpl.class)
public class Template implements Searchable {
	
	@DatabaseField(generatedId = true)
	private int ID = -1;
	
	@DatabaseField(width=100)
	private String name;
	
	@DatabaseField(columnDefinition="VARCHAR_IGNORECASE", width=500)
	private String ignoreCaseName;
	
	@DatabaseField(width=100)
	private String imageFileName;
	
	@DatabaseField
	private Date lastEdited;
	
	@DatabaseField(width=2000)
	private String history;
	
	@DatabaseField(width=2000)
	private String description;
	
	@DatabaseField(width=2000)
	private String lName;
	
	@DatabaseField(width=2000)
	private String fName;
	
	@DatabaseField(width=2000)
	private String gender;

	@DatabaseField(width=2000)
	private String attributes;
	
	@DatabaseField(width=2000)
	private String race;
	
	@DatabaseField(width=2000)
	private String classType;
	
	@DatabaseField(width=2000)
	private String age;
	
	@DatabaseField(foreign=true)
	private Statblock statblock;
	
	@DatabaseField(foreign=true)
	private God god;
	
	@DatabaseField(foreign=true) 
	private Region region;
	
	List<Region> regions;
	
	List<God> gods;
	
	List<Event> events;
	
	public Template() {
		regions = new ArrayList<Region>();
		gods = new ArrayList<God>();
		events = new ArrayList<Event>();
	}
	
	public Npc newFromTemplate() throws SQLException {
		Npc npc = new Npc();
		if (history != null) {
			npc.setHistory(randomize(history));
		}
		
		if (description != null) {
			npc.setDescription(randomize(description));
		}
		
		if (lName != null) {
			npc.setlName(randomize(lName));
		}
		
		if (fName != null) {
			npc.setfName(randomize(fName));
		}
		
		if (gender != null) {
			npc.setGender(randomize(gender));
		}
		
		if (attributes != null) {
			npc.setAttributes(randomize(attributes));
		}
		
		if (race != null) {
			npc.setRace(randomize(race));
		}
		
		if (classType != null) {
			npc.setClassType(randomize(classType));
		}
		
		if (age != null) {
			npc.setAge(randomize(age));
		}
		
		return npc;
	}
	
	private String randomize(String s) {
		List<String> allInputs = spl(s);
		String output = "";
		for (int i = 0; i < allInputs.size(); i++) {
			switch (i%2) {
				case 0:
					output+=allInputs.get(i);
					break;
				case 1:
					String input = allInputs.get(i);
					if (allInputs.get(i).contains("[")) {
						input = randomize(allInputs.get(i));
					} 
					List<String> inputs = new ArrayList<String>();
					for (String choice : input.split(",")) {
						inputs.add(choice);
					}
					output+=ran(inputs);
					break;
			}
		}
		return output;
	}
	
	// Splits on only first set of brackets
	private List<String> spl(String s) {
		List<String> output = new ArrayList<String>();
		String temp = "";
		int br = 0;
		for (char c : s.toCharArray()) {
			if (c == '[' && br == 0) {
				br++;
				output.add(temp);
				temp = "";
			} else if (c == '[') {
				br++;
				temp+=c;
			} else if (c == ']' && br == 1) {
				br--;
				output.add(temp);
				temp = "";
			} else if (c == ']') {
				br--;
				temp+=c;
			} else {
				temp+=c;
			}
		}
		output.add(temp);
		return output;
	}
	
	// returns a random string chosen from a list of strings
	private String ran(List<String> o) {
		return o.get((new Random()).nextInt(o.size()));
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
		this.ignoreCaseName = name;
	}
	
	public Date getLastEdited() {
		return lastEdited;
	}

	public void setLastEdited(Date lastEdited) {
		this.lastEdited = lastEdited;
	}

	public String getImageFileName() {
		return imageFileName;
	}

	public void setImageFileName(String imageFileName) {
		this.imageFileName = imageFileName;
	}

	public String getHistory() {
		return history;
	}

	public void setHistory(String history) {
		this.history = history;
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

	public void setfName(String fName) {
		this.fName = fName;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
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

	public Region getRegion() {
		return region;
	}

	public void setRegion(Region region) {
		this.region = region;
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
	
	public boolean isShown() {
		return false;
	}

	public void setShown(boolean shown) {
		return;
	}
}
