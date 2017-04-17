package com.forj.fwm.entity;

import com.forj.fwm.backend.dao.impl.OMRegionInteractionDaoImpl;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName="OM_REGION_INTERACTION", daoClass = OMRegionInteractionDaoImpl.class)
public class OMRegionInteraction {
	@DatabaseField(foreign=true, canBeNull=false)
	private Region region;
	
	@DatabaseField(foreign=true, canBeNull=false, unique = true)
	private Interaction interaction;
	
	public OMRegionInteraction(){
		
	}
	
	public OMRegionInteraction(Region region, Interaction interaction) {
		this.region = region;
		this.interaction = interaction;
	}

	public Region getRegion() {
		return region;
	}

	public void setRegion(Region region) {
		this.region = region;
	}

	public Interaction getInteraction() {
		return interaction;
	}

	public void setInteraction(Interaction interaction) {
		this.interaction = interaction;
	}
}
