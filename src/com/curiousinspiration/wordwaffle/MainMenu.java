package com.curiousinspiration.wordwaffle;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.ads.AdRequest;
import com.google.ads.AdView;

public class MainMenu extends Activity {

	public static final String HIGH_SCORE_FILE = "High Score";
	private static Context ctx;
	private int[] viewIds = {R.id.highscoreOne, R.id.highscoreTwo, R.id.highscoreThree, R.id.highscoreFour, R.id.highscoreFive};
	private static TextView[] views;
	private AdView adView;
	 
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ctx = getApplicationContext();
        setContentView(R.layout.activity_main_menu);
         
    	views = new TextView[viewIds.length];
        for(int i = 0; i < viewIds.length; i++) views[i] = (TextView) findViewById(viewIds[i]);
        adView = (AdView) findViewById(R.id.ad);
        adView.loadAd(new AdRequest());
    } 
   
    
    @Override
    public void onResume() {
    	super.onResume();
    	updateScores();
    }
    
	public void updateScores() {
		int highscores[] = getHighScores();
		Log.d(WordWaffle.DEBUG_TAG, "Highscores Size: "+highscores.length);
		List<Integer> hs = new ArrayList<Integer>();
		for(int i = 0; i < highscores.length; i++) {
			//Log.d(WordWaffle.DEBUG_TAG, "Highscore: "+highscores[i]);
			hs.add(highscores[i]);
		}
		Collections.sort(hs);
		Collections.reverse(hs);
		
        for(int i = 0; i < hs.size() && i < views.length; i++) {
        	//Log.d(WordWaffle.DEBUG_TAG, "Views: "+hs.get(i));
        	views[i].setText(""+hs.get(i));
        }
	}
    
    public void startGame(View view) {        
    	Intent i = new Intent(this, WordWaffle.class);
    	startActivity(i);
    }
    
    public void showRules(View view) {}
    
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
		} finally {
			if (scores == null) scores = new int[1];
		}
	    return scores;
	}

	@Override
	  public void onDestroy() {
	    if (adView != null) {
	      adView.destroy();
	    }
	    super.onDestroy();
	  }
    
}
