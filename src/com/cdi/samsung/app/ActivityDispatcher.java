package com.cdi.samsung.app;

import java.util.HashMap;

import org.apache.http.NameValuePair;

import android.app.Activity;
import android.content.Intent;

@SuppressWarnings("rawtypes")
public class ActivityDispatcher {

	HashMap<String, Class> handlers;

	public ActivityDispatcher() {
		handlers = new HashMap<String, Class>();
	}

	public void addHandler(String name, Class clazz) {
		this.handlers.put(name, clazz);
	}

	public void open(Activity activity, String name, boolean finish,
			Params params) {
		Class clazz = handlers.get(name);
		if (clazz != null) {
			Intent intent = new Intent(activity, clazz);
			if (params != null) {
				for (NameValuePair param : params) {
					intent.putExtra(param.getName(), param.getValue());
				}
			}
			activity.startActivity(intent);
			if (finish) {
				activity.finish();
			}
		}
	}

	public void open(Activity activity, String name, boolean finish) {
		open(activity, name, finish, null);
	}
}
