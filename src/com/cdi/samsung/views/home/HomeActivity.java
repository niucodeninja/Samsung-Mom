package com.cdi.samsung.views.home;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.cdi.samsung.R;
import com.cdi.samsung.app.Manager;

public class HomeActivity extends Activity {

	private final int EXIT_APP = 0x9;
	private final int RANKING = 0x8;
	private final int POSTULATE = 0x7;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		menu.add(Menu.NONE, EXIT_APP, Menu.NONE, R.string.c_exit_app);
		menu.add(Menu.NONE, RANKING, Menu.NONE, R.string.c_ranking);
		menu.add(Menu.NONE, POSTULATE, Menu.NONE, R.string.c_postulate);
		return true;
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int itemId = item.getItemId();
		switch (itemId) {
		case EXIT_APP:
			System.exit(0);
			break;
		case RANKING:
			Manager.getInstance().getDispatcher().open(this, "ranking", false);
			break;
		case POSTULATE:
			Manager.getInstance().getDispatcher()
					.open(this, "postulate", false);
			break;
		default:
			return false;
		}
		return true;
	}
}
