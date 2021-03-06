package com.codedchai;

import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;

import static java.util.stream.Collectors.toMap;

public class Application {

	public static void main( String[] args ) {
		try {
			Application application = new Application();

			application.run();
		} catch ( Exception e ) {
			e.printStackTrace();

			// Report exception in logging location
		}

	}

	/*
	 * This is our main application code. It will prompt for input and handle the input accordingly.
	 */
	protected void run() throws Exception {

		try ( Scanner scanner = new Scanner( System.in ) ) {
			System.out.print( "Enter search term: " );
			String searchTerm = scanner.nextLine();

			System.out.println( "Search Method: 1) String Match 2) Regular Expression 3) Indexed" );

			int searchID = scanner.nextInt();

			if ( !getSearchFactories().keySet().contains( searchID ) ) {
				System.out.println( "Please run again and enter a valid Search Method: 1) String Match 2) Regular Expression 3) Indexed" );
				return;
			}

			Search search = instantiateSearch( searchID );
			search.initialize();

			System.out.println( "Searching for '" + searchTerm + "' using class " + search.getClass().getSimpleName() );

			Long startTime = System.nanoTime();
			Map < String, Integer > rankedResults = search.getRankedSearchResults( searchTerm );
			Long endTime = System.nanoTime();

			outputRankedResultsInOrder( rankedResults );

			Double elapsedTimeInMillis = (double) (endTime - startTime) / 1000000;
			System.out.println( "\nElapsed time: " + elapsedTimeInMillis + " ms" );

		}
	}

	protected Map < Integer, Supplier < Search > > getSearchFactories() {
		Map < Integer, Supplier < Search > > searchFactories = new HashMap <>();

		searchFactories.put( 1, SimpleSearch::new );
		searchFactories.put( 2, RegexSearch::new );
		searchFactories.put( 3, IndexedSearch::new );

		return searchFactories;
	}

	/*
	 * Instantiate the search class depending on the number that was passed in
	 */
	protected Search instantiateSearch( int searchID ) {
		Map < Integer, Supplier < Search > > searchFactories = getSearchFactories();

		Supplier < Search > defaultSearchSupplier = () -> new SimpleSearch();
		Function < Integer, Search > function = key -> searchFactories.getOrDefault( key, defaultSearchSupplier ).get();

		return function.apply( searchID );
	}

	/*
	 * https://javarevisited.blogspot.com/2017/09/java-8-sorting-hashmap-by-values-in.html for reference on my codes approach. We will sort the ranked results by value and output them as well
	 */
	protected void outputRankedResultsInOrder( Map < String, Integer > rankedResults ) {
		Map < String, Integer > orderedRankedResults = rankedResults.entrySet().stream().sorted( Collections.reverseOrder( Map.Entry.comparingByValue() ) ).collect( toMap( Map.Entry::getKey, Map.Entry::getValue, ( e1, e2 ) -> e2, LinkedHashMap::new ) );

		orderedRankedResults.keySet().stream().forEach( filename -> System.out.println( "\t" + filename + " - " + orderedRankedResults.get( filename ) + " matches" ) );

	}

}
