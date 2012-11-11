package com.csci422.word.waffle;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

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
