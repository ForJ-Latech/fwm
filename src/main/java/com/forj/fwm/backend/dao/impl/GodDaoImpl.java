package com.forj.fwm.backend.dao.impl;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import com.forj.fwm.backend.dao.GodDao;
import com.forj.fwm.entity.God;
import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.support.ConnectionSource;

public class GodDaoImpl extends BaseDaoImpl<God,String> implements GodDao{
	public GodDaoImpl(ConnectionSource connectionSource) throws SQLException{
		super(connectionSource, God.class);
	}
	
	public List<God> queryForLike(String arg0, Object arg1) throws SQLException {
		PreparedQuery<God> preparedQuery = this.queryBuilder().where().like(arg0, "%" + arg1 + "%").prepare();
		return this.query(preparedQuery);
	}
	
	@Override
	public int update(God g) throws SQLException{
		g.setLastEdited(new Date());
		return super.update(g);
	}
}
