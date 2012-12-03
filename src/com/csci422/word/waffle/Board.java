package com.csci422.word.waffle;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import android.util.Log;

import com.badlogic.androidgames.framework.Input.TouchEvent;
import com.badlogic.androidgames.framework.math.OverlapTester;
import com.badlogic.androidgames.framework.math.Rectangle;
import com.badlogic.androidgames.framework.math.Vector2;

public class Board{
	
	public static final float WORLD_WIDTH = 320;
	public static final float WORLD_HEIGHT = 480;
	public static final int BOARD_WIDTH = 7;
	public static final int BOARD_HEIGHT = 7;
	public static final int NUM_LETTERS = 20;
	private float timeLeft = 120.0f;
	
	public static int base_score;
	public static int[] final_score; // will hold all the components of the final score for to be accessed by GameRenderer
	public static String time;
	
	public static final int GAME_READY = 0;
	public static final int GAME_RUNNING = 1;
	public static final int GAME_PAUSED = 2;
	public static final int GAME_OVER = 3;
	
	private char[][] letterLocations;
	public static Rectangle[][] validBoardSpaces;
	public static List<Letter> letters;
	public static Set<Word> valid_words;
	public static Set<Word> invalid_words;
	public static ArrayList<int[]> letter_chain;
	
	public static List<TraySpace> letterTray;
	
	public static Rectangle slideLeftBounds;
	public static Rectangle slideRightBounds;
	
	public int state;
	
	public Board() {
		state = Board.GAME_READY;
		initDataStructures();
		initBoardSpaces();
		initLetters();
		base_score = 0;
	}
	
	private void initDataStructures() {
		letterLocations = new char[BOARD_WIDTH][BOARD_HEIGHT];
		validBoardSpaces = new Rectangle[BOARD_WIDTH][BOARD_HEIGHT];
		letters = new ArrayList<Letter>();
		valid_words = new HashSet<Word>();
		invalid_words = new HashSet<Word>();
		letter_chain = new ArrayList<int[]>();
		letterTray = new ArrayList<TraySpace>();
		slideLeftBounds = new Rectangle(0, 0, 50, 60);
		slideRightBounds = new Rectangle(280, 0, 40, 60);
		time = "2:00";
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
			letters.add(l);
			letterTray.add(new TraySpace(x_offset + i*(Letter.WIDTH+padding), y_offset, Letter.WIDTH, Letter.HEIGHT));

		}
	}
	
	public void update(float deltaTime) {
		calcTimeLeft(deltaTime);
		checkGameOver();
	}
	
	public void checkDraggingLetter(TouchEvent event, Vector2 touchPoint) {
		for (int i = 0; i < letters.size(); i++) {
			Letter l = letters.get(i);
			// letter was tapped, will move if it is being dragged
			if (event.type == TouchEvent.TOUCH_DOWN && OverlapTester.pointInRectangle(l.bounds, touchPoint)) {
	        	l.state = Letter.IS_BEING_DRAGGED;
	        	// if this piece is in a valid board location it has now been picked up
	        	// so set the value for that square back to ' '
	        	if (l.col > -1 && l.row > -1) {
	        		letterLocations[l.row][l.col] = ' ';
	        	} 
	        	// we got it from the letterTray
	        	else { 
	        		letterLeftTray(l);
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
		boolean break_both = false;
		for (int i = 0; i < BOARD_WIDTH; i++) {
			for (int j = 0; j < BOARD_HEIGHT; j++) {
				Rectangle r = validBoardSpaces[i][j];
				// place the letter in the right location if we are over that square
				boolean overLappingRect = OverlapTester.pointInRectangle(r, l.position.x + l.bounds.width/2, l.position.y + l.bounds.height/2);
				if (letterLocations[i][j] == ' '  && overLappingRect) {
					l.setLocation(r.lowerLeft.x, r.lowerLeft.y, i, j);
					letterLocations[i][j] = l.value;
					l.state = Letter.INVALID_LOCATION;
					break_both = true;
					break;
				} else if(overLappingRect) {
					placeLetterInClosestValidBoardLocation(i, j, l);
					break_both = true;
					break;
				} else if (outsideWaffleBottom(l) || outsideWaffleTop(l)) {
					placeLetterInTraySpace(l);
					break_both = true;
					break;
				}
			}
			if (break_both) break;
		}
	}

	// This method assumes the player tried to put a letter in a space that is already taken
	// So it checks if top, right, bottom, or left are taken, and places the square in the first position  
	// that is not taken
	private void placeLetterInClosestValidBoardLocation(int row, int col, Letter l) {
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
			placeLetterInTraySpace(l);
		}
		
	}
	
	private boolean outsideWaffleTop(Letter l) {
		return l.position.y > 390;
	}
	
	private boolean outsideWaffleBottom(Letter l){
		return l.position.y < 80;
	}

	private void placeLetterInTraySpace(Letter l) {
		float pos = l.position.x;
		for (TraySpace t : letterTray) {
			if (pos > t.getRect().lowerLeft.x-10 && pos < t.getRect().lowerLeft.x + t.getRect().width) {
				if (t.isUsed()) {
					boolean was_empty;
					for (int i = letterTray.indexOf(t); i < letterTray.size()-1; i++) {
						if (letters.get(i).state != Letter.IN_TRAY) continue;
						was_empty = letterTray.get(i + 1).isEmpty();
						letters.get(i).setLocation(letterTray.get(i + 1).getRect().lowerLeft.x, letterTray.get(i + 1).getRect().lowerLeft.y, -1, -1);
						letterTray.get(i + 1).setUsed(true);
						if(was_empty) break;
					}
					Letter old = letters.remove(letters.indexOf(l));
					letters.add(letterTray.indexOf(t), old);
				} else {
					Collections.swap(letters, letterTray.indexOf(t), letters.indexOf(l));
				}
				l.setLocation(t.getRect().lowerLeft.x, t.getRect().lowerLeft.y, -1, -1);
				l.state = Letter.IN_TRAY;
				t.setUsed(true);
				break;
			}
		}
		//for (Letter let : letters) Log.d(WordWaffle.DEBUG_TAG, "After Letter index: "+letters.indexOf(let)+" "+let);
		//for (TraySpace t : letterTray) Log.d(WordWaffle.DEBUG_TAG, "After Tray Space: "+letterTray.indexOf(t)+" "+t.isEmpty());	
	}
	
	// This will tighten up the tray by moving all the letters past the letter that is chosen to the left
	private void letterLeftTray(Letter l) {
		int index = letters.indexOf(l) + 1;
		for (int i = index; i < numTilesLeft()+1; i++) {
			if (letters.get(i).state != Letter.IN_TRAY) continue;
			letters.get(i).setLocation(letterTray.get(i - 1).getRect().lowerLeft.x, letterTray.get(i - 1).getRect().lowerLeft.y, -1, -1);
			Collections.swap(letters, i - 1, i);
		}
		Collections.swap(letters, letters.indexOf(l), numTilesLeft());
		letterTray.get(letters.indexOf(l)).setUsed(false);
		//for (Letter let : letters) Log.d(WordWaffle.DEBUG_TAG, "Before Letter index: "+letters.indexOf(let)+" "+let);
		//for (TraySpace t : letterTray) Log.d(WordWaffle.DEBUG_TAG, "Before Tray Space: "+letterTray.indexOf(t)+" "+t.isEmpty());
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
		if (letterTray.get(0).getRect().lowerLeft.x + how_much > 100 || letterTray.get(letterTray.size() - 1).getRect().lowerLeft.x + how_much < 240)
			return;
		for (int i = 0; i < letterTray.size(); i++) {
			Letter l = letters.get(i);
			Rectangle r = letterTray.get(i).getRect();
			r.lowerLeft.x += how_much;
			if (l.state != Letter.IN_TRAY) // only move the letters in the tray
				continue;
			l.setLocation(l.position.x + how_much, l.position.y, -1, -1);
		}
	}
	
	public void checkForValidWords() {
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
		Log.d(WordWaffle.DEBUG_TAG, "Invalid Words: "+invalid_words);
		colorValidLetters();
	}
	
	private void checkIfWordIsValid(String string_to_check, boolean isHorizontal) {
		Log.d(WordWaffle.DEBUG_TAG, "Is connected to star?: "+isConnectedToStar(new Word(string_to_check, letter_chain, isHorizontal)));
		
		//checks if in dictionary and tile is on star space
		if (Dictionary.isValidWord(string_to_check) && letterLocations[3][3] != ' '){
			valid_words.add(new Word(string_to_check, letter_chain, isHorizontal));
		} else if(string_to_check.length() > 1 && letter_chain.size() > 0){
			invalid_words.add(new Word(string_to_check, letter_chain, isHorizontal));
		}
		
	}
	
	private boolean isConnectedToStar(Word word) {
		for (Word w : valid_words) {
			
		}
		
		return false;
	}
	
	private void colorValidLetters() {
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
		base_score = ScoreCalculator.calculateBaseScore(valid_words, invalid_words);
	}
	
	private void calcTimeLeft(float deltaTime) {
		timeLeft -= deltaTime;
		if (timeLeft < 60) {
			time = "0:";
		} else {
			time = "1:";
		}
		double seconds = Math.floor(timeLeft % 60);
		if (seconds < 10) {
			time += "0";
		}
		time += String.format("%.0f", seconds);
	}
	
	private int numTilesLeft() {
		int numTilesLeft = 0;
		for (Letter l : letters) if (l.state == Letter.IN_TRAY) numTilesLeft++;
		return numTilesLeft;
	}
	
	private void checkGameOver() {
		if (timeLeft <= 0) {
			state = Board.GAME_OVER;
			
			
			final_score = ScoreCalculator.calculateScoreEnd(valid_words, invalid_words, numTilesLeft());
			
			//looks at final score and places top 5 high scores in memory
			List<Integer> allScores = new ArrayList<Integer>();
			for(int i : MainMenu.getHighScores()) 
				allScores.add(i); //adds previous scores to list
			allScores.add(final_score[0]); //adds new score to list
			Collections.sort(allScores); //sort in ascending order
			if(allScores.size() > 5) allScores.remove(0); //removes smallest score if over number limit
			
			int[] newScores = new int[allScores.size()];
			for(int a = 0; a < newScores.length; a++) newScores[a] = allScores.get(a);
			MainMenu.saveHighScores(newScores);
		}
		else return;
	}
	

}