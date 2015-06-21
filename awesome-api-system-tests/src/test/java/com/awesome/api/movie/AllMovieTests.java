package com.awesome.api.movie;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({
	   CreateMovieTest.class,
	   GetMovieTest.class,
	   UpdateMovieTest.class,
	   DeleteMovieTest.class
	})
public class AllMovieTests {

}
