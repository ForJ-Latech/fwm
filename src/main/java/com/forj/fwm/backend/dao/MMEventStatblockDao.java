package com.forj.fwm.backend.dao;

import java.sql.SQLException;
import java.util.List;

import com.forj.fwm.entity.MMEventStatblock;
import com.j256.ormlite.dao.Dao;

public interface MMEventStatblockDao extends Dao<MMEventStatblock,String>{
	public List<MMEventStatblock> queryForLike(String arg0, Object arg1) throws SQLException;
}
