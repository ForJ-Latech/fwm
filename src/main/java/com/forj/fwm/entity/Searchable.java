package com.forj.fwm.entity;

import java.util.Date;

public interface Searchable {	
	public int getID();
	public String getShownName();
	public String getImageFileName();
	public Date getLastEdited();
	public String toOneFiveJsonString();
	public boolean isShown();
	public void setShown(boolean b);
}
