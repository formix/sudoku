package org.formix.sudoku;

public class SudokuData {
	private int level;
	private long pid;
	private String data;
	private String mask;

	public SudokuData(int level) {
		this.level = level;
		this.pid = -1;
		this.data = null;
		this.mask = null;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public long getPid() {
		return pid;
	}

	public void setPid(long pid) {
		this.pid = pid;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public String getMask() {
		return mask;
	}

	public void setMask(String mask) {
		this.mask = mask;
	}
	
	public boolean isComplete() {
		return (this.data != null) && (this.mask != null) && (this.pid > -1);
	}

	public String toGridFormat() {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < 81; i++) {
			if ((i > 0) && (i % 9 == 0)) {
				sb.append(System.getProperty("line.separator"));
			}
			if (this.mask.charAt(i) == '0') {
				sb.append('_');
			}
			sb.append(this.data.charAt(i));
		}
		return sb.toString();
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("level:");
		sb.append(this.level);
		sb.append(System.getProperty("line.separator"));
		sb.append("pid:");
		sb.append(this.pid);
		sb.append(System.getProperty("line.separator"));
		sb.append("data:");
		sb.append(this.data);
		sb.append(System.getProperty("line.separator"));
		sb.append("mask:");
		sb.append(this.mask);
		return sb.toString();
	}
}
