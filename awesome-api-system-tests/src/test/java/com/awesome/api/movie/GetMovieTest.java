package com.awesome.api.movie;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class GetMovieTest extends MovieBaseTest {

	private String newMovie1Name = "Back to the Future";
	private Collection<Long> newMovie1GenreIds = Arrays.asList(2L);
	private String newMovie1Rating = "PG";
	private String newMovie1Id;

	private String newMovie2Name = "Back to the Future II";
	private Collection<Long> newMovie2GenreIds = Arrays.asList(2L);
	private String newMovie2Rating = "PG";
	private String newMovie2Id;

	@Before
	public void setUp() {

		super.setUp();

		newMovie1Id = createMovie(newMovie1Name, newMovie1GenreIds, newMovie1Rating);
		newMovie2Id = createMovie(newMovie2Name, newMovie2GenreIds, newMovie2Rating);
	}

	@After
	public void tearDown() {

		// Not explicitly needed, but good practice
		deleteMovie(newMovie1Id);
		deleteMovie(newMovie2Id);

		super.tearDown();
	}

	@Test
	public void getOneMovieTest() throws ClientProtocolException, IOException {

		// Form the request
		HttpGet getRequest = new HttpGet(movieURL + "/" + newMovie1Id);
		getRequest.addHeader("Accept", "application/json");

		// Send the request
		HttpResponse getResponse = httpClient.execute(getRequest);

		// Verify response status code
		Assert.assertEquals("response status code was not as expected", HttpStatus.SC_OK, getResponse.getStatusLine().getStatusCode());

		// Get the response content
		JSONObject data = getBodyAsJSONObject(getResponse);

		// Consume the response entity
		EntityUtils.consume(getResponse.getEntity());

		// Verify response content
		Assert.assertNotNull("response body was null", data);
		Assert.assertNotEquals("response body was empty", 0, data.size());

		Assert.assertEquals("name was not as expected", newMovie1Name, data.get("name"));
		Assert.assertEquals("genre ids were not as expected", newMovie1GenreIds, data.get("genreIds"));
		Assert.assertEquals("rating was not as expected", newMovie1Rating, data.get("rating"));
		Assert.assertEquals("id was not as expected", newMovie1Id, data.get("id"));
	}

	@Test
	public void getAllMoviesTest() throws ClientProtocolException, IOException {

		// Form the request
		HttpGet getRequest = new HttpGet(movieURL);
		getRequest.addHeader("Accept", "application/json");

		// Send the request
		HttpResponse getResponse = httpClient.execute(getRequest);

		// Verify response status code
		Assert.assertEquals("response status code was not as expected", HttpStatus.SC_OK, getResponse.getStatusLine().getStatusCode());

		// Get the response content
		JSONArray data = getBodyAsJSONArray(getResponse);

		// Consume the response entity
		EntityUtils.consume(getResponse.getEntity());

		// Verify response content
		Assert.assertNotNull("response body was null", data);
		Assert.assertEquals("response body did not have the expected number of elements", 2, data.size());

		for (int i = 0; i < data.size(); i++) {
			JSONObject obj = (JSONObject) data.get(i);
			String id = (String) obj.get("id");

			if (id.equals(newMovie1Id)) {
				Assert.assertEquals("name was not as expected", newMovie1Name, obj.get("name"));
				Assert.assertEquals("genre ids were not as expected", newMovie1GenreIds, obj.get("genreIds"));
				Assert.assertEquals("rating was not as expected", newMovie1Rating, obj.get("rating"));
				Assert.assertEquals("id was not as expected", newMovie1Id, obj.get("id"));
			} else if (id.equals(newMovie2Id)) {
				Assert.assertEquals("name was not as expected", newMovie2Name, obj.get("name"));
				Assert.assertEquals("genre ids were not as expected", newMovie2GenreIds, obj.get("genreIds"));
				Assert.assertEquals("rating was not as expected", newMovie2Rating, obj.get("rating"));
				Assert.assertEquals("id was not as expected", newMovie2Id, obj.get("id"));
			} else {
				Assert.fail("Unrecognized movie returned");
			}
		}
	}

	@Test
	public void getOneMovieNonExistingTest() throws ClientProtocolException, IOException {

		// Form the request
		HttpGet getRequest = new HttpGet(movieURL + "/bogusID");
		getRequest.addHeader("Accept", "application/json");

		// Send the request
		HttpResponse getResponse = httpClient.execute(getRequest);

		// Consume the response entity
		EntityUtils.consume(getResponse.getEntity());

		// Verify response status code
		Assert.assertEquals("response status code was not as expected", HttpStatus.SC_NOT_FOUND, getResponse.getStatusLine().getStatusCode());
	}
}
