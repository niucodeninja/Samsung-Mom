/*
 * Copyright (c) 2012 Jorge Osorio <niucodeninja@gmail.com>
 * 
 * Permission is hereby granted, free of charge, to any
 * person obtaining a copy of this software and associated
 * documentation files (the "Software"), to deal in the
 * Software without restriction, including without limitation
 * the rights to use, copy, modify, merge, publish,
 * distribute, sublicense, and/or sell copies of the
 * Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice
 * shall be included in all copies or substantial portions of
 * the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY
 * KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 * WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
 * PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS
 * OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR
 * OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR
 * OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.niucodeninja.webservices;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.niucodeninja.events.EventDispatcher;
import com.niucodeninja.webservices.RestClient.RequestMethod;

public class RestEventDispatcher extends EventDispatcher {

	private String base_url;
	private RestClient rest_client;

	public RestEventDispatcher(String base) {
		super();
		base_url = base;
	}

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			dispatchEvent(new RestEvent(RestEvent.ON_ERROR, null));
		}
	};

	public void execute(final String path, final RequestMethod method,
			ArrayList<NameValuePair> params) {
		final String url_to_call = base_url + path;
		rest_client = new RestClient(url_to_call);
		if (params != null) {
			for (NameValuePair param : params) {
				rest_client.AddParam(param.getName(), param.getValue());
			}
		}
		Log.i("RestEventDispatcher::execute", method + " " + url_to_call);
		Log.i("RestEventDispatcher::execute", params.toString());
		final Runnable r = new Runnable() {

			public void run() {
				String response = null;
				JSONObject data = null;
				try {
					rest_client.Execute(method);
					response = rest_client.getResponse();
					Log.i("RestEventDispatcher::execute", response);
				} catch (Exception e) {
					Log.e("RestEventDispatcher::execute", e.getMessage());
				} finally {
					if (response != null) {
						try {
							data = new JSONObject(response);
							dispatchEvent(new RestEvent(RestEvent.ON_COMPLETE,
									data));
						} catch (JSONException e) {
							Log.e("RestEventDispatcher::execute",
									e.getMessage());
							dispatchEvent(new RestEvent(RestEvent.ON_ERROR,
									null));
						}
					} else {
						dispatchEvent(new RestEvent(RestEvent.ON_ERROR, null));
					}
				}
			}
		};
		handler.postDelayed(r, 1000);
	}
}