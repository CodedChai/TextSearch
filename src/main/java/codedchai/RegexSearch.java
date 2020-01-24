package codedchai;

import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexSearch extends Search {

	// https://stackoverflow.com/questions/7378451/java-regex-match-count
	@Override
	public Map < String, Integer > getRankedSearchResults( String searchTerm, Map < String, String > documentContentsMap ) throws Exception {
		Map<String, Integer> rankedResults = new TreeMap <>(  );

		Pattern pattern = Pattern.compile( searchTerm );

		for(String documentKey : documentContentsMap.keySet()){
			Matcher matcher = pattern.matcher( documentContentsMap.get( documentKey ) );
			int numberOfMatches = 0;
			int matchIndex = 0;
			while(matcher.find(matchIndex)){
				numberOfMatches++;
				matchIndex = matcher.start() + 1;
			}

			rankedResults.put( documentKey, numberOfMatches );
		}

		for(String key : rankedResults.keySet()){
			System.out.println( key + " : " + rankedResults.get( key ) );
		}

		return rankedResults;
	}

	public static void main(String[] args) throws Exception {
		Search search = new RegexSearch();

		search.getRankedSearchResults( "of", search.loadDocuments() );
	}
}
