package com.girfid.sudoku.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.JTextField;
import javax.swing.border.LineBorder;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.PlainDocument;

public class SudokuDigit extends JTextField {
	private static final long serialVersionUID = -8840047692627465890L;

	public static int SIZE = 30;

	private int row;
	private int col;

	public SudokuDigit() {
		super();
		this.initialize();
		this.row = 0;
		this.col = 0;
		Font f = new Font("Arial", Font.PLAIN, SIZE - 5);
		this.setFont(f);
	}

	public int getRow() {
		return row;
	}

	public void setRow(int row) {
		this.row = row;
	}

	public int getCol() {
		return col;
	}

	public void setCol(int col) {
		this.col = col;
	}

	@Override
	protected Document createDefaultModel() {
		return new IntTextDocument();
	}

	public SudokuDigit(int defval, int columns) {
		super("" + defval, columns);
		this.initialize();
	}

	protected void initialize() {
		this.setHorizontalAlignment(CENTER);
		this.addFocusListener(new FocusListener() {
			@Override
			public void focusLost(FocusEvent e) {
				select(0, 0);
			}

			@Override
			public void focusGained(FocusEvent e) {
				select(0, getText().length());
			}
		});
		setBorder(new LineBorder(new Color(172, 172, 172)));
	}

	public int getValue() {
		if (getText() == "") {
			return 0;
		}
		try {
			return Integer.parseInt(getText());
		} catch (NumberFormatException e) {
			return 0;
		}
	}
	
	
	class IntTextDocument extends PlainDocument {
		private static final long serialVersionUID = 886469659631260140L;
		
		public void insertString(int offs, String str, AttributeSet a)
				throws BadLocationException {

			if (str == null)
				return;
						
			if ((getLength() + str.length()) <= 1) {
				String oldString = getText(0, getLength());
				String newString = oldString.substring(0, offs) + str
						+ oldString.substring(offs);

				if (newString.matches("[1-9]?")) {
					try {
						super.insertString(offs, str, a);
						select(0, 1);
					} catch (Exception ex) {
						System.out.println(ex.toString());
					}
				}
			}
			
		}
	}
}
