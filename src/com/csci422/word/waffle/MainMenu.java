package com.csci422.word.waffle;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;

public class MainMenu extends Activity {

	public static final String HIGH_SCORE_FILE = "High Score";
	private static Context ctx;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ctx = getApplicationContext();
        setContentView(R.layout.activity_main_menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main_menu, menu);
        return true;
    }
    
    public void startGame(View view) {
    	Intent i = new Intent(this, WordWaffle.class);
    	startActivity(i);
    }
    
    public static void saveHighScores(int[] scores) {
	    FileOutputStream fos;
	    try {
	        fos = ctx.openFileOutput(HIGH_SCORE_FILE, Context.MODE_PRIVATE);
	
	        ObjectOutputStream oos = new ObjectOutputStream(fos);
	        oos.writeObject(scores); 
	        oos.close();
	    } catch (FileNotFoundException e) {
	        e.printStackTrace();
	    }catch(IOException e){
	        e.printStackTrace();
	    }
	}
	
	public static int[] getHighScores() {
	    FileInputStream fis;
	    int[] scores = null;
	    try {
	        fis = ctx.openFileInput(HIGH_SCORE_FILE);
	
	        ObjectInputStream ois = new ObjectInputStream(fis);
	        scores = (int[]) ois.readObject();
	        ois.close();
	    } catch (FileNotFoundException e) {
	        e.printStackTrace();
	    }catch(IOException e){
	        e.printStackTrace();
	    } catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	    return scores;
	}
    
}
