import java.util.HashMap;
import java.util.Map;

/**
 * Create an initial lock configuration by choosing the 10 most common letters for
 * each word position and adding these letters to the configuration
 * @author jpa5180
 *
 */
public class InitOpCommonLetters extends InitializationOperator{

    /**
     * Create an initial solution
     *
     * @return a lock configuration using the strategy described in the class header
     *
     */
    @Override
    public Solution run() {
        Solution retVal = new Solution();
        //Map<Integer,Integer>[] letterCounts = Dictionary.commonLetters.clone();
        Map<Integer,Integer>[] letterCounts = new Map[Dictionary.commonLetters.length];
        for(int i = 0; i < letterCounts.length; i++) {letterCounts[i] = new HashMap<Integer, Integer>(Dictionary.commonLetters[i]);}

        for (int w = 0; w < Solution.WHEEL_COUNT; w++) {
            while (retVal.getWheelSize(w) < Solution.WHEEL_SIZE) {
                int letter = getMax(letterCounts[w]);
                if (!retVal.hasLetter(w, letter)) retVal.addLetter(w, letter);
                letterCounts[w].remove(letter);
            }
        }
        return retVal;
    }

    /**
     * @param letterCounts a HashMap containing all the letters and the frequency seen at certain position in the words
     * @return the letter that is the most common in all the words at a certain position
     *
     */
    public int getMax(Map<Integer,Integer> letterCounts){
        int count = -1;
        int letter = 0;
        for(Integer l : letterCounts.keySet()){
            int letterTotal = letterCounts.get(l);
            if(count < letterTotal){
                count = letterTotal;
                letter = l;
            }
        }
//        System.out.print(letter + " ");
//        System.out.print(count + "\n");
        return letter;
    }
}
