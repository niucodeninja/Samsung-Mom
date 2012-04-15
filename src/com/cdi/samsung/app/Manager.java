package com.cdi.samsung.app;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.widget.Toast;

import com.cdi.samsung.R;
import com.cdi.samsung.app.postulate.PostulateActivity;
import com.cdi.samsung.views.home.HomeActivity;
import com.cdi.samsung.views.ranking.RankingActivity;
import com.cdi.samsung.views.register.RegisterActivity;
import com.cdi.samsung.views.splashscreen.SplashScreenActivity;
import com.niucodeninja.activities.ActivityDispatcher;

public class Manager {

	private static Manager INSTANCE = new Manager();
	private Activity mainActivity;
	private ActivityDispatcher dispatcher;
	private ProgressDialog dialog;

	public String IMEI = "n/a";

	public static final String WS_BASE = "http://192.168.1.8/index.php";

	private Manager() {
	}

	/**
	 * Initialize the manager
	 * 
	 * @param mainActivity
	 */
	public void initialize(Activity mainActivity) {
		this.mainActivity = mainActivity;

		// Initialize the dispatcher
		dispatcher = new ActivityDispatcher();

		dispatcher.addHandler("splashscreen", SplashScreenActivity.class);
		dispatcher.addHandler("home", HomeActivity.class);
		dispatcher.addHandler("ranking", RankingActivity.class);
		dispatcher.addHandler("register", RegisterActivity.class);
		dispatcher.addHandler("postulate", PostulateActivity.class);
		/*
		 * dispatcher.addHandler("countries", ListCountries.class);
		 * dispatcher.addHandler("teams", ListTeams.class);
		 * dispatcher.addHandler("rankings", ListRankings.class);
		 */
	}

	/**
	 * Show toast message
	 * 
	 * @param activity
	 * @param message
	 */
	public void showMessage(Activity activity, String message) {
		Toast toast = Toast.makeText(activity, message, Toast.LENGTH_SHORT);
		toast.show();
	}

	/**
	 * Returns the activity dispatcher
	 * 
	 * @return
	 */
	public ActivityDispatcher getDispatcher() {
		return dispatcher;
	}

	/**
	 * Display a loading box
	 * 
	 * @param activity
	 */
	public void displayLoading(Activity activity) {
		if (dialog == null) {
			dialog = ProgressDialog.show(
					activity,
					this.mainActivity.getResources().getString(
							R.string.c_progressbar_title),
					this.mainActivity.getResources().getString(
							R.string.c_progressbar_description));
			dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			dialog.setCancelable(false);
		}
	}

	/**
	 * Hide the loading
	 */
	public void hideLoading() {
		if (dialog != null) {
			dialog.cancel();
			dialog = null;
		}
	}

	public Context getContext() {
		return this.mainActivity.getApplicationContext();
	}

	public Activity geActivity() {
		return this.mainActivity;
	}

	public static Manager getInstance() {
		return INSTANCE;
	}
}
