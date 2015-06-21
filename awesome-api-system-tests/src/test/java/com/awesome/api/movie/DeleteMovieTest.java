package com.awesome.api.movie;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class DeleteMovieTest extends MovieBaseTest {

	private String newMovieName = "Reign Over Me";
	private Collection<Long> newMovieGenreIds = Arrays.asList(4L, 12L);
	private String newMovieRating = "R";
	private String newMovieId;

	@Before
	public void setUp() {

		super.setUp();

		newMovieId = createMovie(newMovieName, newMovieGenreIds, newMovieRating);
	}

	@Test
	public void deleteMovieTest() throws ClientProtocolException, IOException {

		// Form the request
		HttpDelete deleteRequest = new HttpDelete(movieURL + "/" + newMovieId);

		// Send the request
		HttpResponse deleteResponse = httpClient.execute(deleteRequest);

		// Consume the response entity
		EntityUtils.consume(deleteResponse.getEntity());

		// Verify response status code
		Assert.assertEquals("response status code was not as expected", HttpStatus.SC_OK, deleteResponse.getStatusLine().getStatusCode());

		// Verify the resource with that ID is gone
		// Form the request
		HttpGet getRequest = new HttpGet(movieURL + "/" + newMovieId);
		getRequest.addHeader("Accept", "application/json");

		// Send the request
		HttpResponse getResponse = httpClient.execute(getRequest);

		// Consume the response entity
		EntityUtils.consume(getResponse.getEntity());

		// Verify response status code
		Assert.assertNotEquals("response status code indicated that the resource was found", HttpStatus.SC_OK, getResponse.getStatusLine().getStatusCode());
	}

	@Test
	public void deleteNonExistingMovieTest() throws ClientProtocolException, IOException {

		// Form the request
		HttpDelete deleteRequest = new HttpDelete(movieURL + "/1"); // Note that
																	// "1" is an
																	// invalid
																	// ID so it
																	// should
																	// not exist

		// Send the request
		HttpResponse deleteResponse = httpClient.execute(deleteRequest);

		// Consume the response entity
		EntityUtils.consume(deleteResponse.getEntity());

		// Verify response status code
		Assert.assertEquals("response status code was not as expected", HttpStatus.SC_OK, deleteResponse.getStatusLine().getStatusCode());

		// Verify the resource with that ID is gone
		// Form the request
		HttpGet getRequest = new HttpGet(movieURL + "/1");
		getRequest.addHeader("Accept", "application/json");

		// Send the request
		HttpResponse getResponse = httpClient.execute(getRequest);

		// Consume the response entity
		EntityUtils.consume(getResponse.getEntity());

		// Verify response status code
		Assert.assertEquals("response status code was not as expected", HttpStatus.SC_NOT_FOUND, getResponse.getStatusLine().getStatusCode());
	}
}
