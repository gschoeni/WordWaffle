package com.curiousinspiration.wordwaffle;

import java.util.List;

import javax.microedition.khronos.opengles.GL10;

import com.badlogic.androidgames.framework.Input.TouchEvent;
import com.badlogic.androidgames.framework.gl.Camera2D;
import com.badlogic.androidgames.framework.gl.SpriteBatcher;
import com.badlogic.androidgames.framework.impl.GLScreen;
import com.badlogic.androidgames.framework.math.OverlapTester;
import com.badlogic.androidgames.framework.math.Rectangle;
import com.badlogic.androidgames.framework.math.Vector2;

public class GameScreen extends GLScreen {

	private Camera2D guiCam;
	private SpriteBatcher batcher;
	private GameRenderer renderer;
	private Board board;
	private Vector2 touchPoint;
	private Rectangle pauseRect;
	private Rectangle timeBonusRect;
	private Rectangle resumeRect;
	private Rectangle quitRect;
	private Rectangle backRect;
	
	
	public GameScreen(WordWaffle game) {
		super(game);
		guiCam = new Camera2D(glGraphics, 320, 480);
		batcher = new SpriteBatcher(glGraphics, 1000);
		board = new Board();
		renderer = new GameRenderer(glGraphics, batcher, board);
		touchPoint = new Vector2();
		pauseRect = new Rectangle(260, 420, 60, 60);
		timeBonusRect = new Rectangle(0, 420, 60, 60);
		resumeRect = new Rectangle(40, 290, 240, 60);
		quitRect = new Rectangle(40, 210, 240, 60);
		backRect = new Rectangle(40, 400, 240, 40);
	}

	@Override
	public void update(float deltaTime) {
		switch (board.state) {
			case Board.GAME_READY:
				updateReady(deltaTime);
				break;
			case Board.GAME_RUNNING:
				updateRunning(deltaTime);
				break;
			case Board.GAME_PAUSED:
				updatePaused(deltaTime);
				break;
			case Board.GAME_OVER:
				showGameOver(deltaTime);
				break;
			default:

				break;
		}
	}
	
	private void updateReady(float deltaTime) {
		List<TouchEvent> touchEvents = game.getInput().getTouchEvents();
		for(int i = 0; i < touchEvents.size(); i++) {
	        TouchEvent event = touchEvents.get(i);
	        if(event.type != TouchEvent.TOUCH_DOWN) return;
			board.state = Board.GAME_RUNNING;
	    }
		
	}
	
	private void updateRunning(float deltaTime) {
		List<TouchEvent> touchEvents = game.getInput().getTouchEvents();
		for(int i = 0; i < touchEvents.size(); i++) {
	        TouchEvent event = touchEvents.get(i);
	        // the points returned are backward in openGL land so we need to convert them to our coordinate space
	        touchPoint = touchPoint.getGLCoords(glGraphics, touchPoint, event.x, event.y, GameRenderer.FRUSTUM_WIDTH, GameRenderer.FRUSTUM_HEIGHT);
	        checkTappedPause(event, touchPoint);
	        checkTappedTimeBonus(event, touchPoint);
	        if(board.checkSlideLettersTray(event, touchPoint)) break; // we are trying to slide the letters in the letters tray
	        board.checkDraggingLetter(event, touchPoint);
	    }
		
		board.update(deltaTime);
	}
	
	private void updatePaused(float deltaTime) {
		List<TouchEvent> touchEvents = game.getInput().getTouchEvents();
		for(int i = 0; i < touchEvents.size(); i++) {
	        TouchEvent event = touchEvents.get(i);
	        // the points returned are backward in openGL land so we need to convert them to our coordinate space
	        touchPoint = touchPoint.getGLCoords(glGraphics, touchPoint, event.x, event.y, GameRenderer.FRUSTUM_WIDTH, GameRenderer.FRUSTUM_HEIGHT);
	        checkTappedResume(event, touchPoint);
	        checkTappedQuit(event, touchPoint);
	    }
	}
	
	private void showGameOver(float deltaTime) {
		List<TouchEvent> touchEvents = game.getInput().getTouchEvents();
		for(int i = 0; i < touchEvents.size(); i++) {
	        TouchEvent event = touchEvents.get(i);
	        // the points returned are backward in openGL land so we need to convert them to our coordinate space
	        touchPoint = touchPoint.getGLCoords(glGraphics, touchPoint, event.x, event.y, GameRenderer.FRUSTUM_WIDTH, GameRenderer.FRUSTUM_HEIGHT);
	        checkTappedBack(event, touchPoint);
	    }
	}
	
	private void finishActivity() {
		glGame.finish();
	}
	
	private void checkTappedPause(TouchEvent event, Vector2 touchPoint) {
		if(event.type != TouchEvent.TOUCH_DOWN) return;
		if (OverlapTester.pointInRectangle(pauseRect, touchPoint)) {
			board.state = Board.GAME_PAUSED;
		}
	}
	
	private void checkTappedTimeBonus(TouchEvent event, Vector2 touchPoint) {
		if(event.type != TouchEvent.TOUCH_DOWN) return;
		if (OverlapTester.pointInRectangle(timeBonusRect, touchPoint)) {
			board.state = Board.TIME_BONUS;
		}
	}
	
	private void checkTappedResume(TouchEvent event, Vector2 touchPoint) {
		if(event.type != TouchEvent.TOUCH_DOWN) return;
		if (OverlapTester.pointInRectangle(resumeRect, touchPoint)) {
			board.state = Board.GAME_RUNNING;
		}
	}
	
	private void checkTappedBack(TouchEvent event, Vector2 touchPoint) {
		if(event.type != TouchEvent.TOUCH_DOWN) return;
		if (OverlapTester.pointInRectangle(backRect, touchPoint)) {
			finishActivity();
		}
	}
	
	private void checkTappedQuit(TouchEvent event, Vector2 touchPoint) {
		if(event.type != TouchEvent.TOUCH_DOWN) return;
		if (OverlapTester.pointInRectangle(quitRect, touchPoint)) {
			finishActivity();
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
