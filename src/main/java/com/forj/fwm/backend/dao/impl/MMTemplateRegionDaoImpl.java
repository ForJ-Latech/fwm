package com.forj.fwm.backend.dao.impl;

import java.sql.SQLException;
import java.util.List;

import com.forj.fwm.backend.dao.MMTemplateRegionDao;
import com.forj.fwm.entity.MMTemplateRegion;
import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.support.ConnectionSource;

public class MMTemplateRegionDaoImpl extends BaseDaoImpl<MMTemplateRegion,String> implements MMTemplateRegionDao {
	public MMTemplateRegionDaoImpl(ConnectionSource connectionSource) throws SQLException {
		super(connectionSource, MMTemplateRegion.class);
	}
	
	public List<MMTemplateRegion> queryForLike(String arg0, Object arg1) throws SQLException {
		PreparedQuery<MMTemplateRegion> preparedQuery;
		if (arg1 instanceof Integer){
			preparedQuery = this.queryBuilder().where().like(arg0, arg1).prepare();
		} else {
			preparedQuery = this.queryBuilder().where().like(arg0, "%" + arg1 + "%").prepare();
		}
		return this.query(preparedQuery);
	}
	
	public void save(MMTemplateRegion relation) throws SQLException {
		if (this.queryBuilder().where().eq("template_id", relation.getTemplate().getID()).and().eq("region_id", relation.getRegion().getID()).query().isEmpty()) {
			this.create(relation);
		}
	}
	
	@Override
	public int delete(MMTemplateRegion relation) throws SQLException {
		DeleteBuilder<MMTemplateRegion, String> deleteBuilder = this.deleteBuilder();
		deleteBuilder.where().eq("template_id", relation.getTemplate().getID()).and().eq("region_id", relation.getRegion().getID());
		deleteBuilder.delete();
		return 1;
	}
}