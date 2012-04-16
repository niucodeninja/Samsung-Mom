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
package com.niucodeninja;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
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

	public String htmlParams() {
		String combinedParams = "";
		for (NameValuePair p : this) {
			String paramString = "";
			try {
				paramString = p.getName() + "="
						+ URLEncoder.encode(p.getValue(), "UTF-8");
			} catch (UnsupportedEncodingException e) {
			}
			if (combinedParams.length() > 1) {
				combinedParams += "&" + paramString;
			} else {
				combinedParams += paramString;
			}
		}
		return combinedParams;
	}
}
