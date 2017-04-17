package com.forj.fwm.backend.dao.impl;

import java.sql.SQLException;
import java.util.List;

import com.forj.fwm.backend.dao.MMEventGodDao;
import com.forj.fwm.entity.MMEventGod;
import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.support.ConnectionSource;

public class MMEventGodDaoImpl extends BaseDaoImpl<MMEventGod,String> implements MMEventGodDao {
	public MMEventGodDaoImpl(ConnectionSource connectionSource) throws SQLException {
		super(connectionSource, MMEventGod.class);
	}

	public List<MMEventGod> queryForLike(String arg0, Object arg1) throws SQLException {
		PreparedQuery<MMEventGod> preparedQuery = this.queryBuilder().where().like(arg0, arg1).prepare();
		return this.query(preparedQuery);
	}
	
	public void save(MMEventGod relation) throws SQLException {
		if (this.queryBuilder().where().eq("event_id", relation.getEvent().getID()).and().eq("god_id", relation.getGod().getID()).query().isEmpty()) {
			this.create(relation);
		}
	}
	
	@Override
	public int delete(MMEventGod relation) throws SQLException {
		DeleteBuilder<MMEventGod, String> deleteBuilder = this.deleteBuilder();
		deleteBuilder.where().eq("event_id", relation.getEvent().getID()).and().eq("god_id", relation.getGod().getID());
		deleteBuilder.delete();
		return 1;
	}
}
