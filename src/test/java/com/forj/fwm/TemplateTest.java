package com.forj.fwm;

import com.forj.fwm.startup.App;

public class TemplateTest {
	
	public static void  main(String... args) throws Exception{
		App.noUiStart();
		String[] tests = new String[] {"[ wa, la, da][ lol, swag, maga]",
				"s[ i feel that [imm good, da best, da worst], you are  a [ lol, swag, maga]]",
				"s[ [wa, la, da] im the greatest lol",
				
		};
//		for(String s : tests){
//			System.err.println(s);
//			Template template = new Template();
//			template.setHistory(s);
//			Npc npc = Backend.getTemplateDao().newFromTemplate(template);
//			System.out.println(npc.getHistory() + "\n");
//		}
	}
	
	
}
