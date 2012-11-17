package com.csci422.word.waffle;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
	//public static Letter[][] letters = new Letter[BOARD_WIDTH][BOARD_HEIGHT];
	public static List<Letter> letters = new ArrayList<Letter>();
	public static Set<Word> valid_words = new HashSet<Word>();
	public static Set<Word> invalid_words = new HashSet<Word>();
	public static ArrayList<int[]> letter_chain = new ArrayList<int[]>();
	
	public static final int NUM_LETTERS = 20;
	public static List<Rectangle> letterTray = new ArrayList<Rectangle>();
	public static boolean[] usedTrayLocations = new boolean[NUM_LETTERS];
	
	// these are the touch bounds to slide the letter tray, so they are a bit bigger, ICEBERG
	public static Rectangle slideLeftBounds = new Rectangle(0, 0, 60, 60);
	public static Rectangle slideRightBounds = new Rectangle(280, 0, 40, 60);
	
	public static int score;
	
	public Board() {
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
		int x_offset = 55;
		int y_offset = 35;
		int padding = 10;
		for (int i = 0; i < NUM_LETTERS; i++) {
			Letter l = new Letter(x_offset + i*(Letter.WIDTH+padding), y_offset, Dictionary.getRandomLetter());
			Log.d(WordWaffle.DEBUG_TAG, "New letter: "+l.value);
			letters.add(l);
			
			letterTray.add(new Rectangle(x_offset + i*(Letter.WIDTH+padding), y_offset, Letter.WIDTH, Letter.HEIGHT));
			usedTrayLocations[i] = true;
		}
	}
	
	public void checkDraggingLetter(TouchEvent event, Vector2 touchPoint) {
		for (Letter l : letters) {
			// letter was tapped, will move if it is being dragged
			if (event.type == TouchEvent.TOUCH_DOWN && OverlapTester.pointInRectangle(l.bounds, touchPoint)) {
	        	l.state = Letter.IS_BEING_DRAGGED;
	        	usedTrayLocations[letters.indexOf(l)] = false; //source of error
	        	//Log.d(WordWaffle.DEBUG_TAG, "Index set to false -- " + letters.indexOf(l));
	        	for(int i = 0; i < NUM_LETTERS; i++)       		
	        		//Log.d(WordWaffle.DEBUG_TAG, "Tray " + i + ": " + usedTrayLocations[i]);
	        	
	        	// if this piece is in a valid board location it has now been picked up
	        	// so set the value for that square back to ' '
	        	if (l.col > -1 && l.row > -1) {
	        		letterLocations[l.row][l.col] = ' ';
	        	}
	        	break;
	        }
	        if (event.type == TouchEvent.TOUCH_DRAGGED && l.state == Letter.IS_BEING_DRAGGED) {
	        	l.setLocation(touchPoint.x, touchPoint.y, -1, -1);
	        } else if (event.type == TouchEvent.TOUCH_UP && l.state == Letter.IS_BEING_DRAGGED) {
	        	checkValidBoardSpace(l);
	        	checkForValidWords();
	        	calculateScore();
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
					l.state = Letter.INVALID_LOCATION;
				} else if(overLappingRect) {
					placeLetterInClosestValidLocation(i, j, l);
				} else if (outsideWaffleTop(l)) {
					placeLetterInTraySpace(l, false);
				} else if (outsideWaffleBottom(l)) {
					placeLetterInTraySpace(l, true);
				}
			}
		}
	}

	// This method assumes the player tried to put a letter in a space that is already taken
	// So it checks if top, right, bottom, or left are taken, and places the square in the first position  
	// that is not taken
	private void placeLetterInClosestValidLocation(int row, int col, Letter l) {
		// will be placed on board or it's state will be set to back in tray below
		l.state = Letter.INVALID_LOCATION;
		
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
		// put back in letter tray
		else {
			placeLetterInTraySpace(l,false);
		}
		
	}
	
	private boolean outsideWaffleTop(Letter l) {
		return l.position.y > 390;
	}
	
	private boolean outsideWaffleBottom(Letter l){
		return l.position.y < 80;
	}
	
	//This method places in tray space if it is empty
	private void placeLetterInTraySpace(Letter l, boolean placeBack) {
		int loc;
		for (loc = 0; loc < NUM_LETTERS; loc++){
			Log.d(WordWaffle.DEBUG_TAG, "Tray Location: "+usedTrayLocations[loc]);
			Log.d(WordWaffle.DEBUG_TAG, "Overlaptester: "+OverlapTester.pointInRectangle(letterTray.get(loc), l.position));
			if(placeBack){ //user drags object to space
				if (usedTrayLocations[loc] == false && OverlapTester.pointInRectangle(letterTray.get(loc), l.position)){
					Log.d(WordWaffle.DEBUG_TAG, "Placing in empty space");
					break;
				}
			}
			else{ //user drags object off board
				if (usedTrayLocations[loc] == false) break;
			}
		}
		l.setLocation(letterTray.get(loc).lowerLeft.x, letterTray.get(loc).lowerLeft.y, -1, -1);
		l.state = Letter.IN_TRAY;
		usedTrayLocations[loc] = true;	
	}
	
	public boolean checkSlideLettersTray(TouchEvent event, Vector2 touchPoint) { 
		if (event.type == TouchEvent.TOUCH_DOWN && OverlapTester.pointInRectangle(slideLeftBounds, touchPoint)) {
			slideLetters(1);
			return true;
		} else if (event.type == TouchEvent.TOUCH_DOWN && OverlapTester.pointInRectangle(slideRightBounds, touchPoint)) {
			slideLetters(-1);
			return true;
		}
		return false;
	}
	
	// direction should be -1 or 1, depending on if we are sliding left or right
	private void slideLetters(int direction) {
		Log.d(WordWaffle.DEBUG_TAG, "Slide");
		int how_much = 50*direction;
		// dont let it slide too far..
		if (letterTray.get(0).lowerLeft.x + how_much > 100 || letterTray.get(letterTray.size() - 1).lowerLeft.x + how_much < 240)
			return;
		for (int i = 0; i < NUM_LETTERS; i++) {
			Letter l = letters.get(i);
			Rectangle r = letterTray.get(i);
			r.lowerLeft.x += how_much;
			if (l.state != Letter.IN_TRAY) // only move the letters in the tray
				continue;
			l.setLocation(l.position.x + how_much, l.position.y, -1, -1);
		}
	}
	
	public void checkForValidWords() {
		Log.d(WordWaffle.DEBUG_TAG, "Check Valid Words");
		String valid_vertical = "";
		String valid_horizontal = "";
		valid_words.clear();
		invalid_words.clear();
		letter_chain.clear();
		
		// Check Horizontal
		boolean horizontal = true;
		for (int i = 0; i < BOARD_WIDTH; i++) {
			for (int j = 0; j < BOARD_HEIGHT; j++) {
				if (letterLocations[j][i] != ' ') {
					valid_horizontal += letterLocations[j][i];
					int[] a = {j, i};
					letter_chain.add(a);
				} else if (letterLocations[j][i] == ' ' && valid_horizontal != "") {
					// case where we have a valid word and the next spot is blank
					checkIfWordIsValid(valid_horizontal, horizontal);
					valid_horizontal = "";
					letter_chain.clear();
				} else {
					valid_horizontal = "";
					letter_chain.clear();
				}
			}
			// case where we are at the end of the row and have a valid word
			checkIfWordIsValid(valid_horizontal, horizontal);
			valid_horizontal = "";
			letter_chain.clear();
		}
		
		
		// Check Vertical
		horizontal = false;
		for (int i = 0; i < BOARD_WIDTH; i++) {
			for (int j = BOARD_HEIGHT - 1; j >= 0; j--) {
				if (letterLocations[i][j] != ' ') {
					valid_vertical += letterLocations[i][j];
					int[] a = {i, j};
					letter_chain.add(a);
				} else if (letterLocations[i][j] == ' ' && valid_vertical != "") {
					// case where we have a valid word and the next spot is blank
					checkIfWordIsValid(valid_vertical, horizontal);
					valid_vertical = "";
					letter_chain.clear();
				} else {
					valid_vertical = "";
					letter_chain.clear();
				}
			}
			// case where we are at the end of the column and have a valid word
			checkIfWordIsValid(valid_vertical, horizontal);
			valid_vertical = "";
			letter_chain.clear();
		}
		
		Log.d(WordWaffle.DEBUG_TAG, "Found Words: "+valid_words);
		Log.d(WordWaffle.DEBUG_TAG, "Invaid Words: "+invalid_words);
		setValidAndInvalidLetters();
	}
	
	private void checkIfWordIsValid(String string_to_check, boolean isHorizontal) {
		if (Dictionary.isValidWord(string_to_check)) {
			valid_words.add(new Word(string_to_check, letter_chain, isHorizontal));
		} else if(string_to_check.length() > 1 && letter_chain.size() > 0) {
			invalid_words.add(new Word(string_to_check, letter_chain, isHorizontal));
		}
	}
	
	private void setValidAndInvalidLetters() {
		// assume all letters are invalid
		for (Letter letter : letters) {
			if (letter.state == Letter.VALID_LOCATION)
				letter.state = Letter.INVALID_LOCATION;
		}
		
		// Letters know where they are on the board,
		// valid_words and invalid_words know about the words that are valid/invalid and where they are.
		// I can't think of a better way to do this without scraping the data structures we have right now
		// but that's okay it works..
		
		// check the valid words we have found and set valid letters appropriately
		for (Letter letter : letters) {
			for (Word w : valid_words) {
				for (int[] letter_location : w.board_locations) {
					if (letter.row == letter_location[0] && letter.col == letter_location[1]) {
						//Log.d(WordWaffle.DEBUG_TAG, "Color Locations row: "+letter_location[0] + " col: " + letter_location[1]);
						letter.state = Letter.VALID_LOCATION;
					} 
				}
			}
		}
		
		// check if a letter that is in a valid word is also in an invalid word
		for (Letter letter : letters) {
			for (Word w : invalid_words) {
				for (int[] letter_location : w.board_locations) {
					if (letter.row == letter_location[0] && letter.col == letter_location[1]) {
						//Log.d(WordWaffle.DEBUG_TAG, "Color Locations row: "+letter_location[0] + " col: " + letter_location[1]);
						letter.state = Letter.INVALID_LOCATION;
					} 
				}
			}
		}
	}
	
	private void calculateScore() {
		score = ScoreCalculator.calculateScore(valid_words, invalid_words);
	}
	

}
