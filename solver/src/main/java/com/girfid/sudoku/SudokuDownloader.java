package com.girfid.sudoku;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class SudokuDownloader {

	private static final String WEBSUDOKU_URL = "http://like.websudoku.com/?level=";

	private SudokuDownloader() {
	}

	public static Sudoku createFromWeb(int level) {
		if ((level < 1) || (level > 4)) {
			throw new IllegalArgumentException("level argument must be "
					+ "between 1 and 4 inclusively.");
		}

		SudokuData data = getSudokuData(level);
		Sudoku sudoku = new Sudoku(data.toGridFormat());
		sudoku.clear();
		return sudoku;
	}

	public static SudokuData getSudokuData(int level) {

		if ((level < 1) || (level > 4)) {
			throw new IllegalArgumentException("level argument must be "
					+ "between 1 and 4 inclusively.");
		}

		BufferedReader br = null;

		try {

			String line = null;
			SudokuData sudokuData = new SudokuData(level);

			Pattern pidPattern = Pattern.compile(".*pid='([0-9]+)'.*");
			Pattern dataPattern = Pattern.compile(".+([1-9]{81}).+");
			Pattern maskPattern = Pattern.compile(".+([01]{81}).+");

			URL url = new URL(WEBSUDOKU_URL + level);
			br = new BufferedReader(new InputStreamReader(url.openStream()));
			while (((line = br.readLine()) != null) && !sudokuData.isComplete()) {
				Matcher pidMatcher = pidPattern.matcher(line);
				if (pidMatcher.matches()) {
					long pid = Long.parseLong(pidMatcher.group(1));
					sudokuData.setPid(pid);
				}

				Matcher dataMatcher = dataPattern.matcher(line);
				if (dataMatcher.matches()) {
					String data = dataMatcher.group(1);
					sudokuData.setData(data);
				}

				Matcher maskMatcher = maskPattern.matcher(line);
				if (maskMatcher.matches()) {
					String mask = maskMatcher.group(1);
					sudokuData.setMask(mask);
				}
			}

			return sudokuData;

		} catch (MalformedURLException mue) {
			mue.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				br.close();
			} catch (IOException ioe) {
				ioe.printStackTrace();
			}
		}

		return null;
	}
}
