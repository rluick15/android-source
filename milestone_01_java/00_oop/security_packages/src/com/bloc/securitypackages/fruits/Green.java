package com.bloc.securitypackages.fruits;

import com.bloc.securitypackages.*;
import com.bloc.securitypackages.colors.*;
import com.bloc.securitypackages.fruits.*;

public class Green extends Fruit {

	public Green() {
		super(Green.class.getSimpleName(), 230, new LimeGreen(), 0.21d);
	}

	void bite() {
		setWeight(getWeight() - 0.02d);
	}

}