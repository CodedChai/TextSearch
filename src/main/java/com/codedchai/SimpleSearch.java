package com.codedchai;

import java.util.HashMap;
import java.util.Map;

public class SimpleSearch extends Search {

	/*
	 * A very simple way of searching text, we will simply be looking at each exact character match that we can make and then saving that, the complexity of this is O(M*N)
	 */
	@Override
	public Map < String, Integer > getRankedSearchResults( String searchTerm, Map < String, String > documentContentsMap ) throws Exception {
		Map < String, Integer > rankedResults = new HashMap <>();
		for ( String documentKey : documentContentsMap.keySet() ) {
			String documentString = documentContentsMap.get( documentKey );
			int numberOfMatches = 0;
			for ( int documentStringIndex = 0; documentStringIndex < documentString.length(); documentStringIndex++ ) {
				int searchTermIndex = 0;
				/* Instead of incrementing documentStringIndex we will just be adding searchTermIndex to it to prevent issues like not being able to match on ararachnid to arachnid.  */
				while ( documentStringIndex + searchTermIndex < documentString.length() && searchTerm.charAt( searchTermIndex ) == documentString.charAt( documentStringIndex + searchTermIndex ) ) {
					searchTermIndex++;

					if ( searchTermIndex == searchTerm.length() ) {
						numberOfMatches++;
						break;
					}
				}
			}

			rankedResults.put( documentKey, numberOfMatches );
		}

		return rankedResults;
	}
}
