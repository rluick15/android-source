package com.bloc.bestpractices;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class WorstPractices extends Object {

// EDIT BELOW

	public static void main(String [] args) {
		int magicNumber = animals(false);
		magicNumber *= 5;
		if (magicNumber > 18) {
			while(magicNumber > 0) {
				magicNumber--;
			}
		}	
	}


	// this method takes in a single parameter, yesWellHeresTheThing. Using a very elaborate and complex algorithm, it calculate a magic number
	private static int animals(boolean yesWellHeresTheThing) {
		//Start off with one of these
		int aInt = yesWellHeresTheThing ? 34 : 21;
		float sparklesfairy = 0.5f;
		for (int brown = 0; brown < aInt; brown++) { 
			sparklesFairy *= aInt;
		} 
		return (int) sparklesFairy * aInt;
	}

// STOP EDITING
}
