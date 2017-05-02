package com.forj.fwm.backend.dao;

import java.sql.SQLException;
import java.util.List;

import com.forj.fwm.entity.God;
import com.j256.ormlite.dao.Dao;

public interface GodDao extends Dao<God,String> {
	public List<God> queryForLike(String arg0, Object arg1) throws SQLException;
	public God getGod(int id) throws SQLException;
	public God getFullGod(int id) throws SQLException;
	public void saveFullGod(God god) throws SQLException;
	public void saveRelationalGod(God god) throws SQLException;
	public List<God> getPantheon(God god);
	public CreateOrUpdateStatus createOrUpdateWLE(God g) throws SQLException;
}
