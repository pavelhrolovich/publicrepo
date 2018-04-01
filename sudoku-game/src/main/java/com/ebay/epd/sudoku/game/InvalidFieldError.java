package com.ebay.epd.sudoku.game;

public class InvalidFieldError {

	private int x;
	private int y;
	
	public InvalidFieldError(int x, int y) {
		super();
		this.x = x;
		this.y = y;
	}
	public int getX() {
		return x;
	}
	public int getY() {
		return y;
	}
}
