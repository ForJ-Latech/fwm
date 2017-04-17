package com.forj.fwm.backend.dao.impl;

import java.sql.SQLException;
import java.util.List;

import com.forj.fwm.backend.dao.OMRegionInteractionDao;
import com.forj.fwm.entity.OMRegionInteraction;
import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.support.ConnectionSource;

public class OMRegionInteractionDaoImpl  extends BaseDaoImpl<OMRegionInteraction,String> implements OMRegionInteractionDao {
	public OMRegionInteractionDaoImpl(ConnectionSource connectionSource) throws SQLException {
		super(connectionSource, OMRegionInteraction.class);
	}
	
	public List<OMRegionInteraction> queryForLike(String arg0, Object arg1) throws SQLException {
		PreparedQuery<OMRegionInteraction> preparedQuery;
		if (arg1 instanceof Integer){
			preparedQuery = this.queryBuilder().where().like(arg0, arg1).prepare();
		} else {
			preparedQuery = this.queryBuilder().where().like(arg0, "%" + arg1 + "%").prepare();
		}
		return this.query(preparedQuery);
	}
	
	public void save(OMRegionInteraction relation) throws SQLException {
		if (this.queryBuilder().where().eq("region_id", relation.getRegion().getID()).and().eq("interaction_id", relation.getInteraction().getID()).query().isEmpty()) {
			this.create(relation);
		}
	}
	
	@Override
	public int delete(OMRegionInteraction relation) throws SQLException {
		DeleteBuilder<OMRegionInteraction, String> deleteBuilder = this.deleteBuilder();
		deleteBuilder.where().eq("region_id", relation.getRegion().getID()).and().eq("interaction_id", relation.getInteraction().getID());
		deleteBuilder.delete();
		return 1;
	}
}
