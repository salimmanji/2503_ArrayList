import java.util.Scanner;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * COMP 2503 Winter 2020 Assignment 1
 * January 27, 2020
 * 
 * This program reads a text file and compiles a list of the words together with
 * how many each word appears.
 *
 * Words from a standard list of stop words are not included.
 * 
 * Updated Winter 2020
 * 
 * @author Maryam Elahi
 * @author Salim Manji
 */

public class A1 {

	private static final int NUM_WORDS_TO_PRINT = 10;

	private ArrayList<Token> wordList = new ArrayList<Token>();

	private String[] stopWords = { "a", "about", "all", "am", "an", "and", "any", "are", "as", "at", "be", "been",
			"but", "by", "can", "cannot", "could", "did", "do", "does", "else", "for", "from", "get", "got", "had",
			"has", "have", "he", "her", "hers", "him", "his", "how", "i", "if", "in", "into", "is", "it", "its", "like",
			"more", "me", "my", "no", "now", "not", "of", "on", "one", "or", "our", "out", "said", "say", "says", "she",
			"so", "some", "than", "that", "the", "their", "them", "then", "there", "these", "they", "this", "to", "too",
			"us", "upon", "was", "we", "were", "what", "with", "when", "where", "which", "while", "who", "whom", "why",
			"will", "you", "your" };

	private int totalWordCount = 0;

	private int stopWordCount = 0;

	private Scanner input = new Scanner(System.in);

	public static void main(String[] args) {
		A1 a1 = new A1();
		a1.run();
	}

	/**
	 * Run the program. Read the file, then print the results.
	 */
	public void run() {
		readFile();
		printResults();
	}

	/**
	 * Determines order of and writes the results for the output .txt file once the
	 * program has run its course.
	 * 
	 */

	private void printResults() {
		int loopEnd = Math.min(NUM_WORDS_TO_PRINT, wordList.size());

		System.out.println("Total Words: " + totalWordCount);
		System.out.println("Unique Words: " + wordList.size());
		System.out.println("Stop Words: " + stopWordCount);
		System.out.println();
		System.out.println("10 Most Frequent");
		FreqAsc freqAsc = new FreqAsc();
		Collections.sort(wordList, freqAsc);
		printQuery(loopEnd);

		/*
		 * TODO: Use collection sort to sort the wordList in ascending frequency order.
		 * Then print the first 10.
		 */

		System.out.println();
		System.out.println("10 Least Frequent");
		FreqDesc freqDesc = new FreqDesc();
		Collections.sort(wordList, freqDesc);
		printQuery(loopEnd);

		/*
		 * TODO: Use collection sort to sort the wordList in descending frequency order.
		 * Then print the first 10.
		 */

		loopEnd = wordList.size();
		System.out.println();
		System.out.println("All");
		Alpha alpha = new Alpha();
		Collections.sort(wordList, alpha);
		printQuery(loopEnd);

		/*
		 * TODO: Use collection sort to sort the wordList by its natural ordering
		 * (alphabetical). Then print all the words.
		 */
	}

	/**
	 * Read the input file and process it. Input is on standard input and is read
	 * one word at a time -- separated by whitespace.
	 * 
	 * All non alphabetic characters are stripped out and words are all converted to
	 * lower case.
	 * 
	 * Any non-stopword word is stored in the list of words and the number of
	 * occurrences is tracked.
	 * 
	 */

	private void readFile() {
		Scanner input = new Scanner(System.in);

		while (input.hasNext()) {
			String currentWord = input.next().trim().toLowerCase().replaceAll("[^a-z]", "");

			if (isString(currentWord) && !isStopWord(currentWord)) {
				totalWordCount++;
				Token currentToken = new Token(currentWord);
				if (isUnique(currentToken)) {
					addNewWord(currentToken);
				} else {
					Token toIncrease = tokenToIncrease(currentToken);
					increaseTally(toIncrease);
				}
			} else if (isString(currentWord) && isStopWord(currentWord)) {
				stopWordCount++;
				totalWordCount++;
			}
		}
		input.close();
	}

	/**
	 * When called, this method searches the ArrayList wordList for the token
	 * containing the current string, then determines the index of that token and
	 * increases the frequency counter held by the object.
	 * 
	 * @param 			 toIncrease is the Token created with the current word read by the
	 *                   scanner.
	 *                   
	 */

	private void increaseTally(Token toIncrease) {
		int indexToIncrease = wordList.indexOf(toIncrease);

		if (indexToIncrease > -1)
			wordList.get(indexToIncrease).increaseFrequency();
	}

	/**
	 * If a word has not yet been read in by the file reader, a new Token object is
	 * generated through the constructor call, then added to the ArrayList wordList.
	 * 
	 * @param 	   curr is the current Token of the word being read in by the file reader
	 *             that needs to be added to the Array List.
	 *             
	 */

	private void addNewWord(Token toAdd) {
		wordList.add(toAdd);
	}

	/**
	 * Determines if the String read in by the file reader contains a value, or if
	 * it is empty.
	 * 
	 * @param 	   curr is the current word being read in by the file reader that needs
	 *             to be checked.
	 * @return 	   True if length is greater than 0, False if 0 chars or less.
	 * 
	 */

	private boolean isString(String curr) {
		return (curr.length() > 0);
	}

	/**
	 * Cycles through the ArrayList of stopWords.
	 * 
	 * @param 	   curr is the current word being read in by the file reader that needs
	 *             to be checked.
	 * @return 	   True if curr is on the list of stopWords, False if not on list of
	 *         	   stopWords.
	 *         
	 */

	private boolean isStopWord(String curr) {
		boolean stopWordFound = false;

		for (String word : stopWords) {
			if (word.equals(curr)) {
				stopWordFound = true;
				break;
			}
		}
		return stopWordFound;
	}

	/**
	 * Determines whether the current word has already been read in by the file
	 * reader and added to the ArrayList wordList.
	 * 
	 * @param 	   curr is the current word being read in by the file reader that needs
	 *             to be checked.
	 * @return 	   True if the word has not yet been read in and is unique, False if it
	 *        	   has already been read in and is not unique.
	 *        
	 */

	private boolean isUnique(Token toCheck) {
		boolean uniqueWord = true;

		for (Token currentToken : wordList) {
			if (currentToken.equals(toCheck)) {
				uniqueWord = false;
				break;
			}
		}
		return uniqueWord;
	}

	/**
	 * Find the correct Token object of which to increase frequency count.
	 * 
	 * @param 		toCheck is the fresh instance of the Token created by the word (freq = 1).
	 * @return 		Token object to increase frequency count of.
	 */

	private Token tokenToIncrease(Token toCheck) {
		Token toIncrease = null;

		for (Token currentToken : wordList) {
			if (currentToken.equals(toCheck)) {
				toIncrease = currentToken;
				break;
			}
		}
		return toIncrease;
	}

	/**
	 * Confirms the wordList arraylist size is greater than zero to avoid a null
	 * pointer exception, then cycles through the arraylist to output each word and
	 * the occurrence of that word to the .txt file.
	 * 
	 * @param 		   endIndex is the size of the arrayList if less than 10 unique words are
	 *                 in wordList, 10 if the arrayList more than 10 unique words
	 *                 are in arrayList or the size of wordList when printing all
	 *                 (10 has been initialized to constant "NUM_WORDS_TO_PRINT").
	 */

	private void printQuery(int endIndex) {
		if (endIndex > 0) {
			for (int index = 0; index < endIndex; index++) {
				String word = wordList.get(index).getWord();
				int frequency = wordList.get(index).getFrequency();
				System.out.println(word + " : " + frequency);
			}
		}
	}

}
