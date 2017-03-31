package com.forj.fwm.entity;

import com.forj.fwm.backend.dao.impl.MMEventStatblockDaoImpl;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName="MM_EVENT_STATBLOCK", daoClass = MMEventStatblockDaoImpl.class)
public class MMEventStatblock {
	@DatabaseField(foreign = true, canBeNull = false, uniqueCombo = true)
	private Statblock statblock;
	
	@DatabaseField(foreign = true, canBeNull = false, uniqueCombo = true)
	private Event event;
	
	public MMEventStatblock() {

	}

	public Statblock getStatblock() {
		return statblock;
	}

	public void setStatblock(Statblock statblock) {
		this.statblock = statblock;
	}

	public Event getEvent() {
		return event;
	}

	public void setEvent(Event event) {
		this.event = event;
	}
}
