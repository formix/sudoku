package org.formix.sudoku.gui;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public abstract class DigitModifiedHandler implements DocumentListener {

	private SudokuDigit digit;

	public DigitModifiedHandler(SudokuDigit digit) {
		this.digit = digit;
	}

	public SudokuDigit getDigit() {
		return digit;
	}

	public void insertUpdate(DocumentEvent e) {
		this.modified(digit);
	}

	public void removeUpdate(DocumentEvent e) {
		this.modified(digit);
	}

	public void changedUpdate(DocumentEvent e) {
	}

	public abstract void modified(SudokuDigit digit);
}
