/**
 * Local search heuristic that chooses a random letter from a random word in the dictionary, and
 * ensures that that letter is present in the lock.  
 * 
 * @author jjb24
 *
 */
public class MutateChooseLetterFromWord extends MutationOperator{
	static int MAX_TRIES = 1000;

	/**
	 * Create a new lock configuration 
	 * @param input the existing configuration
	 * @return a new configuration with one letter changed
	 */
	@Override
	public Solution run(Solution input) {
		Solution solution = new Solution();
		int letter = -1;
		int wheel = -1;
		boolean ok = false;
		int tries = 0;
		while (!ok) {
			tries++;
			if (tries > MAX_TRIES) {
				System.err.println("Giving up trying to find new letter");
				return solution;
			}
			
			int[] word = Dictionary.getRandomWord();
			wheel = Optimizer.prng.nextInt(Solution.WHEEL_COUNT);
			letter = word[wheel];
			ok = !input.hasLetter(wheel, letter);
		}
		int oldLetter = -1;
		ok = false;
		tries = 0;
		while (!ok) {
			tries++;
			if (tries > MAX_TRIES) {
				System.err.println("Giving up trying to find old letter");
				return solution;
			}
			oldLetter = Optimizer.prng.nextInt(27);
			ok = input.hasLetter(wheel, oldLetter);
		}
		for (int w = 0; w < Solution.WHEEL_COUNT; w++) {
			for (int i = 0; i < 27; i++) {
				if (w != wheel || i != oldLetter) {
					if (input.hasLetter(w,i)) {
						solution.addLetter(w, i);
					}
				}
			}
		}
		solution.addLetter(wheel, letter);
		return solution;
	}

}
