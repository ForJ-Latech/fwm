package com.forj.fwm.backend.dao.impl;

import java.sql.SQLException;
import java.util.List;

import com.forj.fwm.backend.dao.MMRegionGodDao;
import com.forj.fwm.entity.MMRegionGod;
import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.support.ConnectionSource;

public class MMRegionGodDaoImpl  extends BaseDaoImpl<MMRegionGod,String> implements MMRegionGodDao {
	public MMRegionGodDaoImpl(ConnectionSource connectionSource) throws SQLException {
		super(connectionSource, MMRegionGod.class);
	}
	
	public List<MMRegionGod> queryForLike(String arg0, Object arg1) throws SQLException {
		PreparedQuery<MMRegionGod> preparedQuery;
		if (arg1 instanceof Integer){
			preparedQuery = this.queryBuilder().where().like(arg0, arg1).prepare();
		} else {
			preparedQuery = this.queryBuilder().where().like(arg0, "%" + arg1 + "%").prepare();
		}
		return this.query(preparedQuery);
	}
	
	public void save(MMRegionGod relation) throws SQLException {
		if (this.queryBuilder().where().eq("region_id", relation.getRegion().getID()).and().eq("god_id", relation.getGod().getID()).query().isEmpty()) {
			this.create(relation);
		}
	}
	
	@Override
	public int delete(MMRegionGod relation) throws SQLException {
		DeleteBuilder<MMRegionGod, String> deleteBuilder = this.deleteBuilder();
		deleteBuilder.where().eq("region_id", relation.getRegion().getID()).and().eq("god_id", relation.getGod().getID());
		deleteBuilder.delete();
		return 1;
	}
}
