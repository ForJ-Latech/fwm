package com.forj.fwm.gui.component;

import com.forj.fwm.entity.Searchable;

import javafx.scene.control.ListView;

public interface Openable {
	public void open(Searchable s) throws Exception;
	public ListView getListView();
}
