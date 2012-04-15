package com.cdi.samsung.app.postulate;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.cdi.samsung.R;
import com.lamerman.FileDialogActivity;

public class PostulateActivity extends Activity implements OnClickListener {

	private Button btnPostulate;
	private int REQUEST_SAVE = 0x99;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.postulate);

		btnPostulate = (Button) findViewById(R.id.btnPostulate);
		btnPostulate.setOnClickListener(this);
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
			}
		} else if (resultCode == Activity.RESULT_CANCELED) {
			Log.i("Postulate", "file not selected");
		}
	}
}
