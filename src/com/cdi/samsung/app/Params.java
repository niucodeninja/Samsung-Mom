package com.cdi.samsung.app;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

@SuppressWarnings("serial")
public class Params extends ArrayList<NameValuePair> {

	public Params() {
		super();
	}

	public void AddParam(String name, String value) {
		add(new BasicNameValuePair(name, value));
	}
}
