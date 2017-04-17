package com.forj.fwm.entity;

import com.forj.fwm.backend.dao.impl.MMTemplateGodDaoImpl;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName="MM_TEMPLATE_GOD", daoClass = MMTemplateGodDaoImpl.class)
public class MMTemplateGod {
	@DatabaseField(foreign = true, canBeNull = false, uniqueCombo = true)
	private Template template;
	
	@DatabaseField(foreign = true, canBeNull = false, uniqueCombo = true)
	private God god;
	
	public MMTemplateGod() {
		
	}
	
	public MMTemplateGod(Template template, God god) {
		this.template = template;
		this.god = god;
	}
	
	public Template getTemplate() {
		return template;
	}

	public void setTemplate(Template template) {
		this.template = template;
	}

	public God getGod() {
		return god;
	}

	public void setGod(God god) {
		this.god = god;
	}
}
