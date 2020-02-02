package com.codedchai;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

public abstract class Search {

	/*
	 * The cache of the document contents in memory
	 */
	protected Map < String, String > documentContentsMap;

	/*
	 * Return a map of that contains each document and how many matches were found
	 */
	public abstract Map < String, Integer > getRankedSearchResults( String searchTerm ) throws Exception;

	/*
	 * Any setup that needs to be done before running the search function, in this case just loading the documents
	 */
	public void initialize() throws Exception {
		documentContentsMap = loadDocuments();
	}

	/*
	 * Load in all searchable files and store them in a Map where the filename is the key and the contents are the value
	 */
	public Map < String, String > loadDocuments() throws IOException {
		Map < String, String > documentContentsMap = new HashMap <>();

		try ( Stream < Path > paths = Files.walk( Paths.get( getDocumentDirectory() ) ) ) {
			paths.filter( Files::isRegularFile ).forEach( file -> documentContentsMap.put( file.getFileName().toString(), readFile( file.toAbsolutePath() ) ) );
		} catch ( Exception e ) {
			throw new IOException( "Failed to get path " + getDocumentDirectory() );
		}

		return documentContentsMap;
	}

	/*
	 * Read in the full file line by line then remove any erroneous spaces
	 */
	public static String readFile( Path pathToFile ) {
		StringBuilder fileContents = new StringBuilder();

		try ( Stream < String > stream = Files.lines( pathToFile, StandardCharsets.UTF_8 ) ) {
			stream.forEach( line -> fileContents.append( line ) );
		} catch ( Exception e ) {
			e.printStackTrace();
		}

		return StringUtils.preProcess( fileContents.toString().strip() );
	}

	public String getDocumentDirectory() {
		return "resources";
	}

	public Map < String, String > getDocumentContentsMap() {
		return documentContentsMap;
	}

}
