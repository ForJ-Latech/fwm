package com.forj.fwm.backend.dao.impl;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import com.forj.fwm.backend.dao.InteractionDao;
import com.forj.fwm.entity.Interaction;
import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.SelectArg;
import com.j256.ormlite.support.ConnectionSource;

public class InteractionDaoImpl extends BaseDaoImpl<Interaction,String> implements InteractionDao {
	public InteractionDaoImpl(ConnectionSource connectionSource) throws SQLException {
		super(connectionSource, Interaction.class);
	}
	
	public List<Interaction> queryForLike(String arg0, Object arg1) throws SQLException {
		PreparedQuery<Interaction> preparedQuery;
		if (arg1 instanceof Integer){
			preparedQuery = this.queryBuilder().where().like(arg0, arg1).prepare();
		} else {
			preparedQuery = this.queryBuilder().where().like(arg0, new SelectArg("%" + arg1 + "%")).prepare();
		}
		return this.query(preparedQuery);
	}
	
	@Override
	public int update(Interaction i) throws SQLException {
		i.setLastEdited(new Date());
		return super.update(i);
	}
	
	@Override
	public int create(Interaction i) throws SQLException {
		i.setLastEdited(new Date());
		return super.create(i);
	}
}
