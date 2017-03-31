package com.forj.fwm.backend.dao;

import java.sql.SQLException;
import java.util.List;

import com.forj.fwm.entity.Event;
import com.j256.ormlite.dao.Dao;

public interface EventDao extends Dao<Event,String> {
	public List<Event> queryForLike(String arg0, Object arg1) throws SQLException;
}
