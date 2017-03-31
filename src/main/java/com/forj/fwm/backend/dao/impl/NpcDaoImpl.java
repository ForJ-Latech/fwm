package com.forj.fwm.backend.dao.impl;

import java.sql.SQLException;
import java.util.List;

import com.forj.fwm.backend.dao.NpcDao;
import com.forj.fwm.entity.Npc;
import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.support.ConnectionSource;

public class NpcDaoImpl  extends BaseDaoImpl<Npc,String> implements NpcDao{
	public NpcDaoImpl(ConnectionSource connectionSource) throws SQLException{
		super(connectionSource, Npc.class);
	}

	public List<Npc> queryForLike(String arg0, Object arg1) throws SQLException {
		PreparedQuery<Npc> preparedQuery = this.queryBuilder().where().like(arg0, "%" + arg1 + "%").prepare();
		return this.query(preparedQuery);
	}
}
