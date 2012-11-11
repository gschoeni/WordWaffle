package com.badlogic.androidgames.framework.math;

public class Rectangle {
    public Vector2 lowerLeft;
    public float width, height;
    
    public Rectangle(float x, float y, float width, float height) {
        this.lowerLeft = new Vector2(x,y);
        this.width = width;
        this.height = height;
    }
    
    public void setLowerLeft(float x, float y) {
    	this.lowerLeft.x = x - width/2;
    	this.lowerLeft.y = y - height/2;
    }
    
    public String toString() {
    	return "Lower Left x:" + lowerLeft.x + " y: " + lowerLeft.y + " Width: " + width + " Height: " + height;
    }
}
