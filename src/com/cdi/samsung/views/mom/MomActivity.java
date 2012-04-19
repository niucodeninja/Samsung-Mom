package com.cdi.samsung.views.mom;

import com.cdi.samsung.R;
import com.cdi.samsung.app.Manager;
import com.cdi.samsung.app.models.Mom;
import com.niucodeninja.BitmapDownloaderTask;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ProgressBar;

public class MomActivity extends Activity implements OnClickListener {

	private ImageView image;
	private ProgressBar progressBar;
	private Mom mom;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mom);

		// Bundle extras = getIntent().getExtras();
		// int position = extras.getInt("mom");

		// -- Mom
		mom = Manager.getInstance().currentMom;// Manager.getInstance().listOfMoms.get(position);

		image = (ImageView) findViewById(R.id.mom_image);
		// image.setScaleType(ImageView.ScaleType.FIT_CENTER);

		progressBar = (ProgressBar) findViewById(R.id.mom_loading);
		BitmapDownloaderTask task = new BitmapDownloaderTask(image, progressBar);
		task.execute(mom.getPic3());
		
		Log.i("Mom", "Load picture: " + mom.getPic3());
	}

	@Override
	public void onClick(DialogInterface dialog, int which) {

	}
}
