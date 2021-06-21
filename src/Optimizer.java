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
	static int rounds = 1_000; // the number of rounds the GA will run
	
//	// The operators used to produce the initial solutions
//	static InitializationOperator[] initOps = { new InitOpRandom(), new InitOpChooseWords(), new InitOpCommonLetters()};
//	// The weight that determines how likely each initialization operator will be used
//	static double[] initOpWeights = {1.0,1.0,1.0};

	// The operators used to produce the initial solutions
	static InitializationOperator[] initOps = { new InitOpRandom(), new InitOpChooseWords()};
	// The weight that determines how likely each initialization operator will be used
	static double[] initOpWeights = {1.0,1.0};
	
	// A list of mutation operators to apply to the solutions
	static MutationOperator[] mutationOperators = { new MutateChooseLetterFromWord()};
	// The maximum number of times each mutation operator will run
	static int[] mutationMaxTimes = {100};

	static double totalWords = 5540.0;
	
	/**
	 * The driver method for the optimization process
	 * @param args not used
	 */
	public static void main(String[] args) {
		prng = new Random(SEED);
		Population population = new Population(initOps, initOpWeights, populationSize);

//		for (int g = 0; g < generations; g++) {
//			System.out.println("Generation " + (g+1));
//			population.runGeneration(mutationOperators, mutationMaxTimes);
//		}
//		System.out.print("Best solution, score: ");
//		System.out.println(population.getBest().getScore());
//		System.out.println(population.getBest().toString());

		int min = Integer.MAX_VALUE;
		int max = Integer.MIN_VALUE;
		double sum = 0.0;
		int[] bestGenerations = new int[rounds];
		for(int r = 0; r < rounds; r++){
			boolean minGenerationFound = false;
			for (int g = 0; g < generations; g++) {
				System.out.println("Generation " + (g+1));
				population.runGeneration(mutationOperators, mutationMaxTimes);
				if(!minGenerationFound && population.getBest().getScore() == totalWords){
					bestGenerations[r] = g;
					minGenerationFound = true;
					sum += g;
					if(g < min) min = g;
					if(g > max) max = g;
				}
			}
			System.out.print("Best solution, score: ");
			System.out.println(population.getBest().getScore());
			System.out.println(population.getBest().toString());
		}
		double mean = sum / rounds;
		int median = bestGenerations[rounds/2];
		double stdDev = calculateSD(bestGenerations,sum, mean);
		System.out.println("Genetic Algorithm Statistics after " + rounds + " for length " + Solution.WHEEL_COUNT + ":");
		System.out.println("Min: " + min);
		System.out.println("Max: " + max);
		System.out.println("Mean: " + mean);
		System.out.println("Standard Deviation: " + stdDev);


	}

	public static double calculateSD(int[] bestGen, double sum, double mean)
	{
		double standardDeviation = 0.0;
		for(int num: bestGen) {
			standardDeviation += Math.pow(num - mean, 2);
		}

		return Math.sqrt(standardDeviation/bestGen.length);
	}
	
}
