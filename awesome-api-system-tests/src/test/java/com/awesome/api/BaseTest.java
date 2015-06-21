package com.awesome.api;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

import org.apache.http.HttpResponse;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.junit.After;
import org.junit.Before;

public abstract class BaseTest {

	protected String baseURL = "http://localhost:8080/awesome-api-0.0.1-SNAPSHOT";

	protected CloseableHttpClient httpClient;

	@Before
	public void setUp() {
		httpClient = HttpClientBuilder.create().build();
	}

	@After
	public void tearDown() {
		try {
			httpClient.close();
		} catch (IOException e) {
			// Maybe don't fail, but print it to the err stream
			System.err.println(e.getMessage());
		}
		httpClient = null;
	}

	protected static String getIdFromLocationURL(String locationURL) {
		return locationURL.substring(locationURL.lastIndexOf('/') + 1);
	}

	protected String getBodyAsString(HttpResponse response) throws IOException {
		// Wrap a BufferedReader around the InputStream
		BufferedReader bufferedReader = new BufferedReader(getBodyAsReader(response));

		// Read response until the end
		String line = "";
		StringBuilder total = new StringBuilder();
		try {
			while ((line = bufferedReader.readLine()) != null) {
				total.append(line);
			}
		} finally {
			bufferedReader.close();
		}

		// Return full string
		return total.toString();
	}

	protected JSONObject getBodyAsJSONObject(HttpResponse response) throws IllegalStateException, IOException {
		Reader reader = null;
		try {
			reader = getBodyAsReader(response);
			return (JSONObject) JSONValue.parse(reader);
		} finally {
			if (reader != null) {
				reader.close();
			}
		}
	}

	protected JSONArray getBodyAsJSONArray(HttpResponse response) throws IllegalStateException, IOException {
		Reader reader = null;
		try {
			reader = getBodyAsReader(response);
			return (JSONArray) JSONValue.parse(reader);
		} finally {
			if (reader != null) {
				reader.close();
			}
		}
	}

	private Reader getBodyAsReader(HttpResponse response) throws IllegalStateException, IOException {
		return new InputStreamReader(response.getEntity().getContent());
	}
}
