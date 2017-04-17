package com.forj.fwm.backend.dao.impl;

import java.sql.SQLException;
import java.util.List;

import com.forj.fwm.backend.dao.MMEventStatblockDao;
import com.forj.fwm.entity.MMEventStatblock;
import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.support.ConnectionSource;

public class MMEventStatblockDaoImpl  extends BaseDaoImpl<MMEventStatblock,String> implements MMEventStatblockDao {
	public MMEventStatblockDaoImpl(ConnectionSource connectionSource) throws SQLException {
		super(connectionSource, MMEventStatblock.class);
	}
	
	public List<MMEventStatblock> queryForLike(String arg0, Object arg1) throws SQLException {
		PreparedQuery<MMEventStatblock> preparedQuery;
		if (arg1 instanceof Integer){
			preparedQuery = this.queryBuilder().where().like(arg0, arg1).prepare();
		} else {
			preparedQuery = this.queryBuilder().where().like(arg0, "%" + arg1 + "%").prepare();
		}
		return this.query(preparedQuery);
	}
	
	public void save(MMEventStatblock relation) throws SQLException {
		if (this.queryBuilder().where().eq("event_id", relation.getEvent().getID()).and().eq("statblock_id", relation.getStatblock().getID()).query().isEmpty()) {
			this.create(relation);
		}
	}
	
	@Override
	public int delete(MMEventStatblock relation) throws SQLException {
		DeleteBuilder<MMEventStatblock, String> deleteBuilder = this.deleteBuilder();
		deleteBuilder.where().eq("event_id", relation.getEvent().getID()).and().eq("statblock_id", relation.getStatblock().getID());
		deleteBuilder.delete();
		return 1;
	}
}
