package com.cdi.samsung.views.register;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Observable;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;

import com.cdi.samsung.R;
import com.cdi.samsung.app.Manager;
import com.cdi.samsung.app.SamsungMomWebservices;
import com.cdi.samsung.app.SessionStore;
import com.facebook.android.AsyncFacebookRunner;
import com.facebook.android.AsyncFacebookRunner.RequestListener;
import com.facebook.android.DialogError;
import com.facebook.android.Facebook.DialogListener;
import com.facebook.android.FacebookError;
import com.facebook.android.Util;
import com.niucodeninja.Validator;
import com.niucodeninja.webservices.WebServicesEvent;

public class RegisterActivity extends Activity implements OnClickListener,
		java.util.Observer {

	private Button submitButtom, btnFacebook;
	private EditText user_name;
	private EditText user_email;
	private EditText user_phone;
	private CheckBox user_information;
	private CheckBox user_terms;

	private SamsungMomWebservices ws;
	private Spinner user_country;

	private Handler mHandler = new Handler();

	String[] countries = { "CO", "BR", "AR", "UY", "PY", "CL", "MX", "PE", "VE" };

	/*
	 * 1 Colombia CO 2 Brasil BR 3 Argentina AR 4 Uruguay UY 5 Paraguay PY 6
	 * Chile CL 7 MŽxico MX 8 Perœ PE 9 Venezuela VE
	 */

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register);

		// Countries
		user_country = ((Spinner) findViewById(R.id.reg_country));
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
				this, R.array.c_register_countries,
				android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		user_country.setAdapter(adapter);

		// UI
		user_name = (EditText) findViewById(R.id.reg_name);
		user_email = (EditText) findViewById(R.id.reg_email);
		user_phone = (EditText) findViewById(R.id.reg_phone);

		user_information = (CheckBox) findViewById(R.id.reg_information);
		user_terms = (CheckBox) findViewById(R.id.reg_terms);

		submitButtom = (Button) findViewById(R.id.btnRegister);
		submitButtom.setOnClickListener(this);

		btnFacebook = (Button) findViewById(R.id.btnFacebook);
		btnFacebook.setOnClickListener(this);

		// Webservices
		ws = new SamsungMomWebservices(Manager.WS_BASE);
		ws.addObserver(this);
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.btnFacebook:
			Manager.getInstance().displayLoading(this);
			if (!SessionStore.restore(Manager.getInstance().fbUtil.facebook,
					Manager.getInstance().getContext())) {

				Manager.getInstance().fbUtil.facebook.authorize(this,
						new String[] { "email", "user_location",
								"publish_stream" }, new DialogListener() {
							@Override
							public void onComplete(Bundle values) {
								Log.i("Facebook", "Facebook:" + values);
								SessionStore.save(
										Manager.getInstance().fbUtil.facebook,
										Manager.getInstance().getContext());
								getUserInformation();
							}

							@Override
							public void onFacebookError(FacebookError error) {
								Manager.getInstance().hideLoading();
								Log.e("Facebook", "Error:onFacebookError "
										+ error.getMessage());
							}

							@Override
							public void onError(DialogError e) {
								Manager.getInstance().hideLoading();
								Log.e("Facebook",
										"Error:onError " + e.getMessage());
							}

							@Override
							public void onCancel() {
								Manager.getInstance().hideLoading();
								Log.e("Facebook", "Error:onCancel");
							}
						});
			} else {
				getUserInformation();
			}
			break;
		case R.id.btnRegister:
			Manager.getInstance().NAME = user_name.getText().toString();
			Manager.getInstance().EMAIL = user_email.getText().toString();
			Manager.getInstance().PHONE = user_phone.getText().toString();
			boolean terms = user_terms.isChecked();
			boolean information = user_information.isChecked();
			Manager.getInstance().COUNTRY = ""
					+ (user_country.getSelectedItemPosition() + 1);

			if (Validator.isValidName(Manager.getInstance().NAME)) {
				if (Validator.isValidEmail(Manager.getInstance().EMAIL)) {
					if (Validator.isValidPhoneNumber(
							Manager.getInstance().PHONE,
							countries[user_country.getSelectedItemPosition()])) {
						if (terms) {
							Manager.getInstance().displayLoading(this);
							ws.register(Manager.getInstance().NAME,
									Manager.getInstance().EMAIL,
									Manager.getInstance().PHONE,
									Manager.getInstance().IMEI,
									Manager.getInstance().PASSWORD,
									Manager.getInstance().COUNTRY, information);
						} else {
							Manager.getInstance().showMessage(
									this,
									getResources().getString(
											R.string.c_reg_accept_terms));
						}
					} else {
						Manager.getInstance().showMessage(
								this,
								getResources().getString(
										R.string.c_reg_invalid_phone));
					}
				} else {
					Manager.getInstance().showMessage(
							this,
							getResources().getString(
									R.string.c_reg_invalid_email));
				}
			} else {
				Manager.getInstance().showMessage(this,
						getResources().getString(R.string.c_reg_invalid_name));
			}
			break;
		}
	}

	protected void getUserInformation() {
		AsyncFacebookRunner mAsyncRunner = new AsyncFacebookRunner(
				Manager.getInstance().fbUtil.facebook);
		mAsyncRunner.request("me", new RequestListener() {

			@Override
			public void onMalformedURLException(MalformedURLException e,
					Object state) {
				Manager.getInstance().hideLoading();
				Log.i("Facebook", "Error:onIOException:me " + e.getMessage());
			}

			@Override
			public void onIOException(IOException e, Object state) {
				Manager.getInstance().hideLoading();
				Log.i("Facebook", "Error:onIOException:me " + e.getMessage());
			}

			@Override
			public void onFileNotFoundException(FileNotFoundException e,
					Object state) {
				Manager.getInstance().hideLoading();
				Log.i("Facebook",
						"Error:onFileNotFoundException:me " + e.getMessage());
			}

			@Override
			public void onFacebookError(FacebookError e, Object state) {
				Manager.getInstance().hideLoading();
				Log.e("Facebook", "Error:onFacebookError:me " + e.getMessage());
			}

			@Override
			public void onComplete(String response, Object state) {
				// process the response here: executed in
				// background thread
				Log.i("Facebook", "Response: " + response.toString());
				try {
					JSONObject json = Util.parseJson(response);

					final String name = json.getString("name");
					final String email = json.getString("email");
					// final String location = json
					// .getJSONObject("location")
					// .getString("name").split(",")[2];
					// final String fbId =
					// json.getString("id");

					mHandler.post(new Runnable() {
						public void run() {
							user_email.setText(email);
							user_name.setText(name);
							Manager.getInstance().hideLoading();
						}
					});
					// user_country.set

				} catch (FacebookError e) {
					Log.e("Facebook", "Error:onComplete:1:me " + e.getMessage());
				} catch (JSONException e) {
					Log.e("Facebook", "Error:onComplete:2:me " + e.getMessage());
				}
			}
		});
	}

	@Override
	public void update(Observable observable, Object data) {
		Manager.getInstance().hideLoading();
		if (data instanceof WebServicesEvent) {
			WebServicesEvent event = (WebServicesEvent) data;
			switch (event.getIdEvent()) {
			case SamsungMomWebservices.REGISTER_COMPLETE:
				JSONObject json_data = (JSONObject) event.getData();
				try {
					Manager.getInstance().ID = json_data.getString("id");
					Manager.getInstance().POSTULATE = "0";
					Manager.getInstance().showMessage(this,
							getResources().getString(R.string.c_reg_user_ok));
				} catch (Exception e) {
					Manager.getInstance().showMessage(
							this,
							getResources().getString(
									R.string.c_reg_user_fail_loading));
				}
				Manager.getInstance().getDispatcher().open(this, "menu", true);
				break;
			case SamsungMomWebservices.REGISTER_ERROR:
				Manager.getInstance().showMessage(this,
						getResources().getString(R.string.c_reg_user_fail));
				break;
			}
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		Manager.getInstance().fbUtil.facebook.authorizeCallback(requestCode,
				resultCode, data);
	}
}
