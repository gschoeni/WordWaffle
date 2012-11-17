package com.csci422.word.waffle;

import com.badlogic.androidgames.framework.gl.Texture;
import com.badlogic.androidgames.framework.gl.TextureRegion;
import com.badlogic.androidgames.framework.impl.GLGame;

public class Assets {
	
	public static Texture background;
    public static TextureRegion backgroundRegion;
    
    public static Texture foregroundItems;
    public static TextRenderer letterRenderer;
    public static TextRenderer numberRenderer;
    public static TextureRegion debugRect;
    public static TextureRegion letter;
    public static TextureRegion valid_letter;
    public static TextureRegion invalid_letter;
    public static TextureRegion leftArrow;
    public static TextureRegion rightArrow;
    
    // Suppress default constructor for noninstantiability
    private Assets() {
        throw new AssertionError();
    }
    
    public static void load(GLGame game) {
    	background = new Texture(game, "waffle.png");
        backgroundRegion = new TextureRegion(background, 0, 0, 320, 480);
        
        foregroundItems = new Texture(game, "game_sprites.png");
        letterRenderer = new TextRenderer(foregroundItems, 0, 40, 40, 40);
        numberRenderer = new TextRenderer(foregroundItems, 0, 280, 40, 40);
        
         
        debugRect = new TextureRegion(foregroundItems, 40, 0, 40, 40);
        letter = new TextureRegion(foregroundItems, 0, 0, 40, 40);
        valid_letter = new TextureRegion(foregroundItems, 160, 0, 40, 40);
        invalid_letter = new TextureRegion(foregroundItems, 200, 0, 40, 40);
        leftArrow = new TextureRegion(foregroundItems, 80, 0, 40, 40);
        rightArrow = new TextureRegion(foregroundItems, 120, 0, 40, 40);
        
        
    }
    
    public static void reload() {
        background.reload();
        foregroundItems.reload();
    }

}
