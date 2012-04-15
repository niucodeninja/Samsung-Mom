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
package com.niucodeninja.activities;

import java.util.HashMap;

import org.apache.http.NameValuePair;

import com.niucodeninja.Params;

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
