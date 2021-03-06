package com.forj.fwm.backend.dao.impl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import com.forj.fwm.backend.Backend;
import com.forj.fwm.backend.dao.RegionDao;
import com.forj.fwm.entity.Event;
import com.forj.fwm.entity.God;
import com.forj.fwm.entity.Interaction;
import com.forj.fwm.entity.MMRegionGod;
import com.forj.fwm.entity.MMRegionNpc;
import com.forj.fwm.entity.MMTemplateRegion;
import com.forj.fwm.entity.Npc;
import com.forj.fwm.entity.OMRegionInteraction;
import com.forj.fwm.entity.OMRegionRegion;
import com.forj.fwm.entity.Region;
import com.forj.fwm.entity.Template;
import com.forj.fwm.gui.tab.RegionTabController;
import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.SelectArg;
import com.j256.ormlite.support.ConnectionSource;

public class RegionDaoImpl  extends BaseDaoImpl<Region,String> implements RegionDao {
	private static Logger log = Logger.getLogger(RegionDaoImpl.class);
	
	public RegionDaoImpl(ConnectionSource connectionSource) throws SQLException {
		super(connectionSource, Region.class);
	}
	
	public List<Region> queryForLike(String arg0, Object arg1) throws SQLException {
		PreparedQuery<Region> preparedQuery;
		if (arg1 instanceof Integer) {
			preparedQuery = this.queryBuilder().where().like(arg0, arg1).prepare();
		} else if (arg0.equals("name")) {
			preparedQuery = this.queryBuilder().where().like("ignoreCaseName", new SelectArg("%" + arg1 + "%")).prepare();
		} else {
			preparedQuery = this.queryBuilder().where().like(arg0, new SelectArg("%" + arg1 + "%")).prepare();
		}
		return this.query(preparedQuery);
	}
	
	public Region getRegion(int id) throws SQLException {
		List<Region> region = this.queryForEq("ID", id);
		if (region != null && !region.isEmpty()) {
			return region.get(0);
		}
		return null;
	}

	public Region getFullRegion(int id) throws SQLException {
		Region region = this.getRegion(id);
		if (region == null) {
			return null;
		}
		region.setFull(true);
		
		for (MMRegionNpc relation : Backend.getMmRegionNpcDao().queryForEq("region_id", region.getID())) {
			region.getNpcs().add(Backend.getNpcDao().queryForId("" + relation.getNpc().getID()));
		}
		
		for (MMRegionGod relation : Backend.getMmRegionGodDao().queryForEq("region_id", region.getID())) {
			region.getGods().add(Backend.getGodDao().queryForId("" + relation.getGod().getID()));
		}
		
		for (MMTemplateRegion relation : Backend.getMmTemplateRegionDao().queryForEq("region_id", region.getID())) {
			region.getTemplates().add(Backend.getTemplateDao().queryForId("" + relation.getTemplate().getID()));
		}
		
		for (OMRegionInteraction relation : Backend.getOmRegionInteractionDao().queryForEq("region_id", region.getID())) {
			region.getInteractions().add(Backend.getInteractionDao().queryForId("" + relation.getInteraction().getID()));
		}
		
		for (OMRegionRegion relation : Backend.getOmRegionRegionDao().queryForEq("superRegion_id", region.getID())) {
			region.getSubRegions().add(Backend.getRegionDao().queryForId("" + relation.getSubRegion().getID()));
		}
		
		for (Event event : Backend.getEventDao().queryForEq("region_id", region.getID())) {
			region.getEvents().add(event);
		}
		
		List<OMRegionRegion> superRegion = Backend.getOmRegionRegionDao().queryForEq("subRegion_id", region.getID());
		if (superRegion != null && !superRegion.isEmpty()) {
			region.setSuperRegion(Backend.getRegionDao().getRegion(superRegion.get(0).getSuperRegion().getID()));
		}
		
		return region;
	}
	
	public void saveFullRegion(Region region) throws SQLException {
		this.createOrUpdateWLE(region);
		this.saveRelationalRegion(region);
	}

	public void saveRelationalRegion(Region region) throws SQLException{
		if (region.getNpcs() != null && !region.getNpcs().isEmpty()) {
			List<MMRegionNpc> relations = new ArrayList<MMRegionNpc>();
			for (Npc npc : region.getNpcs()) {
				Backend.getNpcDao().createIfNotExists(npc);
				relations.add(new MMRegionNpc(region, npc));
			}
			for (MMRegionNpc relation : relations) {
				Backend.getMmRegionNpcDao().save(relation);
			}
		}
		
		if (region.getGods() != null && !region.getGods().isEmpty()) {
			List<MMRegionGod> relations = new ArrayList<MMRegionGod>();
			for (God god : region.getGods()) {
				Backend.getGodDao().createIfNotExists(god);
				relations.add(new MMRegionGod(region, god));
			}
			for (MMRegionGod relation : relations) {
				Backend.getMmRegionGodDao().save(relation);
			}
		}
		
		if (region.getInteractions() != null && !region.getInteractions().isEmpty()) {
			List<OMRegionInteraction> relations = new ArrayList<OMRegionInteraction>();
			for (Interaction interaction : region.getInteractions()) {
				Backend.getInteractionDao().createOrUpdate(interaction);
				relations.add(new OMRegionInteraction(region, interaction));
			}
			for (OMRegionInteraction relation : relations) {
				Backend.getOmRegionInteractionDao().save(relation);
			}
		}
		
		// if this link was presently inside of something, we ought to remove it 
		// it makes altering the tree through insertion MUCH easier this way. 
		// that requires checking if the other things had different super regions and changing it.
		if (region.getSubRegions() != null && !region.getSubRegions().isEmpty()) {
			List<OMRegionRegion> relations = new ArrayList<OMRegionRegion>();
			
			for (Region subRegion : region.getSubRegions()) {
				
				Backend.getRegionDao().createIfNotExists(subRegion);
				List<OMRegionRegion> tempOMs = Backend.getOmRegionRegionDao().queryForEq("subRegion_id", String.valueOf(subRegion.getID()));
				OMRegionRegion subRegionOM = tempOMs.isEmpty() ? null: tempOMs.get(0);
				log.debug("tempOMs size: " + tempOMs.size());
				if(subRegionOM == null){
					Backend.getOmRegionRegionDao().save(new OMRegionRegion(region, subRegion));
				}
				else if(subRegionOM.getSuperRegion().getID() == region.getID()){
					// pass because this link already existed
				}
				else{
					// this link got altered, and we need to 'change' it.
					// ORMLite sucks, just delete and save new. 
					Backend.getOmRegionRegionDao().delete(subRegionOM);
					Backend.getOmRegionRegionDao().save(new OMRegionRegion(region, subRegion));
					log.debug("saving OM subregions: " + region.getID() + ":" + subRegion.getID());
				}
			}
		}
		
		if (region.getTemplates() != null && !region.getTemplates().isEmpty()) {
			List<MMTemplateRegion> relations = new ArrayList<MMTemplateRegion>();
			for (Template template : region.getTemplates()) {
				Backend.getTemplateDao().createIfNotExists(template);
				relations.add(new MMTemplateRegion(template, region));
			}
			for (MMTemplateRegion relation : relations) {
				Backend.getMmTemplateRegionDao().save(relation);
			}
		}
		
		if (region.getEvents() != null && !region.getEvents().isEmpty()) {
			for (Event event : region.getEvents()) {
				event.setRegion(region);
				Backend.getEventDao().createOrUpdate(event);
			}
		}
		
		List<OMRegionRegion> rr = Backend.getOmRegionRegionDao().queryForEq("subRegion_id", region.getID());
		if (!rr.isEmpty()) {
			Backend.getOmRegionRegionDao().delete(rr.get(0));
		}
		if (region.getSuperRegion() != null) {
			Backend.getOmRegionRegionDao().save(new OMRegionRegion(region.getSuperRegion(), region));
		}
		
		updateFullRegion(region);
	}
	
	private void updateFullRegion (Region region) throws SQLException{
		List<MMRegionNpc> npcs = Backend.getMmRegionNpcDao().queryForEq("region_id", region.getID());
		List<MMRegionGod> gods = Backend.getMmRegionGodDao().queryForEq("region_id", region.getID());
		List<MMTemplateRegion> templates = Backend.getMmTemplateRegionDao().queryForEq("region_id", region.getID());
		List<OMRegionInteraction> interactions = Backend.getOmRegionInteractionDao().queryForEq("region_id", region.getID());
		List<OMRegionRegion> subRegions = Backend.getOmRegionRegionDao().queryForEq("superRegion_id", region.getID());
		
		for (int i = 0; i < npcs.size(); i++) {
			MMRegionNpc relation = npcs.get(i);
			for (Npc npc : region.getNpcs()) {
				if (npc.getID() == relation.getNpc().getID()) {
					npcs.remove(i);
					i--;
					break;
				}
			}
		}
		for (MMRegionNpc n : npcs) {
			Backend.getMmRegionNpcDao().delete(n);
		}
		
		for (int i = 0; i < gods.size(); i++) {
			MMRegionGod relation = gods.get(i);
			for (God god : region.getGods()) {
				if (god.getID() == relation.getGod().getID()) {
					gods.remove(i);
					i--;
					break;
				}
			}
		}
		for (MMRegionGod g : gods) {
			Backend.getMmRegionGodDao().delete(g);
		}
		
		for (int i = 0; i < templates.size(); i++) {
			MMTemplateRegion relation = templates.get(i);
			for (Template template : region.getTemplates()) {
				if (template.getID() == relation.getTemplate().getID()) {
					templates.remove(i);
					i--;
					break;
				}
			}
		}
		for (MMTemplateRegion t : templates) {
			Backend.getMmTemplateRegionDao().delete(t);
		}
		
		for (int i = 0; i < interactions.size(); i++) {
			OMRegionInteraction relation = interactions.get(i);
			for (Interaction interaction : region.getInteractions()) {
				if (interaction.getID() == relation.getInteraction().getID()) {
					interactions.remove(i);
					i--;
					break;
				}
			}
		}
		for (OMRegionInteraction i : interactions) {
			Backend.getOmRegionInteractionDao().delete(i);
		}
		
		for (int i = 0; i < subRegions.size(); i++) {
			OMRegionRegion relation = subRegions.get(i);
			for (Region subRegion : region.getSubRegions()) {
				if (subRegion.getID() == relation.getSubRegion().getID()) {
					subRegions.remove(i);
					i--;
					break;
				}
			}
		}
		for (OMRegionRegion r : subRegions) {
			Backend.getOmRegionRegionDao().delete(r);
		}
	}
	
	public List<Region> getAboveFullRegions(Region region) throws SQLException {
		List<Region> aboveRegions = new ArrayList<Region>();
		Region r = region;
		while (r.getSuperRegion() != null) {
			r = getFullRegion(r.getSuperRegion().getID());
			if(r == null){
				// occasionally breaking shit... =(
				break;
			}
			aboveRegions.add(r);
		}
		return aboveRegions;
	}
	
	public List<Region> getBelowFullRegions(Region region) throws SQLException {
		List<Region> belowRegions = new ArrayList<Region>();
		for (Region r : region.getSubRegions()) {
			Region fr = this.getFullRegion(r.getID());
			belowRegions.add(fr);
			belowRegions.addAll(getBelowFullRegions(fr));
		}
		 
		return belowRegions;
	}
	

	public List<Integer> createAboveBelowTree(Region region) {

		List<Integer> allregID = new ArrayList<Integer>();
		allregID.add(region.getID());
		
		
		try {
			List<Region> aboveFull =  getAboveFullRegions(region);
			aboveFull.add(region);
			for (Region reg : aboveFull){
				
				if (!allregID.contains(reg.getID())){
					allregID.add(reg.getID());
				}
				
				for (Region reg2 : getBelowFullRegions(reg)){
					if (!allregID.contains(reg2.getID())){
						allregID.add(reg2.getID());
					}
				}				
			}
			
		} catch (SQLException e) {
			log.error(e);
		}
		
		return allregID;
	}
	
	// WLE -> With Last Edited
	public CreateOrUpdateStatus createOrUpdateWLE(Region r) throws SQLException {
		r.setLastEdited(new Date());
		return super.createOrUpdate(r);
	}
}
