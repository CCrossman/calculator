package com.hbm;

public final class Assertions {

	// import the static methods
	private Assertions() {}

	public static <T> void shouldEqual(T expected, T actual) {
		if (!expected.equals(actual)) {
			throw new IllegalArgumentException("expected '" + expected + "', but found '" + actual + "'");
		}
	}
}
