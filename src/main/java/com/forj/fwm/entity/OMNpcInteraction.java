package com.forj.fwm.entity;

import com.forj.fwm.backend.dao.impl.OMNpcInteractionDaoImpl;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName="OM_NPC_INTERACTION", daoClass = OMNpcInteractionDaoImpl.class)
public class OMNpcInteraction {
	@DatabaseField(foreign=true, canBeNull=false)
	private Npc npc;
	
	@DatabaseField(foreign=true, canBeNull=false, unique = true)
	private Interaction interaction;

	public OMNpcInteraction(){
		
	}

	public Npc getNpc() {
		return npc;
	}

	public void setNpc(Npc npc) {
		this.npc = npc;
	}

	public Interaction getInteraction() {
		return interaction;
	}

	public void setInteraction(Interaction interaction) {
		this.interaction = interaction;
	}
}
