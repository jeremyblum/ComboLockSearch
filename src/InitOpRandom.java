/**
 * Create an initial lock configuration by choosing random letters
 * 
 */
public class InitOpRandom extends InitializationOperator {

	/**
	 * @return a lock configuration with random letters chosen for each wheel
	 */
	@Override
	public Solution run() {
		Solution retVal = new Solution();
		
		for (int w = 0; w < Solution.WHEEL_COUNT; w++) {
			while (retVal.getWheelSize(w) < Solution.WHEEL_SIZE) {
				int letter = Optimizer.prng.nextInt(27);
				if (!retVal.hasLetter(w, letter)) retVal.addLetter(w, letter);
			}
		}
		return retVal;
	}
}
