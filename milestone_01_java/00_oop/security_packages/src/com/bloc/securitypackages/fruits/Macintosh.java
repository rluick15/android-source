package com.bloc.securitypackages.fruits;

import com.bloc.securitypackages.*;
import com.bloc.securitypackages.colors.*;
import com.bloc.securitypackages.fruits.*;

public class Macintosh extends Apple {

	public Macintosh() {
		super(Macintosh.class.getSimpleName(), 200, new Red(), 0.14d);
	}

	void bite() {
		setWeight(getWeight() - 0.01d);
	}

}