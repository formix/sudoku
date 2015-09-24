package com.girfid.sudoku.logging;

import java.util.LinkedList;
import java.util.List;

import javax.swing.AbstractListModel;

public class LoggerModel extends AbstractListModel<String> {
	private static final long serialVersionUID = 1L;

	private List<String> messages;
	
	public LoggerModel() {
		this.messages = new LinkedList<String>();
	}
	
	public void add(String message) {
		this.messages.add(0, message);
		this.fireIntervalAdded(this, 0, 0);
	}
	
	public void clear() {
		this.messages.clear();
		this.fireIntervalRemoved(this, 0, this.messages.size());
	}
	
	@Override
	public int getSize() {
		return messages.size();
	}

	@Override
	public String getElementAt(int index) {
		return messages.get(index);
	}

}
