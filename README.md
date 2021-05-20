# ComboLockSearch

Current Tasks

Get a better word list that includes a frequency weight as well as just the words

Convert the solution scoring for the lock so that it maximizes the sum of the weights, rather than just a word count.

Test this conversion by using the current word list with weights of all 0.5.  The results should be the 1/2 of current results.

For all of the experiments that ask for a score, generate two scores - one with all word weights set to 1, and one with the word frequency as the word weight. 

Add additional local optimization heuristics to try to improve the performance of the system.

Experiment with other aspects of the genetic algorithm set up to try to improve system performance.

Create an initializationOperator that chooses the 10 most common letters for each word position
- Compare the score of this greedy solution with the solution produced by the GA

Create an initializationOperator that chooses the 10 most common letters for a random position in a word.  Then, using only the words that have one of the selected letters at that position, choose another position in the word, and select the 10 most common letters at that position.  Then, using only the words that match the letters at the 2 positions, find the 10 most common letters at the next position, etc.  Note that the configurations will likely be different depending on the order in which you consider positions in the word.
- Collect all n! possible scores from this operator (where n is the max word length); describe them with the following statistics: mean, median, min, max, std dev; compare these results with the output of the GA.

Create an initializationOperator that uses an existing lock from a commercially available lock.
- Compare the scores of the existing locks with the solution produced by the GA


Collect configurations from existing locks, and compare the scores with scores from the GA.
