package codedchai;

import java.util.Map;
import java.util.TreeMap;

public class SimpleSearch extends Search {

	/*
	 * A very simple way of searching text, we will simply be looking at each exact character match that we can make and then saving that
	 * TODO: this could have issues with matches though, say it's arachnid trying to match on ararachnid, we'll miss this match so I will to correct that.
	 */
	@Override
	public Map < String, Integer > getRankedSearchResults( String searchTerm, Map <String, String> documentContentsMap) throws Exception {
		Map<String, Integer> rankedResults = new TreeMap <>(  );
		for(String documentKey : documentContentsMap.keySet()){
			String documentString = documentContentsMap.get( documentKey );
			int numberOfMatches = 0;
			for(int documentStringIndex = 0; documentStringIndex < documentString.length(); documentStringIndex++){
				int searchTermIndex = 0;
				while(documentStringIndex < documentString.length() && searchTerm.charAt( searchTermIndex ) == documentString.charAt( documentStringIndex )){
					searchTermIndex++;
					documentStringIndex++;

					if(searchTermIndex == searchTerm.length()){
						numberOfMatches++;
						break;
					}
				}
			}

			rankedResults.put( documentKey, numberOfMatches );
		}

		for(String key : rankedResults.keySet()){
			System.out.println( key + " : " + rankedResults.get( key ) );
		}

		return rankedResults;
	}

	public static void main(String[] args) throws Exception {
		Search search = new SimpleSearch();

		search.getRankedSearchResults( "of", search.loadDocuments() );
	}
}
