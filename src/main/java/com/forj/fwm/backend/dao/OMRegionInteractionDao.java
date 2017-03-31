package com.forj.fwm.backend.dao;

import java.sql.SQLException;
import java.util.List;

import com.forj.fwm.entity.OMRegionInteraction;
import com.j256.ormlite.dao.Dao;

public interface OMRegionInteractionDao extends Dao<OMRegionInteraction,String>{
	public List<OMRegionInteraction> queryForLike(String arg0, Object arg1) throws SQLException;
}
