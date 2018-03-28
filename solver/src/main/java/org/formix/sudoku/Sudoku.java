package org.formix.sudoku;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.formix.sudoku.logging.Logger;

public class Sudoku implements Cloneable {

	private Cell[][] grid;

	public Sudoku() {
		this(null);
	}

	public Sudoku(String data) {
		this.grid = new Cell[9][9];
		for (int row = 0; row < 9; row++) {
			for (int col = 0; col < 9; col++) {
				this.grid[row][col] = new Cell(this, row + 1, col + 1);
			}
		}
		if (data != null) {
			this.loadFromString(data);
		}
	}

	public Cell getCell(int row, int col) {
		this.checkInputs(row, col, 1);
		return this.grid[row - 1][col - 1];
	}

	public int getValue(int row, int col) {
		this.checkInputs(row, col, 1);
		return this.grid[row - 1][col - 1].getValue();
	}

	private void checkInputs(int row, int col, int value) {
		if ((row < 1) || (row > 9)) {
			throw new IllegalArgumentException(Messages.getString(
					"SudokuGrid.0", row)); //$NON-NLS-1$
		}
		if ((col < 1) || (col > 9)) {
			throw new IllegalArgumentException(Messages.getString(
					"SudokuGrid.1", col)); //$NON-NLS-1$
		}
		if ((value < 0) || (value > 9)) {
			throw new IllegalArgumentException(Messages.getString(
					"SudokuGrid.2", value)); //$NON-NLS-1$
		}
	}

	public boolean setValue(int row, int col, int value) {
		this.checkInputs(row, col, value);
		return this.grid[row - 1][col - 1].setValue(value);
	}

	public Set<Integer> getAllowedValues(int row, int col) {
		this.checkInputs(row, col, 1);
		Set<Integer> values = new HashSet<Integer>();
		values.add(1);
		values.add(2);
		values.add(3);
		values.add(4);
		values.add(5);
		values.add(6);
		values.add(7);
		values.add(8);
		values.add(9);

		Set<Integer> toRemove = new HashSet<Integer>();
		for (Integer value : values) {
			if (this.getBrokenRule(row, col, value) != BrokenRule.NONE) {
				toRemove.add(value);
			}
		}
		values.removeAll(toRemove);

		return new TreeSet<Integer>(values);
	}

	protected BrokenRule getBrokenRule(int row, int col, int value) {
		if (value != 0) {
			if (this.findValueInRow(row, col, value) > 0)
				return BrokenRule.EXISTS_IN_ROW;
			if (this.findValueInCol(row, col, value) > 0)
				return BrokenRule.EXISTS_IN_COL;
			if (this.findValueInSection(row, col, value) > 0)
				return BrokenRule.EXISTS_IN_SECTION;
		}
		return BrokenRule.NONE;
	}

	private int findValueInSection(int row, int col, int value) {
		int rowBound = ((row - 1) / 3) * 3 + 1;
		int colBound = ((col - 1) / 3) * 3 + 1;
		int count = 1;
		for (int r = rowBound; r < (rowBound + 3); r++) {
			for (int c = colBound; c < (colBound + 3); c++) {
				if ((r != row) && (c != col)) {
					if (this.getValue(r, c) == value) {
						return count;
					}
				}
				count++;
			}
		}
		return 0;
	}

	private int findValueInCol(int row, int col, int value) {
		for (int r = 1; r <= 9; r++) {
			if (r != row) {
				if (this.getValue(r, col) == value) {
					return r;
				}
			}
		}
		return 0;
	}

	private int findValueInRow(int row, int col, int value) {
		for (int c = 1; c <= 9; c++) {
			if (c != col) {
				if (this.getValue(row, c) == value) {
					return c;
				}
			}
		}
		return 0;
	}

	public List<Cell> getWritableCells() {
		List<Cell> cells = new ArrayList<Cell>(81);
		for (int row = 1; row <= 9; row++) {
			for (int col = 1; col <= 9; col++) {
				Cell cell = this.getCell(row, col);
				if (cell.isEditable()) {
					cells.add(cell);
				}
			}
		}
		return cells;
	}

	public List<Cell> getWritableEmptyCells() {
		List<Cell> cells = new ArrayList<Cell>(81);
		for (int row = 1; row <= 9; row++) {
			for (int col = 1; col <= 9; col++) {
				Cell cell = this.getCell(row, col);
				if (cell.isEditable() && cell.isEmpty()) {
					cells.add(cell);
				}
			}
		}
		return cells;
	}

	public void load(String path) throws IOException {
		FileReader fr = new FileReader(path);
		int c = -1;
		StringBuilder sb = new StringBuilder();
		try {
			while ((c = fr.read()) != -1) {
				sb.append((char) c);
			}
			this.loadFromString(sb.toString());
		} finally {
			fr.close();
		}

	}

	private void loadFromString(String data) {
		int col = 0;
		int row = 0;
		boolean editable = true;
		for (char c : data.toCharArray()) {
			if (Character.isDigit((char) c)) {
				int value = Integer.parseInt(((char) c) + "");
				this.getCell(row + 1, col + 1).setEditable(true);
				this.setValue(row + 1, col + 1, value);
				this.getCell(row + 1, col + 1).setEditable(editable);
				col++;
				col = col % 9;
				if (col == 0) {
					row++;
				}
				editable = true;
			} else if (c == '_') {
				editable = false;
			}
		}
	}

	public void save(String path) throws IOException {
		FileWriter fw = new FileWriter(path);
		for (int row = 1; row <= 9; row++) {
			for (int col = 1; col <= 9; col++) {
				fw.write(this.getCell(row, col).toString());
			}
			fw.write(System.getProperty("line.separator"));
		}
		fw.close();
	}

	public boolean isComplete() {
		return this.countEmptyCells() == 0;
	}

	public int countEmptyCells() {
		int count = 0;
		for (Cell cell : this.getWritableEmptyCells()) {
			if (cell.isEmpty())
				count++;
		}
		return count;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		String linesep = System.getProperty("line.separator");
		sb.append("+-------+-------+-------+");
		sb.append(linesep);
		for (int row = 1; row <= 9; row++) {
			sb.append("| ");
			for (int col = 1; col <= 9; col++) {
				int val = this.getValue(row, col);
				if (val == 0) {
					sb.append("  ");
				} else {
					sb.append(val);
					sb.append(" ");
				}
				if ((col % 3) == 0 && (col < 9)) {
					sb.append("| ");
				}
			}
			sb.append("|");
			sb.append(linesep);
			if ((row % 3) == 0 && (row < 9)) {
				sb.append("+-------+-------+-------+");
				sb.append(linesep);
			}
		}
		sb.append("+-------+-------+-------+");
		return sb.toString();
	}

	public Sudoku clone() throws CloneNotSupportedException {
		Sudoku sudoku = (Sudoku) super.clone();
		sudoku.grid = new Cell[9][9];
		for (int row = 1; row <= 9; row++) {
			for (int col = 1; col <= 9; col++) {
				sudoku.grid[row - 1][col - 1] = this.getCell(row, col).clone();
				sudoku.getCell(row, col).setSudoku(sudoku);
			}
		}
		return sudoku;
	}

	public void clear() {
		for (Cell cell : this.getWritableCells()) {
			cell.clear();
		}
	}

	public Logger getLogger() {
		return null;
	}
	
	public void copy(Sudoku source) {
		for (int row = 0; row < 9; row++) {
			for (int col = 0; col < 9; col++) {
				Cell cell = this.grid[row][col];
				cell.setEditable(true);
				Cell sourceCell = source.grid[row][col];
				cell.setValue(sourceCell.getValue());
				cell.setEditable(sourceCell.isEditable());
			}
		}
	}
}
