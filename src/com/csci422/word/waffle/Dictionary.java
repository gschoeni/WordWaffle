package com.csci422.word.waffle;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

import com.badlogic.androidgames.framework.FileIO;

// This is just a utility class, should not be instantiated
public class Dictionary {
	
	// There are 100 letters here, so if the distribution isn't giving good enough letters, redistribute appropriately..
	private static char[] alphabet = {
		'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 
		'B', 'B',
		'C', 'C', 
		'D', 'D', 'D', 'D', 'D', 
		'E', 'E', 'E', 'E', 'E', 'E', 'E', 'E', 'E', 'E', 'E', 'E', 
		'F', 'F', 
		'G', 'G', 'G',
		'H', 'H', 
		'I', 'I', 'I', 'I', 'I', 'I', 'I', 'I', 'I', 
		'J', 
		'K', 
		'L', 'L', 'L', 'L', 
		'M', 'M', 
		'N', 'N', 'N', 'N', 'N', 'N', 'N', 
		'O', 'O', 'O', 'O', 'O', 'O', 'O', 'O', 'O', 
		'P', 'P', 
		'Q', 
		'R', 'R', 'R', 'R', 'R', 'R', 'R', 
		'S', 'S', 'S', 'S', 
		'T', 'T', 'T', 'T', 'T', 'T', 
		'U', 'U', 'U', 'U', 
		'V', 'V', 
		'W', 'W', 
		'X', 
		'Y', 'Y', 
		'Z'
	};
	private static Random generator = new Random();
	public final static String words_file = "valid_words.txt";
	public static Set<String> validWords = new TreeSet<String>();
	
	// Suppress default constructor for noninstantiability
    private Dictionary() {
        throw new AssertionError();
    }
    
    // All methods on this class should be static
    
    public static char getRandomLetter() {
    	return alphabet[Math.abs(generator.nextInt()%100)];
    }
    
    public static void readRawTextFile(FileIO files, Context context) {
    	BufferedReader bufferedReader = null;
        try {
        	AssetManager am = context.getAssets();
        	InputStream inputStream = am.open(words_file);
        	InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
        	bufferedReader = new BufferedReader(inputStreamReader);
            String line = null;
            while((line = bufferedReader.readLine()) != null) {
            	validWords.add(line);
            }
            Log.d(WordWaffle.DEBUG_TAG, "Finished Reading File");
        } catch (IOException e) {
        	Log.d(WordWaffle.DEBUG_TAG, "Swallowing IOException "+e);
        } catch (NumberFormatException e) {
        	Log.d(WordWaffle.DEBUG_TAG, "Swallowing NumberFormatException");
        } finally {
        	Log.d(WordWaffle.DEBUG_TAG, "In Finally");
            try {
                if (bufferedReader != null)
                	bufferedReader.close();
            } catch (IOException e) {
            }
        }
    }
    
    public static boolean isValidWord(String s) {
    	//long start = System.nanoTime();
		boolean valid = validWords.contains(s);
		//Log.d(WordWaffle.DEBUG_TAG, "JOB is "+ valid);
		//long elapsedTime = System.nanoTime() - start;
		//Log.d(WordWaffle.DEBUG_TAG, "Finding word in dictionary took: "+ elapsedTime+" ns");
		return valid;
    }
    
}
