package com.csci422.word.waffle;

import java.util.ArrayList;
import java.util.List;

import android.util.Log;

import com.badlogic.androidgames.framework.Input.TouchEvent;
import com.badlogic.androidgames.framework.math.OverlapTester;
import com.badlogic.androidgames.framework.math.Vector2;


public class Board {
	
	public static final float WORLD_WIDTH = 320;
	public static final float WORLD_HEIGHT = 480;
	
	public final List<Letter> letters;
	
	public Board() {
		this.letters = new ArrayList<Letter>();
		initLetters();
	}
	
	private void initLetters() {
		for (int i = 0; i < 5; i++) {
			Letter l = new Letter(10 + i*50, 50, 40, 40);
			letters.add(l);
		}
	}
	
	public void checkDraggingLetter(TouchEvent event, Vector2 touchPoint) {
		for (Letter l : letters) {
			if (event.type == TouchEvent.TOUCH_DOWN && OverlapTester.pointInRectangle(l.bounds, touchPoint)) {
	        	Log.d(WordWaffle.DEBUG_TAG, "letter touched");
	        	l.isBeingDragged = true;
	        	break;
	        }
	        if (event.type == TouchEvent.TOUCH_DRAGGED && l.isBeingDragged) {
	        	l.position.set(touchPoint.x, touchPoint.y);
	        	l.bounds.setLowerLeft(touchPoint.x, touchPoint.y);
	        } else if (event.type == TouchEvent.TOUCH_UP && l.isBeingDragged) {
	        	l.isBeingDragged = false;
	        }
		}
	}

}
