package com.forj.fwm.entity;

import com.forj.fwm.backend.dao.impl.MMRegionNpcDaoImpl;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName="MM_REGION_NPC", daoClass = MMRegionNpcDaoImpl.class)
public class MMRegionNpc {
	@DatabaseField(foreign = true, canBeNull = false, uniqueCombo = true)
	private Npc npc;
	
	@DatabaseField(foreign = true, canBeNull = false, uniqueCombo = true)
	private Region region;
	
	public MMRegionNpc() {
		
	}
	
	public MMRegionNpc(Region region, Npc npc) {
		this.region = region;
		this.npc = npc;
	}
	
	public Npc getNpc() {
		return npc;
	}

	public void setNpc(Npc npc) {
		this.npc = npc;
	}

	public Region getRegion() {
		return region;
	}

	public void setRegion(Region region) {
		this.region = region;
	}
}
