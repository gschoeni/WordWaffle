package com.csci422.word.waffle;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Set;
import java.util.Vector;

public class ScoreCalculator {
	
	// each index in this array represents the point value for that length word
	// for example: 3 letter words are worth 3 point, 4 letter words are 5 points etc
	private static int[] pointsForLengths = {
		0,
		0,
		1,
		3,
		5,
		8,
		10,
		15
	};
	
	private static int[] pointsForLetters = {
		1, // A
		3, // B
		4, // C
		2, // D
		1, // E
		4, // F
		3, // G
		3, // H
		1, // I
		10, // J
		5, // K
		1, // L
		3, // M
		1, // N
		1, // O
		4, // P
		10, // Q
		1, // R
		1, // S
		1, // T
		2, // U
		5, // V
		4, // W
		10, // X
		4, // Y
		10, // Z
	};
	
	// Suppress default constructor for noninstantiability
    private ScoreCalculator() {
        throw new AssertionError();
    }
    
    public static int calculateBaseScore(Set<Word> valid_words, Set<Word> invalid_words) {
    	int score = 0;
    	int word_score = 0;
    	for (Word w : valid_words) {
    		word_score = 0;
    		for (int i = 0; i < w.word.length(); i++) word_score += pointsForLetters[(w.charAt(i) - 'A')];
    		score += pointsForLengths[w.word.length()] + word_score;
    	}
    	return score;
    }

	public static int[] calculateScoreEnd(Set<Word> valid_words, Set<Word> invalid_words, int tilesRemaining) {
		/* 
		 * Score array will hold scores needed to display final score
		 * 0 -> Final Score
		 * 1 -> Long Word Bonus
		 * 2 -> Finished Tray Bonus
		 * 3 -> No Duplicate Words
		 * 4 -> Unused Tiles
		 * 5 -> Invalid Words
		 */
		int[] score = new int[6];
		score[0] = calculateBaseScore(valid_words, invalid_words);
		score[1] = 0;
		score[2] = 0;
		score[3] = 0;
		score[4] = 0;
		score[5] = 0;
    	
    	Vector<Word> sortedWords = new Vector<Word>();
    	sortedWords.addAll(valid_words);
    	Collections.sort(sortedWords);
    	
    	boolean original = true;
    	int shortWords = 0;
    	for(int i = 0; i < sortedWords.size() - 1; i++){
    		if(sortedWords.get(i).compareTo(sortedWords.get(i+1)) == 0){
    			// Took this out for now.. don't really like having negative scores.
    			// score[0] -= 5; // penalized for repeated words
    			// score[3] -= 5;
    			original = false;
    		}
    		if(sortedWords.get(i).word.length() <= 3) shortWords++;
    	}
    	if(sortedWords.size() > 0 && sortedWords.get(sortedWords.size() - 1).word.length() <= 3) shortWords++;
    	
    	if(shortWords == 0 || sortedWords.size() / shortWords > 2) {
    		score[0] += 25; //bonus for 50% long words (>3)
    		score[1] += 25;
    	}
    	if(original) {
    		score[0] += 15; //+15 for no repeated words
    		score[3] += 15;
    	}
    	
    	if(tilesRemaining == 0) {
    		score[0] += 50; // +25 for finishing tray
    		score[2] += 50;
    	}
    	else {
    		// Made penalty less severe for left over tiles, open for debate
    		score[0] -= (tilesRemaining); // penalized for leftoverTiles
    		score[4] += -1 * (tilesRemaining);
    	}
    	
    	for (Word w : invalid_words) {
    		score[0] -= pointsForLengths[w.word.length()];
    		score[5] -= pointsForLengths[w.word.length()];
    	}
    	
    	return score;
	}

}
