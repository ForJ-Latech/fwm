package com.forj.fwm.backend.dao;

import java.sql.SQLException;
import java.util.List;

import com.forj.fwm.entity.OMRegionRegion;
import com.j256.ormlite.dao.Dao;

public interface OMRegionRegionDao extends Dao<OMRegionRegion,String>{
	public List<OMRegionRegion> queryForLike(String arg0, Object arg1) throws SQLException;
}
