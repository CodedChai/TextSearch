package com.codedchai;

import java.util.*;

public class IndexedSearch extends Search {

	protected Map < String, Map < String, Set < Integer > > > documentWordIndexMap = null;

	static final String SPLIT_WHITESPACE = "\\s+";

	Set<String> stopwords = initializeStopwords();

	protected Map<String, List <IndexEntry> > invertedIndex = null;

	@Override
	public Map < String, Integer > getRankedSearchResults( String searchTerm, Map < String, String > documentContentsMap ) throws Exception {
		Map < String, Integer > rankedResults = new HashMap <>();


		for(String searchWord : searchTerm.split( SPLIT_WHITESPACE)){
			searchWord = preProcess( searchWord );

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
		List<String> searchList = Arrays.asList( preProcess( searchTerm).split( SPLIT_WHITESPACE) );
		for ( String documentKey : localDocumentMap.keySet() ) {
			Map<String, Set<Integer>> wordIndexMap = localDocumentMap.get( documentKey );

			if(searchList.isEmpty()){
				continue;
			}

			Set<Integer> currentIndices = wordIndexMap.get( searchList.get( 0 ) );
			if(searchList.size() > 1 && currentIndices != null && !currentIndices.isEmpty()){
				for( int i = 1; i < searchList.size(); i++ ){
					String searchWord = preProcess(searchList.get(i));
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

			rankedResults2.put( documentKey, currentIndices != null ? currentIndices.size() : 0 );
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
				for ( String word : preProcess( documentContentMap.get( documentKey )).split( SPLIT_WHITESPACE) ) {
					Set<Integer> indexSet = wordIndexMap.containsKey( word ) ? wordIndexMap.get( word ) : new HashSet <>(  );

					indexSet.add( wordIndex );
					wordIndexMap.put( word, indexSet );

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
			for ( String word : preProcess(documentContentMap.get( documentKey )).split( SPLIT_WHITESPACE) ) {
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

	// https://www.kdnuggets.com/2019/04/text-preprocessing-nlp-machine-learning.html
	public String preProcess( String text ) {
		text = lowerCase( text );
		text = stripSpecialCharacters(text);
		text = stopwordRemoval( text );

		return text.strip();
	}

	/*
	 * If the character isn't a letter, digit or whitespace replace it with a single space which we may later strip
	 */
	public String stripSpecialCharacters(String text){
		StringBuilder strippedText = new StringBuilder(  );

		List<Character> charactersToBeReplacedBySpace = Arrays.asList( '-', '(', ')' );

		for(Character character : text.toCharArray()){
			if(Character.isLetterOrDigit( character ) || Character.isWhitespace( character )){
				strippedText.append( character );
			} else if(charactersToBeReplacedBySpace.contains( character )){
					strippedText.append( " " );
			}
		}

		return strippedText.toString();
	}


	/*
	 * List of common stop words can be found at https://www.ranks.nl/stopwords
	 */
	protected Set<String> initializeStopwords(){
		Set<String> stopwordsSet = new HashSet <>(  );
		stopwordsSet.addAll( Arrays.asList( "a", "about", "above", "after", "again", "against", "all", "am", "an", "and", "any", "are", "aren't", "as", "at", "be", "because", "been", "before", "being", "below", "between", "both", "but", "by", "can't", "cannot", "could", "couldn't", "did", "didn't", "do", "does", "doesn't", "doing", "don't", "down", "during", "each", "few", "for", "from", "further", "had", "hadn't", "has", "hasn't", "have", "haven't", "having", "he", "he'd", "he'll", "he's", "her", "here", "here's", "hers", "herself", "him", "himself", "his", "how", "how's", "i", "i'd", "i'll", "i'm", "i've", "if", "in", "into", "is", "isn't", "it", "it's", "its", "itself", "let's", "me", "more", "most", "mustn't", "my", "myself", "no", "nor", "not", "of", "off", "on", "once", "only", "or", "other", "ought", "our", "ours	", "ourselves", "out", "over", "own", "same", "shan't", "she", "she'd", "she'll", "she's", "should", "shouldn't", "so", "some", "such", "than", "that", "that's", "the", "their", "theirs", "them", "themselves", "then", "there", "there's", "these", "they", "they'd", "they'll", "they're", "they've", "this", "those", "through", "to", "too", "under", "until", "up", "very", "was", "wasn't", "we", "we'd", "we'll", "we're", "we've", "were", "weren't", "what", "what's", "when", "when's", "where", "where's", "which", "while", "who", "who's", "whom", "why", "why's", "with", "won't", "would", "wouldn't", "you", "you'd", "you'll", "you're", "you've", "your", "yours", "yourself", "yourselves" ) );

		return stopwordsSet;
	}

	/*
	We will only remove stopwords if it won't leave us with an empty string
	 */
	public String stopwordRemoval(String text){
		List<String> words = Arrays.asList( text.split( SPLIT_WHITESPACE ) );
		StringBuilder textWithoutStopwords = new StringBuilder(  );
		for(String word : words){
			if(!stopwords.contains( word )){
				textWithoutStopwords.append( word );
				textWithoutStopwords.append( " " );
			}
		}


		return textWithoutStopwords.toString().isEmpty() ? text : textWithoutStopwords.toString();
	}

	public String lowerCase(String text){
		return text.toLowerCase();
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

		System.out.println( search.preProcess( "faster-than-light" ) );

		search.getRankedSearchResults( "faster than light", search.loadDocuments() );
	}

}
