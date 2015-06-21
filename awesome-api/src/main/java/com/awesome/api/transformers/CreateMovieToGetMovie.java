package com.awesome.api.transformers;

import com.awesome.api.models.CreateMovie;
import com.awesome.api.models.GetMovie;

public class CreateMovieToGetMovie extends BaseTransformer {
	
	public static GetMovie transform(CreateMovie createMovie) {
		
		GetMovie retVal = new GetMovie();
		retVal.id = getRandomId();
		retVal.name = createMovie.name;
		retVal.genreIds = createMovie.genreIds;
		retVal.rating = createMovie.rating;
		return retVal;
		
	}
}
