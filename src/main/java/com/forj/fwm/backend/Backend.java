package com.forj.fwm.backend;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import com.forj.fwm.backend.dao.EventDao;
import com.forj.fwm.backend.dao.GodDao;
import com.forj.fwm.backend.dao.InteractionDao;
import com.forj.fwm.backend.dao.MMEventGodDao;
import com.forj.fwm.backend.dao.MMEventNpcDao;
import com.forj.fwm.backend.dao.MMEventStatblockDao;
import com.forj.fwm.backend.dao.MMRegionGodDao;
import com.forj.fwm.backend.dao.MMRegionNpcDao;
import com.forj.fwm.backend.dao.MMTemplateEventDao;
import com.forj.fwm.backend.dao.MMTemplateGodDao;
import com.forj.fwm.backend.dao.MMTemplateRegionDao;
import com.forj.fwm.backend.dao.NpcDao;
import com.forj.fwm.backend.dao.OMEventInteractionDao;
import com.forj.fwm.backend.dao.OMGodInteractionDao;
import com.forj.fwm.backend.dao.OMNpcInteractionDao;
import com.forj.fwm.backend.dao.OMRegionInteractionDao;
import com.forj.fwm.backend.dao.OMRegionRegionDao;
import com.forj.fwm.backend.dao.RegionDao;
import com.forj.fwm.backend.dao.StatblockDao;
import com.forj.fwm.backend.dao.TemplateDao;
import com.forj.fwm.entity.Event;
import com.forj.fwm.entity.God;
import com.forj.fwm.entity.Interaction;
import com.forj.fwm.entity.MMEventGod;
import com.forj.fwm.entity.MMEventNpc;
import com.forj.fwm.entity.MMEventStatblock;
import com.forj.fwm.entity.MMRegionGod;
import com.forj.fwm.entity.MMRegionNpc;
import com.forj.fwm.entity.MMTemplateEvent;
import com.forj.fwm.entity.MMTemplateGod;
import com.forj.fwm.entity.MMTemplateRegion;
import com.forj.fwm.entity.Npc;
import com.forj.fwm.entity.OMEventInteraction;
import com.forj.fwm.entity.OMGodInteraction;
import com.forj.fwm.entity.OMNpcInteraction;
import com.forj.fwm.entity.OMRegionInteraction;
import com.forj.fwm.entity.OMRegionRegion;
import com.forj.fwm.entity.Region;
import com.forj.fwm.entity.Searchable;
import com.forj.fwm.entity.Statblock;
import com.forj.fwm.entity.Template;
import com.forj.fwm.startup.App;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

public class Backend {
	private static String connectionHeader = "jdbc:h2:";
	private static String connectionName;
	private static Logger log = Logger.getLogger(Backend.class);
	private static EventDao eventDao;
	private static GodDao godDao;
	private static InteractionDao interactionDao;
	private static TemplateDao templateDao;
	private static MMEventGodDao mmEventGodDao;
	private static MMEventNpcDao mmEventNpcDao;
	private static MMEventStatblockDao mmEventStatblockDao;
	private static MMRegionGodDao mmRegionGodDao;
	private static MMRegionNpcDao mmRegionNpcDao;
	private static MMTemplateEventDao mmTemplateEventDao;
	private static MMTemplateGodDao mmTemplateGodDao;
	private static MMTemplateRegionDao mmTemplateRegionDao;
	private static NpcDao npcDao;
	private static OMEventInteractionDao omEventInteractionDao;
	private static OMGodInteractionDao omGodInteractionDao;
	private static OMNpcInteractionDao omNpcInteractionDao;
	private static OMRegionInteractionDao omRegionInteractionDao;
	private static OMRegionRegionDao omRegionRegionDao;
	private static RegionDao regionDao;
	private static StatblockDao statblockDao;

	public static void start() {
		connectionName = connectionHeader + App.worldFileUtil.getDbLocation();

		try {
			DriverManager.registerDriver(new org.h2.Driver());
			ConnectionSource connectionSource = new JdbcConnectionSource(connectionName);

			// Create all tables
			TableUtils.createTableIfNotExists(connectionSource, Event.class);
			TableUtils.createTableIfNotExists(connectionSource, God.class);
			TableUtils.createTableIfNotExists(connectionSource, Interaction.class);
			TableUtils.createTableIfNotExists(connectionSource, Template.class);
			TableUtils.createTableIfNotExists(connectionSource, MMEventGod.class);
			TableUtils.createTableIfNotExists(connectionSource, MMEventNpc.class);
			TableUtils.createTableIfNotExists(connectionSource, MMEventStatblock.class);
			TableUtils.createTableIfNotExists(connectionSource, MMRegionGod.class);
			TableUtils.createTableIfNotExists(connectionSource, MMRegionNpc.class);
			TableUtils.createTableIfNotExists(connectionSource, MMTemplateEvent.class);
			TableUtils.createTableIfNotExists(connectionSource, MMTemplateGod.class);
			TableUtils.createTableIfNotExists(connectionSource, MMTemplateRegion.class);
			TableUtils.createTableIfNotExists(connectionSource, Npc.class);
			TableUtils.createTableIfNotExists(connectionSource, OMEventInteraction.class);
			TableUtils.createTableIfNotExists(connectionSource, OMGodInteraction.class);
			TableUtils.createTableIfNotExists(connectionSource, OMNpcInteraction.class);
			TableUtils.createTableIfNotExists(connectionSource, OMRegionInteraction.class);
			TableUtils.createTableIfNotExists(connectionSource, OMRegionRegion.class);
			TableUtils.createTableIfNotExists(connectionSource, Region.class);
			TableUtils.createTableIfNotExists(connectionSource, Statblock.class);

			// Create all DAOs
			eventDao = DaoManager.createDao(connectionSource, Event.class);
			godDao = DaoManager.createDao(connectionSource, God.class);
			interactionDao = DaoManager.createDao(connectionSource, Interaction.class);
			templateDao = DaoManager.createDao(connectionSource, Template.class);
			mmEventGodDao = DaoManager.createDao(connectionSource, MMEventGod.class);
			mmEventNpcDao = DaoManager.createDao(connectionSource, MMEventNpc.class);
			mmEventStatblockDao = DaoManager.createDao(connectionSource, MMEventStatblock.class);
			mmRegionGodDao = DaoManager.createDao(connectionSource, MMRegionGod.class);
			mmRegionNpcDao = DaoManager.createDao(connectionSource, MMRegionNpc.class);
			mmTemplateEventDao = DaoManager.createDao(connectionSource, MMTemplateEvent.class);
			mmTemplateGodDao = DaoManager.createDao(connectionSource, MMTemplateGod.class);
			mmTemplateRegionDao = DaoManager.createDao(connectionSource, MMTemplateRegion.class);
			npcDao = DaoManager.createDao(connectionSource, Npc.class);
			omEventInteractionDao = DaoManager.createDao(connectionSource, OMEventInteraction.class);
			omGodInteractionDao = DaoManager.createDao(connectionSource, OMGodInteraction.class);
			omNpcInteractionDao = DaoManager.createDao(connectionSource, OMNpcInteraction.class);
			omRegionInteractionDao = DaoManager.createDao(connectionSource, OMRegionInteraction.class);
			omRegionRegionDao = DaoManager.createDao(connectionSource, OMRegionRegion.class);
			regionDao = DaoManager.createDao(connectionSource, Region.class);
			statblockDao = DaoManager.createDao(connectionSource, Statblock.class);
		} catch (SQLException sqle) {
			log.error(sqle.getMessage());
		}
		App.spdc = new ShowPlayersDataModel();
	}

	public static ArrayList<Searchable> searchAllByLike(String name){
		ArrayList<Searchable> results = new ArrayList<Searchable>();
		try {
			for (Npc npc : Backend.getNpcDao().queryForLike("fName", name)) {
				results.add(npc);
			}
			for (God god : Backend.getGodDao().queryForLike("name", name)) {
				results.add(god);
			}
			for (Region region : Backend.getRegionDao().queryForLike("name", name)) {
				results.add(region);
			}
			for (Event event : Backend.getEventDao().queryForLike("name",name)) { 
				results.add(event); 
			}
			for (Template template : Backend.getTemplateDao().queryForLike("name",name)) {
				results.add(template);
			}
		} catch (SQLException e) {
			log.error(e.getStackTrace());
		}
		return results;
	}
	
	public static void SaveSimpleSearchable(Searchable s) throws SQLException{
		if(s instanceof Npc){
			npcDao.createOrUpdate((Npc)s);
			npcDao.refresh((Npc)s);
		}else if(s instanceof God){
			godDao.createOrUpdate((God)s);
			godDao.refresh((God)s);
		}else if(s instanceof Region){
			regionDao.createOrUpdate((Region)s);
			regionDao.refresh((Region)s);
		}else if(s instanceof Event){
			eventDao.createOrUpdate((Event)s);
			eventDao.refresh((Event)s);
		}else if(s instanceof Statblock){
			statblockDao.createOrUpdate((Statblock)s);
			statblockDao.refresh((Statblock)s);
		}
		
		
	}
	
	public static EventDao getEventDao() {
		return eventDao;
	}

	public static GodDao getGodDao() {
		return godDao;
	}

	public static InteractionDao getInteractionDao() {
		return interactionDao;
	}
	
	public static TemplateDao getTemplateDao() {
		return templateDao;
	}

	public static MMEventGodDao getMmEventGodDao() {
		return mmEventGodDao;
	}

	public static MMEventNpcDao getMmEventNpcDao() {
		return mmEventNpcDao;
	}

	public static MMEventStatblockDao getMmEventStatblockDao() {
		return mmEventStatblockDao;
	}

	public static MMRegionGodDao getMmRegionGodDao() {
		return mmRegionGodDao;
	}

	public static MMRegionNpcDao getMmRegionNpcDao() {
		return mmRegionNpcDao;
	}
	
	public static MMTemplateEventDao getMmTemplateEventDao() {
		return mmTemplateEventDao;
	}
	
	public static MMTemplateGodDao getMmTemplateGodDao() {
		return mmTemplateGodDao;
	}
	
	public static MMTemplateRegionDao getMmTemplateRegionDao() {
		return mmTemplateRegionDao;
	}

	public static NpcDao getNpcDao() {
		return npcDao;
	}

	public static OMEventInteractionDao getOmEventInteractionDao() {
		return omEventInteractionDao;
	}

	public static OMGodInteractionDao getOmGodInteractionDao() {
		return omGodInteractionDao;
	}

	public static OMNpcInteractionDao getOmNpcInteractionDao() {
		return omNpcInteractionDao;
	}

	public static OMRegionInteractionDao getOmRegionInteractionDao() {
		return omRegionInteractionDao;
	}

	public static OMRegionRegionDao getOmRegionRegionDao() {
		return omRegionRegionDao;
	}

	public static RegionDao getRegionDao() {
		return regionDao;
	}

	public static StatblockDao getStatblockDao() {
		return statblockDao;
	}
}
