package com.codedchai;

import java.util.Map;
import java.util.Scanner;
import java.util.Set;

public class StressTestApplication extends Application {

	final int MAX_LOOPS = 2000000;
	
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
			// begin nanotime
			for(int i = 0; i < MAX_LOOPS; i++){
				// Select word/phrase
				// search
			}
			// end nanotime
			// give output

		}
	}

}
