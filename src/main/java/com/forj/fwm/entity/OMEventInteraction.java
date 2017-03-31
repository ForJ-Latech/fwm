package com.forj.fwm.entity;

import com.forj.fwm.backend.dao.impl.OMEventInteractionDaoImpl;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName="OM_EVENT_INTERACTION", daoClass = OMEventInteractionDaoImpl.class)
public class OMEventInteraction {
	@DatabaseField(foreign=true, canBeNull=false)
	private Event event;
	
	@DatabaseField(foreign=true, canBeNull=false, unique = true)
	private Interaction interaction;
	
	public OMEventInteraction() {
		
	}

	public Event getEvent() {
		return event;
	}

	public void setEvent(Event event) {
		this.event = event;
	}
	
	public Interaction getInteraction() {
		return interaction;
	}

	public void setInteraction(Interaction interaction) {
		this.interaction = interaction;
	}
}
