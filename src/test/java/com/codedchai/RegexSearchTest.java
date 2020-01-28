package com.codedchai;

import org.junit.jupiter.api.Test;

class RegexSearchTest {

	/*
	 * This test is specifically to confirm that we escape characters and that we don't throw an exception
	 */
	@Test
	void getRankedSearchResults() throws Exception {

		Search search = new RegexSearch();

		search.initialize();

		assert (!search.getRankedSearchResults( "(" ).isEmpty());
	}
}