package com.csci422.word.waffle;

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
	
	
	// Suppress default constructor for noninstantiability
    private ScoreCalculator() {
        throw new AssertionError();
    }
    
    public static int calculateScore(Set<Word> valid_words, Set<Word> invalid_words) {
    	int score = 0;
    	for (Word w : valid_words) {
    		score += pointsForLengths[w.word.length()];
    	}
    	return score;
    }

}
