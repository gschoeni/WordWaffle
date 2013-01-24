package com.curiousinspiration.wordwaffle;

import java.util.Set;

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
	
	public static int[] pointsForLetters = {
		1, // A
		3, // B
		4, // C
		2, // D
		1, // E
		4, // F
		3, // G
		3, // H
		1, // I
		9, // J
		5, // K
		1, // L
		3, // M
		1, // N
		1, // O
		4, // P
		9, // Q
		1, // R
		1, // S
		1, // T
		2, // U
		5, // V
		4, // W
		9, // X
		4, // Y
		9, // Z
	};
		
	// Suppress default constructor for noninstantiability
    private ScoreCalculator() {
        throw new AssertionError();
    }
    
    public static int calculateBaseScore(Set<Word> valid_words, Set<Word> invalid_words) {
    	int score = 0;
    	int word_score = 0;
    	int tripleWord = 1;
    	for (Word w : valid_words) {
    		tripleWord = 1;
    		word_score = 0;
    		for (int i = 0; i < w.word.length(); i++){
    			word_score += pointsForLetters[(w.charAt(i) - 'A')];
    			//check for triple word
				int x = w.board_locations.get(i)[0];
				int y = w.board_locations.get(i)[1];
				if( (x == 0 || x == 6) && (y == 0 || y == 6) ) tripleWord *= 3;
    		}
    		score += (pointsForLengths[w.word.length()] + word_score)*tripleWord;
    	}
    	return score;
    }

	public static int[] calculateScoreEnd(Set<Word> valid_words, Set<Word> invalid_words, int tilesRemaining, float timeLeft) {
		/* 
		 * Score array will hold scores needed to display final score
		 * 0 -> Final Score
		 * 1 -> Time Bonus
		 * 2 -> Used all letters Bonus
		 * 3 -> Invalid Letters
		 */
		int[] score = new int[4];
		score[0] = calculateBaseScore(valid_words, invalid_words);
		score[1] = 0;
		score[2] = 0;
		score[3] = 0;
		
		score[0] += timeLeft;
		score[1] += timeLeft;
    	
    	if(tilesRemaining == 0 && invalid_words.isEmpty()) {
    		score[0] += 99; // for finishing tray
    		score[2] += 99;
    	}
    	else {
    		// Made penalty less severe for left over tiles, open for debate
    		score[0] -= (tilesRemaining); // penalized for leftoverTiles
    		score[3] -= (tilesRemaining);
    	}
    	
    	return score;
	}

}
