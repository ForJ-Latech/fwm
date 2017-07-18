package com.forj.fwm.gui.component;

import com.forj.fwm.gui.InteractionList.ListController;

public interface MainEntityTab extends Saveable {
	public AddableImage getAddableImage();
	public AddableSound getAddableSound();
	public void nameFocus();
	public ListController getListController();
}
