package com.optimumnano.quickcharge.http;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

public class AllHostNameVerifier implements HostnameVerifier {

	@Override
	public boolean verify(String hostname, SSLSession session) {
		return true;
	}

}