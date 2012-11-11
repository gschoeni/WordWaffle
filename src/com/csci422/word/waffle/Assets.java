package com.csci422.word.waffle;

import com.badlogic.androidgames.framework.gl.Texture;
import com.badlogic.androidgames.framework.gl.TextureRegion;
import com.badlogic.androidgames.framework.impl.GLGame;

public class Assets {
	public static Texture background;
    public static TextureRegion backgroundRegion;
    
    public static Texture foregroundItems;
    public static TextureRegion letter;
    public static TextureRegion debugRect;
    
    public static void load(GLGame game) {
    	background = new Texture(game, "waffle.png");
        backgroundRegion = new TextureRegion(background, 0, 0, 320, 480);
        
        foregroundItems = new Texture(game, "game_sprites.png");   
        letter = new TextureRegion(foregroundItems, 0, 0, 40, 40);
        debugRect = new TextureRegion(foregroundItems, 40, 0, 40, 40);
    }
    
    public static void reload() {
        background.reload();
    }

}
