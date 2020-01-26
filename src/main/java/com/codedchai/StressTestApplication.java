package com.codedchai;

import java.util.*;

public class StressTestApplication extends Application {

	final int MAX_LOOPS = 2000000;

	final int MAX_WORDS_TO_SEARCH = 10;

	List <String> wordsInDocuments;
	
	public static void main( String[] args ) {
		try{
			Application application = new StressTestApplication();

			application.run();
		} catch ( Exception e ){
			e.printStackTrace();

			// Report exception in logging location
		}
	}

	@Override
	protected void run() throws Exception {

		Set <Integer> searchIDs = getSearchFactories().keySet();

		for(Integer searchID : searchIDs) {
			Search search = instantiateSearch( searchID );

			search.initialize();
			initializeDocumentWords( search );

			System.out.println( "Starting search using " + search.getClass().getSimpleName() + " for " + MAX_LOOPS + " iterations." );

			Long elapsedTime = 0L;
			for(int searchIteration = 0; searchIteration < MAX_LOOPS; searchIteration++){
				String searchTerm = generateSearchTerm( searchIteration );
				Long startTime = System.nanoTime();
				search.getRankedSearchResults( searchTerm );
				Long endTime = System.nanoTime();
				elapsedTime += (endTime - startTime);
			}
			double elapsedTimeInSeconds = (double)elapsedTime / 1000000000.0;
			System.out.println( "Elapsed time: " + elapsedTimeInSeconds + " seconds for " + search.getClass().getSimpleName() + " over " + MAX_LOOPS + " iterations." );
		}
	}

	protected void initializeDocumentWords(Search search){
		StringBuilder documentContentText = new StringBuilder(  );
		Map<String, String> documentContents = search.getDocumentContentsMap();
		for(String key : documentContents.keySet()){
			documentContentText.append( documentContents.get( key ) );
			documentContentText.append( " " );
		}

		wordsInDocuments = Arrays.asList(documentContentText.toString().strip().split( " " ));
	}

	protected String generateSearchTerm(int searchIteration){

		StringBuilder searchTerm = new StringBuilder(  );

		int numWordsToUse = (searchIteration % MAX_WORDS_TO_SEARCH) + 1;

		int searchIndex = searchIteration % wordsInDocuments.size();

		for(int wordIndex = 0; wordIndex < numWordsToUse && wordIndex + searchIndex < wordsInDocuments.size(); wordIndex++ ){
			searchTerm.append( wordsInDocuments.get(wordIndex + searchIndex) );
			searchTerm.append( " " );
		}

		return searchTerm.toString().stripTrailing();
	}

}
