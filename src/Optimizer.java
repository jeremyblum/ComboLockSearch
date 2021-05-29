/**
 * The class with the main method to drive the optimization process
 */
import java.util.Random;

public class Optimizer {
	/*************************************************
    *  PARAMETERS TO CONTROL THE OPTIMIZATION PROCESS
	**************************************************/
	static int SEED = 4; // A seed for the random number generator to produce consistent results
	public static Random prng; // The prng that should be used throughout the optimization process
	public static int VERBOSITY = 0; // A variable that controls how much output the optimization
									// process produces
	static int populationSize = 40; // The size of a generation in the GA
	static int generations = 1_000; // The number of generations in the optimization process
	
	// The operators used to produce the initial solutions
	static InitializationOperator[] initOps = { new InitOpRandom(), new InitOpChooseWords()};
	// The weight that determines how likely each initialization operator will be used
	static double[] initOpWeights = {1.0,1.0};
	
	// A list of mutation operators to apply to the solutions
	static MutationOperator[] mutationOperators = { new MutateChooseLetterFromWord()};
	// The maximum number of times each mutation operator will run
	static int[] mutationMaxTimes = {100};
	
	/**
	 * The driver method for the optimization process
	 * @param args not used
	 */
	public static void main(String[] args) {
		prng = new Random(SEED);
		Population population = new Population(initOps, initOpWeights, populationSize);

		for (int g = 0; g < generations; g++) {
			System.out.println("Generation " + (g+1));
			population.runGeneration(mutationOperators, mutationMaxTimes);
		}
		System.out.print("Best solution, score: ");
		System.out.println(population.getBest().getScore());
		System.out.println(population.getBest().toString());		
	}
	
}
