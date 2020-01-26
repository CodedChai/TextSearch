package com.codedchai;

import org.junit.jupiter.api.BeforeEach;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

class IndexedSearchTest {

	IndexedSearch indexedSearch;

	@BeforeEach
	void init() throws Exception {
		indexedSearch = new IndexedSearch();
		indexedSearch.initialize();
	}

	@org.junit.jupiter.api.Test
	void getRankedSearchResults() throws Exception {
		assert (!indexedSearch.getRankedSearchResults( "fast" ).isEmpty());
	}

	@org.junit.jupiter.api.Test
	void generateDocumentWordIndexMap() throws Exception {
		assert (!indexedSearch.generateDocumentWordIndexMap().isEmpty());
	}

	@org.junit.jupiter.api.Test
	void lowerCase() {
		IndexedSearch indexedSearch = new IndexedSearch();
		assertEquals( "lowercase", indexedSearch.lowerCase( "LOWERCASE" ) );
	}

	@org.junit.jupiter.api.Test
	void stripSpecialCharacters() {
		assertEquals( indexedSearch.stripSpecialCharacters( "-()" ), "   " );
	}

	@org.junit.jupiter.api.Test
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

	@org.junit.jupiter.api.Test
	void stem() {
		IndexedSearch indexedSearch = new IndexedSearch();
		String connect = "connect";
		List < String > listToStem = Arrays.asList( "connected", "connects", "connectly", "connecting", "connect" );

		assert (listToStem.stream().allMatch( stemmableWord -> connect.equals( indexedSearch.stem( stemmableWord ).stripTrailing() ) ));
	}
}