package com.csci422.word.waffle;

import java.util.List;

import javax.microedition.khronos.opengles.GL10;

import android.util.Log;

import com.badlogic.androidgames.framework.Game;
import com.badlogic.androidgames.framework.Input.TouchEvent;
import com.badlogic.androidgames.framework.gl.Camera2D;
import com.badlogic.androidgames.framework.gl.SpriteBatcher;
import com.badlogic.androidgames.framework.impl.GLScreen;
import com.badlogic.androidgames.framework.math.OverlapTester;
import com.badlogic.androidgames.framework.math.Rectangle;
import com.badlogic.androidgames.framework.math.Vector2;

public class GameScreen extends GLScreen {

	Camera2D guiCam;
	SpriteBatcher batcher;
	GameRenderer renderer;
	Board board;
	Vector2 touchPoint;
	private boolean isDraggingLetter = false;
	
	public GameScreen(Game game) {
		super(game);
		guiCam = new Camera2D(glGraphics, 320, 480);
		batcher = new SpriteBatcher(glGraphics, 1000);
		board = new Board();
		renderer = new GameRenderer(glGraphics, batcher, board);
		touchPoint = new Vector2();
		Log.d(WordWaffle.DEBUG_TAG, "Init game screen");
	}

	@Override
	public void update(float deltaTime) {
		List<TouchEvent> touchEvents = game.getInput().getTouchEvents();
		for(int i = 0; i < touchEvents.size(); i++) {
	        TouchEvent event = touchEvents.get(i);
	        touchPoint = touchPoint.getGLCoords(glGraphics, touchPoint, event.x, event.y, GameRenderer.FRUSTUM_WIDTH, GameRenderer.FRUSTUM_HEIGHT);
	        Letter l = board.letters.get(0);
	        
	        Log.d(WordWaffle.DEBUG_TAG, "Letter x: "+l.bounds.lowerLeft.x+" y: "+l.bounds.lowerLeft.y);
	        
	        if (event.type == TouchEvent.TOUCH_DOWN && OverlapTester.pointInRectangle(l.bounds, touchPoint)) {
	        	Log.d(WordWaffle.DEBUG_TAG, "letter touched");
	        	isDraggingLetter = true;
	        }
	        if (event.type == TouchEvent.TOUCH_DRAGGED && isDraggingLetter) {
	        	l.position.set(touchPoint.x, touchPoint.y);
	        	l.bounds.setLowerLeft(touchPoint.x, touchPoint.y);
	        } else if (event.type == TouchEvent.TOUCH_UP) {
	        	isDraggingLetter = false;
	        }
	    }
		
	}

	@Override
	public void present(float deltaTime) {
		GL10 gl = glGraphics.getGL();
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		gl.glEnable(GL10.GL_TEXTURE_2D);
		
		renderer.render();
		
		gl.glEnable(GL10.GL_BLEND);
		gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		//batcher.beginBatch(Assets.foregroundTexture);
		
		guiCam.setViewportAndMatrices();

		
		//batcher.endBatch();
		gl.glDisable(GL10.GL_BLEND);
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}

}
