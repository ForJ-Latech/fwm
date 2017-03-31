package com.forj.fwm.entity;

import com.forj.fwm.backend.dao.impl.MMEventNpcDaoImpl;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName="MM_EVENT_NPC", daoClass = MMEventNpcDaoImpl.class)
public class MMEventNpc {
	@DatabaseField(foreign = true, canBeNull = false, uniqueCombo = true)
	private Npc npc;
	
	@DatabaseField(foreign = true, canBeNull = false, uniqueCombo = true)
	private Event event;
	
	public MMEventNpc(){
		
	}

	public Npc getNpc() {
		return npc;
	}

	public void setNpc(Npc npc) {
		this.npc = npc;
	}

	public Event getEvent() {
		return event;
	}

	public void setEvent(Event event) {
		this.event = event;
	}
}
