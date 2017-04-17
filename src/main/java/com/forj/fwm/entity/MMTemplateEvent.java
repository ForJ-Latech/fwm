package com.forj.fwm.entity;

import com.forj.fwm.backend.dao.impl.MMTemplateEventDaoImpl;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName="MM_TEMPLATE_EVENT", daoClass = MMTemplateEventDaoImpl.class)
public class MMTemplateEvent {
	@DatabaseField(foreign = true, canBeNull = false, uniqueCombo = true)
	private Template template;
	
	@DatabaseField(foreign = true, canBeNull = false, uniqueCombo = true)
	private Event event;
	
	public MMTemplateEvent() {
		
	}
	
	public MMTemplateEvent(Template template, Event event) {
		this.template = template;
		this.event = event;
	}

	public Template getTemplate() {
		return template;
	}

	public void setTemplate(Template template) {
		this.template = template;
	}

	public Event getEvent() {
		return event;
	}

	public void setEvent(Event event) {
		this.event = event;
	}
}
