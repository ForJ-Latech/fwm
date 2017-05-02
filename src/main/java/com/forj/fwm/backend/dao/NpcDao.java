package com.forj.fwm.backend.dao;

import java.sql.SQLException;
import java.util.List;

import com.forj.fwm.entity.Npc;
import com.j256.ormlite.dao.Dao;

public interface NpcDao extends Dao<Npc,String> {
	public List<Npc> queryForLike(String arg0, Object arg1) throws SQLException;
	public Npc getNpc(int id) throws SQLException;
	public Npc getFullNpc(int id) throws SQLException;
	public void saveFullNpc(Npc npc) throws SQLException;
	public void saveRelationalNpc(Npc npc) throws SQLException;
	public List<Npc> getFamily(Npc npc);
	public CreateOrUpdateStatus createOrUpdateWLE(Npc n) throws SQLException;
}
