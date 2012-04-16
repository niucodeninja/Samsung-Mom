package com.cdi.samsung.app.postulate;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Observable;
import java.util.Observer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.cdi.samsung.R;
import com.cdi.samsung.app.Manager;
import com.lamerman.FileDialogActivity;
import com.niucodeninja.HttpFileUploader;
import com.niucodeninja.Params;
import com.niucodeninja.Validator;
import com.niucodeninja.events.Event;

public class PostulateActivity extends Activity implements OnClickListener,
		Observer {

	private Button btnPostulate;
	private int REQUEST_SAVE = 0x99;
	private EditText mother_name;
	private EditText pos_tipical_sentence;
	private EditText smart_mother_sentence;
	private EditText why_be_mother_of_year;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.postulate);

		btnPostulate = (Button) findViewById(R.id.btnPostulate);
		btnPostulate.setOnClickListener(this);

		mother_name = (EditText) findViewById(R.id.pos_name);
		pos_tipical_sentence = (EditText) findViewById(R.id.pos_tipical_sentence);
		smart_mother_sentence = (EditText) findViewById(R.id.pos_smart_mother_sentence);
		why_be_mother_of_year = (EditText) findViewById(R.id.pos_why_be_mother_of_year);
	}

	@Override
	public void onClick(View arg) {
		if (arg.getId() == R.id.btnPostulate) {
			showFileDialog();
		}
	}

	private void showFileDialog() {
		Intent intent = new Intent(getBaseContext(), FileDialogActivity.class);
		intent.putExtra(FileDialogActivity.START_PATH, "/sdcard");
		intent.putExtra(FileDialogActivity.CAN_SELECT_DIR, false);
		intent.putExtra(FileDialogActivity.FORMAT_FILTER, new String[] { "png",
				"jpge" });
		startActivityForResult(intent, REQUEST_SAVE);
	}

	public synchronized void onActivityResult(final int requestCode,
			int resultCode, final Intent data) {

		if (resultCode == Activity.RESULT_OK) {
			String filePath = data
					.getStringExtra(FileDialogActivity.RESULT_PATH);
			if (requestCode == REQUEST_SAVE) {
				Log.i("Postulate", "Selected: " + filePath);
				// Validate Data
				String name = mother_name.getText().toString();
				String tsentence = pos_tipical_sentence.getText().toString();
				String smother_sentence = smart_mother_sentence.getText()
						.toString();
				String wmother_of_year = why_be_mother_of_year.getText()
						.toString();
				if (Validator.isValidName(name)) {
					if (tsentence.length() > 2 && smother_sentence.length() > 2
							&& wmother_of_year.length() > 2) {

						// Upload the file
						Params params = new Params();
						params.AddParam("imei", Manager.getInstance().IMEI);
						params.AddParam("pais", Manager.getInstance().COUNTRY);
						params.AddParam("frase_tipica", tsentence);
						params.AddParam("smart_favorito", smother_sentence);
						params.AddParam("frase_porque", tsentence);

						FileInputStream fis;
						try {
							fis = new FileInputStream(filePath);
							HttpFileUploader htfu = new HttpFileUploader(
									Manager.WS_BASE, params, "mom");
							htfu.doStart(fis);
							htfu.addObserver(this);
							new Thread(htfu).start();

							Manager.getInstance().displayLoading(this);
						} catch (FileNotFoundException e) {
							Log.i("Postulate", "Error: " + e.getMessage());
						}
					} else {
						Manager.getInstance().showMessage(
								this,
								getResources().getString(
										R.string.c_pos_fill_all_fields));
					}
				} else {
					Manager.getInstance().showMessage(
							this,
							getResources().getString(
									R.string.c_reg_invalid_name));
				}
			}
		} else if (resultCode == Activity.RESULT_CANCELED) {
			Log.i("Postulate", "file not selected");
		}
	}

	@Override
	public void update(Observable observable, Object data) {
		Manager.getInstance().hideLoading();
		if (data instanceof Event) {
			Event event = (Event) data;
			switch (event.getIdEvent()) {
			case HttpFileUploader.ON_UPLOAD_COMPLETE:
				Log.i("Postulate", "Uploaded success");
				Manager.getInstance().showMessage(this,
						getResources().getString(R.string.c_postulate_ok));
				break;
			case HttpFileUploader.ON_UPLOAD_ERROR:
				Log.i("Postulate", "Uploaded fail");
				Manager.getInstance().showMessage(this,
						getResources().getString(R.string.c_postulate_fail));
				break;
			}
		}
	}
}
