package com.girfid.sudoku.gui;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.SwingUtilities;

import org.formix.sudoku.Cell;
import org.formix.sudoku.Sudoku;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.IOException;
import java.util.List;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class SudokuGrid extends JComponent {
	private static final long serialVersionUID = 2948869648931709181L;
	private static final Color ERR_CELL_COLOR = new Color(235, 200, 200);
	private static final Color OK_CELL_COLOR = new Color(255, 255, 255);
	private static final Color READ_ONLY_CELL_COLOR = new Color(200, 200, 200);

	private SudokuDigit[][] digits;
	private Sudoku sudokuGrid;
	private boolean compositionMode;

	protected static boolean loading;

	/**
	 * Create the panel.
	 */
	public SudokuGrid() {
		addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				centerGrid(e.getComponent().getSize());
			}
		});
		setLayout(null);
		this.initializeGrid();
		this.sudokuGrid = new Sudoku();
		this.compositionMode = false;
		//this.logger = new JListLogger()
		loading = false;
	}

	public Sudoku getSudokuGrid() {
		return sudokuGrid;
	}

	public boolean isCompositionMode() {
		return compositionMode;
	}

	public void setCompositionMode(boolean compositionMode) {
		this.compositionMode = compositionMode;
		if (compositionMode) {
			this.setBorder(BorderFactory.createLineBorder(Color.BLUE));
		} else {
			this.setBorder(null);
		}
	}

	@Override
	public void setEnabled(boolean enabled) {
		for (int row = 1; row <= 9; row++) {
			for (int col = 1; col <= 9; col++) {
				this.getDigit(row, col).setEnabled(enabled);
			}
		}
		super.setEnabled(enabled);
	}

	public void setSudokuGrid(Sudoku sudokuGrid) {
		loading = true;
		this.clearDigits();
		this.sudokuGrid = sudokuGrid;
		this.refreshDigits();
		loading = false;
	}

	/**
	 * Center the grind in the component and returns the resulting rectangle.
	 * 
	 * @param compSize
	 *            the size of the component.
	 * 
	 * @return The resulting rectangle.
	 */
	protected Rectangle centerGrid(Dimension compSize) {
		this.setIgnoreRepaint(true);
		int squareSize = (SudokuDigit.SIZE - 1) * 9 + 2;
		int dx = (compSize.width - squareSize) / 2;
		int dy = (compSize.height - squareSize) / 2;
		int y = dy;
		int x = dx;
		for (int row = 0; row < 9; row++) {
			for (int col = 0; col < 9; col++) {
				this.digits[row][col].setBounds(x, y, SudokuDigit.SIZE,
						SudokuDigit.SIZE);
				x += SudokuDigit.SIZE - 1;
				if ((col % 3 == 2) && (x > 0)) {
					x++;
				}
			}
			x = dx;
			y += SudokuDigit.SIZE - 1;
			if ((row % 3 == 2) && (y > 0)) {
				y++;
			}
		}
		this.setIgnoreRepaint(false);
		this.repaint();
		return new Rectangle(dx, dy, squareSize, squareSize);
	}

	private void initializeGrid() {
		this.digits = new SudokuDigit[9][9];
		for (int row = 0; row < 9; row++) {
			for (int col = 0; col < 9; col++) {
				SudokuDigit digit = new SudokuDigit();
				this.digits[row][col] = digit;
				digit.setRow(row + 1);
				digit.setCol(col + 1);
				digit.getDocument().addDocumentListener(
						new DigitModifiedHandler(digit) {
							@Override
							public void modified(SudokuDigit digit) {
								if (!loading) {
									updateCellValue(digit);
								}
							}
						});
				digit.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseClicked(MouseEvent e) {
						if (SwingUtilities.isLeftMouseButton(e)
								&& compositionMode) {
							toggleEditable((SudokuDigit) e.getSource());
						}
					}
				});
				this.add(digit);
			}
		}
		Rectangle rect = this.centerGrid(this.getSize());
		Dimension dim = new Dimension((int) rect.getSize().getWidth() + 5,
				(int) rect.getSize().getHeight() + 5);
		this.setMinimumSize(dim);
		this.setPreferredSize(dim);
	}

	protected void toggleEditable(SudokuDigit digit) {
		if (digit.isEditable() && (digit.getValue() == 0))
			return;

		boolean editable = digit.isEditable();
		this.setEditable(digit, !editable);
	}

	public void setEditable(SudokuDigit digit, boolean editable) {
		digit.setEditable(editable);
		int row = digit.getRow();
		int col = digit.getCol();
		Cell cell = sudokuGrid.getCell(row, col);
		cell.setEditable(editable);
		if (digit.isEditable()) {
			Font f = new Font(digit.getFont().getName(), Font.PLAIN, digit
					.getFont().getSize());
			digit.setFont(f);
			digit.setBackground(OK_CELL_COLOR);
		} else {
			Font f = new Font(digit.getFont().getName(), Font.BOLD, digit
					.getFont().getSize());
			digit.setFont(f);
			digit.setBackground(READ_ONLY_CELL_COLOR);
		}
	}

	protected void updateCellValue(SudokuDigit digit) {
		int row = digit.getRow();
		int col = digit.getCol();
		int value = digit.getValue();
		Cell cell = sudokuGrid.getCell(row, col);
		if (cell.isEditable()) {
			boolean success = cell.setValue(value);
			if (success) {
				digit.setBackground(OK_CELL_COLOR);
			} else {
				digit.setBackground(ERR_CELL_COLOR);
			}
		}
	}

	private void clearDigits() {
		for (int row = 1; row <= 9; row++) {
			for (int col = 1; col <= 9; col++) {
				SudokuDigit digit = this.getDigit(row, col);
				setEditable(digit, true);
			}
		}
	}

	private void refreshDigits() {
		for (int row = 1; row <= 9; row++) {
			for (int col = 1; col <= 9; col++) {
				Cell cell = this.sudokuGrid.getCell(row, col);
				SudokuDigit digit = this.getDigit(row, col);
				boolean isEditable = cell.isEditable();
				digit.setEditable(true);
				digit.setText(Integer.toString(cell.getValue()));
				setEditable(digit, isEditable);
			}
		}
	}

	public void load(String absolutePath) throws IOException {
		loading = true;
		this.clearDigits();
		this.sudokuGrid = new Sudoku();
		this.sudokuGrid.load(absolutePath);
		this.refreshDigits();
		loading = false;
	}

	private SudokuDigit getDigit(int row, int col) {
		return this.digits[row - 1][col - 1];
	}

	public void clear() {
		loading = true;
		List<Cell> cells = this.sudokuGrid.getWritableCells();
		for (Cell cell : cells) {
			cell.setValue(0);
		}
		this.refreshDigits();
		loading = false;
	}

}
