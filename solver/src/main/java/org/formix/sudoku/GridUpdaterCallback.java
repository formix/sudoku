package org.formix.sudoku;



public abstract class GridUpdaterCallback {

	private int refreshRate;
	
	public GridUpdaterCallback(int refreshRate) {
		this.refreshRate = refreshRate;
	}
	
	public int getRefreshRate() {
		return this.refreshRate;
	}
	
	public abstract void updateGrid(final Sudoku sudoku);
}
