package com.forj.fwm.backend.dao;

import java.sql.SQLException;
import java.util.List;

import com.forj.fwm.entity.MMEventNpc;
import com.j256.ormlite.dao.Dao;

public interface MMEventNpcDao extends Dao<MMEventNpc,String> {
	public List<MMEventNpc> queryForLike(String arg0, Object arg1) throws SQLException;
	public void save(MMEventNpc relation) throws SQLException;
}
