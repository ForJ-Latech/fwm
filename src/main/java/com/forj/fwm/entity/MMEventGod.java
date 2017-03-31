package com.forj.fwm.entity;

import com.forj.fwm.backend.dao.impl.MMEventGodDaoImpl;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName="MM_EVENT_GOD", daoClass = MMEventGodDaoImpl.class)
public class MMEventGod {
	@DatabaseField(foreign = true, canBeNull = false, uniqueCombo = true)
	private God god;
	
	@DatabaseField(foreign = true, canBeNull = false, uniqueCombo = true)
	private Event event;

	public MMEventGod(){
		
	}
	
	public God getGod() {
		return god;
	}

	public void setGod(God god) {
		this.god = god;
	}

	public Event getEvent() {
		return event;
	}

	public void setEvent(Event event) {
		this.event = event;
	}
}
