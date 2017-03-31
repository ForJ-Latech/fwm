package com.forj.fwm.backend.dao.impl;

import java.sql.SQLException;
import java.util.List;

import com.forj.fwm.backend.dao.StatblockDao;
import com.forj.fwm.entity.Statblock;
import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.support.ConnectionSource;

public class StatblockDaoImpl  extends BaseDaoImpl<Statblock,String> implements StatblockDao{
	public StatblockDaoImpl(ConnectionSource connectionSource) throws SQLException{
		super(connectionSource, Statblock.class);
	}
	
	public List<Statblock> queryForLike(String arg0, Object arg1) throws SQLException {
		PreparedQuery<Statblock> preparedQuery = this.queryBuilder().where().like(arg0, "%" + arg1 + "%").prepare();
		return this.query(preparedQuery);
	}
}
