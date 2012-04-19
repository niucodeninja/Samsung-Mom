package com.cdi.samsung.views.ranking;

import java.util.LinkedList;
import java.util.Observable;
import java.util.Observer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ListActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;

import com.cdi.samsung.R;
import com.cdi.samsung.app.Manager;
import com.cdi.samsung.app.SamsungMomWebservices;
import com.cdi.samsung.app.models.Mom;
import com.markupartist.android.widget.PullToRefreshListView;
import com.markupartist.android.widget.PullToRefreshListView.OnRefreshListener;
import com.niucodeninja.webservices.WebServicesEvent;

public class RankingActivity extends ListActivity implements Observer {

	private LinkedList<String> list_of_moms;
	private SamsungMomWebservices ws;
	private boolean firstTime = true;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ranking);

		// Set a listener to be invoked when the list should be refreshed.
		((PullToRefreshListView) getListView())
				.setOnRefreshListener(new OnRefreshListener() {
					@Override
					public void onRefresh() {
						loadData();
					}
				});

		list_of_moms = new LinkedList<String>();
		// list_of_moms.addAll(Arrays.asList(mStrings));

		// Webservices
		ws = new SamsungMomWebservices(Manager.WS_BASE);
		ws.addObserver(this);

		// ((PullToRefreshListView) getListView()).onRefresh();
		loadData();
		Manager.getInstance().displayLoading(this);
	}

	private void loadData() {
		ws.getRanking(Manager.getInstance().IMEI,
				Manager.getInstance().PASSWORD, Manager.getInstance().COUNTRY);
	}

	/*
	 * private String[] mStrings = { "Abbaye de Belloc",
	 * "Abbaye du Mont des Cats", "Abertam", "Abondance", "Ackawi", "Acorn",
	 * "Adelost", "Affidelice au Chablis", "Afuega'l Pitu", "Airag", "Airedale",
	 * "Aisy Cendre", "Allgauer Emmentaler" };
	 */
	@Override
	public void update(Observable observable, Object data) {
		if (firstTime == false) {
			((PullToRefreshListView) getListView()).onRefreshComplete();
		} else {
			Manager.getInstance().hideLoading();
			firstTime = false;
		}
		if (data instanceof WebServicesEvent) {
			WebServicesEvent event = (WebServicesEvent) data;
			switch (event.getIdEvent()) {
			case SamsungMomWebservices.RANKING_COMPLETE:
				JSONObject json_data = (JSONObject) event.getData();
				try {
					JSONArray moms = json_data.getJSONArray("madres");
					if (moms.length() > 0) {
						list_of_moms.clear();
						for (int i = 0; i < moms.length(); ++i) {
							Mom mom = new Mom(moms.getJSONObject(i));
							if (mom.isOk()) {
								list_of_moms.add(mom.getName() + "\nVotos: " + mom.getVotes());
							}
						}
						ArrayAdapter<String> adapter = new ArrayAdapter<String>(
								this, android.R.layout.simple_list_item_1,
								list_of_moms);
						setListAdapter(adapter);
					} else {
						Manager.getInstance().showMessage(
								this,
								getResources().getString(
										R.string.c_no_ranking_moms));
					}
				} catch (JSONException e) {
				}
				break;
			case SamsungMomWebservices.RANKING_ERROR:
				Manager.getInstance().showMessage(
						this,
						getResources().getString(
								R.string.c_error_loading_ranking));
				break;
			}
		}
	}
}
