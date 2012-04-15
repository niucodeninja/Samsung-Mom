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

import java.util.Observable;
import java.util.Observer;

import org.json.JSONObject;

import android.util.Log;

import com.niucodeninja.Params;
import com.niucodeninja.events.EventDispatcher;
import com.niucodeninja.webservices.RestClient.RequestMethod;

public class WebServices extends EventDispatcher {

	private String base;

	public WebServices(String base) {
		this.base = base;
	}

	public void call(final String service, Params params,
			final RequestMethod request_method, final int id_event_complete,
			final int id_event_error, final boolean collect_data) {
		RestEventDispatcher rest_event_dispatcher = new RestEventDispatcher(
				base);
		rest_event_dispatcher.addObserver(new Observer() {
			public void update(Observable observable, Object data) {
				switch (((RestEvent) data).getIdEvent()) {
				case RestEvent.ON_COMPLETE:
					try {
						JSONObject jsData = (JSONObject) ((RestEvent) data)
								.getData();
						if (jsData.getString("status").equals("success")) {

							JSONObject jsObjects = null;
							if (collect_data) {
								jsObjects = jsData.getJSONObject("data");
							}
							dispatchEvent(new WebServicesEvent(
									id_event_complete, jsObjects));
						} else {
							// JSONArray errors = jsData.getJSONArray("errors");
							dispatchEvent(new WebServicesEvent(id_event_error,
									null));
						}
					} catch (Exception e) {
						Log.e("WebServices", "Service: " + service
								+ ", Error: " + e.getMessage());
						dispatchEvent(new WebServicesEvent(id_event_error, null));
					}
					break;
				case RestEvent.ON_ERROR:
					dispatchEvent(new WebServicesEvent(id_event_error, null));
					break;
				}
			}
		});
		rest_event_dispatcher.execute(service, request_method, params);
	}
}