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
		if (this.messages.size() > 5000) {
			// above 5000 entries, the list gets really slow!
			this.removeLast();
		}
	}
	
	private void removeLast() {
		if (this.messages instanceof LinkedList) {
			LinkedList<String> list = (LinkedList<String>) this.messages;
			list.removeLast();
		} else {
			this.messages.remove(this.messages.size() - 1);
		}
		this.fireIntervalRemoved(this, this.messages.size(), this.messages.size());
	}
	
	
	public void clear() {
		this.messages.clear();
		this.fireIntervalRemoved(this, 0, this.messages.size());
	}
	
	public int getSize() {
		return messages.size();
	}

	public String getElementAt(int index) {
		return messages.get(index);
	}

}
