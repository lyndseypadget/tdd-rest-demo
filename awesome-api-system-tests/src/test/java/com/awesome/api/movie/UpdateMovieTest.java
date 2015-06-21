package com.awesome.api.movie;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;

import org.apache.http.Consts;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;
import org.json.simple.JSONObject;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class UpdateMovieTest extends MovieBaseTest {

	private String newMovie1Name = "Cold Mountain";
	private Collection<Long> newMovie1GenreIds = Arrays.asList(8L, 13L);
	private String newMovie1Rating = "R";
	private String newMovie1Id;

	private String newMovie2Name = "Inception";
	private Collection<Long> newMovie2GenreIds = Arrays.asList(2L);
	private String newMovie2Rating = "PG-13";
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

	@SuppressWarnings("unchecked")
	@Test
	public void updateMovieAllFieldsChangedTest() throws ClientProtocolException, IOException {

		String updatedMovieName = "Moulin Rouge";
		Collection<Long> updatedMovieGenreIds = Arrays.asList(10L);
		String updatedMovieRating = "PG-13";

		// Form the request
		HttpPut putRequest = new HttpPut(movieURL + "/" + newMovie1Id);
		putRequest.addHeader("Content-type", "application/json");

		JSONObject jsonObj = new JSONObject();
		jsonObj.put("id", newMovie1Id);
		jsonObj.put("name", updatedMovieName);
		jsonObj.put("genreIds", updatedMovieGenreIds);
		jsonObj.put("rating", updatedMovieRating);
		StringEntity entity = new StringEntity(jsonObj.toString(), Consts.UTF_8);
		putRequest.setEntity(entity);

		// Send the request
		HttpResponse putResponse = httpClient.execute(putRequest);

		// Consume the response entity
		EntityUtils.consume(putResponse.getEntity());

		// Verify response status code
		Assert.assertEquals("response status code was not as expected", HttpStatus.SC_OK, putResponse.getStatusLine().getStatusCode());

		// Verify the data was modified
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

		Assert.assertEquals("name was not as expected", updatedMovieName, data.get("name"));
		Assert.assertEquals("genre ids were not as expected", updatedMovieGenreIds, data.get("genreIds"));
		Assert.assertEquals("rating was not as expected", updatedMovieRating, data.get("rating"));
		Assert.assertEquals("id was not as expected", newMovie1Id, data.get("id"));
	}

	@SuppressWarnings("unchecked")
	@Test
	public void updateMovieAllFieldsUnchangedTest() throws ClientProtocolException, IOException {

		// Form the request
		HttpPut putRequest = new HttpPut(movieURL + "/" + newMovie1Id);
		putRequest.addHeader("Content-type", "application/json");

		JSONObject jsonObj = new JSONObject();
		jsonObj.put("id", newMovie1Id);
		jsonObj.put("name", newMovie1Name);
		jsonObj.put("genreIds", newMovie1GenreIds);
		jsonObj.put("rating", newMovie1Rating);
		StringEntity entity = new StringEntity(jsonObj.toString(), Consts.UTF_8);
		putRequest.setEntity(entity);

		// Send the request
		HttpResponse putResponse = httpClient.execute(putRequest);

		// Consume the response entity
		EntityUtils.consume(putResponse.getEntity());

		// Verify response status code
		Assert.assertEquals("response status code was not as expected", HttpStatus.SC_OK, putResponse.getStatusLine().getStatusCode());

		// Verify the data was [not] modified
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

	@SuppressWarnings("unchecked")
	@Test
	public void updateMovieEmptyNameTest() throws ClientProtocolException, IOException {

		String updatedMovieName = "";
		Collection<Long> updatedMovieGenreIds = Arrays.asList(9L);
		String updatedMovieRating = "PG-13";

		// Form the request
		HttpPut putRequest = new HttpPut(movieURL + "/" + newMovie1Id);
		putRequest.addHeader("Content-type", "application/json");

		JSONObject jsonObj = new JSONObject();
		jsonObj.put("id", newMovie1Id);
		jsonObj.put("name", updatedMovieName);
		jsonObj.put("genreIds", updatedMovieGenreIds);
		jsonObj.put("rating", updatedMovieRating);
		StringEntity entity = new StringEntity(jsonObj.toString(), Consts.UTF_8);
		putRequest.setEntity(entity);

		// Send the request
		HttpResponse putResponse = httpClient.execute(putRequest);

		// Consume the response entity
		EntityUtils.consume(putResponse.getEntity());

		// Verify response status code
		Assert.assertEquals("response status code was not as expected", HttpStatus.SC_BAD_REQUEST, putResponse.getStatusLine().getStatusCode());
	}

	@SuppressWarnings("unchecked")
	@Test
	public void updateMovieWhitespaceNameTest() throws ClientProtocolException, IOException {
		// Form the request
		HttpPut putRequest = new HttpPut(movieURL + "/" + newMovie1Id);
		putRequest.addHeader("Content-type", "application/json");

		JSONObject jsonObj = new JSONObject();
		jsonObj.put("id", newMovie1Id);
		jsonObj.put("name", "      ");
		jsonObj.put("genreIds", newMovie1GenreIds);
		jsonObj.put("rating", newMovie1Rating);
		StringEntity entity = new StringEntity(jsonObj.toString(), Consts.UTF_8);
		putRequest.setEntity(entity);

		// Send the request
		HttpResponse putResponse = httpClient.execute(putRequest);

		// Consume the response entity
		EntityUtils.consume(putResponse.getEntity());

		// Verify response status code
		Assert.assertEquals("response status code was not as expected", HttpStatus.SC_BAD_REQUEST, putResponse.getStatusLine().getStatusCode());
	}

	@SuppressWarnings("unchecked")
	@Test
	public void updateMovieTrimmedNameTest() throws ClientProtocolException, IOException {

		// Form the request
		HttpPut putRequest = new HttpPut(movieURL + "/" + newMovie1Id);
		putRequest.addHeader("Content-Type", "application/json");

		JSONObject jsonObj = new JSONObject();
		jsonObj.put("id", newMovie1Id);
		jsonObj.put("name", "    " + newMovie1Name + "        ");
		jsonObj.put("genreIds", newMovie1GenreIds);
		jsonObj.put("rating", newMovie1Rating);
		StringEntity entity = new StringEntity(jsonObj.toString(), Consts.UTF_8);
		putRequest.setEntity(entity);

		// Send the request
		HttpResponse putResponse = httpClient.execute(putRequest);

		// Consume the response entity
		EntityUtils.consume(putResponse.getEntity());

		// Verify response status code
		Assert.assertEquals("response status code was not as expected", HttpStatus.SC_OK, putResponse.getStatusLine().getStatusCode());

		// Verify the data was [not] modified
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

	@SuppressWarnings("unchecked")
	@Test
	public void updateMovieEmptyIdTest() throws ClientProtocolException, IOException {
		String updatedMovieName = "Fight Club";
		Collection<Long> updatedMovieGenreIds = Arrays.asList(11L);
		String updatedMovieRating = "R";

		// Form the request
		HttpPut putRequest = new HttpPut(movieURL + "/" + newMovie1Id);
		putRequest.addHeader("Content-type", "application/json");

		JSONObject jsonObj = new JSONObject();
		jsonObj.put("id", "");
		jsonObj.put("name", updatedMovieName);
		jsonObj.put("genreIds", updatedMovieGenreIds);
		jsonObj.put("rating", updatedMovieRating);
		StringEntity entity = new StringEntity(jsonObj.toString(), Consts.UTF_8);
		putRequest.setEntity(entity);

		// Send the request
		HttpResponse putResponse = httpClient.execute(putRequest);

		// Consume the response entity
		EntityUtils.consume(putResponse.getEntity());

		// Verify response status code
		Assert.assertEquals("response status code was not as expected", HttpStatus.SC_BAD_REQUEST, putResponse.getStatusLine().getStatusCode());
	}

	@SuppressWarnings("unchecked")
	@Test
	public void updateMovieNullIdTest() throws ClientProtocolException, IOException {
		String updatedMovieName = "Fight Club";
		Collection<Long> updatedMovieGenreIds = Arrays.asList(9L);
		String updatedMovieRating = "R";

		// Form the request
		HttpPut putRequest = new HttpPut(movieURL + "/" + newMovie1Id);
		putRequest.addHeader("Content-type", "application/json");

		JSONObject jsonObj = new JSONObject();
		jsonObj.put("name", updatedMovieName);
		jsonObj.put("genreIds", updatedMovieGenreIds);
		jsonObj.put("rating", updatedMovieRating);
		StringEntity entity = new StringEntity(jsonObj.toString(), Consts.UTF_8);
		putRequest.setEntity(entity);

		// Send the request
		HttpResponse putResponse = httpClient.execute(putRequest);

		// Consume the response entity
		EntityUtils.consume(putResponse.getEntity());

		// Verify response status code
		Assert.assertEquals("response status code was not as expected", HttpStatus.SC_BAD_REQUEST, putResponse.getStatusLine().getStatusCode());
	}

	@SuppressWarnings("unchecked")
	@Test
	public void updateMovieChangedIdTest() throws ClientProtocolException, IOException {

		// Form the request
		HttpPut putRequest = new HttpPut(movieURL + "/" + newMovie1Id);
		putRequest.addHeader("Content-type", "application/json");

		JSONObject jsonObj = new JSONObject();

		// Note that "1" is not a "valid" ID, but we want to ensure it won't
		// conflict with one that might already exist
		jsonObj.put("id", "1");

		jsonObj.put("name", "Legends of the Fall");
		jsonObj.put("genreIds", Arrays.asList(3L, 9L));
		jsonObj.put("rating", "R");
		StringEntity entity = new StringEntity(jsonObj.toString(), Consts.UTF_8);
		putRequest.setEntity(entity);

		// Send the request
		HttpResponse putResponse = httpClient.execute(putRequest);

		// Consume the response entity
		EntityUtils.consume(putResponse.getEntity());

		// Verify response status code
		Assert.assertEquals("response status code was not as expected", HttpStatus.SC_BAD_REQUEST, putResponse.getStatusLine().getStatusCode());
	}

	@SuppressWarnings("unchecked")
	@Test
	public void updateMovieConflictingNameTest() throws ClientProtocolException, IOException {

		// Form the request
		HttpPut putRequest = new HttpPut(movieURL + "/" + newMovie1Id);
		putRequest.addHeader("Content-type", "application/json");

		JSONObject jsonObj = new JSONObject();
		jsonObj.put("id", newMovie1Id);
		jsonObj.put("name", newMovie2Name);
		jsonObj.put("genreIds", Arrays.asList(10L, 2L));
		jsonObj.put("rating", "G");
		StringEntity entity = new StringEntity(jsonObj.toString(), Consts.UTF_8);
		putRequest.setEntity(entity);

		// Send the request
		HttpResponse putResponse = httpClient.execute(putRequest);

		// Verify response status code
		Assert.assertEquals("response status code was not as expected", HttpStatus.SC_CONFLICT, putResponse.getStatusLine().getStatusCode());

		// Read the response body
		String message = getBodyAsString(putResponse);

		// Verify the conflict ID is at the end of the message
		Assert.assertTrue("conflicting id was expected at the end of the response message", message.endsWith(newMovie2Id));
	}
}
