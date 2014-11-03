package com.bloc.securitypackages.colors;

import com.bloc.securitypackages.*;
import com.bloc.securitypackages.colors.*;
import com.bloc.securitypackages.fruits.*;

public class Color extends Object {
	// Name of the color
	String mName;
	// Alpha value
	int mAlpha;
	// Red value
	int mRed;
	// Green value
	int mGreen;
	// Blue value
	int mBlue;

	protected Color(int red, int green, int blue) {
		this(null, red, green, blue);
	}

	protected Color(String name, int red, int green, int blue) {
		mName = name;
		mRed = red;
		mGreen = green;
		mBlue = blue;
	}
}