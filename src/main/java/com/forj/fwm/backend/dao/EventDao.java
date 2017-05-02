package com.forj.fwm.backend.dao;

import java.sql.SQLException;
import java.util.List;

import com.forj.fwm.entity.Event;
import com.j256.ormlite.dao.Dao;

public interface EventDao extends Dao<Event,String> {
	public List<Event> queryForLike(String arg0, Object arg1) throws SQLException;
	public Event getEvent(int id) throws SQLException;
	public Event getFullEvent(int id) throws SQLException;
	public void saveFullEvent(Event event) throws SQLException;
	public void saveRelationalEvent(Event event) throws SQLException;
	public CreateOrUpdateStatus createOrUpdateWLE(Event e) throws SQLException;
}
