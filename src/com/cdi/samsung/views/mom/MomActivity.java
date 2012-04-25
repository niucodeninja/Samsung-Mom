package com.cdi.samsung.views.mom;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.cdi.samsung.R;
import com.cdi.samsung.app.Manager;
import com.cdi.samsung.app.SessionStore;
import com.cdi.samsung.app.models.Mom;
import com.facebook.android.DialogError;
import com.facebook.android.FacebookError;
import com.facebook.android.Facebook.DialogListener;
import com.niucodeninja.BitmapDownloaderTask;

public class MomActivity extends Activity implements OnClickListener {

	private ImageView image;
	private ProgressBar progressBar;
	private Mom mom;
	private Button fbVote;
	private Handler mHandler = new Handler();

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

		fbVote = (Button) findViewById(R.id.fbVote);
		fbVote.setOnClickListener(this);

		Log.i("Mom", "Load picture: " + mom.getPic3());
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.fbVote:
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
								publishUser();
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
				publishUser();
			}
			break;
		}
	}

	protected void publishUser() {
		final MomActivity self = this;
		mHandler.post(new Runnable() {
			public void run() {
				// post on user's wall.
				Manager.getInstance().fbUtil.facebook.dialog(self, "feed",
						new DialogListener() {

							@Override
							public void onComplete(Bundle values) {
								Log.i("publishUser Facebook", "Facebook:"
										+ values);
							}

							@Override
							public void onFacebookError(FacebookError error) {
								Log.e("publishUser Facebook",
										"Error:onFacebookError "
												+ error.getMessage());
							}

							@Override
							public void onError(DialogError e) {
								Log.e("publishUser Facebook", "Error:onError "
										+ e.getMessage());
							}

							@Override
							public void onCancel() {
								Log.e("publishUser Facebook", "Error:onCancel");
							}
						});
			}
		});
	}
}
