package com.csci422.word.waffle;

import com.badlogic.androidgames.framework.GameObject;

public class Letter extends GameObject {

	public boolean isBeingDragged = false;
	public char value;
	
	public Letter(float x, float y, float width, float height, char value) {
		super(x, y, width, height);
		this.value = value;
	}
	
	public void setLocation(float x, float y) {
		position.set(x, y);
    	bounds.setLowerLeft(x, y);
	}
	
	public String toString() {
		return ""+value;
	}

}
