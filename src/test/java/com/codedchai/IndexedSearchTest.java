package com.codedchai;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

class IndexedSearchTest {
/*
	IndexedSearch indexedSearch;

	@BeforeEach
	void init() throws Exception {
		indexedSearch = new IndexedSearch();
		indexedSearch.initialize();
	}

	@Test
	void getRankedSearchResults() throws Exception {
		assert (!indexedSearch.getRankedSearchResults( "fast" ).isEmpty());
	}

	@Test
	void generateDocumentWordIndexMap() throws Exception {
		assert (!indexedSearch.getDocumentWordIndexMap().isEmpty());
	}

	@Test
	void lowerCase() {
		IndexedSearch indexedSearch = new IndexedSearch();
		assertEquals( "lowercase", indexedSearch.lowerCase( "LOWERCASE" ) );
	}

	@Test
	void stripSpecialCharacters() {
		assertEquals( indexedSearch.stripSpecialCharacters( "-()!@#" ), "   " );
	}

	@Test
	void stopwordRemoval() {
		IndexedSearch indexedSearch = new IndexedSearch();
		Set < String > stopwords = indexedSearch.initializeStopwords();
		boolean removedAllStopwords = true;
		for ( String stopword : stopwords ) {
			String temp = indexedSearch.stopwordRemoval( "test " + stopword );
			if ( !"test ".equals( temp ) ) {
				System.out.println( "Did not remove stopword: " + temp );
				removedAllStopwords = false;
			}
		}
		assert (removedAllStopwords);
	}

	@Test
	void stem() {
		IndexedSearch indexedSearch = new IndexedSearch();
		String connect = "connect";
		List < String > listToStem = Arrays.asList( "connected", "connects", "connectly", "connecting", "connecter", "connect" );

		assert (listToStem.stream().allMatch( stemmableWord -> connect.equals( indexedSearch.stem( stemmableWord ).stripTrailing() ) ));
	}
	*/

}