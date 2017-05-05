package com.forj.fwm.backend;

import java.sql.SQLException;
import java.util.Date;

import org.apache.log4j.Logger;

import com.forj.fwm.entity.Event;
import com.forj.fwm.entity.Searchable;
import com.forj.fwm.entity.Statblock;
import com.forj.fwm.gui.InteractionList.ListController;
import com.forj.fwm.gui.component.AddableImage;
import com.forj.fwm.gui.component.AddableSound;
import com.forj.fwm.gui.tab.Saveable;

import javafx.scene.control.Tab;

public class DefaultStatblockBackend {

	private static Logger log = Logger.getLogger(DefaultStatblockBackend.class);
	
	private static Statblock NpcStat;
	public static Statblock getNpcStat(){return NpcStat;}
	private static Statblock GodStat;
	public static Statblock getGodStat(){return GodStat;}
	private static Statblock RegionStat;
	public static Statblock getRegionStat() {return RegionStat;}
	private static Statblock GroupStat;
	public static Statblock getGroupStat(){return GroupStat;}
	
	private static final int NPCStatId = 1;
	private static final int GodStatId = 2;
	private static final int RegionStatId = 3;
	private static final int GroupStatId = 4;
	
	public static void createStatBlocks() throws SQLException{
		log.debug("Attempting to change statblocks.");
		NpcStat = Backend.getStatblockDao().queryForId(String.valueOf(NPCStatId));
		if(NpcStat == null){
			log.debug("had to create a new NpcStat");
			NpcStat = new Statblock();
			NpcStat.setID(NPCStatId);
			Backend.getStatblockDao().createIfNotExists(NpcStat);
		}
		GodStat = Backend.getStatblockDao().queryForId(String.valueOf(GodStatId));
		if(GodStat == null){
			log.debug("had to create a new GodStat");
			GodStat = new Statblock();
			GodStat.setID(GodStatId);
			Backend.getStatblockDao().createIfNotExists(GodStat);
		}
		RegionStat = Backend.getStatblockDao().queryForId(String.valueOf(RegionStatId));
		if(RegionStat == null){
			log.debug("had to create a new RegionStat");
			RegionStat = new Statblock();
			RegionStat.setID(RegionStatId);
			Backend.getStatblockDao().createIfNotExists(RegionStat);
		}
		GroupStat = Backend.getStatblockDao().queryForId(String.valueOf(GroupStatId));
		if(GroupStat == null){
			log.debug("had to create a new GroupStat");
			GroupStat = new Statblock();
			GroupStat.setID(GroupStatId);
			Backend.getStatblockDao().createIfNotExists(GroupStat);
		}
		log.debug("statblocks should have changed.");
	}
	
	private static DefaultStatSaveable npcDefaultSaveable = new DefaultStatSaveable("NPC Default Statblock");
	private static DefaultStatSaveable godDefaultSaveable = new DefaultStatSaveable("God Default Statblock");
	private static DefaultStatSaveable regionDefaultSaveable = new DefaultStatSaveable("Region Default Statblock");
	private static DefaultStatSaveable groupDefaultSaveable = new DefaultStatSaveable(Event.WHAT_IT_DO + " Default Statblock");
	public static DefaultStatSaveable getNpcDefaultSaveable() {
		return npcDefaultSaveable;
	}
	public static DefaultStatSaveable getGodDefaultSaveable() {
		return godDefaultSaveable;
	}
	public static DefaultStatSaveable getRegionDefaultSaveable() {
		return regionDefaultSaveable;
	}
	public static DefaultStatSaveable getGroupDefaultSaveable() {
		return groupDefaultSaveable;
	}
	
	public static DefaultStatSaveable spawnEmptyStatSaveable(){
		return new DefaultStatSaveable("");
	}
	
	private static class DefaultStatSaveable implements Saveable{
		private static class DefaultSearchable implements Searchable{
			private String name;
			public DefaultSearchable(String name){
				this.name = name;
			}
			
			public int getID() {
				return 0;
			}

			public String getShownName() {
				return name;
			}

			public String getImageFileName() {
				return null;
			}

			public Date getLastEdited() {
				return null;
			}

			public String toOneFiveJsonString() {
				return null;
			}

			public boolean isShown() {
				return false;
			}

			public void setShown(boolean b) {	
			}
		}
		private Searchable s;
		public DefaultStatSaveable(String name){
			s = new DefaultSearchable(name);
		}
		
		public void fullSave() {
		}

		public void simpleSave() {
		}

		public void relationalSave() {
		}

		public AddableImage getAddableImage() {
			return null;
		}

		public AddableSound getAddableSound() {
			return null;
		}

		public Searchable getThing() {
			return s;
		}

		public void nameFocus() {
		}

		public ListController getListController() {
			return null;
		}

		public Tab getTab() {
			return null;
		}
	}
}
