package com.forj.fwm.gui.tab;

import javafx.scene.control.Tab;

public interface Saveable {
	public void save();
	
	public Object getThing();
	public Tab getTab();
}