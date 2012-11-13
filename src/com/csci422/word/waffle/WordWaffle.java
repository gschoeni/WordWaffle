package com.csci422.word.waffle;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.util.Log;

import com.badlogic.androidgames.framework.Screen;
import com.badlogic.androidgames.framework.impl.GLGame;

public class WordWaffle extends GLGame {
	
	boolean firstTimeCreate = true;
	public static final String DEBUG_TAG = "WordWaffle";

	@Override
	public Screen getStartScreen() {
		return new GameScreen(this);
	}
	
	@Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {         
        super.onSurfaceCreated(gl, config);
        if(firstTimeCreate) {
            Assets.load(this);
            
            // This takes about a half second to load all the valid words.. 
            // should probably present "ready" screen before this work gets done
            long start = System.nanoTime();    
    		Dictionary.readRawTextFile(getFileIO(), getApplicationContext());
    		long elapsedTime = System.nanoTime() - start;
//    		Log.d(WordWaffle.DEBUG_TAG, "Loading file elapsed time: "+ elapsedTime);
//    		boolean valid = Dictionary.validWords.contains("bdslkaj");
//    		Log.d(WordWaffle.DEBUG_TAG, "After load JOB is "+ valid);
            firstTimeCreate = false;            
        } else {
            Assets.reload();
        }
    }     
    
    @Override
    public void onPause() {
        super.onPause();
    }

}
