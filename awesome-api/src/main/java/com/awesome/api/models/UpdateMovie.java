package com.awesome.api.models;

import java.util.Collection;

public class UpdateMovie {
	public String id;
	public String name;
	public Collection<Long> genreIds;
	public String rating;
}
