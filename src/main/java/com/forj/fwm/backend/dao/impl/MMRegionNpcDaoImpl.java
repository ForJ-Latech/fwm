package com.forj.fwm.backend.dao.impl;

import java.sql.SQLException;
import java.util.List;

import com.forj.fwm.backend.dao.MMRegionNpcDao;
import com.forj.fwm.entity.MMRegionNpc;
import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.support.ConnectionSource;

public class MMRegionNpcDaoImpl extends BaseDaoImpl<MMRegionNpc,String> implements MMRegionNpcDao {
	public MMRegionNpcDaoImpl(ConnectionSource connectionSource) throws SQLException {
		super(connectionSource, MMRegionNpc.class);
	}
	
	public List<MMRegionNpc> queryForLike(String arg0, Object arg1) throws SQLException {
		PreparedQuery<MMRegionNpc> preparedQuery;
		if (arg1 instanceof Integer){
			preparedQuery = this.queryBuilder().where().like(arg0, arg1).prepare();
		} else {
			preparedQuery = this.queryBuilder().where().like(arg0, "%" + arg1 + "%").prepare();
		}
		return this.query(preparedQuery);
	}
	
	public void save(MMRegionNpc relation) throws SQLException {
		if (this.queryBuilder().where().eq("region_id", relation.getRegion().getID()).and().eq("npc_id", relation.getNpc().getID()).query().isEmpty()) {
			this.create(relation);
		}
	}
	
	@Override
	public int delete(MMRegionNpc relation) throws SQLException {
		DeleteBuilder<MMRegionNpc, String> deleteBuilder = this.deleteBuilder();
		deleteBuilder.where().eq("region_id", relation.getRegion().getID()).and().eq("npc_id", relation.getNpc().getID());
		deleteBuilder.delete();
		return 1;
	}
}
