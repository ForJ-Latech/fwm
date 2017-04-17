package com.forj.fwm.backend.dao;

import java.sql.SQLException;
import java.util.List;

import com.forj.fwm.entity.Interaction;
import com.j256.ormlite.dao.Dao;

public interface InteractionDao extends Dao<Interaction,String> {
	public List<Interaction> queryForLike(String arg0, Object arg1) throws SQLException;
}
