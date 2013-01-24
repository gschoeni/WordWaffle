package com.curiousinspiration.wordwaffle;


import com.badlogic.androidgames.framework.gl.SpriteBatcher;
import com.badlogic.androidgames.framework.gl.Texture;
import com.badlogic.androidgames.framework.gl.TextureRegion;

public class TextRenderer {
	public final Texture texture;
    public final int glyphWidth;
    public final int glyphHeight;
    public final int num_chars = 94;
    public final TextureRegion[] glyphs = new TextureRegion[num_chars];   
    
    public TextRenderer(Texture texture, 
                int offsetX, int offsetY, int glyphWidth, int glyphHeight) {        
        this.texture = texture;
        this.glyphWidth = glyphWidth;
        this.glyphHeight = glyphHeight;
        int x = offsetX;
        int y = offsetY;
        int num_cols = 11;
        for(int i = 0; i < num_chars; i++) {
            glyphs[i] = new TextureRegion(texture, x, y, glyphWidth, glyphHeight);
            x += glyphWidth;
            if(x == offsetX + num_cols * glyphWidth) {
                x = offsetX;
                y += glyphHeight;
            }
        }        
    }
    
    public void drawText(SpriteBatcher batcher, String text, float x, float y, float width, float height, float x_offset) {
        int len = text.length();
        for(int i = 0; i < len; i++) {
        	//Log.d(WordWaffle.DEBUG_TAG, "char: "+text.charAt(i));
        	int c = text.charAt(i);
            c -= 32; // we start drawing at the space character from the sprite sheet
            TextureRegion glyph = glyphs[c];
            batcher.drawSprite(x, y, width, height, glyph);
            x += x_offset;
            c += 32; // get back to ascii 
            if (c == 58 || c == 73 || c == 105 || c == 114) { // if the character is a ':' or 'i' or 'I' or 'r' then lets tighten it up
            	x -= x_offset / 4;
            }
            
        }
    }

}
