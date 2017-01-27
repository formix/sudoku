package com.girfid.sudoku.logging;

import javax.swing.JList;
import javax.swing.SwingUtilities;

public class JListLogger implements Logger {

	private LoggerModel messages;
	private int index;

	public JListLogger(JList<String> list) {
		this.messages = new LoggerModel();
		list.setModel(this.messages);
		this.index = 0;
	}

	public void info(final String text) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				index++;
				messages.add(String.format("%05d: %s", index, text));
				if (messages.getSize() > 1000) {
					
				}
			}
		});
	}

	public void clear() {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				messages.clear();
				index = 0;
			}
		});
	}

}
