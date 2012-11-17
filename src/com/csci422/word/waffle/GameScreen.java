package com.csci422.word.waffle;

import java.util.List;

import javax.microedition.khronos.opengles.GL10;

import android.content.Context;

import com.badlogic.androidgames.framework.Game;
import com.badlogic.androidgames.framework.Input.TouchEvent;
import com.badlogic.androidgames.framework.gl.Camera2D;
import com.badlogic.androidgames.framework.gl.SpriteBatcher;
import com.badlogic.androidgames.framework.impl.GLScreen;
import com.badlogic.androidgames.framework.math.Vector2;

public class GameScreen extends GLScreen {

	Camera2D guiCam;
	SpriteBatcher batcher;
	GameRenderer renderer;
	Board board;
	Vector2 touchPoint;
	private float timeLeft = 120.0f;
	public static String time;
	
	public GameScreen(Game game) {
		super(game);
		guiCam = new Camera2D(glGraphics, 320, 480);
		batcher = new SpriteBatcher(glGraphics, 1000);
		board = new Board();
		renderer = new GameRenderer(glGraphics, batcher, board);
		touchPoint = new Vector2();
	}

	@Override
	public void update(float deltaTime) {
		// We will want to do different things depending on the game state, but we don't have game states yet so hold your horses
		calcTimeLeft(deltaTime);
		List<TouchEvent> touchEvents = game.getInput().getTouchEvents();
		for(int i = 0; i < touchEvents.size(); i++) {
	        TouchEvent event = touchEvents.get(i);
	        // the points returned are backward in openGL land so we need to convert them to our coordinate space
	        touchPoint = touchPoint.getGLCoords(glGraphics, touchPoint, event.x, event.y, GameRenderer.FRUSTUM_WIDTH, GameRenderer.FRUSTUM_HEIGHT);
	        if(board.checkSlideLettersTray(event, touchPoint)) {
	        	// we are trying to slide the letters in the letters tray
	        	break;
	        }
	        board.checkDraggingLetter(event, touchPoint);
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
	
	private void calcTimeLeft(float deltaTime) {
		timeLeft -= deltaTime;
		if (timeLeft < 60) {
			time = "0:";
		} else {
			time = "1:";
		}
		
		if (timeLeft % 60 < 10) {
			time += "0";
		}
		time += String.format("%.0f", timeLeft % 60);
	}

}
