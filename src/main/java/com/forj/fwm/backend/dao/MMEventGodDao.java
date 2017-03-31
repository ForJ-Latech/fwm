package com.forj.fwm.backend.dao;

import java.sql.SQLException;
import java.util.List;

import com.forj.fwm.entity.MMEventGod;
import com.j256.ormlite.dao.Dao;

public interface MMEventGodDao extends Dao<MMEventGod,String>{
	public List<MMEventGod> queryForLike(String arg0, Object arg1) throws SQLException;
}
