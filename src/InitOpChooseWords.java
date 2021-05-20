/**
 * Create an initial lock configuration by choosing random words from the dictionary and
 * adding letters from these words to the configuration
 * @author jjb24
 *
 */
public class InitOpChooseWords extends InitializationOperator{

	/**
	 * Create an initial solution
	 * 
	 * @return a lock configuration using the strategy described in the class header
	 * 
	 */
	@Override
	public Solution run() {
		Solution retVal = new Solution();
		
		boolean done = false;
		while (!done) {
			int[] word = Dictionary.getRandomWord();
			for (int i = 0; i < Solution.WHEEL_COUNT; i++) {
				if (retVal.getWheelSize(i) < Solution.WHEEL_SIZE && !retVal.hasLetter(i, word[i])) { 
					retVal.addLetter(i, word[i]);
				}
			}
			done = true;
			for (int i = 0; done && i < Solution.WHEEL_COUNT; i++) {
				if (retVal.getWheelSize(i) < Solution.WHEEL_SIZE) { 
					done = false;
				}
			}
		}
		return retVal;
	}
}
