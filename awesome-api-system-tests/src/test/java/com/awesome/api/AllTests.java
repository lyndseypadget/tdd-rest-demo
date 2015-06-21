package com.awesome.api;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import com.awesome.api.movie.AllMovieTests;

@RunWith(Suite.class)
@Suite.SuiteClasses({
	   AllMovieTests.class
	})
public class AllTests {

}
