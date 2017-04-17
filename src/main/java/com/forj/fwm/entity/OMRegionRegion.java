package com.forj.fwm.entity;

import com.forj.fwm.backend.dao.impl.OMRegionRegionDaoImpl;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName="OM_REGION_REGION", daoClass = OMRegionRegionDaoImpl.class)
public class OMRegionRegion {
	@DatabaseField(foreign = true, canBeNull = false)
	private Region superRegion;
	
	@DatabaseField(foreign = true, canBeNull = false, unique = true)
	private Region subRegion;
	
	public OMRegionRegion() {
		
	}
	
	public OMRegionRegion(Region superRegion, Region subRegion) {
		this.superRegion = superRegion;
		this.subRegion = subRegion;
	}

	public Region getSuperRegion() {
		return superRegion;
	}

	public void setSuperRegion(Region superRegion) {
		this.superRegion = superRegion;
	}

	public Region getSubRegion() {
		return subRegion;
	}

	public void setSubRegion(Region subRegion) {
		this.subRegion = subRegion;
	}
}
