package com.curiousinspiration.wordwaffle;

import com.badlogic.androidgames.framework.math.Rectangle;

public class BoardSpace {
	public Letter letter;
	public Rectangle rect;
	
	public BoardSpace(Rectangle rect) {
		this.letter = null;
		this.rect = rect;
	}
	
	boolean isEmpty() {
		return letter == null;
	}
}
