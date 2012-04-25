package com.cdi.samsung.app;

import com.facebook.android.Facebook;

public class FacebookUtil {
	public Facebook facebook;

	public FacebookUtil(String appId) {
		facebook = new Facebook(appId);
	}
}
