package com.girfid.sudoku;

import org.junit.Test;

public class TestSudokuDownloader {

	@Test
	public void testGetSudokuData() throws Exception {
		SudokuData sudokuData = SudokuDownloader.getSudokuData(1);
		Sudoku sudoku = new Sudoku(sudokuData.toGridFormat());
		sudoku.clear();
		System.out.println(sudokuData);
		System.out.println(sudokuData.toGridFormat());
		System.out.println(sudoku.toString());
	}
	
}
