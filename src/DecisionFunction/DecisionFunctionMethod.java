
package DecisionFunction;

import java.util.ArrayList;

/**
 *
 * @author Anika Raghuvanshi
 */
public class DecisionFunctionMethod {
    public ArrayList<String> evaluate(String[] termsString) {
        ArrayList<ArrayList<Integer>> terms = new ArrayList<>();
        
        // Convert terms from String to Integer Array
        for (String t : termsString) {
            if (t.isEmpty()) {continue;}
            ArrayList<Integer> term = new ArrayList<>();
            char[] xArray = t.toCharArray();
            for (char x : xArray) {
                //A-Z are represented by integers 1-26
                if (x >= 'A' && x <= 'Z') {
                    Integer intVal = x - 'A' + 1;
                    term.add(intVal);
                }
                //a-z are represented by integers (-1)-(-26)
                if (x >= 'a' && x <= 'z') {
                    Integer intVal = x - 'a' + 1;
                    intVal = -intVal;
                    term.add(intVal);
                }                
            }  
            terms.add(term);
        }
        
        ArrayList<ArrayList<Integer>> solutions = evaluate (terms);
        // Now convert ArrayList solutions to String
        ArrayList<String> solutionString = new ArrayList<>();
        for (ArrayList<Integer> solution : solutions) {
            String temp = "";
            for (Integer i : solution ) {
                char ch = (char)((int)'A' + i -1);
                temp = temp + ch;
            }
            solutionString.add(temp);
        }
       
        return solutionString;
    }
    
    public ArrayList<ArrayList<Integer>> evaluate(ArrayList<ArrayList<Integer>> terms) {
        return null;
    }
       
}
