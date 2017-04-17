package com.forj.fwm.backend.dao.impl;

import java.sql.SQLException;
import java.util.List;

import com.forj.fwm.backend.dao.OMGodInteractionDao;
import com.forj.fwm.entity.OMGodInteraction;
import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.support.ConnectionSource;

public class OMGodInteractionDaoImpl  extends BaseDaoImpl<OMGodInteraction,String> implements OMGodInteractionDao {
	public OMGodInteractionDaoImpl(ConnectionSource connectionSource) throws SQLException {
		super(connectionSource, OMGodInteraction.class);
	}
	
	public List<OMGodInteraction> queryForLike(String arg0, Object arg1) throws SQLException {
		PreparedQuery<OMGodInteraction> preparedQuery;
		if (arg1 instanceof Integer){
			preparedQuery = this.queryBuilder().where().like(arg0, arg1).prepare();
		} else {
			preparedQuery = this.queryBuilder().where().like(arg0, "%" + arg1 + "%").prepare();
		}
		return this.query(preparedQuery);
	}
	
	public void save(OMGodInteraction relation) throws SQLException {
		if (this.queryBuilder().where().eq("god_id", relation.getGod().getID()).and().eq("interaction_id", relation.getInteraction().getID()).query().isEmpty()) {
			this.create(relation);
		}
	}
	
	@Override
	public int delete(OMGodInteraction relation) throws SQLException {
		DeleteBuilder<OMGodInteraction, String> deleteBuilder = this.deleteBuilder();
		deleteBuilder.where().eq("god_id", relation.getGod().getID()).and().eq("interaction_id", relation.getInteraction().getID());
		deleteBuilder.delete();
		return 1;
	}
}
