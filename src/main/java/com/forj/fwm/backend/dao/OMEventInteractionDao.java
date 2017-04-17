package com.forj.fwm.backend.dao;

import java.sql.SQLException;
import java.util.List;

import com.forj.fwm.entity.OMEventInteraction;
import com.j256.ormlite.dao.Dao;

public interface OMEventInteractionDao extends Dao<OMEventInteraction,String> {
	public List<OMEventInteraction> queryForLike(String arg0, Object arg1) throws SQLException;
	public void save(OMEventInteraction relation) throws SQLException;
}
