package com.csci422.word.waffle;

import com.badlogic.androidgames.framework.GameObject;

public class Letter extends GameObject {

	public int state = 0;
	public static final int IN_TRAY = 0;
	public static final int IS_BEING_DRAGGED = 1;
	public static final int INVALID_LOCATION = 2;
	public static final int VALID_LOCATION = 3;
	public char value;
	public int row;
	public int col;
	public static final int WIDTH = 43;
	public static final int HEIGHT = 43;
	
	public Letter(float x, float y, char value) {
		super(x, y, WIDTH, HEIGHT);
		this.value = value;
		this.row = -1;
		this.col = -1;
	}
	
	public boolean equals(Letter l) {
		return l.value == value;
	}
	
	public void setLocation(float x, float y, int r, int c) {
		position.set(x, y);
    	bounds.setLowerLeft(x, y);
    	row = r;
    	col = c;
	}
	
	public String toString() {
		//return "Value: "+value+" row: "+row+" col: "+col;
		return ""+value;
	}

}
