import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
/**
 * A class with static methods that represent a collection of words. 
 * The maximum word length is indicated by the WHEEL_COUNT field of the Solution class.  All words from the words.txt file with
 * a length less than or equal to the maximum are stored in the collection.
 * Words containing non-alphabetic symbols are ignored.
 * The words themselves are stored as arrays of integers with a 1 corresponding to 'a', 2 to 'b', ..., and 26 to 'z'
 * For words less than the maximum length a 0 is used to represent a space.
 */
public class Dictionary {
	static int[][] words; // words[i][j] contains the integer value of the jth letter in the ith word
	static double[] word_count; // frequency of words[i] derived from the Google Web Trillion Word Corpus
								// word_count[i] = [actual count derived from Google]/[least common word]
	
	/**
	 * Load the dictionary
	 */
	static {
		int wordSize = Solution.WHEEL_COUNT;
		Scanner sc;
		try {
			sc = new Scanner(new File("words_with_frequency.txt"));
		} catch (FileNotFoundException e) {
			throw new RuntimeException("Missing word list");
		}
		ArrayList<String> rawWords = new ArrayList<>();
		//ArrayList<Long> rawWords_count = new ArrayList<>();
		while (sc.hasNext()) {
			String word = sc.next().toLowerCase();
			Long count = sc.nextLong();
			boolean ok = true;
			if (word.length() > wordSize) { 
				ok = false;
			}
			for (int i = 0; ok && i < word.length(); i++) {
				if (word.charAt(i) < 'a' || word.charAt(i) > 'z') ok = false;
			}
			
			if (ok) {
				rawWords.add(word);
				//rawWords_count.add(count);
			}
		}
		sc.close();
		words = new int[rawWords.size()][wordSize];
		word_count = new double[rawWords.size()];
		//double smallest_count = (double) rawWords_count.get(rawWords_count.size() - 1); // holds the count of the least common word
		for (int i = 0; i < rawWords.size(); i++) {
			for (int j = 0; j < wordSize; j++) {
				int letter = 0;
				if (j < rawWords.get(i).length()) {
					letter = rawWords.get(i).charAt(j) - 'a' + 1;
				}
				words[i][j] = letter;
			}
			//word_count[i] = rawWords_count.get(i) / smallest_count;
			word_count[i] = 1.0;
		}
	}
	
//	/**
//	 * Return the number of words in the dictionary that can be made with a given
//	 * lock configuration
//	 *
//	 * @param solution the lock configuration
//	 * @return the number of words in the dictionary that can be represented on the lock
//	 */
//	static int score(Solution solution) {
//		int score = 0;
//		for (int i = 0; i < words.length; i++) {
//			boolean ok = true;
//			for (int j = 0; ok && j < words[i].length; j++) {
//				if (!solution.hasLetter(j, words[i][j])) ok = false;
//			}
//			if (ok) score++;
//		}
//		return score;
//	}

	/**
	 * Return the total weight of words in the dictionary (based on their frequency) that can be made with a given
	 * lock configuration
	 *
	 * @param solution the lock configuration
	 * @return the total weight of the words in the dictionary that can be represented on the lock
	 */
	static double score(Solution solution) {
		double score = 0.0;
		for (int i = 0; i < words.length; i++) {
			boolean ok = true;
			for (int j = 0; ok && j < words[i].length; j++) {
				if (!solution.hasLetter(j, words[i][j])) ok = false;
			}
			if (ok) score += word_count[i];
		}
		return score;
	}
	
	/**
	 * Get a random word from the dictionary
	 * 
	 * @return a word from the dictionary chosen at random, with letter encoded as described in the 
	 *         header comment for this class
	 */
	static int[] getRandomWord() {
		int i = Optimizer.prng.nextInt(words.length);
		return words[i];
	}
}
