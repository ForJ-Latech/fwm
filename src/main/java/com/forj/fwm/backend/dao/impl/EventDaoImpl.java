package com.forj.fwm.backend.dao.impl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.forj.fwm.backend.Backend;
import com.forj.fwm.backend.dao.EventDao;
import com.forj.fwm.entity.Event;
import com.forj.fwm.entity.God;
import com.forj.fwm.entity.Interaction;
import com.forj.fwm.entity.MMEventGod;
import com.forj.fwm.entity.MMEventNpc;
import com.forj.fwm.entity.MMEventStatblock;
import com.forj.fwm.entity.MMTemplateEvent;
import com.forj.fwm.entity.Npc;
import com.forj.fwm.entity.OMEventInteraction;
import com.forj.fwm.entity.Statblock;
import com.forj.fwm.entity.Template;
import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.SelectArg;
import com.j256.ormlite.support.ConnectionSource;

public class EventDaoImpl extends BaseDaoImpl<Event,String> implements EventDao {
	public EventDaoImpl(ConnectionSource connectionSource) throws SQLException {
		super(connectionSource, Event.class);
	}
	
	public List<Event> queryForLike(String arg0, Object arg1) throws SQLException {
		PreparedQuery<Event> preparedQuery;
		if (arg1 instanceof Integer) {
			preparedQuery = this.queryBuilder().where().like(arg0, arg1).prepare();
		} else if (arg0.equals("name")) {
			preparedQuery = this.queryBuilder().where().like("ignoreCaseName", new SelectArg("%" + arg1 + "%")).prepare();
		} else {
			preparedQuery = this.queryBuilder().where().like(arg0, new SelectArg("%" + arg1 + "%")).prepare();
		}
		return this.query(preparedQuery);
	}

	public Event getEvent(int id) throws SQLException {
		List<Event> event = this.queryForEq("ID", id);
		if (event != null && !event.isEmpty()) {
			return event.get(0);
		}
		return null;
	}

	public Event getFullEvent(int id) throws SQLException {
		Event event = this.getEvent(id);
		if (event == null) {
			return null;
		}
		event.setFull(true);
		
		for (OMEventInteraction relation : Backend.getOmEventInteractionDao().queryForEq("event_id", event.getID())) {
			event.getInteractions().add(Backend.getInteractionDao().queryForId("" + relation.getInteraction().getID()));
		}
		
		for (MMEventStatblock relation : Backend.getMmEventStatblockDao().queryForEq("event_id", event.getID())) {
			event.getStatblocks().add(Backend.getStatblockDao().queryForId("" + relation.getStatblock().getID()));
		}
		
		for (MMEventNpc relation : Backend.getMmEventNpcDao().queryForEq("event_id", event.getID())) {
			event.getNpcs().add(Backend.getNpcDao().queryForId("" + relation.getNpc().getID()));
		}
		
		for (MMEventGod relation : Backend.getMmEventGodDao().queryForEq("event_id", event.getID())) {
			event.getGods().add(Backend.getGodDao().queryForId("" + relation.getGod().getID()));
		}
		
		for (MMTemplateEvent relation : Backend.getMmTemplateEventDao().queryForEq("event_id", event.getID())) {
			event.getTemplates().add(Backend.getTemplateDao().queryForId("" + relation.getTemplate().getID()));
		}
		
		return event;
	}

	public void saveFullEvent(Event event) throws SQLException {
		this.createOrUpdateWLE(event);
		this.saveRelationalEvent(event);
	}
	
	public void saveRelationalEvent(Event event) throws SQLException {
		if (event.getInteractions() != null && !event.getInteractions().isEmpty()) {
			List<OMEventInteraction> relations = new ArrayList<OMEventInteraction>();
			for (Interaction interaction : event.getInteractions()) {
				Backend.getInteractionDao().createOrUpdate(interaction);
				relations.add(new OMEventInteraction(event, interaction));
			}
			for (OMEventInteraction relation : relations) {
				Backend.getOmEventInteractionDao().save(relation);
			}
		}
		
		if (event.getStatblocks() != null && !event.getStatblocks().isEmpty()) {
			List<MMEventStatblock> relations = new ArrayList<MMEventStatblock>();
			for (Statblock statblock : event.getStatblocks()) {
				Backend.getStatblockDao().createIfNotExists(statblock);
				relations.add(new MMEventStatblock(event, statblock));
			}
			for (MMEventStatblock relation : relations) {
				Backend.getMmEventStatblockDao().save(relation);
			}
		}
		
		if (event.getNpcs() != null && !event.getNpcs().isEmpty()) {
			List<MMEventNpc> relations = new ArrayList<MMEventNpc>();
			for (Npc npc : event.getNpcs()) {
				Backend.getNpcDao().createIfNotExists(npc);
				relations.add(new MMEventNpc(event, npc));
			}
			for (MMEventNpc relation : relations) {
				Backend.getMmEventNpcDao().save(relation);
			}
		}
		
		if (event.getGods() != null && !event.getGods().isEmpty()) {
			List<MMEventGod> relations = new ArrayList<MMEventGod>();
			for (God god : event.getGods()) {
				Backend.getGodDao().createIfNotExists(god);
				relations.add(new MMEventGod(event, god));
			}
			for (MMEventGod relation : relations) {
				Backend.getMmEventGodDao().save(relation);
			}
		}
		
		if (event.getTemplates() != null && !event.getTemplates().isEmpty()) {
			List<MMTemplateEvent> relations = new ArrayList<MMTemplateEvent>();
			for (Template template : event.getTemplates()) {
				Backend.getTemplateDao().createIfNotExists(template);
				relations.add(new MMTemplateEvent(template, event));
			}
			for (MMTemplateEvent relation : relations) {
				Backend.getMmTemplateEventDao().save(relation);
			}
		}
		
		updateFullEvent(event);
	}
	
	private void updateFullEvent (Event event) throws SQLException{
		List<OMEventInteraction> interactions = Backend.getOmEventInteractionDao().queryForEq("event_id", event.getID());
		List<MMEventStatblock> statblocks = Backend.getMmEventStatblockDao().queryForEq("event_id", event.getID());
		List<MMEventNpc> npcs = Backend.getMmEventNpcDao().queryForEq("event_id", event.getID());
		List<MMEventGod> gods = Backend.getMmEventGodDao().queryForEq("event_id", event.getID());
		List<MMTemplateEvent> templates = Backend.getMmTemplateEventDao().queryForEq("event_id", event.getID());
		
		for (int i = 0; i < interactions.size(); i++) {
			OMEventInteraction relation = interactions.get(i);
			for (Interaction interaction : event.getInteractions()) {
				if (interaction.getID() == relation.getInteraction().getID()) {
					interactions.remove(i);
					i--;
					break;
				}
			}
		}
		for (OMEventInteraction i : interactions) {
			Backend.getOmEventInteractionDao().delete(i);
		}
		
		for (int i = 0; i < statblocks.size(); i++) {
			MMEventStatblock relation = statblocks.get(i);
			for (Statblock statblock : event.getStatblocks()) {
				if (statblock.getID() == relation.getStatblock().getID()) {
					statblocks.remove(i);
					i--;
					break;
				}
			}
		}
		for (MMEventStatblock s : statblocks) {
				Backend.getMmEventStatblockDao().delete(s);
		}
		
		for (int i = 0; i < npcs.size(); i++) {
			MMEventNpc relation = npcs.get(i);
			for (Npc npc : event.getNpcs()) {
				if (npc.getID() == relation.getNpc().getID()) {
					npcs.remove(i);
					i--;
					break;
				}
			}
		}
		for (MMEventNpc n : npcs) {
			Backend.getMmEventNpcDao().delete(n);
		}

		for (int i = 0; i < gods.size(); i++) {
			MMEventGod relation = gods.get(i);
			for (God god : event.getGods()) {
				if (god.getID() == relation.getGod().getID()) {
					gods.remove(i);
					i--;
					break;
				}
			}
		}
		for (MMEventGod g : gods) {
			Backend.getMmEventGodDao().delete(g);
		}
		
		for (int i = 0; i < templates.size(); i++) {
			MMTemplateEvent relation = templates.get(i);
			for (Template template : event.getTemplates()) {
				if (template.getID() == relation.getTemplate().getID()) {
					templates.remove(i);
					i--;
					break;
				}
			}
		}
		for (MMTemplateEvent t : templates) {
			Backend.getMmTemplateEventDao().delete(t);
		}
	}
	
	// WLE -> With Last Edited
	public CreateOrUpdateStatus createOrUpdateWLE(Event e) throws SQLException {
		e.setLastEdited(new Date());
		return super.createOrUpdate(e);
	}
}
