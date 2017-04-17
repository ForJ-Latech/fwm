package com.forj.fwm.backend.dao;

import java.sql.SQLException;
import java.util.List;

import com.forj.fwm.entity.MMRegionNpc;
import com.j256.ormlite.dao.Dao;

public interface MMRegionNpcDao extends Dao<MMRegionNpc,String> {
	public List<MMRegionNpc> queryForLike(String arg0, Object arg1) throws SQLException;
	public void save(MMRegionNpc relation) throws SQLException;
}
