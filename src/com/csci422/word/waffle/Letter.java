package com.csci422.word.waffle;

import com.badlogic.androidgames.framework.GameObject;

public class Letter extends GameObject {

	public boolean isBeingDragged = false;
	
	public Letter(float x, float y, float width, float height) {
		super(x, y, width, height);
	}
	
	public void setLocation(float x, float y) {
		position.set(x, y);
    	bounds.setLowerLeft(x, y);
	}

}
