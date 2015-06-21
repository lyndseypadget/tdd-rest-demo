package com.awesome.api.controllers;

import java.util.Collection;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import com.awesome.api.models.CreateMovie;
import com.awesome.api.models.GetMovie;
import com.awesome.api.models.UpdateMovie;
import com.awesome.api.transformers.CreateMovieToGetMovie;
import com.awesome.api.transformers.UpdateMovieToGetMovie;

@Path("/movie")
public class Movie {

	private static Map<String, GetMovie> movies = new Hashtable<String, GetMovie>();

	@GET
	@Path("/{movieId}")
	@Produces(MediaType.APPLICATION_JSON)
	public GetMovie getMovie(@PathParam("movieId") String movieId) {
		GetMovie movie = movies.get(movieId);
		if (movie == null) {
			throw new NotFoundException(String.format("The movie with id %s could not be found.", movieId));
		}
		return movie;
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<GetMovie> getMovies() {
		return movies.values();
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response createMovie(@Context UriInfo uriInfo, CreateMovie movieToCreate) {
		if (movieToCreate.name != null) {
			movieToCreate.name = movieToCreate.name.trim();
		}
		
		if (movieToCreate.name == null || movieToCreate.name.equals("")) {
			return Response.status(Status.BAD_REQUEST).entity("The name of the movie cannot be empty").build();
		}
		
		String conflictingNameMovieId = getMovieIdWithSameName(movieToCreate.name.trim());
		if (conflictingNameMovieId != null) {
			return Response.status(Status.CONFLICT).entity("The movie with the following id already has the same name: " + conflictingNameMovieId).build();
		}
		
		GetMovie newMovie = CreateMovieToGetMovie.transform(movieToCreate);
		movies.put(newMovie.id, newMovie);
		String location = String.format("%s/%s", uriInfo.getAbsolutePath().toString(), newMovie.id);
		return Response.status(Status.CREATED).header("Location", location).entity(newMovie.id).build();
	}

	@DELETE
	@Path("/{movieId}")
	public Response deleteMovie(@PathParam("movieId") String movieId) {
		movies.remove(movieId);
		return Response.status(Status.OK).build();
	}

	@PUT
	@Path("/{movieId}")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response updateMovie(@PathParam("movieId") String movieId, UpdateMovie updatedMovie) {
		if (updatedMovie.id == null || updatedMovie.id.equals("")) {
			return Response.status(Status.BAD_REQUEST).entity("The id of the movie cannot be empty").build();
		}
		
		if (!movieId.equals(updatedMovie.id)) {
			return Response.status(Status.BAD_REQUEST).entity("The id of the movie cannot be changed").build();
		}
		
		if (updatedMovie.name != null) {
			updatedMovie.name = updatedMovie.name.trim();
		}

		if (updatedMovie.name == null || updatedMovie.name.equals("")) {
			return Response.status(Status.BAD_REQUEST).entity("The name of the movie cannot be empty").build();
		}

		String conflictingNameMovieId = getMovieIdWithSameName(updatedMovie.name);
		if (conflictingNameMovieId != null && !conflictingNameMovieId.equals(movieId)) {
			return Response.status(Status.CONFLICT).entity("The movie with the following id already has the same name: " + conflictingNameMovieId).build();
		}
		
		GetMovie getMovie = UpdateMovieToGetMovie.transform(updatedMovie);
		movies.put(movieId, getMovie);
		return Response.status(Status.OK).build();
	}

	private String getMovieIdWithSameName(String movieName) {
		Iterator<String> it = movies.keySet().iterator();
		while (it.hasNext()) {
			String movieId = it.next();
			GetMovie movie = movies.get(movieId);
			if (movie.name.equalsIgnoreCase(movieName)) {
				return movieId;
			}
		}

		return null;
	}
}
