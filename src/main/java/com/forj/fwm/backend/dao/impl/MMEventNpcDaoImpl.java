package com.forj.fwm.backend.dao.impl;

import java.sql.SQLException;
import java.util.List;

import com.forj.fwm.backend.dao.MMEventNpcDao;
import com.forj.fwm.entity.MMEventNpc;
import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.support.ConnectionSource;

public class MMEventNpcDaoImpl  extends BaseDaoImpl<MMEventNpc,String> implements MMEventNpcDao{
	public MMEventNpcDaoImpl(ConnectionSource connectionSource) throws SQLException{
		super(connectionSource, MMEventNpc.class);
	}
	
	public List<MMEventNpc> queryForLike(String arg0, Object arg1) throws SQLException {
		PreparedQuery<MMEventNpc> preparedQuery = this.queryBuilder().where().like(arg0, "%" + arg1 + "%").prepare();
		return this.query(preparedQuery);
	}
}
