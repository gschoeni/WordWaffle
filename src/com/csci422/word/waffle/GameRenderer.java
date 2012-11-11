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
        //renderDebugBoard();
        renderLetters();
        
        batcher.endBatch();
        gl.glDisable(GL10.GL_BLEND);
	}
	
	private void renderLetters() {
		for(int i = 0; i < board.letters.size(); i++) {
            Letter l = board.letters.get(i);       
            batcher.drawSprite(l.position.x, l.position.y, 40, 40, Assets.letter);            
        }
	}
	
	private void renderDebugBoard() {
		for (Rectangle r : board.validBoardSpaces) {
            batcher.drawSprite(r.lowerLeft.x, r.lowerLeft.y, r.width, r.height, Assets.debugRect);            
        }
	}

}
