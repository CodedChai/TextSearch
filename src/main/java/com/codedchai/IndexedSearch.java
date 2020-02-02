package com.codedchai;

import java.util.*;

public class IndexedSearch extends Search {

	protected Map < String, Map < String, Set < Integer > > > documentWordIndexMap;


	/*
	 * We will need to create our index after loading in our text and preprocess the text
	 */
	@Override
	public void initialize() throws Exception {
		super.initialize();
		getDocumentWordIndexMap();
	}

	/*
	 * We will search the index word by word making sure that the nth word's index is next to the nth + 1 word's index
	 */
	@Override
	public Map < String, Integer > getRankedSearchResults( String searchTerm ) throws Exception {
		Map < String, Integer > rankedResults = new HashMap <>();

		List < String > searchList = Arrays.asList( StringUtils.preProcess( searchTerm ).split( StringUtils.SPLIT_WHITESPACE ) );
		for ( String documentKey : getDocumentWordIndexMap().keySet() ) {
			Map < String, Set < Integer > > wordIndexMap = getDocumentWordIndexMap().get( documentKey );

			if ( searchList.isEmpty() ) {
				continue;
			}

			Set < Integer > currentIndices = wordIndexMap.get( searchList.get( 0 ) );
			if ( searchList.size() > 1 && currentIndices != null && !currentIndices.isEmpty() ) {
				for ( int i = 1; i < searchList.size(); i++ ) {
					String searchWord = searchList.get( i );
					Set < Integer > nextIndices = wordIndexMap.get( searchWord );
					Set < Integer > tempValidIndices = new HashSet <>();

					for ( Integer currentValidIndex : currentIndices ) {
						if ( nextIndices != null && nextIndices.contains( currentValidIndex + 1 ) ) {
							tempValidIndices.add( currentValidIndex + 1 );
						}
					}
					currentIndices = tempValidIndices;
				}
			}

			rankedResults.put( documentKey, currentIndices != null ? currentIndices.size() : 0 );
		}

		return rankedResults;
	}

	/*
	 * Only generate our index map if it doesn't already exist. Otherwise we will return what we have previously generated. This isn't ideal
	 * if documents could change while the program is running and this needs to recache the new information.
	 *
	 * We will store a map for each document that contains the unique word as a key and that words indices in the text as a value
	 */
	public Map < String, Map < String, Set < Integer > > > getDocumentWordIndexMap() throws Exception {
		if ( documentWordIndexMap == null ) {
			documentWordIndexMap = new HashMap <>();

			Map < String, String > documentContentMap = loadDocuments();

			for ( String documentKey : documentContentMap.keySet() ) {
				Map < String, Set < Integer > > wordIndexMap = new HashMap <>();

				int wordIndex = 0;
				for ( String word : documentContentMap.get( documentKey ).split( StringUtils.SPLIT_WHITESPACE ) ) {
					Set < Integer > indexSet = wordIndexMap.containsKey( word ) ? wordIndexMap.get( word ) : new HashSet <>();

					indexSet.add( wordIndex );
					wordIndexMap.put( word, indexSet );

					wordIndex++;
				}

				documentWordIndexMap.put( documentKey, wordIndexMap );
			}
		}

		return documentWordIndexMap;
	}



}
