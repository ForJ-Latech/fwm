package com.forj.fwm.entity;

import com.forj.fwm.backend.dao.impl.MMTemplateRegionDaoImpl;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName="MM_TEMPLATE_REGION", daoClass = MMTemplateRegionDaoImpl.class)
public class MMTemplateRegion {
	@DatabaseField(foreign = true, canBeNull = false, uniqueCombo = true)
	private Template template;
	
	@DatabaseField(foreign = true, canBeNull = false, uniqueCombo = true)
	private Region region;
	
	public MMTemplateRegion() {
		
	}
	
	public MMTemplateRegion(Template template, Region region) {
		this.template = template;
		this.region = region;
	}

	public Template getTemplate() {
		return template;
	}

	public void setTemplate(Template template) {
		this.template = template;
	}

	public Region getRegion() {
		return region;
	}

	public void setRegion(Region region) {
		this.region = region;
	}
}
