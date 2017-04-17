package com.forj.fwm.backend.dao.impl;

import java.sql.SQLException;
import java.util.List;

import com.forj.fwm.backend.dao.MMTemplateGodDao;
import com.forj.fwm.entity.MMTemplateGod;
import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.support.ConnectionSource;

public class MMTemplateGodDaoImpl extends BaseDaoImpl<MMTemplateGod,String> implements MMTemplateGodDao {
	public MMTemplateGodDaoImpl(ConnectionSource connectionSource) throws SQLException {
		super(connectionSource, MMTemplateGod.class);
	}
	
	public List<MMTemplateGod> queryForLike(String arg0, Object arg1) throws SQLException {
		PreparedQuery<MMTemplateGod> preparedQuery;
		if (arg1 instanceof Integer){
			preparedQuery = this.queryBuilder().where().like(arg0, arg1).prepare();
		} else {
			preparedQuery = this.queryBuilder().where().like(arg0, "%" + arg1 + "%").prepare();
		}
		return this.query(preparedQuery);
	}
	
	public void save(MMTemplateGod relation) throws SQLException {
		if (this.queryBuilder().where().eq("template_id", relation.getTemplate().getID()).and().eq("god_id", relation.getGod().getID()).query().isEmpty()) {
			this.create(relation);
		}
	}
	
	@Override
	public int delete(MMTemplateGod relation) throws SQLException {
		DeleteBuilder<MMTemplateGod, String> deleteBuilder = this.deleteBuilder();
		deleteBuilder.where().eq("template_id", relation.getTemplate().getID()).and().eq("god_id", relation.getGod().getID());
		deleteBuilder.delete();
		return 1;
	}
}
