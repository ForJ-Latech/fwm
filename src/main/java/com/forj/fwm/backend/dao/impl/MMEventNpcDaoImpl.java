package com.forj.fwm.backend.dao.impl;

import java.sql.SQLException;
import java.util.List;

import com.forj.fwm.backend.dao.MMEventNpcDao;
import com.forj.fwm.entity.MMEventNpc;
import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.support.ConnectionSource;

public class MMEventNpcDaoImpl  extends BaseDaoImpl<MMEventNpc,String> implements MMEventNpcDao {
	public MMEventNpcDaoImpl(ConnectionSource connectionSource) throws SQLException {
		super(connectionSource, MMEventNpc.class);
	}
	
	public List<MMEventNpc> queryForLike(String arg0, Object arg1) throws SQLException {
		PreparedQuery<MMEventNpc> preparedQuery;
		if (arg1 instanceof Integer){
			preparedQuery = this.queryBuilder().where().like(arg0, arg1).prepare();
		} else {
			preparedQuery = this.queryBuilder().where().like(arg0, "%" + arg1 + "%").prepare();
		}
		return this.query(preparedQuery);
	}
	
	public void save(MMEventNpc relation) throws SQLException {
		if (this.queryBuilder().where().eq("event_id", relation.getEvent().getID()).and().eq("npc_id", relation.getNpc().getID()).query().isEmpty()) {
			this.create(relation);
		}
	}
	
	@Override
	public int delete(MMEventNpc relation) throws SQLException {
		DeleteBuilder<MMEventNpc, String> deleteBuilder = this.deleteBuilder();
		deleteBuilder.where().eq("event_id", relation.getEvent().getID()).and().eq("npc_id", relation.getNpc().getID());
		deleteBuilder.delete();
		return 1;
	}
}
