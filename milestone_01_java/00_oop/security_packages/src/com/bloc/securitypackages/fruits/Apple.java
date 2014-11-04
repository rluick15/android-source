package com.bloc.securitypackages.fruits;

import com.bloc.securitypackages.*;
import com.bloc.securitypackages.colors.*;
import com.bloc.securitypackages.fruits.*;

abstract class Apple extends Fruit {	

	Apple(String name, int calories, Color color, double weight) {
    	super(name, calories, color, weight);
	}

	abstract void bite();

}