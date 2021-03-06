package com.curiousinspiration.wordwaffle;

import java.util.ArrayList;

// A word will hold a string that is a valid word on the board
// as well as its letters locations on the board
// it will be used to store this information and make sure 
// words do not get counted twice

public class Word implements Comparable {

	public String word;
	public ArrayList<int[]> board_locations = new ArrayList<int[]>();
	public boolean isHorizontal = false;
	
	public Word(String w, ArrayList<int[]> locations, boolean isH) {
		word = w;
		isHorizontal = isH;
		for (int[] l : locations)
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
		return word;
	}

	@Override
	public int compareTo(Object another) {
		Word other = (Word) another;
		return word.compareTo(other.word);
	}

	public char charAt(int i) {
		return word.charAt(i);
	}
	
	public boolean areConnected(Word w) {
		for (int[] location : board_locations) {
			if (w.board_locations.contains(location)) return true;
		}
		return false;
	}

}
