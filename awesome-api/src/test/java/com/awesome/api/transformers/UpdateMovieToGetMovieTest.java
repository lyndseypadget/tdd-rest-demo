package com.awesome.api.transformers;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Assert;
import org.junit.Test;

import com.awesome.api.models.GetMovie;
import com.awesome.api.models.UpdateMovie;

public class UpdateMovieToGetMovieTest {

	@Test
	public void test() {

		String movieName = "Marie Antoinette";
		Collection<Long> movieGenreIds = Arrays.asList(2L, 7L, 10L);
		String movieRating = "PG-13";
		String movieId = "9876";

		// Make an object to transform
		UpdateMovie movieToUpdate = new UpdateMovie();
		movieToUpdate.name = movieName;
		movieToUpdate.genreIds = movieGenreIds;
		movieToUpdate.rating = movieRating;
		movieToUpdate.id = movieId;

		// Perform transformation
		GetMovie movieToGet = UpdateMovieToGetMovie.transform(movieToUpdate);

		// Verify results
		Assert.assertEquals("name was not as expected", movieName, movieToGet.name);
		Assert.assertEquals("genre ids were not as expected", movieGenreIds, movieToGet.genreIds);
		Assert.assertEquals("rating was not as expected", movieRating, movieToGet.rating);
		Assert.assertEquals("id was not as expected", movieId, movieToGet.id);
	}

}
