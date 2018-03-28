package org.formix.sudoku.logging;


public class ConsoleLogger implements Logger {

	public void info(String text) {
		System.out.println(text);
	}
	
	public void clear() {
	}

}
