package com.csci422.word.waffle;

import android.util.Log;

import com.badlogic.androidgames.framework.gl.SpriteBatcher;
import com.badlogic.androidgames.framework.gl.Texture;
import com.badlogic.androidgames.framework.gl.TextureRegion;

public class TextRenderer {
	public final Texture texture;
    public final int glyphWidth;
    public final int glyphHeight;
    public final TextureRegion[] glyphs = new TextureRegion[26];   
    
    public TextRenderer(Texture texture, 
                int offsetX, int offsetY, int glyphWidth, int glyphHeight) {        
        this.texture = texture;
        this.glyphWidth = glyphWidth;
        this.glyphHeight = glyphHeight;
        int x = offsetX;
        int y = offsetY;
        int num_cols = 5;
        for(int i = 0; i < 26; i++) {
            glyphs[i] = new TextureRegion(texture, x, y, glyphWidth, glyphHeight);
            x += glyphWidth;
            if(x == offsetX + num_cols * glyphWidth) {
                x = offsetX;
                y += glyphHeight;
            }
        }        
    }
    
    public void drawText(SpriteBatcher batcher, String text, float x, float y, int width, int height) {
        int len = text.length();
        for(int i = 0; i < len; i++) {
        	//Log.d(WordWaffle.DEBUG_TAG, "char: "+text.charAt(i));
            int c = text.charAt(i) - 'A';
            //Log.d(WordWaffle.DEBUG_TAG, "int: "+c);
            if(c < 0 || c > glyphs.length - 1) 
                continue;
            
            TextureRegion glyph = glyphs[c];
            batcher.drawSprite(x, y, width, height, glyph);
            x += glyphWidth;
        }
    }
}
