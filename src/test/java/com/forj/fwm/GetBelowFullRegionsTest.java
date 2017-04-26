package com.forj.fwm;

import com.forj.fwm.backend.Backend;
import com.forj.fwm.entity.Region;
import com.forj.fwm.startup.App;

public class GetBelowFullRegionsTest {
	public static void  main(String... args) throws Exception{
		App.noUiStart();
		
		Region r = new Region();
		r.setName("test");
		
		for (int i = 0; i < 99; i++) {
			r.addSubRegion(new Region());
		}
		Region r1 = new Region();
		for (int i = 0; i < 100; i++) {
			r1.addSubRegion(new Region());
		}
		r.addSubRegion(r1);
		
		Backend.getRegionDao().saveFullRegion(r1);
		Backend.getRegionDao().saveFullRegion(r);
		
		System.out.println("");
		System.out.println("Starting Test");
		System.out.println("");
		System.out.println("Test Size: 200");
		System.out.println("Returned Size: " + Backend.getRegionDao().getBelowFullRegions(r).size());
		System.out.println("");
	}	
}
