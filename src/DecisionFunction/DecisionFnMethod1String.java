
package DecisionFunction;

import java.util.*; 
/**
 *
 * @author Anika Raghuvanshi
 */

//Method 1 is an exhaustive branching method which uses recursion
public class DecisionFnMethod1String extends DecisionFunctionMethod{
    //main entry point method
    //given an array of terms, returns all optimal solutions
    //calls the recursive method 
    @Override
    public ArrayList<String> evaluate(String[] terms) {
        ArrayList<String> solutions = new ArrayList<>();
        String used = ""; 
        String allLiterals = uniqueLiterals(terms);
        solutions = evaluateRecursive(used, allLiterals, terms);
        
        int i = Integer.MAX_VALUE;
        ArrayList<String> optimal = new ArrayList<>(); 
        for(int j=0; j<solutions.size(); j++) {            
            for(int k=0; k<solutions.size(); k++) {
                if(isSubsetOf(solutions.get(k), solutions.get(j)) && k!=j) { 
                    solutions.set(j, ""); //sets a solution to null if it is a subset of another solution
                }
            }
        }
        for(String s: solutions) {
            if(!s.equals("")) {
                optimal.add(s);
            }
        }
        //System.out.print("Optimal solutions: ");
        //System.out.println(optimal);
        return optimal;
    }
    
    //internal recursive method 
    private ArrayList<String> evaluateRecursive(String used, String allLiterals, String[] terms) {
        ArrayList<String> solutions = new ArrayList<>();
        
        if(!used.isEmpty()) {    
            String var = used.substring(used.length()-1);
            for(int i=0; i<terms.length; i++) {
                if (terms[i].contains(var)) {
                    terms[i] = "";
                }                
                if(terms[i].contains(var.toLowerCase())) {
                    terms[i] = terms[i].replaceAll(var.toLowerCase(), "");
                }
            }
        }
        
        //end condition: check if all terms are equal to 1
        if(allOnes(terms)) {
            return addSolutions(used, solutions);
        }
        
        //recursive routine to find all solutions
        for(int i=0; i<allLiterals.length(); i++) {
            String x = allLiterals.substring(i,i+1);
            String usedTemp = used + x;
            String allLiteralsTemp = allLiterals.replaceAll(x, "");
            solutions = addSolutions(evaluateRecursive(usedTemp, allLiteralsTemp, terms.clone()), solutions);   
        }        
        //System.out.println(solutions);
        return solutions;
    }
    
    //finds the unique variables to begin the exhaustive branching
    //ie. for an expression like (A+B)(B+C+D) it will return A,B,C,and D
    private String uniqueLiterals(String[] terms) {
        String allLiterals = "";
        String temp = "";
        for (String s : terms) {
            temp = temp + s.toUpperCase();
        }
        while (temp.length() > 0) {
            String var = temp.substring(0, 1);
            allLiterals = allLiterals + var;
            temp = temp.replaceAll(var, ""); 
        }       
        return allLiterals;
    }
    
    //order the solution and add it to 'solutions' only if it is unique
    private ArrayList<String> addSolutions(String used, ArrayList<String> solutions) {
        char[] vars = used.toCharArray();
        Arrays.sort(vars);
        String temp = new String(vars);
        for(String s: solutions) {
            if (s.contentEquals(temp)) {return solutions;}
        }
        solutions.add(temp);
        return solutions;
    }
    
    //add all solutions from an array of solutions to 'solutions' if they are unique
    private ArrayList<String> addSolutions(ArrayList<String> used, ArrayList<String> solutions) {
        for(String s: used) {
            solutions = addSolutions(s, solutions);
        }
        return solutions;
    }
    
    private void printTerms(String label, String[] terms) {
        
        System.out.print(label + " ");
        for(String t: terms) {
            if (t.isEmpty()) {System.out.print("null ");}
            else {System.out.print(t + " ");}
        }
        System.out.println(".");
    }
    
    private boolean allOnes(String[] terms) {
        for(String t:terms) {
            if(allPositive(t)) {return false;} //the method will return true if every term contains a negated variable
        }
        return true;
    }
    
    private boolean allPositive(String s) {
        char[] chars = s.toCharArray();
        if(chars.length == 0) {return false;}
        for(char c: chars) {
            if(Character.isLowerCase(c)) {return false;}
        }
        return true;
    }
    
    private boolean isSubsetOf(String stringX, String stringY) {
        char[] charsX = stringX.toCharArray();
        char[] charsY = stringY.toCharArray();
        if(charsX.length > charsY.length) {return false;} //stringX is not a subset of stringY if it is bigger than stringY 
        if(charsX.length == 0) {return false;} //returns false if the string is null
        for(char x: charsX) {
            if(stringY.indexOf(x) == -1) {return false;} //returns false if stringY does not contain a variable from stringX
        }
        return true;
    }
}
