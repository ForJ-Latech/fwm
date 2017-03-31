package com.forj.fwm.backend.dao.impl;

import java.sql.SQLException;
import java.util.List;

import com.forj.fwm.backend.dao.OMRegionInteractionDao;
import com.forj.fwm.entity.OMRegionInteraction;
import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.support.ConnectionSource;

public class OMRegionInteractionDaoImpl  extends BaseDaoImpl<OMRegionInteraction,String> implements OMRegionInteractionDao{
	public OMRegionInteractionDaoImpl(ConnectionSource connectionSource) throws SQLException{
		super(connectionSource, OMRegionInteraction.class);
	}
	
	public List<OMRegionInteraction> queryForLike(String arg0, Object arg1) throws SQLException {
		PreparedQuery<OMRegionInteraction> preparedQuery = this.queryBuilder().where().like(arg0, "%" + arg1 + "%").prepare();
		return this.query(preparedQuery);
	}
}
