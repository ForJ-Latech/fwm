package com.forj.fwm;

import com.forj.fwm.backend.Backend;
import com.forj.fwm.entity.Region;
import com.forj.fwm.startup.App;

public class GetAboveFullRegionsTest {
	public static void  main(String... args) throws Exception{
		App.noUiStart();
		final int testSize = 3;
		
		Region sr3 = new Region();
		sr3.setName("test");
		Backend.getRegionDao().saveFullRegion(sr3);
		
		Region sr2 = new Region();
		sr2.setName("test");
		sr2.setSuperRegion(sr3);
		Backend.getRegionDao().saveFullRegion(sr2);
		
		Region sr = new Region();
		sr.setName("test");
		sr.setSuperRegion(sr2);
		Backend.getRegionDao().saveFullRegion(sr);
		
		Region r = new Region();
		r.setName("test");
		r.setSuperRegion(sr);
//		r = createTestRegion(testSize, r);
		
		Backend.getRegionDao().saveFullRegion(r);
		
		System.out.println("");
		System.out.println("Starting Test");
		System.out.println("");
		System.out.println("Test Size: " + testSize);
		System.out.println("Returned Size: " + Backend.getRegionDao().getAboveFullRegions(r).size());
		System.out.println("");
		
//		Region tst = r;
//		for (int i = 0; i < 10; i++) {
//			if (tst.getSuperRegion() != null) {
//				System.out.println("Test");
//			}
//			tst = Backend.getRegionDao().getFullRegion(tst.getSuperRegion().getID());
//		}
	}
	
//	public static Region createTestRegion(int i, Region r) throws Exception{
//		if (i != 0) {
//			System.out.println("TO much swag");
//			i--;
//			Region tmp = new Region();
//			tmp.setName("test");
//			Backend.getRegionDao().saveFullRegion(tmp);
//			r.setSuperRegion(tmp);
//			Backend.getRegionDao().saveFullRegion(r);
//			createTestRegion(i, tmp);
//		}
//		return r;
//	}
}
