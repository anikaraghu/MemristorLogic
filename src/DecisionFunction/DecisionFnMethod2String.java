package DecisionFunction;

import java.util.ArrayList;
import java.util.Arrays;

/**
 *
 * @author Anika Raghuvanshi
 */

//Method 2 branches based on the most occuring variable(s)
public class DecisionFnMethod2String extends DecisionFunctionMethod{
    @Override 
    public ArrayList<String> evaluate(String[] terms) { 
        ArrayList<String> solutions;
        String used = "";

        solutions = evaluateRecursive(used, terms);
        //System.out.print("All solutions: ");
        //System.out.println(solutions);
        int i = 1000;
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
    
    private ArrayList<String> evaluateRecursive(String used, String[] terms) {
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
        
        // see if there are any terms that are just single positive literals
        // if so, these must be removed from terms and added to used array
        for (String t:terms) {
            if (t.length() == 1) {
                char c = t.charAt(0);
                if (Character.isUpperCase(c)) {
                    used = used + c;
                    for (int i=0; i<terms.length; i++) {
                        if (terms[i].indexOf(c) != -1) {terms[i] = "";}
                    }   
                }
            }
        }      
        
        //check if all terms are equal to 1
        if(allOnes(terms)) {
            return addSolutions(used, solutions);
        }
        
        String bestLiterals = findBestLiterals(terms);
        //recursive routine to find all solutions
        for(int i=0; i<bestLiterals.length(); i++) {
            String x = bestLiterals.substring(i,i+1);
            String usedTemp = used + x;
            solutions = addSolutions(evaluateRecursive(usedTemp, terms.clone()), solutions);   
        }        
        
        //System.out.println(solutions);
        return solutions;
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
    
    private String findBestLiterals (String[] terms) {
        //Find all unique literals remaining in the expression 
        char[] allLiterals = uniqueLiterals(terms).toCharArray();
           
        //Find the variable that occurs the most
        int bestCount = 0;
        String bestLiteral = "";
        
        for (char ch: allLiterals) {
            int count = 0;
            for (String s: terms) {
                if (s.contentEquals(s.toUpperCase())) {
                    if (s.indexOf(ch) != -1) {
                        count++;
                    }
                }            
            }
            if (count > bestCount) {
                bestCount = count;
                bestLiteral = Character.toString(ch);
            }
            if (count == bestCount) {
                bestCount = count;
                bestLiteral += Character.toString(ch);
            }            
        }
        return bestLiteral;
    }

    public String uniqueLiterals(String[] terms) {
        String allLiterals = "";
        String temp = "";
        
        for (String s: terms) {
            temp = temp + s.toUpperCase();
        }
        
        while (temp.length() > 0) {
            String var = temp.substring(0, 1);
            allLiterals = allLiterals + var;
            temp = temp.replaceAll(var, "");
        }
        return allLiterals;
    }
}
