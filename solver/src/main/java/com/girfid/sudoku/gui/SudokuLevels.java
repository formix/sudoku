package com.girfid.sudoku.gui;

public enum SudokuLevels {

	EASY (Messages.getString("SudokuLevels.0"), 1), //$NON-NLS-1$
	MEDIUM (Messages.getString("SudokuLevels.1"), 2), //$NON-NLS-1$
	HARD (Messages.getString("SudokuLevels.2"), 3), //$NON-NLS-1$
	EVIL (Messages.getString("SudokuLevels.3"), 4); //$NON-NLS-1$

	private final int level;
	private final String text;

	SudokuLevels(String text, int level) {
		this.text = text;
		this.level = level;
	}
	
	public int getLevel() {
		return level;
	}
	
	public String getText() {
		return this.text;
	}
	
	@Override
	public String toString() {
		return this.text;
	}
}
