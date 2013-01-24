package com.curiousinspiration.wordwaffle;

import com.badlogic.androidgames.framework.gl.Texture;
import com.badlogic.androidgames.framework.gl.TextureRegion;
import com.badlogic.androidgames.framework.impl.GLGame;

public class Assets {
	
	public static Texture background;
    public static TextureRegion backgroundRegion;
    public static TextureRegion gameOverScreen;
    public static TextureRegion readyScreen;
    
    public static Texture foregroundItems;
    
    public static Texture letters;
    public static TextRenderer letterRenderer;
    
    public static TextureRegion debugRect;
    public static TextureRegion letter;
    public static TextureRegion valid_letter;
    public static TextureRegion invalid_letter;
    public static TextureRegion leftArrow;
    public static TextureRegion rightArrow;
    public static TextureRegion clock;
    public static TextureRegion pauseScreen;
    
    // Suppress default constructor for noninstantiability
    private Assets() {
        throw new AssertionError();
    }
    
    public static void load(GLGame game) {
    	background = new Texture(game, "WaffleBig.png");
        backgroundRegion = new TextureRegion(background, 0, 0, 682, 1024);
        gameOverScreen = new TextureRegion(background, 682, 0, 682, 1024);
        readyScreen = new TextureRegion(background, 1364, 0, 682, 1024);
        
        foregroundItems = new Texture(game, "game_sprites.png");
        letterRenderer = new TextRenderer(foregroundItems, 0, 80, 80, 80);
         
        
        letter = new TextureRegion(foregroundItems, 0, 0, 80, 80);
        debugRect = new TextureRegion(foregroundItems, 80, 0, 80, 80);
        valid_letter = new TextureRegion(foregroundItems, 160, 0, 80, 80);
        invalid_letter = new TextureRegion(foregroundItems, 240, 0, 80, 80);
        leftArrow = new TextureRegion(foregroundItems, 320, 0, 80, 80);
        rightArrow = new TextureRegion(foregroundItems, 400, 0, 80, 80);
        clock = new TextureRegion(foregroundItems, 480, 0, 80, 80);
        pauseScreen = new TextureRegion(foregroundItems, 900, 80, 550, 600);
        
    }
    
    public static void reload() {
        background.reload();
        foregroundItems.reload();
    }

}
