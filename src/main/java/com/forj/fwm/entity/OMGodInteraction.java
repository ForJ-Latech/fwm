package com.forj.fwm.entity;

import com.j256.ormlite.table.*;
import com.forj.fwm.backend.dao.impl.OMGodInteractionDaoImpl;
import com.j256.ormlite.field.*;

@DatabaseTable(tableName="OM_GOD_INTERACTION", daoClass = OMGodInteractionDaoImpl.class)
public class OMGodInteraction {
	@DatabaseField(foreign=true, canBeNull=false)
	private God god;
	
	@DatabaseField(foreign=true, canBeNull=false, unique = true)
	private Interaction interaction;

	public OMGodInteraction(){
		
	}

	public God getGod() {
		return god;
	}

	public void setGod(God god) {
		this.god = god;
	}

	public Interaction getInteraction() {
		return interaction;
	}

	public void setInteraction(Interaction interaction) {
		this.interaction = interaction;
	}
}
