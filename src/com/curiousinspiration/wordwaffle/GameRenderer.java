package com.curiousinspiration.wordwaffle;

import javax.microedition.khronos.opengles.GL10;


import com.badlogic.androidgames.framework.gl.Camera2D;
import com.badlogic.androidgames.framework.gl.SpriteBatcher;
import com.badlogic.androidgames.framework.impl.GLGraphics;

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
        if (board.state != Board.GAME_READY) {
        	renderForeground();
        }
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
            Assets.letterRenderer.drawText(batcher, ""+ScoreCalculator.pointsForLetters[l.value - 'A'], l.position.x+13, l.position.y+11, 10 * letter_scale, 10 * letter_scale, 0);
            Assets.letterRenderer.drawText(batcher, ""+l.value, l.position.x+2, l.position.y-2, 25 * letter_scale, 25 * letter_scale, 0);
            letter_scale = 1.0f;
        }
		// Make it so the letter that is being dragged is above all the other letters
		if (beingDragged != null) {
			letter_scale = 1.5f;
    		batcher.drawSprite(beingDragged.position.x, beingDragged.position.y, Letter.WIDTH * letter_scale, Letter.HEIGHT * letter_scale, Assets.letter);
    		Assets.letterRenderer.drawText(batcher, beingDragged.value+"", beingDragged.position.x, beingDragged.position.y, 25 * letter_scale, 25 * letter_scale, 0);
            letter_scale = 1.0f;
		}
	}
	
	private void renderLetterTray() {
		batcher.drawSprite(20, 35, 40, 40, Assets.leftArrow);
		batcher.drawSprite(300, 35, 40, 40, Assets.rightArrow);
	}
	
	private void renderScore() {
		 Assets.letterRenderer.drawText(batcher, "Score:", 20, 415, 20, 20, 12);
		 Assets.letterRenderer.drawText(batcher, ""+Board.base_score, 95, 415, 20, 20, 12);
	}
	
	private void renderTimer() {
		batcher.drawSprite(250, 415, 20, 20, Assets.clock);
		//Assets.letterRenderer.drawText(batcher, "Time", 200, 415, 20, 20, 15);
		Assets.letterRenderer.drawText(batcher, Board.time, 275, 415, 20, 20, 12);
	}
	
	
	private void renderFinalScores() {
		//draw base score
		Assets.letterRenderer.drawText(batcher, ""+Board.base_score, 175, 323, 15, 15, 8);
		
		//Draw Time Bonus
		Assets.letterRenderer.drawText(batcher, ""+Board.final_score[1], 112, 240, 15, 15, 8);
		
		//Draw Used All Letters
		Assets.letterRenderer.drawText(batcher, ""+Board.final_score[2], 217, 235, 15, 15, 8);
		
		//Invalid letters
		int bump = 0;
		if (Board.final_score[3] == 0) {
			// bump over the drawing a bit
			bump = 3;
		}
		Assets.letterRenderer.drawText(batcher, ""+Board.final_score[3], 153+bump, 175, 15, 15, 7);
		
		//draw final score
		Assets.letterRenderer.drawText(batcher, ""+Board.final_score[0], 180, 110, 25, 25, 16);
		
	}
	
	private void renderPausedScreen() {
		batcher.drawSprite(165, 280, 300, 300, Assets.pauseScreen);
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
