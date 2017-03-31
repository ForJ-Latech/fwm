package com.forj.fwm.backend.dao.impl;

import java.sql.SQLException;
import java.util.List;

import com.forj.fwm.backend.dao.InteractionDao;
import com.forj.fwm.entity.Interaction;
import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.support.ConnectionSource;

public class InteractionDaoImpl extends BaseDaoImpl<Interaction,String> implements InteractionDao {
	public InteractionDaoImpl(ConnectionSource connectionSource) throws SQLException{
		super(connectionSource, Interaction.class);
	}
	
	public List<Interaction> queryForLike(String arg0, Object arg1) throws SQLException {
		PreparedQuery<Interaction> preparedQuery = this.queryBuilder().where().like(arg0, "%" + arg1 + "%").prepare();
		return this.query(preparedQuery);
	}
}
