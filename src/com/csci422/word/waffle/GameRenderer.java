package com.csci422.word.waffle;

import javax.microedition.khronos.opengles.GL10;

import android.util.Log;

import com.badlogic.androidgames.framework.Game;
import com.badlogic.androidgames.framework.gl.Camera2D;
import com.badlogic.androidgames.framework.gl.SpriteBatcher;
import com.badlogic.androidgames.framework.impl.GLGraphics;
import com.badlogic.androidgames.framework.math.Rectangle;

public class GameRenderer {
	
	static final float FRUSTUM_WIDTH = 320;
	static final float FRUSTUM_HEIGHT = 480;
	GLGraphics glGraphics;
	Board board;
	Camera2D cam;
	SpriteBatcher batcher;
	
	public GameRenderer(GLGraphics g, SpriteBatcher batcher, Board board){
		this.glGraphics = g;
		this.batcher = batcher;
		this.board = board;
		this.cam = new Camera2D(g, FRUSTUM_WIDTH, FRUSTUM_HEIGHT);
	}
	
	public void render() {
        cam.setViewportAndMatrices();
        renderBackground();
        if (board.state != Board.GAME_READY) renderForeground();
    }
	
	private void renderBackground() {
		GL10 gl = glGraphics.getGL();
		gl.glEnable(GL10.GL_BLEND);
        
        gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		batcher.beginBatch(Assets.background);
		switch (board.state) {
			case Board.GAME_READY:
				
				batcher.drawSprite(cam.position.x, cam.position.y,
	                    FRUSTUM_WIDTH, FRUSTUM_HEIGHT, 
	                    Assets.backgroundRegion);
				// Overlay the ready screen on the original background
				batcher.drawSprite(cam.position.x, cam.position.y,
                        FRUSTUM_WIDTH, FRUSTUM_HEIGHT, 
                        Assets.readyScreen);
			break;
			
			case Board.GAME_RUNNING:
				
				batcher.drawSprite(cam.position.x, cam.position.y,
                        FRUSTUM_WIDTH, FRUSTUM_HEIGHT, 
                        Assets.backgroundRegion);
			break;
			
			case Board.GAME_PAUSED:
				
				batcher.drawSprite(cam.position.x, cam.position.y,
                        FRUSTUM_WIDTH, FRUSTUM_HEIGHT, 
                        Assets.backgroundRegion);
			break;
			
			case Board.GAME_OVER:
				batcher.drawSprite(cam.position.x, cam.position.y,
                        FRUSTUM_WIDTH, FRUSTUM_HEIGHT, 
                        Assets.backgroundRegion);
				
				// Overlay the game over on the original background
				batcher.drawSprite(cam.position.x, cam.position.y,
                        FRUSTUM_WIDTH, FRUSTUM_HEIGHT, 
                        Assets.gameOverScreen);
				break;
		}
		batcher.endBatch();
	}
	
	private void renderForeground() {
		GL10 gl = glGraphics.getGL();
        gl.glEnable(GL10.GL_BLEND);
        
        gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
        batcher.beginBatch(Assets.foregroundItems);
        
        switch (board.state) {
        
        	case Board.GAME_READY:

        		break;
        
			case Board.GAME_RUNNING:
				renderDebugSquares();
		        renderScore();
		        renderTimer();
		        renderLetters();
		        renderLetterTray();
			break;
			
			case Board.GAME_PAUSED:
				renderDebugSquares();
		        renderScore();
		        renderTimer();
		        renderLetters();
		        renderLetterTray();
		        renderPausedScreen();
			break;
			
			case Board.GAME_OVER:
				renderFinalScores();
				break;
		}
        
        
        
        batcher.endBatch();
        gl.glDisable(GL10.GL_BLEND);
	}
	
	private void renderLetters() {
		float letter_scale = 1.0f;
		Letter beingDragged = null;
		for(int i = 0; i < Board.letters.size(); i++) {
            Letter l = Board.letters.get(i);
            switch(l.state) {
            	case Letter.VALID_LOCATION:
            		batcher.drawSprite(l.position.x, l.position.y, Letter.WIDTH, Letter.HEIGHT, Assets.valid_letter);
            		break;
            	case Letter.INVALID_LOCATION:
            		batcher.drawSprite(l.position.x, l.position.y, Letter.WIDTH, Letter.HEIGHT, Assets.invalid_letter);
            		break;
            	case Letter.IS_BEING_DRAGGED:
            		beingDragged = l;
            		break;
            	default:
            		batcher.drawSprite(l.position.x, l.position.y, Letter.WIDTH, Letter.HEIGHT, Assets.letter);
            		break;
            }
            
            Assets.letterRenderer.drawText(batcher, l.toString(), l.position.x, l.position.y, 25 * letter_scale, 25 * letter_scale, 0);
            letter_scale = 1.0f;
        }
		// Make it so the letter that is being dragged is above all the other letters
		if (beingDragged != null) {
			letter_scale = 1.5f;
    		batcher.drawSprite(beingDragged.position.x, beingDragged.position.y, Letter.WIDTH * letter_scale, Letter.HEIGHT * letter_scale, Assets.letter);
    		Assets.letterRenderer.drawText(batcher, beingDragged.toString(), beingDragged.position.x, beingDragged.position.y, 25 * letter_scale, 25 * letter_scale, 0);
            letter_scale = 1.0f;
		}
	}
	
	private void renderLetterTray() {
		batcher.drawSprite(20, 35, 40, 40, Assets.leftArrow);
		batcher.drawSprite(300, 35, 40, 40, Assets.rightArrow);
	}
	
	private void renderScore() {
		 Assets.letterRenderer.drawText(batcher, "SCORE", 10, 415, 20, 20, 15);
		 Assets.blackNumberRenderer.drawNumber(batcher, ""+Board.base_score, 95, 415, 20, 20, 15);
	}
	
	private void renderTimer() {
		Assets.letterRenderer.drawText(batcher, "TIME", 200, 415, 20, 20, 15);
		Assets.blackNumberRenderer.drawNumber(batcher, Board.time, 265, 415, 20, 20, 15);
	}
	
	
	private void renderFinalScores() {
		//draw total score
		Assets.whiteNumberRenderer.drawNumber(batcher, ""+Board.base_score, 110, 325, 30, 30, 20);
		
		//Draw Long Word Bonus
		Assets.whiteNumberRenderer.drawNumber(batcher, ""+Board.final_score[1], 170, 270, 15, 15, 10);
		
		//Draw Finishing Tray Points
		Assets.whiteNumberRenderer.drawNumber(batcher, ""+Board.final_score[2], 180, 242, 15, 15, 10);
		
		//Repeated Words Score
		Assets.whiteNumberRenderer.drawNumber(batcher, ""+Board.final_score[3], 222, 215, 15, 15, 10);
		
		//Draw unused tile score
		Assets.whiteNumberRenderer.drawNumber(batcher, ""+Board.final_score[4], 175, 155, 15, 15, 10);
		
		//Draw invalid word score
		Assets.whiteNumberRenderer.drawNumber(batcher, ""+Board.final_score[5], 180, 125, 15, 15, 10);
		
		//draw final score
		Assets.whiteNumberRenderer.drawNumber(batcher, ""+Board.final_score[0], 140, 50, 40, 40, 25);
		
	}
	
	private void renderPausedScreen() {
		batcher.drawSprite(160, 280, 220, 220, Assets.pauseScreen);
	}
	
	// Used to check any rectangles
	private void renderDebugSquares() {
//		for (int i = 0; i < Board.BOARD_WIDTH; i++) {
//			for (int j = 0; j < Board.BOARD_HEIGHT; j++) {
//				Rectangle r = Board.validBoardSpaces[i][j];
//				batcher.drawSprite(r.lowerLeft.x, r.lowerLeft.y, r.width, r.height, Assets.debugRect); 
//			}
//                       
//        }
		for (TraySpace t : Board.letterTray) {
			batcher.drawSprite(t.getRect().lowerLeft.x, t.getRect().lowerLeft.y, t.getRect().width, t.getRect().height, Assets.debugRect); 
		}
	}

}
