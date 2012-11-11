package com.csci422.word.waffle;

import java.util.ArrayList;
import java.util.List;

import android.util.Log;

import com.badlogic.androidgames.framework.Input.TouchEvent;
import com.badlogic.androidgames.framework.math.OverlapTester;
import com.badlogic.androidgames.framework.math.Rectangle;
import com.badlogic.androidgames.framework.math.Vector2;


public class Board {
	
	public static final float WORLD_WIDTH = 320;
	public static final float WORLD_HEIGHT = 480;
	
	public static final float BOARD_WIDTH = 7;
	public static final float BOARD_HEIGHT = 7;
	
	
	public final List<Letter> letters;
	public final List<Rectangle> validBoardSpaces;
	
	public Board() {
		this.letters = new ArrayList<Letter>();
		this.validBoardSpaces = new ArrayList<Rectangle>();
		initBoardSpaces();
		initLetters();
	}
	
	private void initBoardSpaces() {
		int space_width = 45;
		int offset_x = 25;
		int offset_y = 95;
		for (int i = 0; i < BOARD_WIDTH; i++) {
			for (int j = 0; j < BOARD_HEIGHT; j++) {
				validBoardSpaces.add(new Rectangle(offset_x + i*space_width, offset_y + j*space_width, space_width, space_width));
			}
		}
	}
	
	private void initLetters() {
		for (int i = 0; i < 5; i++) {
			Letter l = new Letter(10 + i*50, 50, 40, 40, Dictionary.getRandomLetter());
			Log.d(WordWaffle.DEBUG_TAG, "New letter: "+l.value);
			letters.add(l);
		}
	}
	
	public void checkDraggingLetter(TouchEvent event, Vector2 touchPoint) {
		for (Letter l : letters) {
			if (event.type == TouchEvent.TOUCH_DOWN && OverlapTester.pointInRectangle(l.bounds, touchPoint)) {
	        	l.isBeingDragged = true;
	        	break;
	        }
	        if (event.type == TouchEvent.TOUCH_DRAGGED && l.isBeingDragged) {
	        	l.setLocation(touchPoint.x, touchPoint.y);
	        } else if (event.type == TouchEvent.TOUCH_UP && l.isBeingDragged) {
	        	l.isBeingDragged = false;
	        	checkValidBoardSpace(l);
	        }
		}
	}
	
	private void checkValidBoardSpace(Letter l) {
		for (Rectangle r : validBoardSpaces) {
			if (OverlapTester.pointInRectangle(r, l.position.x + l.bounds.width/2, l.position.y + l.bounds.height/2)) {
				l.setLocation(r.lowerLeft.x, r.lowerLeft.y);
			}
		}
	}

}
