package com.awesome.api.movie;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;

import org.apache.http.Consts;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;
import org.json.simple.JSONObject;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class CreateMovieTest extends MovieBaseTest {

	private String alreadyCreatedMovieName = "Elf";
	Collection<Long> alreadyCreatedMovieGenreIds = Arrays.asList(1L, 11L);
	private String alreadyCreatedMovieRating = "PG";
	private String alreadyCreatedMovieId;

	@Before
	public void setUp() {

		super.setUp();

		alreadyCreatedMovieId = createMovie(alreadyCreatedMovieName, alreadyCreatedMovieGenreIds, alreadyCreatedMovieRating);
	}

	@After
	public void tearDown() {

		// Not explicitly needed, but good practices
		deleteMovie(alreadyCreatedMovieId);

		super.tearDown();
	}

	@SuppressWarnings("unchecked")
	@Test
	public void createMovieAllDataTest() throws ClientProtocolException, IOException {

		String newMovieName = "Zoolander";
		Collection<Long> newMovieGenreIds = Arrays.asList(9L);
		String newMovieRating = "PG-13";
		String newMovieId;

		// Form the request
		HttpPost postRequest = new HttpPost(movieURL);
		postRequest.addHeader("Content-Type", "application/json");

		JSONObject jsonObj = new JSONObject();
		jsonObj.put("name", newMovieName);
		jsonObj.put("genreIds", newMovieGenreIds);
		jsonObj.put("rating", newMovieRating);
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

		// Get the ID
		newMovieId = getIdFromLocationURL(locationURL);

		try {

			// Use the location header to perform a get
			HttpGet getRequest = new HttpGet(locationURL);
			getRequest.addHeader("Accept", "application/json");

			// Send the request
			HttpResponse getResponse = httpClient.execute(getRequest);

			// Verify response status code
			Assert.assertEquals("response status code was not as expected", HttpStatus.SC_OK, getResponse.getStatusLine().getStatusCode());

			// Verify response content
			JSONObject data = getBodyAsJSONObject(getResponse);
			Assert.assertNotNull("response body was null", data);
			Assert.assertNotEquals("response body was empty", 0, data.size());

			Assert.assertEquals("name was not as expected", newMovieName, data.get("name"));
			Collection<Long> actual = (Collection<Long>) data.get("genreIds");
			Assert.assertTrue("genre ids were not as expected", newMovieGenreIds.equals(actual));
			Assert.assertEquals("rating was not as expected", newMovieRating, data.get("rating"));
			Assert.assertEquals("id was not as expected", newMovieId, data.get("id"));

		} finally {
			deleteMovie(newMovieId);
		}
	}

	@SuppressWarnings("unchecked")
	@Test
	public void createMovieEmptyNameTest() throws ClientProtocolException, IOException {
		// Form the request
		HttpPost postRequest = new HttpPost(movieURL);
		postRequest.addHeader("Content-Type", "application/json");

		JSONObject jsonObj = new JSONObject();
		jsonObj.put("name", "");
		jsonObj.put("genreIds", Arrays.asList(6L));
		jsonObj.put("rating", "PG-13");
		StringEntity entity = new StringEntity(jsonObj.toString(), Consts.UTF_8);
		postRequest.setEntity(entity);

		// Send the request
		HttpResponse postResponse = httpClient.execute(postRequest);

		// Consume the response entity
		EntityUtils.consume(postResponse.getEntity());

		// Verify response status code
		Assert.assertEquals("response status code was not as expected", HttpStatus.SC_BAD_REQUEST, postResponse.getStatusLine().getStatusCode());
	}

	@SuppressWarnings("unchecked")
	@Test
	public void createMovieWhitespaceNameTest() throws ClientProtocolException, IOException {
		// Form the request
		HttpPost postRequest = new HttpPost(movieURL);
		postRequest.addHeader("Content-Type", "application/json");

		JSONObject jsonObj = new JSONObject();
		jsonObj.put("name", "      ");
		jsonObj.put("genreIds", Arrays.asList(6L));
		jsonObj.put("rating", "PG-13");
		StringEntity entity = new StringEntity(jsonObj.toString(), Consts.UTF_8);
		postRequest.setEntity(entity);

		// Send the request
		HttpResponse postResponse = httpClient.execute(postRequest);

		// Consume the response entity
		EntityUtils.consume(postResponse.getEntity());

		// Verify response status code
		Assert.assertEquals("response status code was not as expected", HttpStatus.SC_BAD_REQUEST, postResponse.getStatusLine().getStatusCode());
	}

	@SuppressWarnings("unchecked")
	@Test
	public void createMovieTrimmedNameTest() throws ClientProtocolException, IOException {

		String newMovieName = "      The Italian   Job   ";
		Collection<Long> newMovieGenreIds = Arrays.asList(6L);
		String newMovieRating = "PG-13";
		String newMovieId;

		// Form the request
		HttpPost postRequest = new HttpPost(movieURL);
		postRequest.addHeader("Content-Type", "application/json");

		JSONObject jsonObj = new JSONObject();
		jsonObj.put("name", newMovieName);
		jsonObj.put("genreIds", newMovieGenreIds);
		jsonObj.put("rating", newMovieRating);
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

		// Get the ID
		newMovieId = getIdFromLocationURL(locationURL);

		try {

			// Use the location header to perform a get
			HttpGet getRequest = new HttpGet(locationURL);
			getRequest.addHeader("Accept", "application/json");

			// Send the request
			HttpResponse getResponse = httpClient.execute(getRequest);

			// Verify response status code
			Assert.assertEquals("response status code was not as expected", HttpStatus.SC_OK, getResponse.getStatusLine().getStatusCode());

			// Verify response content
			JSONObject data = getBodyAsJSONObject(getResponse);
			Assert.assertNotNull("response body was null", data);
			Assert.assertNotEquals("response body was empty", 0, data.size());

			Assert.assertEquals("name was not as expected", newMovieName.trim(), data.get("name"));
			Collection<Long> actual = (Collection<Long>) data.get("genreIds");
			Assert.assertTrue("genre ids were not as expected", newMovieGenreIds.equals(actual));
			Assert.assertEquals("rating was not as expected", newMovieRating, data.get("rating"));
			Assert.assertEquals("id was not as expected", newMovieId, data.get("id"));

		} finally {
			deleteMovie(newMovieId);
		}
	}

	@SuppressWarnings("unchecked")
	@Test
	public void createMovieConflictingNameTest() throws ClientProtocolException, IOException {

		// Form the request
		HttpPost postRequest = new HttpPost(movieURL);
		postRequest.addHeader("Content-Type", "application/json");

		JSONObject jsonObj = new JSONObject();
		jsonObj.put("name", alreadyCreatedMovieName);
		jsonObj.put("genreIds", Arrays.asList(2L, 10L));
		jsonObj.put("rating", "R");
		StringEntity entity = new StringEntity(jsonObj.toString(), Consts.UTF_8);
		postRequest.setEntity(entity);

		// Send the request
		HttpResponse postResponse = httpClient.execute(postRequest);

		// Verify response status code
		Assert.assertEquals("response status code was not as expected", HttpStatus.SC_CONFLICT, postResponse.getStatusLine().getStatusCode());

		// Read the response body
		String message = getBodyAsString(postResponse);

		// Verify the conflict ID is at the end of the message
		Assert.assertTrue("conflicting id was expected at the end of the response message", message.endsWith(alreadyCreatedMovieId));
	}
}
