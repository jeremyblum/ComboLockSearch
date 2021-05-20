/**
 * The population of lock configurations for the GA
 */

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;

public class Population {
	private enum ResultCode {DUPLICATE, SUCCESS, BEST_SO_FAR}; // the result of adding a lock configuration to the population
	private ArrayDeque<Solution> populationList = new ArrayDeque<>(); // a queue of populations, used to remove the old generation
	private HashSet<Solution> populationSet = new HashSet<>(); // the current set of lock configurations, used to prevent duplicate
															   // configurations
	private Solution bestSolutionSoFar; // The best configuration so far
	private int targetSize; // The target population size
	
	
	/**
	 * Seed the population with initial solutions
	 * @param agents an array of initialization agents that generate initial solutions
	 * @param weights weights[i]/[Sum of weights] is the probability that agents[i] will run
	 * @param targetSize the size of the population 
	 */
	Population(InitializationOperator[] agents, double[] weights, int targetSize) {
		this.targetSize = targetSize;
		if (agents.length == 0) {
			throw new IllegalArgumentException("You must pass at least one initialization operator to seed the population.");
		}
		if (agents.length != weights.length) {
			throw new IllegalArgumentException("The weights array must be the same length as the agents array.");
		}
		double sum = 0.0;
		for (int i = 0; i < agents.length; i++) {
			if (weights[i] <= 0.0) {
				throw new IllegalArgumentException("The weights array must contain positive values.");
			}
			sum += weights[i];
		}
		
		// Seed the population with initial solutions
		while (populationList.size() != targetSize) {
			InitializationOperator agent = rouletteSelection(agents, weights, sum);
			Solution solution = agent.run();
			switch (insert(solution)) {
			case SUCCESS:
				if (Optimizer.VERBOSITY > 1) System.out.println(agent.getName() + ": " + solution.getScore() + " " + solution.toString());
				break;
			case DUPLICATE:
				if (Optimizer.VERBOSITY > 0) System.out.println(agent.getName() + ": produced duplicate solution." + " " + solution.toString());
				break;
			case BEST_SO_FAR:
				System.out.println("BEST " + agent.getName() + ": " + solution.getScore() + " " + solution.toString());
				break;
			}
		}
	}

	/**
	 * Create a new generation of child solutions
	 * 
	 * @param mutationOperators an array of mutation operators to apply to the children
	 * @param maxMutations maxMutations[i] is the maximum number of mutations to apply mutationOperator[i]
	 */
	public void runGeneration(MutationOperator[] mutationOperators, int[] maxMutations) {
		// Create children
		while (populationList.size() < targetSize * 2) {
			generateChildren(mutationOperators, maxMutations);
		}
		// Kill the parents
		while (populationList.size() > targetSize) {
			populationSet.remove(populationList.remove());
		}
	}
	
	/**
	 * Go through the selection, crossover, and mutation phases
	 * @param mutationOperators the mutation operators to apply to offspring
	 * @param maxMutations we choose a random number in [0..maxMutations[i]) to apply operator at mutation[i]
	 */
	public void generateChildren(MutationOperator[] mutationOperators, int[] maxMutations) {
		Solution[] parents = selectParents();
		Solution[] children = crossover(parents);
		
		for (int i = 0; i < children.length; i++) {
			// Run a greedy mutation process for child
			Solution solution = children[i];
			for (int j = 0; j < mutationOperators.length; j++) {
				MutationOperator mutation =  mutationOperators[j];
				int times = Optimizer.prng.nextInt(maxMutations[j]);
				for (int t = 0; t < times; t++) {
					//solution = mutation.run(children[i]); // This would be another way to generate the mutations by always
															// starting with the best mutatation seen so far
					solution = mutation.run(solution);
					if (solution.getScore() > children[i].getScore()) {
						children[i] = solution;
					}
				}
			}
			switch(insert(children[i])) {
			case DUPLICATE:
				if (Optimizer.VERBOSITY > 0) System.out.println("Offspring produced was duplicate. " + children[i].toString());
				break;
			case BEST_SO_FAR:
				System.out.print("BEST " + children[i].toString() + " " + children[i].getScore() + " " + children[i].toString());
				break;
			case SUCCESS:
				if (Optimizer.VERBOSITY > 0) System.out.println(children[i].getScore() + " " + children[i].toString());
			}
		}
	}

	/** 
	 * Accessor method 
	 * @return the best solution seen so far
	 */
	public Solution getBest() {
		return bestSolutionSoFar;
	}
	
	/**
	 * Accessor method
	 * @return the number of children in the population
	 */
	public int getSize() {
		return populationList.size();
	}
	
	/**
	 * Select an agent using a roulette wheel selection
	 * @param agents the array of agents
	 * @param weights weights[i]/sum is the probability that agents[i] will run
	 * @param sum the sum of values in the weights array
	 * @return
	 */
	private InitializationOperator rouletteSelection(InitializationOperator[] agents, double[] weights, double sum) {
		double selector = Optimizer.prng.nextDouble() * sum;
		int index = 0;
		while (selector > weights[index] && index < agents.length - 1) {
			selector -= weights[index];
			index++;
		}
		return agents[index];
	}
	
	/**
	 * Use roulette wheel selection to select fit parents
	 * @return an arraylist with two parents
	 */
	private Solution[] selectParents() {
		Solution[] parents = new Solution[2];
		double sum = 0;
		int count = 0;
		for (Solution solution: populationList) {
			if (count == targetSize) break;
			sum += solution.getScore();
			count++;
		}
		double selector = sum * Optimizer.prng.nextDouble();
		count = 0;
		for (Solution solution: populationList) {
			selector -= solution.getScore();
			if (selector <= 0 || count == targetSize - 1) {
				parents[0] = solution;
				break;
			}
			count++;
		}
		sum = 0;
		count = 0;
		for (Solution solution: populationList) {
			if (count == targetSize) break;
			if (solution != parents[0]) {
				sum += solution.getScore();
				count++;
			}
		}
		selector = sum * Optimizer.prng.nextDouble();
		count = 0;
		for (Solution solution: populationList) {
			if (solution != parents[0]) {
				selector -= solution.getScore();
				if (selector <= 0) {
					parents[1] = solution;
					break;
				}
			}
			count++;
			if (count == targetSize) {
				parents[1] = solution;
				break;
			}
		}
		return parents;
	}
	
	/**
	 * Create 2 children by applying a crossover operator to the parents.
	 * At each wheel we choose a random half of the letters for one offspring, and use
	 * the other letters for the other offspring.
	 * 
	 * @param parents the parents
	 * @return an array of two child lock configurations
	 */
	private Solution[] crossover(Solution[] parents) {
		Solution[] children = {new Solution(), new Solution()};
		
		for (int w = 0; w < Solution.WHEEL_COUNT; w++) {
			ArrayList<Integer> commonLetters = new ArrayList<>(); // Letters present in both parents
			ArrayList<Integer> uniqueLetters = new ArrayList<>(); // Letters unique to one parent
			for (int letter = 0; letter < 26; letter++) {
				if (parents[0].hasLetter(w, letter)) {
					if (parents[1].hasLetter(w, letter)) {
						commonLetters.add(letter);
					}
					else {
						uniqueLetters.add(letter);
					}
				}
				else if (parents[1].hasLetter(w, letter)) {
						uniqueLetters.add(letter);
				}
			}
			Collections.shuffle(uniqueLetters);
			for (int letter: commonLetters) {
				for (int c = 0; c < 2; c++) {
					children[c].addLetter(w, letter);
				}
			}
			for (int letter: uniqueLetters) {
				if (children[0].getWheelSize(w) < Solution.WHEEL_SIZE)
					children[0].addLetter(w,letter);
				else
					children[1].addLetter(w,letter);
			}
		}
		
		return children;
	}

	/**
	 * Add a solution to the population, if there is not an identical solution already present
	 * @param solution the solution to be added
	 * @return whether the solution was not added because it is a duplicate, whether it was added, and whether
	 *         it is the best solution seen so far
	 */
	private ResultCode insert(Solution solution) {
		if (populationSet.contains(solution)) return ResultCode.DUPLICATE;
		int score = solution.getScore();
		populationList.add(solution);
		populationSet.add(solution);
		if (bestSolutionSoFar == null || bestSolutionSoFar.getScore() < score) {
			bestSolutionSoFar = solution;
			return ResultCode.BEST_SO_FAR;
		}
		return ResultCode.SUCCESS;
	}
	
	
}
