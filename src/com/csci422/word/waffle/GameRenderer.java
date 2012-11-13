package com.csci422.word.waffle;

import javax.microedition.khronos.opengles.GL10;

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
        renderForeground();
    }
	
	private void renderBackground() {
		batcher.beginBatch(Assets.background);
        batcher.drawSprite(cam.position.x, cam.position.y,
                           FRUSTUM_WIDTH, FRUSTUM_HEIGHT, 
                           Assets.backgroundRegion);
        batcher.endBatch();
	}
	
	private void renderForeground() {
		GL10 gl = glGraphics.getGL();
        gl.glEnable(GL10.GL_BLEND);
        gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
        
        batcher.beginBatch(Assets.foregroundItems);
        renderDebugSquares();
        renderLetters();
        renderLetterTray();
        
        batcher.endBatch();
        gl.glDisable(GL10.GL_BLEND);
	}
	
	private void renderLetters() {
		
		for(int i = 0; i < Board.letters.size(); i++) {
            Letter l = Board.letters.get(i);
            if (l.state == Letter.VALID_LOCATION) {
            	batcher.drawSprite(l.position.x, l.position.y, 40, 40, Assets.valid_letter);
            } else {
            	batcher.drawSprite(l.position.x, l.position.y, 40, 40, Assets.letter);
            }
            
            Assets.font.drawText(batcher, l.toString(), l.position.x, l.position.y, 30, 30);
        }
	}
	
	private void renderLetterTray() {
		batcher.drawSprite(20, 35, 40, 40, Assets.leftArrow);
		batcher.drawSprite(300, 35, 40, 40, Assets.rightArrow);
	}
	
	// Used to check that the validBoardSquares line up with the waffle
	private void renderDebugSquares() {
//		for (int i = 0; i < Board.BOARD_WIDTH; i++) {
//			for (int j = 0; j < Board.BOARD_HEIGHT; j++) {
//				Rectangle r = Board.validBoardSpaces[i][j];
//				batcher.drawSprite(r.lowerLeft.x, r.lowerLeft.y, r.width, r.height, Assets.debugRect); 
//			}
//                       
//        }
		for (Rectangle r : Board.letterTray) {
			batcher.drawSprite(r.lowerLeft.x, r.lowerLeft.y, r.width, r.height, Assets.debugRect); 
		}
	}

}
