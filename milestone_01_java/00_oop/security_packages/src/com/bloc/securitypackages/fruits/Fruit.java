package com.bloc.securitypackages.fruits;

import com.bloc.securitypackages.*;
import com.bloc.securitypackages.colors.*;
import com.bloc.securitypackages.fruits.*;

public abstract class Fruit extends Object {
	// The name of the fruit
	protected String mName;
	// Number of calories
	protected int mCalories;
	// Color of the fruit
	protected Color mColor;
	// Weight of the fruit, in pounds
	protected double mWeight;

	protected Fruit() {
		this("Apple");
		// Default fruit
	}

	protected Fruit(String name) {
		this(name, 0);
	}

	protected Fruit(String name, int calories) {
		this(name, calories, null);
	}

	protected Fruit(String name, int calories, Color color) {
		this(name, calories, color, 0d);
	}

	protected Fruit(String name, int calories, Color color, double weight) {
		this.mName = name;
		this.mCalories = calories;
		this.mColor = color;
		this.mWeight = weight;
	}

	public String getName() {
		return mName;
	}

	public void setName(String name) {
		mName = name;
	}

	public int getCalories() {
		return mCalories;
	}

	public void setCalories(int calories) {
		mCalories = calories;
	}

	public Color getColor() {
		return mColor;
	}

	public void setColor(Color color) {
		mColor = color;
	}

	public double getWeight() {
		return mWeight;
	}

	public void setWeight(double weight) {
		mWeight = weight;
	}
}