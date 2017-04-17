package com.forj.fwm.backend.dao;

import java.sql.SQLException;
import java.util.List;

import com.forj.fwm.entity.MMTemplateRegion;
import com.j256.ormlite.dao.Dao;

public interface MMTemplateRegionDao extends Dao<MMTemplateRegion,String> {
	public List<MMTemplateRegion> queryForLike(String arg0, Object arg1) throws SQLException;
	public void save(MMTemplateRegion relation) throws SQLException;
}
