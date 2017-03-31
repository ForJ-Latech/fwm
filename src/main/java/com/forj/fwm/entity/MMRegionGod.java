package com.forj.fwm.entity;

import com.forj.fwm.backend.dao.impl.MMRegionGodDaoImpl;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName="MM_REGION_GOD", daoClass = MMRegionGodDaoImpl.class)
public class MMRegionGod {
	@DatabaseField(foreign = true, canBeNull = false, uniqueCombo = true)
	private God god;
	
	@DatabaseField(foreign = true, canBeNull = false, uniqueCombo = true)
	private Region region;
	
	public MMRegionGod() {
		
	}
	
	public God getGod() {
		return god;
	}

	public void setGod(God god) {
		this.god = god;
	}

	public Region getRegion() {
		return region;
	}

	public void setRegion(Region region) {
		this.region = region;
	}	
}