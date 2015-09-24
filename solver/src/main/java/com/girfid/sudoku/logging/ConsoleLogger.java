package com.girfid.sudoku.logging;


public class ConsoleLogger implements Logger {

	@Override
	public void info(String text) {
		System.out.println(text);
	}

}
