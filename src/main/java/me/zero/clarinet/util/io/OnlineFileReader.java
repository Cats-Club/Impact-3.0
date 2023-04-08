package me.zero.clarinet.util.io;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;

import me.zero.clarinet.util.Callback;

public class OnlineFileReader {
	
	private ArrayList<String> data = new ArrayList<String>();
	
	public final URL url;
	
	public OnlineFileReader(URL url) {
		this.url = url;
	}
	
	public static String getString(URL url) {
		OnlineFileReader ofr = new OnlineFileReader(url);
		ofr.read();
		if (ofr.hasFirst()) {
			return ofr.getFirst();
		}
		return null;
	}
	
	public static void getStringNewThread(Callback<String> callback, URL url) {
		new Thread() {
			@Override
			public void run() {
				callback.done(getString(url));
			}
		}.start();
	}
	
	public void read() {
		data.clear();
		try {
			if (url != null) {
				BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
				String line;
				while ((line = reader.readLine()) != null) {
					data.add(line);
				}
			} else {
			}
		} catch (IOException e) {
		}
	}
	
	public ArrayList<String> getRead() {
		return this.data;
	}
	
	public boolean hasFirst() {
		return data.size() > 0;
	}
	
	public String getFirst() {
		return data.get(0);
	}
}
