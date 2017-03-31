package com.forj.fwm.backend.dao.impl;

import java.sql.SQLException;
import java.util.List;

import com.forj.fwm.backend.dao.OMNpcInteractionDao;
import com.forj.fwm.entity.OMNpcInteraction;
import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.support.ConnectionSource;

public class OMNpcInteractionDaoImpl  extends BaseDaoImpl<OMNpcInteraction,String> implements OMNpcInteractionDao{
	public OMNpcInteractionDaoImpl(ConnectionSource connectionSource) throws SQLException{
		super(connectionSource, OMNpcInteraction.class);
	}

	public List<OMNpcInteraction> queryForLike(String arg0, Object arg1) throws SQLException {
		PreparedQuery<OMNpcInteraction> preparedQuery = this.queryBuilder().where().like(arg0, "%" + arg1 + "%").prepare();
		return this.query(preparedQuery);
	}
}
