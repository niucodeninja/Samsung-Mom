package com.cdi.samsung.views;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.cdi.samsung.R;
import com.cdi.samsung.app.Manager;

public class MenuActivity extends Activity implements OnClickListener {

	private Button btnPostulate;
	private Button btnRanking;
	private Button btnApps;
	private Button btnStatus;
	private Button btnMecanica;

	private final int EXIT_APP = 0x9;
	private Button btnVote;

	// private final int RANKING = 0x8;
	// private final int POSTULATE = 0x7;
	// private final int SEE_MY_MOM = 0x6;

	/** Called when the activity is first created. */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.menu);

		btnPostulate = (Button) findViewById(R.id.postulate_menu_btn);
		btnRanking = (Button) findViewById(R.id.ranking_menu_btn);
		btnApps = (Button) findViewById(R.id.momsapp_menu_btn);
		btnStatus = (Button) findViewById(R.id.status_menu_btn);
		btnMecanica = (Button) findViewById(R.id.mecanica_menu_btn);
		btnVote = (Button) findViewById(R.id.vote_menu_btn);

		btnPostulate.setOnClickListener(this);
		btnRanking.setOnClickListener(this);
		btnApps.setOnClickListener(this);
		btnStatus.setOnClickListener(this);
		btnMecanica.setOnClickListener(this);
		btnVote.setOnClickListener(this);
	}

	@Override
	public void onClick(View arg) {
		if (arg.getId() == R.id.postulate_menu_btn) {
			Manager.getInstance().getDispatcher()
					.open(this, "postulate", false);
		} else if (arg.getId() == R.id.ranking_menu_btn) {
			Manager.getInstance().getDispatcher().open(this, "ranking", false);
		} else if (arg.getId() == R.id.momsapp_menu_btn) {

		} else if (arg.getId() == R.id.status_menu_btn) {
			{
				Manager.getInstance().currentMom = Manager.getInstance().userMom;
				Manager.getInstance().getDispatcher().open(this, "mom", false);
			}
		} else if (arg.getId() == R.id.vote_menu_btn) {
			Manager.getInstance().getDispatcher().open(this, "home", false);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		menu.add(Menu.NONE, EXIT_APP, Menu.NONE, R.string.c_exit_app);
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
		default:
			return false;
		}
		return true;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			// preventing default implementation previous to
			// android.os.Build.VERSION_CODES.ECLAIR
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}
