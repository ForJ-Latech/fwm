package com.forj.fwm.backend.dao.impl;

import java.sql.SQLException;
import java.util.List;

import com.forj.fwm.backend.dao.EventDao;
import com.forj.fwm.entity.Event;
import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.support.ConnectionSource;

public class EventDaoImpl extends BaseDaoImpl<Event,String> implements EventDao{
	public EventDaoImpl(ConnectionSource connectionSource) throws SQLException{
		super(connectionSource, Event.class);
	}
	
	public List<Event> queryForLike(String arg0, Object arg1) throws SQLException {
		PreparedQuery<Event> preparedQuery = this.queryBuilder().where().like(arg0, "%" + arg1 + "%").prepare();
		return this.query(preparedQuery);
	}
}
