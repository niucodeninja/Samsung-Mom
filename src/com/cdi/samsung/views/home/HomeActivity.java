package com.cdi.samsung.views.home;

import android.app.Activity;
import android.os.Bundle;

import com.cdi.samsung.R;
import com.cdi.samsung.app.Manager;

public class HomeActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home);

		Manager.getInstance().getDispatcher().open(this, "ranking", false);
	}
}
