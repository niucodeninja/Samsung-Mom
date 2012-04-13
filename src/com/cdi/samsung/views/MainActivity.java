package com.cdi.samsung.views;

import android.app.Activity;
import android.os.Bundle;

import com.cdi.samsung.app.Manager;

public class MainActivity extends Activity {

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Initialize application
		Manager.getInstance().initialize(this);
		Manager.getInstance().getDispatcher().open(this, "splashscreen", true);
	}
}