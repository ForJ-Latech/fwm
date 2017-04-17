package com.forj.fwm.backend.dao.impl;

import java.sql.SQLException;
import java.util.List;

import com.forj.fwm.backend.dao.MMTemplateEventDao;
import com.forj.fwm.entity.MMTemplateEvent;
import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.support.ConnectionSource;

public class MMTemplateEventDaoImpl extends BaseDaoImpl<MMTemplateEvent,String> implements MMTemplateEventDao {
	public MMTemplateEventDaoImpl(ConnectionSource connectionSource) throws SQLException {
		super(connectionSource, MMTemplateEvent.class);
	}
	
	public List<MMTemplateEvent> queryForLike(String arg0, Object arg1) throws SQLException {
		PreparedQuery<MMTemplateEvent> preparedQuery;
		if (arg1 instanceof Integer){
			preparedQuery = this.queryBuilder().where().like(arg0, arg1).prepare();
		} else {
			preparedQuery = this.queryBuilder().where().like(arg0, "%" + arg1 + "%").prepare();
		}
		return this.query(preparedQuery);
	}
	
	public void save(MMTemplateEvent relation) throws SQLException {
		if (this.queryBuilder().where().eq("template_id", relation.getTemplate().getID()).and().eq("event_id", relation.getEvent().getID()).query().isEmpty()) {
			this.create(relation);
		}
	}
	
	@Override
	public int delete(MMTemplateEvent relation) throws SQLException {
		DeleteBuilder<MMTemplateEvent, String> deleteBuilder = this.deleteBuilder();
		deleteBuilder.where().eq("template_id", relation.getTemplate().getID()).and().eq("event_id", relation.getEvent().getID());
		deleteBuilder.delete();
		return 1;
	}
}
