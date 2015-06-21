package com.awesome.api.transformers;

import com.awesome.api.models.GetMovie;
import com.awesome.api.models.UpdateMovie;

public class UpdateMovieToGetMovie extends BaseTransformer {
	
	public static GetMovie transform(UpdateMovie updateMovie) {
		
		GetMovie retVal = new GetMovie();
		retVal.id = updateMovie.id;
		retVal.name = updateMovie.name;
		retVal.genreIds = updateMovie.genreIds;
		retVal.rating = updateMovie.rating;
		return retVal;
		
	}
}
