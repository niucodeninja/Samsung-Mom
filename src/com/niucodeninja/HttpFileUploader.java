package com.niucodeninja;

import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.http.NameValuePair;

import android.util.Log;

import com.niucodeninja.events.Event;
import com.niucodeninja.events.EventDispatcher;

public class HttpFileUploader extends EventDispatcher implements Runnable {

	public static final int ON_UPLOAD_COMPLETE = 0x1;
	public static final int ON_UPLOAD_ERROR = 0x2;

	URL connectURL;
	Params params;
	String responseString;
	String fileName;
	byte[] dataToServer;
	FileInputStream fileInputStream = null;

	public HttpFileUploader(String urlString, Params params, String fileName) {
		try {
			connectURL = new URL(urlString);
		} catch (Exception ex) {
			Log.e("HttpFileUploader", ex.getMessage());
		}
		this.params = params;
		this.fileName = fileName;
	}

	public void doStart(FileInputStream stream) {
		fileInputStream = stream;
	}

	void requestUpload() {
		String exsistingFileName = fileName;

		String lineEnd = "\r\n";
		String twoHyphens = "--";
		String boundary = "*****";
		try {
			// ------------------ CLIENT REQUEST

			HttpURLConnection conn = (HttpURLConnection) connectURL
					.openConnection();
			conn.setDoInput(true);
			conn.setDoOutput(true);
			conn.setUseCaches(false);

			conn.setRequestMethod("POST");
			conn.setRequestProperty("Connection", "Keep-Alive");
			conn.setRequestProperty("Content-Type",
					"multipart/form-data;boundary=" + boundary);
			DataOutputStream dos = new DataOutputStream(conn.getOutputStream());

			if (params.size() > 0) {
				// String[] _params = params.split("&");
				// if (_params.length > 0) {
				dos.writeBytes("--");
				dos.writeBytes(boundary);
				dos.writeBytes("\r\n");
				for (NameValuePair p : params) {
					// String[] _p = _params[p].split("=");
					dos.writeBytes("Content-Disposition: form-data; name=\"");
					dos.writeBytes(p.getName());
					dos.writeBytes("\"\r\n");
					dos.writeBytes("\r\n");
					dos.writeBytes(p.getValue());
					dos.writeBytes("\r\n");
					dos.writeBytes("--");
					dos.writeBytes(boundary);
					dos.writeBytes("\r\n");
				}
				// }
			}
			dos.writeBytes(twoHyphens + boundary + lineEnd);
			dos.writeBytes("Content-Disposition: form-data; name=\"uploadedfile\";filename=\""
					+ exsistingFileName + "\"" + lineEnd);
			dos.writeBytes(lineEnd);
			int bytesAvailable = fileInputStream.available();
			int maxBufferSize = 1024;
			int bufferSize = Math.min(bytesAvailable, maxBufferSize);
			byte[] buffer = new byte[bufferSize];

			int bytesRead = fileInputStream.read(buffer, 0, bufferSize);

			while (bytesRead > 0) {
				dos.write(buffer, 0, bufferSize);
				bytesAvailable = fileInputStream.available();
				bufferSize = Math.min(bytesAvailable, maxBufferSize);
				bytesRead = fileInputStream.read(buffer, 0, bufferSize);
			}

			// enviar multipart form data
			dos.writeBytes(lineEnd);
			dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

			// cerramos
			fileInputStream.close();
			dos.flush();

			InputStream is = conn.getInputStream();
			// retrieve the response from server
			int ch;

			StringBuffer b = new StringBuffer();
			while ((ch = is.read()) != -1) {
				b.append((char) ch);
			}
			String s = b.toString();
			Log.i("HttpFileUploader", "Data: " + s);
			dos.close();
			dispatchEvent(new Event(ON_UPLOAD_COMPLETE, null));
		} catch (MalformedURLException ex) {
			Log.e("HttpFileUploader", "error: " + ex.getMessage(), ex);
			dispatchEvent(new Event(ON_UPLOAD_ERROR, null));
		} catch (IOException ioe) {
			Log.e("HttpFileUploader", "error: " + ioe.getMessage(), ioe);
			dispatchEvent(new Event(ON_UPLOAD_ERROR, null));
		}
	}

	public void run() {
		requestUpload();
	}
}