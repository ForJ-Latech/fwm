package com.forj.fwm.backend.dao.impl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.forj.fwm.backend.Backend;
import com.forj.fwm.backend.dao.NpcDao;
import com.forj.fwm.entity.Event;
import com.forj.fwm.entity.Interaction;
import com.forj.fwm.entity.MMEventNpc;
import com.forj.fwm.entity.MMRegionNpc;
import com.forj.fwm.entity.Npc;
import com.forj.fwm.entity.OMNpcInteraction;
import com.forj.fwm.entity.Region;
import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.SelectArg;
import com.j256.ormlite.support.ConnectionSource;

public class NpcDaoImpl extends BaseDaoImpl<Npc,String> implements NpcDao {
	public NpcDaoImpl(ConnectionSource connectionSource) throws SQLException {
		super(connectionSource, Npc.class);
	}

	public List<Npc> queryForLike(String arg0, Object arg1) throws SQLException {
		PreparedQuery<Npc> preparedQuery;
		if (arg1 instanceof Integer) {
			preparedQuery = this.queryBuilder().where().like(arg0, arg1).prepare();
		} else if (arg0.equals("fName")) {
			preparedQuery = this.queryBuilder().where().like("ignoreCaseName", new SelectArg("%" + arg1 + "%")).prepare();
		} else {
			preparedQuery = this.queryBuilder().where().like(arg0, new SelectArg("%" + arg1 + "%")).prepare();
		}
		return this.query(preparedQuery);
	}
	
	public Npc getNpc(int id) throws SQLException {
		List<Npc> npc = this.queryForEq("ID", id);
		if (npc != null && !npc.isEmpty()) {
			return npc.get(0);
		}
		return null;
	}
	
	public Npc getFullNpc(int id) throws SQLException{
		Npc npc = this.getNpc(id);
		if (npc == null) {
			return null;
		}
		npc.setFull(true);
		
		for (OMNpcInteraction relation : Backend.getOmNpcInteractionDao().queryForEq("npc_id", npc.getID())) {
			npc.getInteractions().add(Backend.getInteractionDao().queryForId("" + relation.getInteraction().getID()));
		}
		
		for (MMRegionNpc relation : Backend.getMmRegionNpcDao().queryForEq("npc_id", npc.getID())) {
			npc.getRegions().add(Backend.getRegionDao().queryForId("" + relation.getRegion().getID()));
		}
		
		for (MMEventNpc relation : Backend.getMmEventNpcDao().queryForEq("npc_id", npc.getID())) {
			npc.getEvents().add(Backend.getEventDao().queryForId("" + relation.getEvent().getID()));
		}
		
		return npc;
	}
	
	public void saveFullNpc(Npc npc) throws SQLException {
		this.createOrUpdateWLE(npc);
		this.saveRelationalNpc(npc);
	}
	
	public void saveRelationalNpc(Npc npc) throws SQLException{
		if (npc.getInteractions() != null && !npc.getInteractions().isEmpty()) {
			List<OMNpcInteraction> relations = new ArrayList<OMNpcInteraction>();
			for (Interaction interaction : npc.getInteractions()) {
				Backend.getInteractionDao().createIfNotExists(interaction);
				relations.add(new OMNpcInteraction(npc, interaction));
			}
			for (OMNpcInteraction relation : relations) {
				Backend.getOmNpcInteractionDao().save(relation);
			}
		}
		
		if (npc.getRegions() != null && !npc.getRegions().isEmpty()) {
			List<MMRegionNpc> relations = new ArrayList<MMRegionNpc>();
			for (Region region : npc.getRegions()) {
				Backend.getRegionDao().createIfNotExists(region);
				relations.add(new MMRegionNpc(region, npc));
			}
			for (MMRegionNpc relation : relations) {
				Backend.getMmRegionNpcDao().save(relation);
			}
		}
		
		if (npc.getEvents() != null && !npc.getEvents().isEmpty()) {
			List<MMEventNpc> relations = new ArrayList<MMEventNpc>();
			for (Event event : npc.getEvents()) {
				Backend.getEventDao().createIfNotExists(event);
				relations.add(new MMEventNpc(event, npc));
			}
			for (MMEventNpc relation : relations) {
				Backend.getMmEventNpcDao().save(relation);
			}
		}
		
		updateFullNpc(npc);
	}
	
	private void updateFullNpc (Npc npc) throws SQLException{
		List<OMNpcInteraction> interactions = Backend.getOmNpcInteractionDao().queryForEq("npc_id", npc.getID());
		List<MMRegionNpc> regions = Backend.getMmRegionNpcDao().queryForEq("npc_id", npc.getID());
		List<MMEventNpc> events = Backend.getMmEventNpcDao().queryForEq("npc_id", npc.getID());
		
		for (int i = 0; i < interactions.size(); i++) {
			OMNpcInteraction relation = interactions.get(i);
			for (Interaction interaction : npc.getInteractions()) {
				if (interaction.getID() == relation.getInteraction().getID()) {
					interactions.remove(i);
					i--;
					break;
				}
			}
		}
		for (OMNpcInteraction i : interactions) {
			Backend.getOmNpcInteractionDao().delete(i);
		}
		
		for (int i = 0; i < regions.size(); i++) {
			MMRegionNpc relation = regions.get(i);
			for (Region region : npc.getRegions()) {
				if (region.getID() == relation.getRegion().getID()) {
					regions.remove(i);
					i--;
					break;
				}
			}
		}
		for (MMRegionNpc r : regions) {
			Backend.getMmRegionNpcDao().delete(r);
		}
		
		for (int i = 0; i < events.size(); i++) {
			MMEventNpc relation = events.get(i);
			for (Event event : npc.getEvents()) {
				if (event.getID() == relation.getEvent().getID()) {
					events.remove(i);
					i--;
					break;
				}
			}
		}
		for (MMEventNpc e : events) {
			Backend.getMmEventNpcDao().delete(e);
		}
	}
	
	public ArrayList<Npc> getFamily(Npc npc){
		ArrayList<Npc> fam = new ArrayList<Npc>();
		if (npc.getlName() != null)
		{
			try {
				for (Npc g : this.queryForEq("lName", new SelectArg(npc.getlName()))){
					if (g.getID() != npc.getID()){
						fam.add(g);
					}
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return fam;
	}
	
	// WLE -> With Last Edited
	public CreateOrUpdateStatus createOrUpdateWLE(Npc n) throws SQLException {
		n.setLastEdited(new Date());
		return super.createOrUpdate(n);
	}
}
