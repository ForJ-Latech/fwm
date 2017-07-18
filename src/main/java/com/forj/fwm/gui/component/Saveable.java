package com.forj.fwm.gui.component;

import com.forj.fwm.entity.Searchable;
import com.forj.fwm.gui.InteractionList.ListController;

import javafx.scene.control.Tab;

/**
 * This is the smaller package for something to be tabbalbe in the application.
 * It includes basics such as saving, and having something searchable associated with it. 
 * @author jehlmann
 *
 */
public interface Saveable {
	public void fullSave();
	public void simpleSave();
	public void relationalSave();
	public Searchable getThing();
	public Tab getTab();
	public void autoUpdateTab();
	public void manualUpdateTab();
}