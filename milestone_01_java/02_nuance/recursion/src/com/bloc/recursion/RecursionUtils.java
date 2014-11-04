package com.bloc.recursion;

import java.util.*;

public class RecursionUtils extends Object {

	static int mMaxNumber = 0;
	/*
	 * findMaxRecursively
	 * Takes a list of numbers and finds the largest among them
	 * using recursive calls.
	 *
	 * @param numbers a list of numbers, can be odd or even numbered
	 * @return the largest number in the list
	 *
	 * Hint: your base case may be a comparison of 2 numbers
	 */
	public static final int findMaxRecursively(List<Integer> numbers) {
		//IMPLEMENT ME
		if(numbers.size() == 0) {
			int maxNum = mMaxNumber;
			mMaxNumber = 0;
			return maxNum;
		}

		if(mMaxNumber < numbers.get(0)) {
			mMaxNumber = numbers.get(0);
		}

		numbers.remove(0);

		return findMaxRecursively(numbers);
	}
}