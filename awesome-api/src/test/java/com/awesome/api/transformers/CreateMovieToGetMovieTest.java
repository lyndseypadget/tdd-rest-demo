package com.awesome.api.transformers;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Assert;
import org.junit.Test;

import com.awesome.api.models.CreateMovie;
import com.awesome.api.models.GetMovie;

public class CreateMovieToGetMovieTest {

	@Test
	public void test() {

		String movieName = "XMen";
		Collection<Long> movieGenreIds = Arrays.asList(9L, 3L);
		String movieRating = "PG-13";

		// Make an object to transform
		CreateMovie movieToCreate = new CreateMovie();
		movieToCreate.name = movieName;
		movieToCreate.genreIds = movieGenreIds;
		movieToCreate.rating = movieRating;

		// Perform transformation
		GetMovie movieToGet = CreateMovieToGetMovie.transform(movieToCreate);

		// Verify results
		Assert.assertEquals("name was not as expected", movieName, movieToGet.name);
		Assert.assertEquals("genre ids were not as expected", movieGenreIds, movieToGet.genreIds);
		Assert.assertEquals("rating was not as expected", movieRating, movieToGet.rating);
		Assert.assertNotNull("id was null", movieToGet.id);

		// This part is optional - might go "too far" and make the test brittle
		int idAsInt = Integer.parseInt(movieToGet.id);
		String msg = String.format("id %d was outside the expected range", idAsInt);
		Assert.assertTrue(msg, idAsInt >= 1000 && idAsInt < 10000);
	}
}
