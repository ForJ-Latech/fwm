package com.forj.fwm.backend.dao;

import java.sql.SQLException;
import java.util.List;

import com.forj.fwm.entity.MMTemplateGod;
import com.j256.ormlite.dao.Dao;

public interface MMTemplateGodDao extends Dao<MMTemplateGod,String> {
	public List<MMTemplateGod> queryForLike(String arg0, Object arg1) throws SQLException;
	public void save(MMTemplateGod relation) throws SQLException;
}
