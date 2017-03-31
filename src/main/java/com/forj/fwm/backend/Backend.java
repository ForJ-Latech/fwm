package com.forj.fwm.backend;

import java.sql.DriverManager;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import com.forj.fwm.backend.dao.EventDao;
import com.forj.fwm.backend.dao.GodDao;
import com.forj.fwm.backend.dao.InteractionDao;
import com.forj.fwm.backend.dao.MMEventGodDao;
import com.forj.fwm.backend.dao.MMEventNpcDao;
import com.forj.fwm.backend.dao.MMEventStatblockDao;
import com.forj.fwm.backend.dao.MMRegionGodDao;
import com.forj.fwm.backend.dao.MMRegionNpcDao;
import com.forj.fwm.backend.dao.NpcDao;
import com.forj.fwm.backend.dao.OMEventInteractionDao;
import com.forj.fwm.backend.dao.OMGodInteractionDao;
import com.forj.fwm.backend.dao.OMNpcInteractionDao;
import com.forj.fwm.backend.dao.OMRegionInteractionDao;
import com.forj.fwm.backend.dao.OMRegionRegionDao;
import com.forj.fwm.backend.dao.RegionDao;
import com.forj.fwm.backend.dao.StatblockDao;
import com.forj.fwm.conf.AppConfig;
import com.forj.fwm.entity.Event;
import com.forj.fwm.entity.God;
import com.forj.fwm.entity.Interaction;
import com.forj.fwm.entity.MMEventGod;
import com.forj.fwm.entity.MMEventNpc;
import com.forj.fwm.entity.MMEventStatblock;
import com.forj.fwm.entity.MMRegionGod;
import com.forj.fwm.entity.MMRegionNpc;
import com.forj.fwm.entity.Npc;
import com.forj.fwm.entity.OMEventInteraction;
import com.forj.fwm.entity.OMGodInteraction;
import com.forj.fwm.entity.OMNpcInteraction;
import com.forj.fwm.entity.OMRegionInteraction;
import com.forj.fwm.entity.OMRegionRegion;
import com.forj.fwm.entity.Region;
import com.forj.fwm.entity.Statblock;
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
	private static MMEventGodDao mmEventGodDao;
	private static MMEventNpcDao mmEventNpcDao;
	private static MMEventStatblockDao mmEventStatblockDao;
	private static MMRegionGodDao mmRegionGodDao;
	private static MMRegionNpcDao mmRegionNpcDao;
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
			TableUtils.createTableIfNotExists(connectionSource, MMEventGod.class);
			TableUtils.createTableIfNotExists(connectionSource, MMEventNpc.class);
			TableUtils.createTableIfNotExists(connectionSource, MMEventStatblock.class);
			TableUtils.createTableIfNotExists(connectionSource, MMRegionGod.class);
			TableUtils.createTableIfNotExists(connectionSource, MMRegionNpc.class);
			TableUtils.createTableIfNotExists(connectionSource, Npc.class);
			TableUtils.createTableIfNotExists(connectionSource, OMEventInteraction.class);
			TableUtils.createTableIfNotExists(connectionSource, OMGodInteraction.class);
			TableUtils.createTableIfNotExists(connectionSource, OMNpcInteraction.class);
			TableUtils.createTableIfNotExists(connectionSource, OMRegionInteraction.class);
			TableUtils.createTableIfNotExists(connectionSource, OMRegionRegion.class);
			TableUtils.createTableIfNotExists(connectionSource, Region.class);
			TableUtils.createTableIfNotExists(connectionSource, Statblock.class);
		
			// Create all DAOs
			eventDao =  DaoManager.createDao(connectionSource, Event.class);
			godDao =  DaoManager.createDao(connectionSource, God.class);
			interactionDao =  DaoManager.createDao(connectionSource, Interaction.class);
			mmEventGodDao =  DaoManager.createDao(connectionSource, MMEventGod.class);
			mmEventNpcDao =  DaoManager.createDao(connectionSource, MMEventNpc.class);
			mmEventStatblockDao =  DaoManager.createDao(connectionSource, MMEventStatblock.class);
			mmRegionGodDao =  DaoManager.createDao(connectionSource, MMRegionGod.class);
			mmRegionNpcDao =  DaoManager.createDao(connectionSource, MMRegionNpc.class);
			npcDao =  DaoManager.createDao(connectionSource, Npc.class);
			omEventInteractionDao =  DaoManager.createDao(connectionSource, OMEventInteraction.class);
			omGodInteractionDao =  DaoManager.createDao(connectionSource, OMGodInteraction.class);
			omNpcInteractionDao =  DaoManager.createDao(connectionSource, OMNpcInteraction.class);
			omRegionInteractionDao =  DaoManager.createDao(connectionSource, OMRegionInteraction.class);
			omRegionRegionDao =  DaoManager.createDao(connectionSource, OMRegionRegion.class);
			regionDao =  DaoManager.createDao(connectionSource, Region.class);
			statblockDao =  DaoManager.createDao(connectionSource, Statblock.class);
		} catch (SQLException sqle) {
			log.error(sqle.getErrorCode());
			log.error(sqle.getStackTrace());
		}
		ShowPlayersDataModel.startConnector();
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
