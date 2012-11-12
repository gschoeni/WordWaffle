package com.csci422.word.waffle;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Random;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

import com.badlogic.androidgames.framework.FileIO;

// This is just a utility class, should not be instantiated
public class Dictionary {
	
	private static char[] alphabet = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};
	private static Random generator = new Random();
	public final static String words_file = "valid_words.txt";
	public static ArrayList<String> validWords = new ArrayList<String>();
	
	// Suppress default constructor for noninstantiability
    private Dictionary() {
        throw new AssertionError();
    }
    
    // All methods on this class should be static
    
    public static char getRandomLetter() {
    	return alphabet[Math.abs(generator.nextInt()%26)];
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
    
    public boolean isValidWord(String s) {
    	// long start = System.nanoTime();
		boolean valid = Dictionary.validWords.contains("xybjhsdka");
		// long elapsedTime = System.nanoTime() - start;
		// Log.d(WordWaffle.DEBUG_TAG, "Finding word in dictionary: "+ elapsedTime);
		return valid;
    }
    
}
