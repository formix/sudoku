package com.girfid.sudoku;

import org.junit.Test;

public class TestSolver {

	@Test
	public void testEvilSolver() {
		Solver solver = new Solver("grids/easy1.txt");
		solver.run();
	}
	
}
