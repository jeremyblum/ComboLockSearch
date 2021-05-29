/**
 * A class whose objects represent lock configurations
 */
public class Solution {
	static int WHEEL_COUNT = 4; // The target word length
	static int WHEEL_SIZE = 10; // The number of letters on each wheel
	static String alphabet = " ABCDEFGHIJKLMNOPQRSTUVWXYZ"; // The symbols used on the wheels - note that
															// changing this will break the system
									
	int[] wheelSizes = new int[WHEEL_COUNT]; // The number of letters currently selected for each wheel
	int[] wheelMasks = new int[WHEEL_COUNT]; // A mask where bit i is set if the ith letter in alphabet is on 
	                                         // a wheel
	//Integer score; // The fitness of the lock, or null if the fitness still needs to be calculated.
	Double score; // The fitness of the lock, or null if the fitness still needs to be calculated.

//	/**
//	 * Get the fitness of the lock configuration
//	 * @return a score, which is currently the number of words that can be made from the
//	 *         lock configuration
//	 */
//	int getScore() {
//		if (score == null)
//			score = Dictionary.score(this);
//		return score;
//	}

	/**
	 * Get the fitness of the lock configuration
	 * @return a score, which is currently the total weight of words that can be made from the
	 *         lock configuration
	 */
	double getScore() {
		if (score == null)
			score = Dictionary.score(this);
		return score;
	}
	
	/**
	 * Determine if a wheel contains a specific letter
	 * @param wheel the wheel in question in range [0..wheel count)
	 * @param letter an integer representation of the letter, with 0 = ' ', 1 = 'a', 2 = 'b', etc.
	 * @return true iff wheel has this letter
	 */
	boolean hasLetter(int wheel, int letter) {
		if (wheel < 0 || wheel >= WHEEL_COUNT) {
			throw new IllegalArgumentException("Attempted to check to a non-existent wheel #" + wheel + ")");
		}
		if (letter < 0 || letter > 26) {
			throw new IllegalArgumentException("Attempted to check a non existent letter in a wheel (letter #" + letter + ")");
		}
		int letterMask = 1 << letter;
		return (letterMask & wheelMasks[wheel]) != 0;
	}
	
	/**
	 * Add a letter to a wheel
	 * @param wheel the target wheel
	 * @param letter an integer representation of the letter, with 0 = ' ', 1 = 'a', 2 = 'b', etc.
	 */
	void addLetter(int wheel, int letter) {
		if (wheel < 0 || wheel >= WHEEL_COUNT) {
			throw new IllegalArgumentException("Attempted to add a letter to a non-existent wheel #" + wheel + ")");
		}
		if (wheelSizes[wheel] >= WHEEL_SIZE) {
			throw new IllegalArgumentException("Attempted to add a letter to a full wheel");
		}
		if (letter < 0 || letter > 26) {
			throw new IllegalArgumentException("Attempted to add a non existent letter to a wheel (letter #" + letter + ")");
		}
		int letterMask = 1 << letter;
		if ((wheelMasks[wheel] & letterMask) != 0) {
			throw new IllegalArgumentException("Attempted to add a duplicate letter to a wheel (wheel #" + wheel + ", letter #" + letter + ")");
		}
		wheelMasks[wheel] |= letterMask;
		wheelSizes[wheel]++;
		score = null;
	}

	/**
	 * Return the number of letters currently selected for a wheel
	 * @param wheel the wheel in question
	 * @return the number of letters that have been specified for the wheel
	 */
	int getWheelSize(int wheel) {
		return wheelSizes[wheel];
	}
	
	/**
	 * @return a string representation of the lock, with the letter that have been selected
	 */
	@Override 
	public String toString() {
		StringBuffer sb = new StringBuffer();
		
		for (int w = 0; w < wheelMasks.length; w++) {
			sb.append("/");
			for (int letter = 0; letter <= 27; letter++) {
				int mask = 1 << letter;
				if ((mask & wheelMasks[w]) != 0) {
					sb.append(alphabet.charAt(letter));
				}
			}
			sb.append("/");
		}
		return sb.toString();
	}
	
	/**
	 * @return true iff the two solutions have exactly the same letters selected for each wheel
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj == null) return false;
		if (this == obj) return true;
		if (this.getClass() != obj.getClass()) return false;
		Solution that = (Solution) obj;
		for (int w = 0; w < wheelSizes.length; w++) {
			if (this.wheelMasks[w] != that.wheelMasks[w]) return false;
		}
		return true;
	}

	/**
	 * @return an appropriate hash code for the lock
	 */
	@Override
	public int hashCode() {
		int retVal = 0;
		for (int w = 0; w < wheelSizes.length; w++) {
			retVal = retVal * 31 + wheelMasks[w];
		}
		return retVal;
	}

}
