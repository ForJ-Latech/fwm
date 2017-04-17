package com.forj.fwm.backend.dao.impl;

import java.sql.SQLException;
import java.util.List;

import com.forj.fwm.backend.dao.OMEventInteractionDao;
import com.forj.fwm.entity.OMEventInteraction;
import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.support.ConnectionSource;

public class OMEventInteractionDaoImpl  extends BaseDaoImpl<OMEventInteraction,String> implements OMEventInteractionDao {
	public OMEventInteractionDaoImpl(ConnectionSource connectionSource) throws SQLException {
		super(connectionSource, OMEventInteraction.class);
	}
	
	public List<OMEventInteraction> queryForLike(String arg0, Object arg1) throws SQLException {
		PreparedQuery<OMEventInteraction> preparedQuery;
		if (arg1 instanceof Integer){
			preparedQuery = this.queryBuilder().where().like(arg0, arg1).prepare();
		} else {
			preparedQuery = this.queryBuilder().where().like(arg0, "%" + arg1 + "%").prepare();
		}return this.query(preparedQuery);
	}
	
	public void save(OMEventInteraction relation) throws SQLException {
		if (this.queryBuilder().where().eq("event_id", relation.getEvent().getID()).and().eq("interaction_id", relation.getInteraction().getID()).query().isEmpty()) {
			this.create(relation);
		}
	}
	
	@Override
	public int delete(OMEventInteraction relation) throws SQLException {
		DeleteBuilder<OMEventInteraction, String> deleteBuilder = this.deleteBuilder();
		deleteBuilder.where().eq("event_id", relation.getEvent().getID()).and().eq("interaction_id", relation.getInteraction().getID());
		deleteBuilder.delete();
		return 1;
	}
}
