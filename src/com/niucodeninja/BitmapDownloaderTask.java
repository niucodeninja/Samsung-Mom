package com.niucodeninja;

import java.io.InputStream;
import java.lang.ref.WeakReference;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

/*
 class FlushedInputStream extends FilterInputStream {
 public FlushedInputStream(InputStream inputStream) {
 super(inputStream);
 }

 @Override
 public long skip(long n) throws IOException {
 long totalBytesSkipped = 0L;
 while (totalBytesSkipped < n) {
 long bytesSkipped = in.skip(n - totalBytesSkipped);
 if (bytesSkipped == 0L) {
 int _byte = read();
 if (_byte < 0) {
 break; // we reached EOF
 } else {
 bytesSkipped = 1; // we read one byte
 }
 }
 totalBytesSkipped += bytesSkipped;
 }
 return totalBytesSkipped;
 }
 }
 */
public class BitmapDownloaderTask extends AsyncTask<String, Void, Bitmap> {

	private final WeakReference<ImageView> imageViewReference;
	private ProgressBar progress_bar;

	public BitmapDownloaderTask(ImageView imageView, ProgressBar progress_bar) {
		imageViewReference = new WeakReference<ImageView>(imageView);
		this.progress_bar = progress_bar;
	}

	@Override
	// Actual download method, run in the task thread
	protected Bitmap doInBackground(String... params) {
		// params comes from the execute() call: params[0] is the url.
		return downloadBitmap(params[0]);
	}

	@Override
	// Once the image is downloaded, associates it to the imageView
	protected void onPostExecute(Bitmap bitmap) {
		if (isCancelled()) {
			bitmap = null;
		}

		if (imageViewReference != null) {
			ImageView imageView = imageViewReference.get();
			if (imageView != null) {
				progress_bar.setVisibility(View.GONE);
				imageView.setImageBitmap(bitmap);
			}
		}
	}

	private Bitmap downloadBitmap(String url) {
		final AndroidHttpClient client = AndroidHttpClient
				.newInstance("Android");
		final HttpGet getRequest = new HttpGet(url);

		try {
			HttpResponse response = client.execute(getRequest);
			final int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode != HttpStatus.SC_OK) {
				Log.w("ImageDownloader", "Error " + statusCode
						+ " while retrieving bitmap from " + url);
				return null;
			}

			final HttpEntity entity = response.getEntity();
			if (entity != null) {
				InputStream inputStream = null;
				try {
					inputStream = entity.getContent();
					final Bitmap bitmap = BitmapFactory
							.decodeStream(inputStream);
					return bitmap;
				} finally {
					if (inputStream != null) {
						inputStream.close();
					}
					entity.consumeContent();
				}
			}
		} catch (Exception e) {
			// Could provide a more explicit error message for IOException or
			// IllegalStateException
			getRequest.abort();
			Log.e("ImageDownloader", "Error while retrieving bitmap from "
					+ url);
		} finally {
			if (client != null) {
				client.close();
			}
		}
		return null;
	}
}