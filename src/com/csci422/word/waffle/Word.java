package com.csci422.word.waffle;

import java.util.ArrayList;
import java.util.List;

// A word will hold a string that is a valid word on the board
// as well as its letters locations on the board
// it will be used to store this information and make sure 
// words do not get counted twice

public class Word {

	public String word;
	public ArrayList<Integer> board_locations = new ArrayList<Integer>();
	public boolean isHorizontal = false;
	
	public Word(String w, ArrayList<Integer> locations, boolean isH) {
		word = w;
		isHorizontal = isH;
		for (Integer l : locations)
			board_locations.add(l);
		
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((board_locations == null) ? 0 : board_locations.hashCode());
		result = prime * result + ((word == null) ? 0 : word.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Word other = (Word) obj;
		if (board_locations == null) {
			if (other.board_locations != null)
				return false;
		} else if (!board_locations.equals(other.board_locations))
			return false;
		if (word == null) {
			if (other.word != null)
				return false;
		} else if (!word.equals(other.word))
			return false;
		return true;
	}



	@Override
	public String toString() {
		return word + " locations: " +board_locations + " isHorizontal: "+isHorizontal;
	}
}
