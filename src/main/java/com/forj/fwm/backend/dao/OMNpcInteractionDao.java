package com.forj.fwm.backend.dao;

import java.sql.SQLException;
import java.util.List;

import com.forj.fwm.entity.OMNpcInteraction;
import com.j256.ormlite.dao.Dao;

public interface OMNpcInteractionDao extends Dao<OMNpcInteraction,String>{
	public List<OMNpcInteraction> queryForLike(String arg0, Object arg1) throws SQLException;
}
