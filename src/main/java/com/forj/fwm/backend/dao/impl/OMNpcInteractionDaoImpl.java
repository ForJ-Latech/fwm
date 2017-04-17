package com.forj.fwm.backend.dao.impl;

import java.sql.SQLException;
import java.util.List;

import com.forj.fwm.backend.dao.OMNpcInteractionDao;
import com.forj.fwm.entity.OMNpcInteraction;
import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.support.ConnectionSource;

public class OMNpcInteractionDaoImpl  extends BaseDaoImpl<OMNpcInteraction,String> implements OMNpcInteractionDao {
	public OMNpcInteractionDaoImpl(ConnectionSource connectionSource) throws SQLException {
		super(connectionSource, OMNpcInteraction.class);
	}

	public List<OMNpcInteraction> queryForLike(String arg0, Object arg1) throws SQLException {
		PreparedQuery<OMNpcInteraction> preparedQuery;
		if (arg1 instanceof Integer){
			preparedQuery = this.queryBuilder().where().like(arg0, arg1).prepare();
		} else {
			preparedQuery = this.queryBuilder().where().like(arg0, "%" + arg1 + "%").prepare();
		}
		return this.query(preparedQuery);
	}

	public void save(OMNpcInteraction relation) throws SQLException {
		if (this.queryBuilder().where().eq("npc_id", relation.getNpc().getID()).and().eq("interaction_id", relation.getInteraction().getID()).query().isEmpty()) {
			this.create(relation);
		}
	}
	
	@Override
	public int delete(OMNpcInteraction relation) throws SQLException {
		DeleteBuilder<OMNpcInteraction, String> deleteBuilder = this.deleteBuilder();
		deleteBuilder.where().eq("npc_id", relation.getNpc().getID()).and().eq("interaction_id", relation.getInteraction().getID());
		deleteBuilder.delete();
		return 1;
	}
}
