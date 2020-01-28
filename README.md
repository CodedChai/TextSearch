# Comparing Text Searching Algorithms

Testing different algorithms to search text documents and return results in order based on the frequency of the search term.

# The Three Different Algorithms

## Simple Search

This is a naive brute force approach to searching the text. Essentially we just go character by character to make each comparison. This is extremely slow as we will have to iterate over the entire text and the search phrase. 

## Regex Search

Essentially I just took the search phrase, compiled it into regex and then ran a regex matcher. I ran into two issues with this. The first is that if the search phrase had a '(' character without a ')' character then the regex would fail to compile. I fixed this by wrapping the search term with Pattern.quote() to escape special characters. The second issue I cam across was mostly a conceptual one. It was where the regex matching will pick up again after a successful match as opposed to starting one character after the successful match. To prevent missing any matches I manually set the match index after any successful match. You can read more about this specific problem [here.](https://stackoverflow.com/questions/7378451/java-regex-match-count)

## Indexed Search

This is where the bulk of my work was. It involves three main parts: text preprocessing, indexing the documents, and searching the indexed text.

### Text Preprocessing

Text preprocessing is ran on all of the contents of the documents as well as the search term to help normalize the data. With more normalized data we can theoretically have a higher success rate for matches helping increase the relevancy of our results.

The text pre-processing phase consists of a four separate parts. The fist part is lowercasing, I make the entire text lowercase so searches aren't case sensitive. Second I strip special characters, one example would be faster-than-light would then become faster than light. This is so we have a bit more flexibility for search terms. Next we remove stopwords which would be words like "a", "about", "am", etc. I used a list of common stopwords from https://www.ranks.nl/stopwords. This helps normalize the search terms and the content of the documents. Finally, the words go through suffix stemming. This would be removing things like "ed", "ing", etc. from the end of a word. This also helps normalize our search terms to what is actually stored in the documents. 

### Indexing the Documents

The documents were indexed using a similar method to an inverted index. A typical inverted index will use the word as a key and then have a list of tuples that contain the associated document and index of the word in the document. Instead my solution is more like an inverted index for each document. I have the document as a key and then another map where the word is a key and it relates to all of the indices of the word in that document. I would be curious what the performance difference (if any) may be between my implementation vs a more traditional inverted index. 

I store the entire index in memory which could cause issues if there is a lot of text to search through. This could easily be fixed by storing the cached index on disk or in a database or better yet by using sharding. 

### Searching the indexed text

Searching for a phrase is pretty simple with this implementation. I create a set of all of the indices of the nth word and then I create a set of all of the indices for the nth + 1 word. I then loop through all of the nth word indices to see if their index + 1 exists in the nth + 1 indices set. If the index exists there then I keep track of that one as well and repeat this process with the nth + 1 word becoming the nth word. Then however many numbers are in the indices set by the end is how many full matches we had and is used to determine the rank of the documents. 

# Testing Approach

## Performance Testing

To test the performance of each algorithm I ran them for 2 million iterations each. To come up with search terms I took all of the text from the documents and cycled through them using 1-10 words at a time. While doing performance testing I found that I was accidentally preprocessing every word a second time during my indexed search. Once I removed that logic it significantly improved the performance of this search dropping it from an 11 second runtime to 7 second runtime on 2 million searches. 

## Testing the Algorithms 

I used a mixture of unit testing and running specific search words through each algorithm to ensure they worked as expected. I ensured that the results were the same between the three algorithms as well. Through my performance testing I also found that there could be issues with the regex search depending on what characters were used so I had to use Pattern.quote(). 

# Summary

I ran performance tests on all three algorithms and compiled the results. I found that my indexed search is the fastest followed by the regex match and then the brute force approach. On running a search on 2 million valid search terms, my indexed search took about *7 seconds* to complete, the regex match took about *11 seconds* to complete and the brute force search took about *31 seconds* to complete. 

![alt text](https://i.imgur.com/bdiqOAG.png "Benchmark Results")

# How to Use

I have committed my entire IntelliJ project so it is super easy to run the code. There are two main classes to be ran. The first would be *Application.java*. This will allow you to do a one time run where you can choose which algorithm to run and what your search term would be. The second is *StressTestApplication.java* which will run the performance benchmark for all of the algorithms. To add any other documents to be searched on they simply need to be added to the resources directory. The only limitation is that documents must have unique names.

Please note that I am using Java 11 features.

![alt text](https://i.imgur.com/6mu8LFx.png "Program Input Example")