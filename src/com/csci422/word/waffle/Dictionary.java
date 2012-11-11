package com.csci422.word.waffle;

import java.util.Random;

// This is just a utility class, should not be instantiated
public class Dictionary {
	
	private static char[] alphabet = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};
	private static Random generator = new Random();
	
	// Suppress default constructor for noninstantiability
    private Dictionary() {
        throw new AssertionError();
    }
    
    // All methods on this class should be static
    public static char getRandomLetter() {
    	return alphabet[Math.abs(generator.nextInt()%26)];
    }
}
