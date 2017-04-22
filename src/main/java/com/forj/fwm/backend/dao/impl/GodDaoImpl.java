package com.forj.fwm.backend.dao.impl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.forj.fwm.backend.Backend;
import com.forj.fwm.backend.dao.GodDao;
import com.forj.fwm.entity.Event;
import com.forj.fwm.entity.God;
import com.forj.fwm.entity.Interaction;
import com.forj.fwm.entity.MMEventGod;
import com.forj.fwm.entity.MMRegionGod;
import com.forj.fwm.entity.Npc;
import com.forj.fwm.entity.OMGodInteraction;
import com.forj.fwm.entity.Region;
import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.SelectArg;
import com.j256.ormlite.support.ConnectionSource;

public class GodDaoImpl extends BaseDaoImpl<God,String> implements GodDao {
	public GodDaoImpl(ConnectionSource connectionSource) throws SQLException {
		super(connectionSource, God.class);
	}
	
	public List<God> queryForLike(String arg0, Object arg1) throws SQLException {
		PreparedQuery<God> preparedQuery;
		if (arg1 instanceof Integer){
			preparedQuery = this.queryBuilder().where().like(arg0, arg1).prepare();
		} else {
			preparedQuery = this.queryBuilder().where().like(arg0, new SelectArg("%" + arg1 + "%")).prepare();
		}
		return this.query(preparedQuery);
	}
	
	public God getGod(int id) throws SQLException {
		List<God> god = this.queryForEq("ID", id);
		if (god != null && !god.isEmpty()) {
			return god.get(0);
		}
		return null;
	}

	public God getFullGod(int id) throws SQLException {
		God god = this.getGod(id);
		if (god == null) {
			return null;
		}
		god.setFull(true);
		
		for (OMGodInteraction relation : Backend.getOmGodInteractionDao().queryForEq("god_id", god.getID())) {
			god.getInteractions().add(Backend.getInteractionDao().queryForId("" + relation.getInteraction().getID()));
		}
		
		for (MMRegionGod relation : Backend.getMmRegionGodDao().queryForEq("god_id", god.getID())) {
			god.getRegions().add(Backend.getRegionDao().queryForId("" + relation.getRegion().getID()));
		}
		
		for (MMEventGod relation : Backend.getMmEventGodDao().queryForEq("god_id", god.getID())) {
			god.getEvents().add(Backend.getEventDao().queryForId("" + relation.getEvent().getID()));
		}
		
		for (Npc npc : Backend.getNpcDao().queryForEq("god_id", god.getID())) {
			god.getNpcs().add(npc);
		}
		
		return god;
	}
	
	public void saveRelationalGod(God god) throws SQLException{
		if (god.getInteractions() != null && !god.getInteractions().isEmpty()) {
			List<OMGodInteraction> relations = new ArrayList<OMGodInteraction>();
			for (Interaction interaction : god.getInteractions()) {
				Backend.getInteractionDao().createOrUpdate(interaction);
				relations.add(new OMGodInteraction(god, interaction));
			}
			for (OMGodInteraction relation : relations) {
				Backend.getOmGodInteractionDao().save(relation);
			}
		}
		
		if (god.getRegions() != null && !god.getRegions().isEmpty()) {
			List<MMRegionGod> relations = new ArrayList<MMRegionGod>();
			for (Region region : god.getRegions()) {
				Backend.getRegionDao().createOrUpdate(region);
				relations.add(new MMRegionGod(region, god));
			}
			for (MMRegionGod relation : relations) {
				Backend.getMmRegionGodDao().save(relation);
			}
		}
		
		if (god.getEvents() != null && !god.getEvents().isEmpty()) {
			List<MMEventGod> relations = new ArrayList<MMEventGod>();
			for (Event event : god.getEvents()) {
				Backend.getEventDao().createOrUpdate(event);
				relations.add(new MMEventGod(event, god));
			}
			for (MMEventGod relation : relations) {
				Backend.getMmEventGodDao().save(relation);
			}
		}
		
		if (god.getNpcs() != null && !god.getNpcs().isEmpty()) {
			for (Npc npc : god.getNpcs()) {
				npc.setGod(god);
				Backend.getNpcDao().createOrUpdate(npc);
			}
		}
		
		updateFullGod(god);
	}
	
	public void saveFullGod(God god) throws SQLException {
		this.createOrUpdate(god);
		saveRelationalGod(god);
	}
	
	private void updateFullGod (God god) throws SQLException{
		List<OMGodInteraction> interactions = Backend.getOmGodInteractionDao().queryForEq("god_id", god.getID());
		List<MMRegionGod> regions = Backend.getMmRegionGodDao().queryForEq("god_id", god.getID());
		List<MMEventGod> events = Backend.getMmEventGodDao().queryForEq("god_id", god.getID());
		List<Npc> npcs = Backend.getNpcDao().queryForEq("god_id", god.getID());
		
		for (int i = 0; i < interactions.size(); i++) {
			OMGodInteraction relation = interactions.get(i);
			for (Interaction interaction : god.getInteractions()) {
				if (interaction.getID() == relation.getInteraction().getID()) {
					interactions.remove(i);
					i--;
					break;
				}
			}
		}
		for (OMGodInteraction i : interactions) {
			Backend.getOmGodInteractionDao().delete(i);
		}
		
		for (int i = 0; i < regions.size(); i++) {
			MMRegionGod relation = regions.get(i);
			for (Region region : god.getRegions()) {
				if (region.getID() == relation.getRegion().getID()) {
					regions.remove(i);
					i--;
					break;
				}
			}
		}
		for (MMRegionGod r : regions) {
			Backend.getMmRegionGodDao().delete(r);
		}
		
		for (int i = 0; i < events.size(); i++) {
			MMEventGod relation = events.get(i);
			for (Event event : god.getEvents()) {
				if (event.getID() == relation.getEvent().getID()) {
					events.remove(i);
					i--;
					break;
				}
			}
		}
		for (MMEventGod e : events) {
			Backend.getMmEventGodDao().delete(e);
		}

		for (int i = 0; i < npcs.size(); i++) {
			Npc npc1 = npcs.get(i);
			for (Npc npc2 : god.getNpcs()) {
				if (npc2.getID() == npc1.getID()) {
					npcs.remove(i);
					i--;
					break;
				}
			}
		}
		for (Npc n : npcs) {
			n.setGod(null);
			Backend.getNpcDao().update(n);
		}
	}
	
	@Override
	public int update(God g) throws SQLException {
		g.setLastEdited(new Date());
		return super.update(g);
	}
	
	@Override
	public CreateOrUpdateStatus createOrUpdate(God g) throws SQLException {
		g.setLastEdited(new Date());
		return super.createOrUpdate(g);
	}
}
