package com.forj.fwm.entity;

public interface Searchable {	
	public int getID();
	
	public String getName();
	
	public String getImageFileName();
	
	public String toOneFiveJsonString();
	
	public boolean isShown();
	public void setShown(boolean b);
}
