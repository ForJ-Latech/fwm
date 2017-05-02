package com.forj.fwm.backend.dao;

import java.sql.SQLException;
import java.util.List;

import com.forj.fwm.entity.Template;
import com.j256.ormlite.dao.Dao;

public interface TemplateDao extends Dao<Template,String> {
	public List<Template> queryForLike(String arg0, Object arg1) throws SQLException;
	public Template getTemplate(int id) throws SQLException;
	public Template getFullTemplate(int id) throws SQLException;
	public void saveFullTemplate(Template template) throws SQLException;
	public CreateOrUpdateStatus createOrUpdateWLE(Template t) throws SQLException;
}
