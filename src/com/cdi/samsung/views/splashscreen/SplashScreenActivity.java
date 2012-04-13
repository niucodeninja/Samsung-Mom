package com.cdi.samsung.views.splashscreen;

import com.cdi.samsung.R;
import com.cdi.samsung.app.Manager;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.MotionEvent;

public class SplashScreenActivity extends Activity {

	protected boolean _active = true;
	protected int _splashTime = 5000;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splashscreen);

		// thread for displaying the SplashScreen
		final Activity currentActivity = this;
		Thread splashTread = new Thread() {
			@Override
			public void run() {
				try {
					int waited = 0;
					while (_active && (waited < _splashTime)) {
						sleep(100);
						if (_active) {
							waited += 100;
						}
					}
				} catch (InterruptedException e) {
					// do nothing
				} finally {

					// get the IMEI
					TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
					Manager.getInstance().IMEI = telephonyManager.getDeviceId();

					// Display IMEI
					Log.i("Home", "Phone IMEI: " + Manager.getInstance().IMEI);

					// Call the web service for asking availability of IMEI

					// Go home
					Manager.getInstance().getDispatcher()
							.open(currentActivity, "postulate", true);
				}
			}
		};
		splashTread.start();
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			_active = false;
		}
		return true;
	}
}
