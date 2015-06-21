package com.awesome.api.movie;

import java.io.IOException;
import java.util.Collection;

import org.apache.http.Consts;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.junit.After;
import org.junit.Assert;

import com.awesome.api.BaseTest;

public abstract class MovieBaseTest extends BaseTest {

	protected String movieURL = baseURL + "/movie";

	@After
	public void tearDown() {
		try {
			cleanUpResidualMovies();
		} catch (IllegalStateException | IOException e) {
			// Maybe don't fail, but print it to the err stream
			System.err.println(e.getMessage());
		} finally {
			super.tearDown();
		}
	}

	private void cleanUpResidualMovies() throws IllegalStateException, IOException {
		// Use the location header to perform a get
		HttpGet getRequest = new HttpGet(movieURL);
		getRequest.addHeader("Accept", "application/json");

		// Send the request
		HttpResponse getResponse = httpClient.execute(getRequest);

		// Verify response status code
		Assert.assertEquals("response status code was not as expected", HttpStatus.SC_OK, getResponse.getStatusLine().getStatusCode());

		// Verify response content
		JSONArray data = getBodyAsJSONArray(getResponse);
		Assert.assertNotNull("response body was null", data);

		for (int i = 0; i < data.size(); i++) {
			JSONObject obj = (JSONObject) data.get(i);
			deleteMovie((String) obj.get("id"));
		}
	}

	protected void deleteMovie(String movieId) {
		try {

			// Form the request
			HttpDelete deleteRequest = new HttpDelete(movieURL + "/" + movieId);

			// Send the request
			HttpResponse deleteResponse = httpClient.execute(deleteRequest);

			// Consume the response entity
			EntityUtils.consume(deleteResponse.getEntity());

			// Verify response status code
			Assert.assertEquals("response status code was not as expected", HttpStatus.SC_OK, deleteResponse.getStatusLine().getStatusCode());
		} catch (IOException e) {
			// Maybe don't fail, but print it to the err stream
			System.err.println(e.getMessage());
		}
	}

	@SuppressWarnings("unchecked")
	protected String createMovie(String name, Collection<Long> genreIds, String rating) {
		try {
			// Form the request
			HttpPost postRequest = new HttpPost(movieURL);
			postRequest.addHeader("Content-Type", "application/json");

			JSONObject jsonObj = new JSONObject();
			jsonObj.put("name", name);
			jsonObj.put("genreIds", genreIds);
			jsonObj.put("rating", rating);
			StringEntity entity = new StringEntity(jsonObj.toString(), Consts.UTF_8);
			postRequest.setEntity(entity);

			// Send the request
			HttpResponse postResponse = httpClient.execute(postRequest);

			// Consume the response entity
			EntityUtils.consume(postResponse.getEntity());

			// Verify response status code
			Assert.assertEquals("response status code was not as expected", HttpStatus.SC_CREATED, postResponse.getStatusLine().getStatusCode());

			// Verify location header
			Header locationHeader = postResponse.getHeaders("Location")[0];
			String locationURL = locationHeader.getValue();
			Assert.assertNotNull("location header was null", locationURL);
			Assert.assertNotEquals("location header was empty", "", locationURL);

			return getIdFromLocationURL(locationURL);

		} catch (IOException e) {
			// Maybe don't fail, but print it to the err stream
			System.err.println(e.getMessage());
		}

		return null;
	}
}
