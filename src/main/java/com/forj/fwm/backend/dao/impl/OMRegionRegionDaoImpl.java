package com.forj.fwm.backend.dao.impl;

import java.sql.SQLException;
import java.util.List;

import com.forj.fwm.backend.dao.OMRegionRegionDao;
import com.forj.fwm.entity.OMRegionRegion;
import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.support.ConnectionSource;

public class OMRegionRegionDaoImpl  extends BaseDaoImpl<OMRegionRegion,String> implements OMRegionRegionDao{
	public OMRegionRegionDaoImpl(ConnectionSource connectionSource) throws SQLException{
		super(connectionSource, OMRegionRegion.class);
	}

	public List<OMRegionRegion> queryForLike(String arg0, Object arg1) throws SQLException {
		PreparedQuery<OMRegionRegion> preparedQuery = this.queryBuilder().where().like(arg0, "%" + arg1 + "%").prepare();
		return this.query(preparedQuery);
	}
}
