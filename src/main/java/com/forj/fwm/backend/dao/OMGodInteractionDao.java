package com.forj.fwm.backend.dao;

import java.sql.SQLException;
import java.util.List;

import com.forj.fwm.entity.OMGodInteraction;
import com.j256.ormlite.dao.Dao;

public interface OMGodInteractionDao extends Dao<OMGodInteraction,String>{
	public List<OMGodInteraction> queryForLike(String arg0, Object arg1) throws SQLException;
}
