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
	public static final int BOARD_WIDTH = 7;
	public static final int BOARD_HEIGHT = 7;
	private char[][] letterLocations = new char[BOARD_WIDTH][BOARD_HEIGHT];
	public static Rectangle[][] validBoardSpaces = new Rectangle[BOARD_WIDTH][BOARD_HEIGHT];
	
	public final List<Letter> letters;
	
	public Board() {
		this.letters = new ArrayList<Letter>();
		initBoardSpaces();
		initLetters();
	}
	
	private void initBoardSpaces() {
		int space_width = 45;
		int offset_x = 25;
		int offset_y = 95;
		for (int i = 0; i < BOARD_WIDTH; i++) {
			for (int j = 0; j < BOARD_HEIGHT; j++) {
				validBoardSpaces[i][j] = new Rectangle(offset_x + i*space_width, offset_y + j*space_width, space_width, space_width);
				letterLocations[i][j] = ' ';
			}
		}
	}
	
	private void initLetters() {
		int x_offset = 30;
		int y_offset = 35;
		int padding = 10;
		for (int i = 0; i < 6; i++) {
			Letter l = new Letter(x_offset + i*(Letter.WIDTH+padding), y_offset, Dictionary.getRandomLetter());
			Log.d(WordWaffle.DEBUG_TAG, "New letter: "+l.value);
			letters.add(l);
		}
	}
	
	public void checkDraggingLetter(TouchEvent event, Vector2 touchPoint) {
		for (Letter l : letters) {
			// letter was tapped, will move if it is being dragged
			if (event.type == TouchEvent.TOUCH_DOWN && OverlapTester.pointInRectangle(l.bounds, touchPoint)) {
	        	l.isBeingDragged = true;
	        	// if this piece is in a valid board location, set the value for that square back to ' '
	        	if (l.col > -1 && l.row > -1) {
	        		letterLocations[l.row][l.col] = ' ';
	        	}
	        	break;
	        }
	        if (event.type == TouchEvent.TOUCH_DRAGGED && l.isBeingDragged) {
	        	l.setLocation(touchPoint.x, touchPoint.y, -1, -1);
	        } else if (event.type == TouchEvent.TOUCH_UP && l.isBeingDragged) {
	        	l.isBeingDragged = false;
	        	checkValidBoardSpace(l);
	        }
		}
	}
	
	// check if we can place a letter in the spot it was dropped
	private void checkValidBoardSpace(Letter l) {
		for (int i = 0; i < BOARD_WIDTH; i++) {
			for (int j = 0; j < BOARD_HEIGHT; j++) {
				Rectangle r = validBoardSpaces[i][j];
				// place the letter in the right location if we are over that square
				boolean overLappingRect = OverlapTester.pointInRectangle(r, l.position.x + l.bounds.width/2, l.position.y + l.bounds.height/2);
				if (letterLocations[i][j] == ' '  && overLappingRect) {
					l.setLocation(r.lowerLeft.x, r.lowerLeft.y, i, j);
					letterLocations[i][j] = l.value;
				} else if(overLappingRect) {
					placeLetterInClosestValidLocation(i, j, l);
				}
			}
		}
	}
	
	private void placeLetterInClosestValidLocation(int row, int col, Letter l) {
		// Log.d(WordWaffle.DEBUG_TAG, "row: " + row + " col: "+ col + " letter: "+letterLocations[row][col]);
		// check top position
		if (col < BOARD_HEIGHT - 1 && letterLocations[row][col+1] == ' ') {
			l.setLocation(validBoardSpaces[row][col+1].lowerLeft.x, validBoardSpaces[row][col+1].lowerLeft.y, row, col+1);
			// can't figure out why I don't need to set letterLocations[row][col+1] = l.value; here... but it works
		} 
		// check right
		else if (row < BOARD_WIDTH - 1 && letterLocations[row+1][col] == ' ') {
			l.setLocation(validBoardSpaces[row+1][col].lowerLeft.x, validBoardSpaces[row+1][col].lowerLeft.y, row+1, col);
			// can't figure out why I don't need to set letterLocations[row+1][col] = l.value; here... but it works
		}
		// check bottom
		else if (col > 0 && letterLocations[row][col-1] == ' ') {
			l.setLocation(validBoardSpaces[row][col-1].lowerLeft.x, validBoardSpaces[row][col-1].lowerLeft.y, row, col-1);
			letterLocations[row][col-1] = l.value;
		}
		// check left
		else if (row > 0 && letterLocations[row-1][col] == ' ') {
			l.setLocation(validBoardSpaces[row-1][col].lowerLeft.x, validBoardSpaces[row-1][col].lowerLeft.y, row-1, col);
			letterLocations[row-1][col] = l.value;
		} 
		// put back in letter rack
		else {
			l.setLocation(30, 35, -1, -1);
		}
		
	}

}
