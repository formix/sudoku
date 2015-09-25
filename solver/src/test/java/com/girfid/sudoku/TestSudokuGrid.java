package com.girfid.sudoku;

import java.util.Set;
import java.util.TreeSet;

import org.formix.sudoku.Sudoku;
import org.junit.Assert;
import org.junit.Test;

public class TestSudokuGrid {
	
	@Test
	public void testToString() {
		System.out.println("testToString: ");
		Sudoku grid = new Sudoku();
		System.out.println(grid.toString());
	}
	
	@Test
	public void testNotSameRow() {
		System.out.println("testNotSameRow: ");
		Sudoku grid = new Sudoku();
		grid.setValue(1, 1, 1);
		grid.setValue(1, 2, 2);
		boolean result = grid.setValue(1, 4, 2);
		System.out.println(grid.toString());
		Assert.assertFalse(result);
	}
	
	@Test
	public void testNotSameCol() {
		System.out.println("testNotSameCol: ");
		Sudoku grid = new Sudoku();
		grid.setValue(1, 1, 1);
		grid.setValue(2, 1, 2);
		boolean result = grid.setValue(4, 1, 2);
		System.out.println(grid.toString());
		Assert.assertFalse(result);
	}
	
	@Test
	public void testNotSameSection() {
		System.out.println("testNotSameSection: ");
		Sudoku grid = new Sudoku();
		grid.setValue(1, 1, 1);
		grid.setValue(2, 2, 2);
		boolean result = grid.setValue(3, 3, 2);
		System.out.println(grid.toString());
		Assert.assertFalse(result);
	}
	
	@Test 
	public void testGetPossibleValues() {
		System.out.println("testGetPossibleValues: ");
		Sudoku grid = new Sudoku();
		grid.setValue(1, 1, 1);
		grid.setValue(3, 3, 2);
		grid.setValue(2, 7, 3);
		grid.setValue(4, 2, 4);
		
		Set<Integer> expectedValues = new TreeSet<Integer>();
		expectedValues.add(5);
		expectedValues.add(6);
		expectedValues.add(7);
		expectedValues.add(8);
		expectedValues.add(9);
		
		Set<Integer> values = grid.getAllowedValues(2, 2);
		
		Assert.assertEquals(expectedValues, values);
		
		
		System.out.println(grid.toString());
	}

}
