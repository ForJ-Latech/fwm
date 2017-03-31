package com.forj.fwm.backend.dao;

import java.sql.SQLException;
import java.util.List;

import com.forj.fwm.entity.Region;
import com.j256.ormlite.dao.Dao;

public interface RegionDao extends Dao<Region,String>{
	public List<Region> queryForLike(String arg0, Object arg1) throws SQLException;
}
