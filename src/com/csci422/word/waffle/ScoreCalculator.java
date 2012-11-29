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

	public static int calculateScoreEnd(Set<Word> valid_words, Set<Word> invalid_words, int tilesRemaining) {
		int score = calculateScore(valid_words, invalid_words);
    	
    	Vector<Word> sortedWords = new Vector<Word>();
    	sortedWords.addAll(valid_words);
    	Collections.sort(sortedWords);
    	
    	boolean original = true;
    	int shortWords = 0;
    	for(int i = 0; i < sortedWords.size() - 1; i++){
    		if(sortedWords.get(i).compareTo(sortedWords.get(i+1)) == 0){
    			score -= 5; // penalized for repeated words
    			original = false;
    		}
    		if(sortedWords.get(i).word.length() <= 3) shortWords++;
    	}
    	if(sortedWords.size() > 0 && sortedWords.get(sortedWords.size() - 1).word.length() <= 3) shortWords++;
    	
    	if(shortWords == 0 || sortedWords.size() / shortWords > 2) score += 25; //bonus for 50% long words (>3)
    	if(original) score += 15; //+15 for no repeated words
    	
    	if(tilesRemaining == 0) score += 50; // +25 for finishing tray
    	else score -= (tilesRemaining * 3); // penalized for leftoverTiles
    	
    	return score;
	}

}
