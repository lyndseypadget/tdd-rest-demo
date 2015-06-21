package com.awesome.api.transformers;

import java.util.Random;

public abstract class BaseTransformer {

	protected static String getRandomId() {
		Random r = new Random();
		int Low = 1000;
		int High = 9999;
		return Integer.toString(r.nextInt(High - Low) + Low);
	}
}
