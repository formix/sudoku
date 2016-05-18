package org.formix.sudoku;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

import com.girfid.sudoku.logging.ConsoleLogger;
import com.girfid.sudoku.logging.Logger;

public class Solver implements Runnable {

	private static final int RETRACT_CELL_COUNT = 3;
	private static final int INITIAL_THREASHOLD = 20;

	private String filePath;
	private Logger logger;
	private boolean solving;
	private int anger;

	private static Random random = new Random(Calendar.getInstance()
			.getTimeInMillis());

	public static void main(String[] args) {
		Solver solver = new Solver(args[0]);
		solver.run();
	}

	public Solver() {
		this(new ConsoleLogger());
	}

	public Solver(Logger logger) {
		this(null, logger);
	}

	public Solver(String filePath) {
		this(filePath, new ConsoleLogger());
	}

	public Solver(String filePath, Logger logger) {
		this.filePath = filePath;
		this.logger = logger;
		this.solving = false;
		this.anger = 0;
	}

	public boolean isSolving() {
		return this.solving;
	}

	public void stop() {
		this.solving = false;
	}

	public void run() {
		try {
			Sudoku sudoku = new Sudoku();
			sudoku.load(this.filePath);
			logger.info(Messages.getString("Solver.0")); //$NON-NLS-1$
			logger.info(sudoku.toString());
			Sudoku solvedSudoku = this.solve(sudoku);
			logger.info(solvedSudoku.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public Sudoku solve(Sudoku sudoku) {
		return this.solve(sudoku, new GridUpdaterCallback(Integer.MAX_VALUE) {
			@Override
			public void updateGrid(Sudoku sudoku) {
			}
		});
	}

	public Sudoku solve(Sudoku sudoku, GridUpdaterCallback callBack) {
		long startTime = System.nanoTime();
		Sudoku finalSudoku = this.internalSolve(sudoku, callBack);
		long endTime = System.nanoTime();
		long duration = endTime - startTime;
		double durationTime = duration / 1000000000.0;
		logger.info(String.format(Messages.getString("Solver.1"), //$NON-NLS-1$
				durationTime));
		return finalSudoku;
	}
	
	
	private Sudoku internalSolve(Sudoku sudoku, GridUpdaterCallback callBack) {
		PrintWriter stats = null;
		try {
			this.solving = true;

			stats = new PrintWriter(new FileWriter("stats.txt"));
			

			logger.info(Messages.getString("Solver.11", Calendar
					.getInstance().getTime()));

			Sudoku bestSudoku = sudoku;
			Sudoku currSudoku = bestSudoku.clone();

			int threshold = INITIAL_THREASHOLD;
			stats.println("loop\tthreshold\tanger\tempty_cells");
			
			int loopCount = 0;
			while (!currSudoku.isComplete() && this.solving) {

				int bestEmptyCellCount = bestSudoku.countEmptyCells();
				int currEmptyCellCount = this.fillEmptyCells(currSudoku);

				stats.println(String.format("%d\t%d\t%d\t%d", loopCount, 
						threshold, anger, bestEmptyCellCount));
				
				if (currEmptyCellCount > 0) {
					if (this.anger < threshold) {
						currEmptyCellCount += this.retractWeighted(currSudoku);
					} else {
						currEmptyCellCount += this.retractRandom(currSudoku);
					}
				}

				if (currEmptyCellCount < bestEmptyCellCount) {
					this.anger -= (5);
					logger.info(String.format(
							Messages.getString("Solver.4"), //$NON-NLS-1$
							bestEmptyCellCount, currEmptyCellCount));
				} else if (currEmptyCellCount > bestEmptyCellCount){
					logger.info(String.format(
							Messages.getString("Solver.10"), //$NON-NLS-1$
							bestEmptyCellCount, currEmptyCellCount));
				}
				
				bestSudoku = currSudoku;
				currSudoku = currSudoku.clone();
				
				loopCount++;
				this.anger++;
				
				if (loopCount % 200 == 0) {
					threshold -= 50;
				}
			}
			
			logger.info(String.format(
					Messages.getString("Solver.14"), //$NON-NLS-1$
					loopCount));
			
			callBack.updateGrid(currSudoku.clone());
			return currSudoku;
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			this.solving = false;

			logger.info(Messages.getString("Solver.12", Calendar
					.getInstance().getTime()));
			
			if (stats != null) {
				stats.close();
			}
		}

		return null;
	}	

	private int fillEmptyCells(Sudoku currSudoku) {
		List<Cell> cells = currSudoku.getWritableEmptyCells();
		this.sortCellsByAllowedValueCount(cells);
		for (Cell cell : cells) {
			this.setRandomValue(cell);
		}
		int currEmptyCellCount = currSudoku.countEmptyCells();
		return currEmptyCellCount;
	}

	private boolean setRandomValue(Cell cell) {
		Integer[] values = cell.getAllowedValues().toArray(new Integer[0]);
		if (values.length > 0) {
			int value = values[random.nextInt(values.length)];
			cell.setValue(value);
			return true;
		}
		return false;
	}
	
	private int retractRandom(Sudoku currSudoku) {
		List<Cell> cells = currSudoku.getWritableCells();
		List<Cell> filledCells = new ArrayList<Cell>();
		for (Cell cell : cells) {
			if (cell.getValue() > 0) {
				filledCells.add(cell);
			}
		}
		
		int retractions = RETRACT_CELL_COUNT + (this.anger / 50);
		if (retractions > filledCells.size()) {
			retractions = filledCells.size();
		}
		
		Collections.shuffle(filledCells);
		for (int i = 0; i < retractions; i++) {
			filledCells.get(i).setValue(0);
		}
		
		return retractions;
	}
	

	private void sortCellsByAllowedValueCount(List<Cell> cells) {
		if (cells.size() < 81) {
			Collections.sort(cells, new Comparator<Cell>() {
				public int compare(Cell o1, Cell o2) {
					return o1.getAllowedValues().size()
							- o2.getAllowedValues().size();
				}
			});
		} else {
			// Solving an empty grid shuffle cells
			Collections.shuffle(cells);
		}
	}

	private int retractWeighted(Sudoku currSudoku) {
		
		List<Cell> cells = currSudoku.getWritableCells();
		List<Cell> emptyCells = new ArrayList<Cell>();
		List<WeightedCell> weightedCells = new ArrayList<WeightedCell>();
		WeightedCell[][] wCellTable = this.createWeightedCellTable();
		for (Cell cell : cells) {
			if (cell.getAllowedValues().size() != 0) {
				WeightedCell wcell = new WeightedCell(cell);
				wCellTable[cell.getRow() - 1][cell.getCol() - 1] = wcell;
				weightedCells.add(wcell);
			}
			else {
				emptyCells.add(cell);
			}
		}
		
		for (Cell cell : emptyCells) {
			weightUpSameRow(wCellTable, cell);
			weightUpSameCol(wCellTable, cell);
			weightUpSameSection(wCellTable, cell);
		}
		
		// sort by descending weight
		Collections.sort(weightedCells, new Comparator<WeightedCell>() {
			public int compare(WeightedCell o1, WeightedCell o2) {
				return -(o1.getWeight() - o2.getWeight());
			}
		});
		
		// clear the heaviest cells.
		int retractions = RETRACT_CELL_COUNT + (this.anger / 50);
		if (retractions > weightedCells.size()) {
			retractions = weightedCells.size();
		}
		
		for (int i = 0; i < retractions; i++) {
			weightedCells.get(i).getCell().setValue(0);
		}
		
		return retractions;
	}


	private WeightedCell[][] createWeightedCellTable() {
		WeightedCell[][] wCellTable = new WeightedCell[9][9];
		for (int row = 0; row < wCellTable.length ; row++) {
			for (int col = 0; col < wCellTable[row].length; col++) {
				wCellTable[row][col] = null;
			}
		}
		return wCellTable;
	}

	private void weightUpSameRow(WeightedCell[][] wCellTable, Cell cell) {
		int row = cell.getRow() - 1;
		for (int col = 0; col < 9; col++) {
			WeightedCell wcell = wCellTable[row][col];
			if (wcell != null) {
				wcell.weightUp();
			}
		}
	}

	private void weightUpSameCol(WeightedCell[][] wCellTable, Cell cell) {
		int col = cell.getCol() - 1;
		for (int row = 0; row < 9; row++) {
			WeightedCell wcell = wCellTable[row][col];
			if (wcell != null) {
				wcell.weightUp();
			}
		}
	}

	private void weightUpSameSection(WeightedCell[][] wCellTable, Cell cell) {
		int rowBound = ((cell.getRow() - 1) / 3) * 3;
		int colBound = ((cell.getCol() - 1) / 3) * 3;
		for(int row = rowBound; row < rowBound + 3; row++) {
			for(int col = colBound; col < colBound + 3; col++) {
				WeightedCell wcell = wCellTable[row][col];
				if (wcell != null) {
					wcell.weightUp();
				}
			}
		}
	}


	private static class WeightedCell {
	
		private Cell cell;
		private int weight;
		
		public WeightedCell(Cell cell) {
			this.cell = cell;
			this.weight = 0;
		}
		
		public Cell getCell() {
			return this.cell;
		}
	
		public void weightUp() {
			this.weight++;
		}
		
		public int getWeight() {
			return this.weight * this.cell.getAllowedValues().size();
		}
		
		@Override
		public String toString() {
			return Integer.toString(this.getWeight());
		}
	}
}
