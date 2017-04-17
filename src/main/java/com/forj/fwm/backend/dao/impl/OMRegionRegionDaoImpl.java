package com.forj.fwm.backend.dao.impl;

import java.sql.SQLException;
import java.util.List;

import com.forj.fwm.backend.dao.OMRegionRegionDao;
import com.forj.fwm.entity.OMRegionRegion;
import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.support.ConnectionSource;

public class OMRegionRegionDaoImpl  extends BaseDaoImpl<OMRegionRegion,String> implements OMRegionRegionDao {
	public OMRegionRegionDaoImpl(ConnectionSource connectionSource) throws SQLException {
		super(connectionSource, OMRegionRegion.class);
	}

	public List<OMRegionRegion> queryForLike(String arg0, Object arg1) throws SQLException {
		PreparedQuery<OMRegionRegion> preparedQuery;
		if (arg1 instanceof Integer){
			preparedQuery = this.queryBuilder().where().like(arg0, arg1).prepare();
		} else {
			preparedQuery = this.queryBuilder().where().like(arg0, "%" + arg1 + "%").prepare();
		}
		return this.query(preparedQuery);
	}
	
	public void save(OMRegionRegion relation) throws SQLException {
		if (this.queryBuilder().where().eq("subRegion_id", relation.getSubRegion().getID()).and().eq("superRegion_id", relation.getSuperRegion().getID()).query().isEmpty()) {
			this.create(relation);
		}
	}
	
	@Override
	public int delete(OMRegionRegion relation) throws SQLException {
		DeleteBuilder<OMRegionRegion, String> deleteBuilder = this.deleteBuilder();
		deleteBuilder.where().eq("subRegion_id", relation.getSubRegion().getID()).and().eq("superRegion_id", relation.getSuperRegion().getID());
		deleteBuilder.delete();
		return 1;
	}
}
