package com.forj.fwm.entity;

import com.forj.fwm.backend.dao.impl.OMRegionRegionDaoImpl;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName="OM_REGION_REGION", daoClass = OMRegionRegionDaoImpl.class)
public class OMRegionRegion {
	@DatabaseField(foreign = true, canBeNull = false)
	private Region region;
	
	@DatabaseField(foreign = true, canBeNull = false, unique = true)
	private Region SubRegion;
	
	public OMRegionRegion() {
		
	}

	public Region getRegion() {
		return region;
	}

	public void setRegion(Region region) {
		this.region = region;
	}

	public Region getSubRegion() {
		return SubRegion;
	}

	public void setSubRegion(Region subRegion) {
		SubRegion = subRegion;
	}
}
