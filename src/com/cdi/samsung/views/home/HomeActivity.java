package com.cdi.samsung.views.home;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Gallery;

import com.cdi.samsung.R;
import com.cdi.samsung.app.Manager;
import com.cdi.samsung.app.SamsungMomWebservices;
import com.cdi.samsung.app.models.Mom;
import com.niucodeninja.webservices.WebServicesEvent;

public class HomeActivity extends Activity implements Observer {

	private final int EXIT_APP = 0x9;
	private final int RANKING = 0x8;
	private final int POSTULATE = 0x7;
	private Gallery moms_gallery;

	private SamsungMomWebservices ws;
	private ArrayList<Mom> listOfMoms;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home);

		moms_gallery = (Gallery) findViewById(R.id.moms);

		// On select Mom's picture
		moms_gallery.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0,
					android.view.View arg1, int arg2, long arg3) {
				// TODO Auto-generated method stub

			}
		});

		// Webservices
		ws = new SamsungMomWebservices(Manager.WS_BASE);
		ws.addObserver(this);

		loadData();
	}

	private void loadData() {
		Manager.getInstance().displayLoading(this);
		ws.getMoms(Manager.getInstance().IMEI, Manager.getInstance().PASSWORD,
				Manager.getInstance().COUNTRY);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		menu.add(Menu.NONE, EXIT_APP, Menu.NONE, R.string.c_exit_app);
		menu.add(Menu.NONE, RANKING, Menu.NONE, R.string.c_ranking);
		if (Manager.getInstance().POSTULATE.equals("0")) {
			menu.add(Menu.NONE, POSTULATE, Menu.NONE, R.string.c_postulate);
		}
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

	@Override
	public void update(Observable observable, Object data) {
		Manager.getInstance().hideLoading();
		if (data instanceof WebServicesEvent) {
			WebServicesEvent event = (WebServicesEvent) data;
			switch (event.getIdEvent()) {
			case SamsungMomWebservices.MOMS_COMPLETE:
				JSONObject json_data = (JSONObject) event.getData();
				try {
					JSONArray moms = json_data.getJSONArray("madres");
					if (moms.length() > 0) {
						listOfMoms = new ArrayList<Mom>();
						for (int i = 0; i < moms.length(); ++i) {
							Mom mom = new Mom(moms.getJSONObject(i));
							if (mom.isOk()) {
								listOfMoms.add(mom);
							}
						}
						moms_gallery.setAdapter(new ImageAdapter(this,
								listOfMoms));
					} else {
						Manager.getInstance().showMessage(
								this,
								getResources().getString(
										R.string.c_no_postulated_moms));
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;
			case SamsungMomWebservices.MOMS_ERROR:
				Manager.getInstance()
						.showMessage(
								this,
								getResources().getString(
										R.string.c_error_loading_moms));
				break;
			}
		}
	}
}
