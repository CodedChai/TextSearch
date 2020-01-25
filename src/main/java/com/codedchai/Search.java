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
	 * We will want to return an ordered list of our top results that we searched.
	 */
	public abstract Map < String, Integer > getRankedSearchResults( String searchTerm, Map < String, String > documentContentsMap ) throws Exception;

	/*
	 * Load in all searchable files and store them in a Map
	 */
	public Map < String, String > loadDocuments() throws IOException {
		Map < String, String > documentContentsMap = new HashMap <>();

		try ( Stream < Path > paths = Files.walk( Paths.get( getDocumentDirectory() ) ) ) {
			paths.filter( Files::isRegularFile ).forEach( file -> documentContentsMap.put( file.getFileName().toString(), readFile( file.toAbsolutePath() ) ) );
		} catch ( Exception e ) {
			throw new IOException( "Failed to get path " + getDocumentDirectory() );
		}

		documentContentsMap.keySet().stream().forEach( key -> System.out.println( key + " : " + documentContentsMap.get( key ) ) );

		return documentContentsMap;
	}

	public static String readFile( Path pathToFile ) {
		StringBuilder fileContents = new StringBuilder();

		try ( Stream < String > stream = Files.lines( pathToFile, StandardCharsets.UTF_8 ) ) {
			stream.forEach( line -> fileContents.append( line ) );
		} catch ( IOException e ) {
			e.printStackTrace();
		}

		return fileContents.toString();
	}

	public String getDocumentDirectory() {
		return "resources";
	}

}
