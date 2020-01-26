package com.codedchai;

import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;

import static java.util.stream.Collectors.toMap;

public class Application {

	public static void main( String[] args ) {
		Application application = new Application();

		application.run();
	}

	protected void run() {

		try ( Scanner scanner = new Scanner( System.in ) ) {
			System.out.print( "Enter search term: " );
			String searchTerm = scanner.nextLine();

			System.out.println( "Search Method: 1) String Match 2) Regular Expression 3) Indexed" );

			int searchID = scanner.nextInt();

			Search search = instantiateSearch( searchID );
			search.initialize();

			System.out.println( "Searching for '" + searchTerm + "' using class " + search.getClass().getSimpleName() );

			Long startTime = System.nanoTime();
			Map < String, Integer > rankedResults = search.getRankedSearchResults( searchTerm );
			Long endTime = System.nanoTime();

			outputRankedResultsInOrder( rankedResults );

			Double elapsedTimeInMillis = (double) (endTime - startTime) / 1000000;
			System.out.println( "\nElapsed time: " + elapsedTimeInMillis + " ms" );

		} catch ( Exception e ) {
			e.printStackTrace();
		}
	}

	protected Search instantiateSearch( int searchID ) {
		Map < Integer, Supplier < Search > > searchFactories = new HashMap <>();

		searchFactories.put( 1, SimpleSearch::new );
		searchFactories.put( 2, RegexSearch::new );
		searchFactories.put( 3, IndexedSearch::new );

		Supplier < Search > defaultSearchSupplier = () -> new SimpleSearch();
		Function < Integer, Search > function = key -> searchFactories.getOrDefault( key, defaultSearchSupplier ).get();

		return function.apply( searchID );
	}

	// https://javarevisited.blogspot.com/2017/09/java-8-sorting-hashmap-by-values-in.html
	protected void outputRankedResultsInOrder( Map < String, Integer > rankedResults ) {
		Map < String, Integer > orderedRankedResults = rankedResults.entrySet().stream().sorted( Collections.reverseOrder( Map.Entry.comparingByValue() ) ).collect( toMap( Map.Entry::getKey, Map.Entry::getValue, ( e1, e2 ) -> e2, LinkedHashMap::new ) );

		orderedRankedResults.keySet().stream().forEach( filename -> System.out.println( "\t" + filename + " - " + orderedRankedResults.get( filename ) + " matches" ) );

	}

}
