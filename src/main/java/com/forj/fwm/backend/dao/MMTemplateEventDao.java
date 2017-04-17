package com.forj.fwm.backend.dao;

import java.sql.SQLException;
import java.util.List;

import com.forj.fwm.entity.MMTemplateEvent;
import com.j256.ormlite.dao.Dao;

public interface MMTemplateEventDao extends Dao<MMTemplateEvent,String>  {
	public List<MMTemplateEvent> queryForLike(String arg0, Object arg1) throws SQLException;
	public void save(MMTemplateEvent relation) throws SQLException;
}
