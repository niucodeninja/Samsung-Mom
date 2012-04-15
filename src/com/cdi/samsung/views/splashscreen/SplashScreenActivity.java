package com.cdi.samsung.views.splashscreen;

import java.util.Observable;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.MotionEvent;

import com.cdi.samsung.R;
import com.cdi.samsung.app.Manager;
import com.cdi.samsung.app.SamsungMomWebservices;
import com.niucodeninja.webservices.WebServicesEvent;

public class SplashScreenActivity extends Activity implements
		java.util.Observer {

	protected boolean _active = true;
	protected int _splashTime = 5000;
	private SamsungMomWebservices ws;

	final Activity currentActivity = this;
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {

			// Display IMEI
			Log.i("SplashScreenActivity",
					"Phone IMEI: " + Manager.getInstance().IMEI);

			// Call the web service for asking availability of IMEI
			ws.verifyIMEI(Manager.getInstance().IMEI);
			Manager.getInstance().displayLoading(currentActivity);
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splashscreen);

		// Webservices
		ws = new SamsungMomWebservices(Manager.WS_BASE);
		ws.addObserver(this);

		// thread for displaying the SplashScreen
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

					handler.sendEmptyMessage(0);
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

	@Override
	public void update(Observable observable, Object data) {
		Manager.getInstance().hideLoading();
		if (data instanceof WebServicesEvent) {
			WebServicesEvent event = (WebServicesEvent) data;
			switch (event.getIdEvent()) {
			case SamsungMomWebservices.VERIFY_IMEI_COMPLETE:
				JSONObject json_data = (JSONObject) event.getData();
				try {
					if (json_data.getBoolean("existe")) {
						Manager.getInstance().getDispatcher()
								.open(this, "home", true);
					} else {
						Manager.getInstance().getDispatcher()
								.open(this, "register", true);
					}
				} catch (JSONException e) {
					Manager.getInstance().showMessage(
							this,
							getResources()
									.getString(R.string.c_load_imei_error));
				}
				break;
			case SamsungMomWebservices.VERIFY_IMEI_ERROR:
				Manager.getInstance().showMessage(this,
						getResources().getString(R.string.c_load_imei_error));
				break;
			}
		}
	}
}
