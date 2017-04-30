package com.forj.fwm.gui.tab;

import com.forj.fwm.entity.Searchable;
import com.forj.fwm.gui.InteractionList.ListController;
import com.forj.fwm.gui.component.AddableImage;
import com.forj.fwm.gui.component.AddableSound;

import javafx.scene.control.Tab;

public interface Saveable {
	public void fullSave();
	public void simpleSave();
	public void relationalSave();
	public AddableImage getAddableImage();
	public AddableSound getAddableSound();
	public Searchable getThing();
	public void nameFocus();
	public ListController getListController();
	public Tab getTab();
}