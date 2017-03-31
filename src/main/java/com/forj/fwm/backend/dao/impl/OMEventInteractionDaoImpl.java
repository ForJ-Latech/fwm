package com.forj.fwm.backend.dao.impl;

import java.sql.SQLException;
import java.util.List;

import com.forj.fwm.backend.dao.OMEventInteractionDao;
import com.forj.fwm.entity.OMEventInteraction;
import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.support.ConnectionSource;

public class OMEventInteractionDaoImpl  extends BaseDaoImpl<OMEventInteraction,String> implements OMEventInteractionDao{
	public OMEventInteractionDaoImpl(ConnectionSource connectionSource) throws SQLException{
		super(connectionSource, OMEventInteraction.class);
	}
	
	public List<OMEventInteraction> queryForLike(String arg0, Object arg1) throws SQLException {
		PreparedQuery<OMEventInteraction> preparedQuery = this.queryBuilder().where().like(arg0, "%" + arg1 + "%").prepare();
		return this.query(preparedQuery);
	}
}
