package com.csci422.word.waffle;

import java.util.ArrayList;
import java.util.List;


public class Board {
	
	public static final float WORLD_WIDTH = 320;
	public static final float WORLD_HEIGHT = 480;
	
	public final List<Letter> letters;
	
	public Board() {
		this.letters = new ArrayList<Letter>();
		Letter l = new Letter(100, 100, 40, 40);
		letters.add(l);
	}

}
