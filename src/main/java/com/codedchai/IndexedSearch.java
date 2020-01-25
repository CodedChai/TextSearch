package com.codedchai;

import java.util.*;

public class IndexedSearch extends Search {

	protected Map < String, Map < String, Set < Integer > > > documentWordIndexMap = null;

	protected Map<String, List <IndexEntry> > invertedIndex = null;

	@Override
	public Map < String, Integer > getRankedSearchResults( String searchTerm, Map < String, String > documentContentsMap ) throws Exception {
		Map < String, Integer > rankedResults = new HashMap <>();


		for(String searchWord : searchTerm.split( " " )){
			searchWord = clean( searchWord );

			List<IndexEntry> wordEntries = invertedIndex.get( searchWord );

			if(wordEntries == null){
				continue;
			}
			for(IndexEntry entry : wordEntries){
				if(!rankedResults.containsKey( entry.documentName )){
					rankedResults.put(entry.documentName, 0);
				}
				rankedResults.put( entry.documentName, rankedResults.get(entry.documentName) + 1 );
			}


		}


		for ( String key : rankedResults.keySet() ) {
			System.out.println( key + " : " + rankedResults.get( key ) );
		}

		Map < String, Integer > rankedResults2 = new TreeMap <>();

		Map < String, Map < String, Set < Integer > > > localDocumentMap = getDocumentWordIndexMap();
		List<String> searchList = Arrays.asList( searchTerm.split( " " ) );
		for ( String documentKey : localDocumentMap.keySet() ) {
			Map<String, Set<Integer>> wordIndexMap = localDocumentMap.get( documentKey );

			if(searchList.isEmpty()){
				continue;
			}

			Set<Integer> currentIndices = wordIndexMap.get( clean(searchList.get(0)) );
			if(searchList.size() > 1){
				for( int i = 1; i < searchList.size(); i++ ){
					String searchWord = clean(searchList.get(i));
					Set<Integer> nextIndices = wordIndexMap.get( searchWord );
					Set<Integer> tempValidIndices = new HashSet <>(  );

					for(Integer currentValidIndex : currentIndices){
						if(nextIndices.contains( currentValidIndex + 1 )){
							tempValidIndices.add( currentValidIndex + 1 );
						}
					}
					currentIndices = tempValidIndices;
				}
			}

			rankedResults2.put( documentKey, currentIndices.size() );
		}
		for ( String key : rankedResults2.keySet() ) {
			System.out.println( key + " : " + rankedResults2.get( key ) );
		}
			return rankedResults;
	}

	public Map < String, Map < String, Set < Integer > > > getDocumentWordIndexMap() throws Exception {
		if ( documentWordIndexMap == null ) {
			documentWordIndexMap = new HashMap <>();

			Map < String, String > documentContentMap = loadDocuments();

			for ( String documentKey : documentContentMap.keySet() ) {
				Map<String, Set<Integer>> wordIndexMap = new HashMap <>(  );

				int wordIndex = 0;
				for ( String word : documentContentMap.get( documentKey ).split( " " ) ) {
					Set<Integer> indexSet = wordIndexMap.containsKey( clean(word) ) ? wordIndexMap.get( clean(word) ) : new HashSet <>(  );

					indexSet.add( wordIndex );
					wordIndexMap.put( clean(word), indexSet );

					wordIndex++;
				}

				documentWordIndexMap.put( documentKey, wordIndexMap );
			}
		}

		return documentWordIndexMap;
	}

	public void indexDocuments() throws Exception {
		Map < String, String > documentContentMap = loadDocuments();
		invertedIndex = new HashMap <>(  );

		for ( String documentKey : documentContentMap.keySet() ) {
			int wordIndex = 0;
			for ( String word : documentContentMap.get( documentKey ).split( " " ) ) {
				word = clean( word );
				List<IndexEntry> indexEntries;
				if(invertedIndex.containsKey( word )){
					indexEntries = invertedIndex.get( word );
				} else {
					indexEntries = new ArrayList <>(  );
				}
				indexEntries.add( new IndexEntry( documentKey, wordIndex ) );
				invertedIndex.put( word, indexEntries);
				wordIndex++;
			}
		}
	}

	public String clean( String textToClean ) {
		return textToClean.toLowerCase();
	}

	private class IndexEntry {
		private String documentName;
		private int wordIndex;

		public IndexEntry(String documentName, int wordIndex){
			this.documentName = documentName;
			this.wordIndex = wordIndex;
		}
	}

	public static void main( String[] args ) throws Exception {
		IndexedSearch search = new IndexedSearch();

		search.indexDocuments();

		search.getRankedSearchResults( "by the", search.loadDocuments() );
	}

}
