package com.codedchai;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class StringUtils {

	public static final String SPLIT_WHITESPACE = "\\s+";
	public static List < String > suffixes = Arrays.asList( "ed", "ing", "ly", "s", "er" );
	public static Set < String > stopwords = initializeStopwords();

	/*
	 * https://www.kdnuggets.com/2019/04/text-preprocessing-nlp-machine-learning.html to read more about preprocessing text.
	 *
	 * We will only be lowercasing, stripping special characters, removing stopwords and stemming the suffixes of the text.
	 */
	public static String preProcess( String text ) {
		text = lowerCase( text );
		text = stripSpecialCharacters( text );
		text = stopwordRemoval( text );
		text = stem( text );
		return text.strip();
	}

	public static String lowerCase( String text ) {
		return text.toLowerCase();
	}

	/*
	 * If the character isn't a letter, digit or whitespace replace it with a single space which we may later strip
	 */
	public static String stripSpecialCharacters( String text ) {
		StringBuilder strippedText = new StringBuilder();

		List < Character > charactersToBeReplacedBySpace = Arrays.asList( '-', '(', ')' );

		for ( Character character : text.toCharArray() ) {
			if ( Character.isLetterOrDigit( character ) || Character.isWhitespace( character ) ) {
				strippedText.append( character );
			} else if ( charactersToBeReplacedBySpace.contains( character ) ) {
				strippedText.append( " " );
			}
		}

		return strippedText.toString();
	}

	/*
	 * We will only remove stopwords if it won't leave us with an empty string
	 */
	public static String stopwordRemoval( String text ) {
		List < String > words = Arrays.asList( text.split( SPLIT_WHITESPACE ) );
		StringBuilder textWithoutStopwords = new StringBuilder();
		for ( String word : words ) {
			if ( !stopwords.contains( word ) ) {
				textWithoutStopwords.append( word );
				textWithoutStopwords.append( " " );
			}
		}

		return textWithoutStopwords.toString().isEmpty() ? text : textWithoutStopwords.toString();
	}

	/*
	 * List of common stop words can be found at https://www.ranks.nl/stopwords
	 */
	protected static Set < String > initializeStopwords() {
		Set < String > stopwordsSet = new HashSet <>();
		stopwordsSet.addAll( Arrays.asList( "a", "about", "above", "after", "again", "against", "all", "am", "an", "and", "any", "are", "aren't", "as", "at", "be", "because", "been", "before", "being", "below", "between", "both", "but", "by", "can't", "cannot", "could", "couldn't", "did", "didn't", "do", "does", "doesn't", "doing", "don't", "down", "during", "each", "few", "for", "from", "further", "had", "hadn't", "has", "hasn't", "have", "haven't", "having", "he", "he'd", "he'll", "he's", "her", "here", "here's", "hers", "herself", "him", "himself", "his", "how", "how's", "i", "i'd", "i'll", "i'm", "i've", "if", "in", "into", "is", "isn't", "it", "it's", "its", "itself", "let's", "me", "more", "most", "mustn't", "my", "myself", "no", "nor", "not", "of", "off", "on", "once", "only", "or", "other", "ought", "our", "ours", "ourselves", "out", "over", "own", "same", "shan't", "she", "she'd", "she'll", "she's", "should", "shouldn't", "so", "some", "such", "than", "that", "that's", "the", "their", "theirs", "them", "themselves", "then", "there", "there's", "these", "they", "they'd", "they'll", "they're", "they've", "this", "those", "through", "to", "too", "under", "until", "up", "very", "was", "wasn't", "we", "we'd", "we'll", "we're", "we've", "were", "weren't", "what", "what's", "when", "when's", "where", "where's", "which", "while", "who", "who's", "whom", "why", "why's", "with", "won't", "would", "wouldn't", "you", "you'd", "you'll", "you're", "you've", "your", "yours", "yourself", "yourselves" ) );

		return stopwordsSet;
	}

	/*
	 * A basic stemmer that removes suffixes
	 */
	public static String stem( String text ) {
		StringBuilder stringBuilder = new StringBuilder();

		for ( String word : text.split( SPLIT_WHITESPACE ) ) {

			String stemmableSuffix = suffixes.stream().filter( suffix -> word.endsWith( suffix ) && !word.equals( suffix ) ).findFirst().orElse( "" );

			if ( !stemmableSuffix.isBlank() ) {
				stringBuilder.append( word, 0, word.length() - stemmableSuffix.length() );
			} else {
				stringBuilder.append( word );
			}
			stringBuilder.append( " " );

		}
		return stringBuilder.toString();
	}

}
