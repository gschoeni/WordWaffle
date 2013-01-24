package com.curiousinspiration.wordwaffle;

import com.badlogic.androidgames.framework.math.Rectangle;

public class TraySpace {
	
	private Rectangle rect;
	private boolean used;
	
	public TraySpace(float x, float y, float width, float height) {
		this.rect = new Rectangle(x, y, width, height);
		this.used = true;
	}
	
	public void setUsed(boolean used) {
		this.used = used;
	}

	public boolean isUsed() {
		return used;
	}
	
	public boolean isEmpty() {
		return !used;
	}
	
	public Rectangle getRect() {
		return rect;
	}
}
