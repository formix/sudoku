package org.formix.sudoku;

import java.util.Set;

public class Cell implements Cloneable {

	private int row;
	private int col;
	private int value;
	private boolean editable;
	private Sudoku sudoku;

	public Cell(Sudoku sudokuGrid, int row, int col) {
		this(sudokuGrid, row, col, 0);
	}

	public Cell(Sudoku sudoku, int row, int col, int value) {
		this.setSudoku(sudoku);
		this.row = row;
		this.col = col;
		this.value = value;
		this.editable = true;
	}
	
	protected void setSudoku(Sudoku value) {
		if (value == null) {
			throw new IllegalArgumentException(
					Messages.getString("Cell.1"));
		}
		this.sudoku = value;
	}
	
	public Sudoku getSudoku() {
		return this.sudoku;
	}

	public int getRow() {
		return row;
	}

	public int getCol() {
		return col;
	}

	public int getValue() {
		return value;
	}

	public boolean setValue(int value) {
		if (!this.editable) {
			throw new IllegalStateException(Messages.getString("Cell.0")); //$NON-NLS-1$
		}
		if (this.sudoku.getBrokenRule(row, col, value) != BrokenRule.NONE) {
			return false;
		}
		this.value = value;
		return true;
	}

	public boolean isEditable() {
		return editable;
	}

	public void setEditable(boolean fixed) {
		this.editable = fixed;
	}

	public Set<Integer> getAllowedValues() {
		return this.sudoku.getAllowedValues(row, col);
	}
	
	public Cell clone() throws CloneNotSupportedException {
		return (Cell) super.clone();
	}

	@Override
	public String toString() {
		String ret = "";
		if (!this.editable)
			ret = "_";
		ret += this.value;
		return ret;
	}

	public boolean isEmpty() {
		return this.value == 0;
	}

	public void clear() {
		this.value = 0;
	}
}
