package com.forj.fwm.backend.dao;

import java.sql.SQLException;
import java.util.List;

import com.forj.fwm.entity.MMRegionGod;
import com.j256.ormlite.dao.Dao;

public interface MMRegionGodDao extends Dao<MMRegionGod,String>{
	public List<MMRegionGod> queryForLike(String arg0, Object arg1) throws SQLException;
}
