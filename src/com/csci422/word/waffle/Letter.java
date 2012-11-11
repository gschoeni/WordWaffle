package com.csci422.word.waffle;

import com.badlogic.androidgames.framework.GameObject;

public class Letter extends GameObject {

	public boolean isBeingDragged = false;
	public char value;
	public int row = -1;
	public int col = -1;
	public static final int WIDTH = 40;
	public static final int HEIGHT = 40;
	
	public Letter(float x, float y, char value) {
		super(x, y, WIDTH, HEIGHT);
		this.value = value;
	}
	
	public void setLocation(float x, float y, int r, int c) {
		position.set(x, y);
    	bounds.setLowerLeft(x, y);
    	row = r;
    	col = c;
	}
	
	public String toString() {
		return ""+value;
	}

}
