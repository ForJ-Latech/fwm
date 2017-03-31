package com.forj.fwm.backend.dao.impl;

import java.sql.SQLException;
import java.util.List;

import com.forj.fwm.backend.dao.RegionDao;
import com.forj.fwm.entity.Region;
import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.support.ConnectionSource;

public class RegionDaoImpl  extends BaseDaoImpl<Region,String> implements RegionDao{
	public RegionDaoImpl(ConnectionSource connectionSource) throws SQLException{
		super(connectionSource, Region.class);
	}
	
	public List<Region> queryForLike(String arg0, Object arg1) throws SQLException {
		PreparedQuery<Region> preparedQuery = this.queryBuilder().where().like(arg0, "%" + arg1 + "%").prepare();
		return this.query(preparedQuery);
	}
}
