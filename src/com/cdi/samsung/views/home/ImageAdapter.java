package com.cdi.samsung.views.home;

import java.util.ArrayList;

import android.content.Context;
import android.content.res.TypedArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.Gallery.LayoutParams;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.cdi.samsung.R;
import com.cdi.samsung.app.models.Mom;
import com.niucodeninja.BitmapDownloaderTask;

public class ImageAdapter extends BaseAdapter {

	int item_background;
	// private Context context;
	private TypedArray attr;
	private ArrayList<Mom> listOfMoms;
	// private boolean[] flags;
	private LayoutInflater inflater;

	public ImageAdapter(Context context, ArrayList<Mom> listOfMoms) {
		// this.context = context;
		this.listOfMoms = listOfMoms;
		// flags = new boolean[listOfMoms.size()];
		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		attr = context.obtainStyledAttributes(R.styleable.moms_gallery_style);
		item_background = attr
				.getResourceId(
						R.styleable.moms_gallery_style_android_galleryItemBackground,
						0);
		attr.recycle();
	}

	public int getCount() {
		return this.listOfMoms.size();
	}

	public Object getItem(int position) {
		return position;
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		/*
		 * ImageView i = new ImageView(context); i.setLayoutParams(new
		 * Gallery.LayoutParams(150, 100));
		 * i.setScaleType(ImageView.ScaleType.FIT_XY);
		 * i.setBackgroundResource(item_background); return i;
		 */
		convertView = inflater.inflate(R.layout.gallery_item, null);
		// if (flags[position] == false) {
		ImageView image = (ImageView) convertView.findViewById(R.id.idImage);
		TextView momName = (TextView) convertView.findViewById(R.id.momName);
		TextView votos = (TextView) convertView.findViewById(R.id.mom_flowers);
		TextView tipica = (TextView) convertView.findViewById(R.id.tipical_mom);
		TextView description = (TextView) convertView
				.findViewById(R.id.description_mom);

		// image.setBackgroundResource(item_background);
		image.setScaleType(ImageView.ScaleType.FIT_CENTER);
		ProgressBar progressBar = (ProgressBar) convertView
				.findViewById(R.id.idProgressBar);
		BitmapDownloaderTask task = new BitmapDownloaderTask(image, progressBar);
		task.execute(listOfMoms.get(position).getPic3());
		momName.setText(listOfMoms.get(position).getName());
		// votos.setText(listOfMoms.get(position).getVotes());

		tipica.setText(listOfMoms.get(position).getTypicalSentence());
		description.setText(listOfMoms.get(position).getWhyBeASmartMom());
		// flags[position] = true;
		// }
		// convertView.setLayoutParams(new Gallery.LayoutParams(150, 100));
		convertView.setLayoutParams(new Gallery.LayoutParams(
				LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
		return convertView;
	}
}
