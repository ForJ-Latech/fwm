package com.forj.fwm;

import com.forj.fwm.backend.Backend;
import com.forj.fwm.entity.Event;
import com.forj.fwm.entity.God;
import com.forj.fwm.entity.Npc;
import com.forj.fwm.entity.Region;
import com.forj.fwm.entity.Statblock;
import com.forj.fwm.entity.Template;
import com.forj.fwm.startup.App;

public class QueryTest {
	public static void  main(String... args) throws Exception{
		App.noUiStart();
		for (int i = 0; i < 1000; i++) {
			Npc npc = new Npc();
			npc.setfName("tESt");
			Backend.getNpcDao().saveFullNpc(npc);
		}
		for (int i = 0; i < 1000; i++) {
			Region region = new Region();
			region.setName("tESt");
			Backend.getRegionDao().saveFullRegion(region);
		}
		for (int i = 0; i < 1000; i++) {
			Event event = new Event();		
			event.setName("tESt");
			Backend.getEventDao().saveFullEvent(event);
		}
		for (int i = 0; i < 1000; i++) {
			God god = new God();
			god.setName("tESt");
			Backend.getGodDao().saveFullGod(god);
		}
		for (int i = 0; i < 1000; i++) {
			Template template = new Template();
			template.setName("tESt");
			Backend.getTemplateDao().saveFullTemplate(template);
		}
		for (int i = 0; i < 1000; i++) {
			Statblock statblock = new Statblock();
			statblock.setName("tESt");
			Backend.getStatblockDao().create(statblock);
		}
		
		System.out.println("");
		System.out.println("Created 1000 of each given entity with the name set to 'tESt'");
		System.out.println("Starting Test: ");
		
		System.out.println("");
		System.out.println("Region: ");
		System.out.println("QueryForEq   'test': " + Backend.getRegionDao().queryForEq("name", "test").size());
		System.out.println("QueryForEq   'tESt': " + Backend.getRegionDao().queryForEq("name", "tESt").size());
		System.out.println("QueryForLike 'test': " + Backend.getRegionDao().queryForLike("name", "test").size());
		
		System.out.println("");
		System.out.println("Npc: ");
		System.out.println("QueryForEq   'test': " + Backend.getNpcDao().queryForEq("fName", "test").size());
		System.out.println("QueryForEq   'tESt': " + Backend.getNpcDao().queryForEq("fName", "tESt").size());
		System.out.println("QueryForLike 'test': " + Backend.getNpcDao().queryForLike("fName", "test").size());
		
		System.out.println("");
		System.out.println("Event: ");
		System.out.println("QueryForEq   'test': " + Backend.getEventDao().queryForEq("name", "test").size());
		System.out.println("QueryForEq   'tESt': " + Backend.getEventDao().queryForEq("name", "tESt").size());
		System.out.println("QueryForLike 'test': " + Backend.getEventDao().queryForLike("name", "test").size());
		
		System.out.println("");
		System.out.println("God: ");
		System.out.println("QueryForEq   'test': " + Backend.getGodDao().queryForEq("name", "test").size());
		System.out.println("QueryForEq   'tESt': " + Backend.getGodDao().queryForEq("name", "tESt").size());
		System.out.println("QueryForLike 'test': " + Backend.getGodDao().queryForLike("name", "test").size());
		
		System.out.println("");
		System.out.println("Template: ");
		System.out.println("QueryForEq   'test': " + Backend.getTemplateDao().queryForEq("name", "test").size());
		System.out.println("QueryForEq   'tESt': " + Backend.getTemplateDao().queryForEq("name", "tESt").size());
		System.out.println("QueryForLike 'test': " + Backend.getTemplateDao().queryForLike("name", "test").size());
		
		System.out.println("");
		System.out.println("Statblock: ");
		System.out.println("QueryForEq   'test': " + Backend.getStatblockDao().queryForEq("name", "test").size());
		System.out.println("QueryForEq   'tESt': " + Backend.getStatblockDao().queryForEq("name", "tESt").size());
		System.out.println("QueryForLike 'test': " + Backend.getStatblockDao().queryForLike("name", "test").size());
		
		System.out.println("");
		System.out.println("Finished Test");
	}
}
