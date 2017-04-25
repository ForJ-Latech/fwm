package com.forj.fwm.backend.dao.impl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.forj.fwm.backend.Backend;
import com.forj.fwm.backend.dao.TemplateDao;
import com.forj.fwm.entity.Event;
import com.forj.fwm.entity.God;
import com.forj.fwm.entity.MMTemplateEvent;
import com.forj.fwm.entity.MMTemplateGod;
import com.forj.fwm.entity.MMTemplateRegion;
import com.forj.fwm.entity.Region;
import com.forj.fwm.entity.Template;
import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.SelectArg;
import com.j256.ormlite.support.ConnectionSource;

public class TemplateDaoImpl extends BaseDaoImpl<Template,String> implements TemplateDao {
	public TemplateDaoImpl(ConnectionSource connectionSource) throws SQLException {
		super(connectionSource, Template.class);
	}

	public List<Template> queryForLike(String arg0, Object arg1) throws SQLException {
		PreparedQuery<Template> preparedQuery;
		if (arg1 instanceof Integer) {
			preparedQuery = this.queryBuilder().where().like(arg0, arg1).prepare();
		} else if (arg0.equals("name")) {
			preparedQuery = this.queryBuilder().where().like("ignoreCaseName", new SelectArg("%" + arg1 + "%")).prepare();
		} else {
			preparedQuery = this.queryBuilder().where().like(arg0, new SelectArg("%" + arg1 + "%")).prepare();
		}
		return this.query(preparedQuery);
	}

	public Template getTemplate(int id) throws SQLException {
		List<Template> template = this.queryForEq("ID", id);
		if (template != null && !template.isEmpty()) {
			return template.get(0);
		}
		return null;
	}

	public Template getFullTemplate(int id) throws SQLException {
		Template template = this.getTemplate(id);
		if (template == null) {
			return null;
		}
		
		for (MMTemplateEvent relation : Backend.getMmTemplateEventDao().queryForEq("template_id", template.getID())) {
			template.getEvents().add(Backend.getEventDao().queryForId("" + relation.getEvent().getID()));
		}
		
		for (MMTemplateGod relation : Backend.getMmTemplateGodDao().queryForEq("template_id", template.getID())) {
			template.getGods().add(Backend.getGodDao().queryForId("" + relation.getGod().getID()));
		}
		
		for (MMTemplateRegion relation : Backend.getMmTemplateRegionDao().queryForEq("template_id", template.getID())) {
			template.getRegions().add(Backend.getRegionDao().queryForId("" + relation.getRegion().getID()));
		}
		
		return template;
	}

	public void saveFullTemplate(Template template) throws SQLException {
		this.create(template);
		
		if (template.getEvents() != null && !template.getEvents().isEmpty()) {
			List<MMTemplateEvent> relations = new ArrayList<MMTemplateEvent>();
			for (Event event : template.getEvents()) {
				Backend.getEventDao().createOrUpdate(event);
				relations.add(new MMTemplateEvent(template, event));
			}
			for (MMTemplateEvent relation : relations) {
				Backend.getMmTemplateEventDao().save(relation);
			}
		}
		
		if (template.getGods() != null && !template.getGods().isEmpty()) {
			List<MMTemplateGod> relations = new ArrayList<MMTemplateGod>();
			for (God god : template.getGods()) {
				Backend.getGodDao().createOrUpdate(god);
				relations.add(new MMTemplateGod(template, god));
			}
			for (MMTemplateGod relation : relations) {
				Backend.getMmTemplateGodDao().save(relation);
			}
		}
		
		if (template.getRegions() != null && !template.getRegions().isEmpty()) {
			List<MMTemplateRegion> relations = new ArrayList<MMTemplateRegion>();
			for (Region region : template.getRegions()) {
				Backend.getRegionDao().createOrUpdate(region);
				relations.add(new MMTemplateRegion(template, region));
			}
			for (MMTemplateRegion relation : relations) {
				Backend.getMmTemplateRegionDao().save(relation);
			}
		}
		
		updateFullEvent(template);
	}
	
	private void updateFullEvent (Template template) throws SQLException{
		List<MMTemplateEvent> events = Backend.getMmTemplateEventDao().queryForEq("template_id", template.getID());
		List<MMTemplateGod> gods = Backend.getMmTemplateGodDao().queryForEq("template_id", template.getID());
		List<MMTemplateRegion> regions = Backend.getMmTemplateRegionDao().queryForEq("template_id", template.getID());
		
		for (int i = 0; i < events.size(); i++) {
			MMTemplateEvent relation = events.get(i);
			for (Event event : template.getEvents()) {
				if (event.getID() == relation.getEvent().getID()) {
					events.remove(i);
					i--;
					break;
				}
			}
		}
		for (MMTemplateEvent i : events) {
			Backend.getMmTemplateEventDao().delete(i);
		}
		
		for (int i = 0; i < gods.size(); i++) {
			MMTemplateGod relation = gods.get(i);
			for (God god : template.getGods()) {
				if (god.getID() == relation.getGod().getID()) {
					gods.remove(i);
					i--;
					break;
				}
			}
		}
		for (MMTemplateGod e : gods) {
			Backend.getMmTemplateGodDao().delete(e);
		}
		
		for (int i = 0; i < regions.size(); i++) {
			MMTemplateRegion relation = regions.get(i);
			for (Region region : template.getRegions()) {
				if (region.getID() == relation.getRegion().getID()) {
					regions.remove(i);
					i--;
					break;
				}
			}
		}
		for (MMTemplateRegion r : regions) {
			Backend.getMmTemplateRegionDao().delete(r);
		}
	}

	@Override
	public int update(Template t) throws SQLException {
		t.setLastEdited(new Date());
		return super.update(t);
	}
	
	@Override
	public CreateOrUpdateStatus createOrUpdate(Template t) throws SQLException {
		t.setLastEdited(new Date());
		return super.createOrUpdate(t);
	}
}
