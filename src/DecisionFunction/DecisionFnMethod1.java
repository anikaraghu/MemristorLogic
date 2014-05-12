
package DecisionFunction;

import java.util.*; 
/**
 *
 * @author Anika Raghuvanshi
 */

//Method 1 is an exhaustive branching method which uses recursion
public class DecisionFnMethod1 extends DecisionFunctionMethod{
    //main entry point method
    //given an array of terms, returns all optimal solutions
    public ArrayList<ArrayList<Integer>> evaluate(ArrayList<ArrayList<Integer>> terms) {
        ArrayList<ArrayList<Integer>> solutions;
        ArrayList<Integer> used = new ArrayList<>(); 
        ArrayList<Integer> allLiterals = uniqueLiterals(terms);
        solutions = evaluateRecursive(used, allLiterals, terms);

        //printListList("All solutions", solutions);

        ArrayList<ArrayList<Integer>> optimal = new ArrayList<>(); 
        
        for (ArrayList<Integer> x : solutions ) {
            for (ArrayList<Integer> y : solutions) {
                if (x == y) {continue;}
                if(isSubsetOf(x, y)) {
                    y.clear();
                }
            }
        }

        for(ArrayList<Integer> s: solutions) {
            if(!s.isEmpty()) {
                optimal.add(s);
            }
        }
        
        //printListList("Optimal solutions", optimal);
        return optimal;
    }
    
    //internal recursive method 
    private ArrayList<ArrayList<Integer>> evaluateRecursive(
            ArrayList<Integer> used, 
            ArrayList<Integer> allLiterals, 
            ArrayList<ArrayList<Integer>> terms) {
        
       
        ArrayList<ArrayList<Integer>> solutions = new ArrayList<>();
                
        if(!used.isEmpty()) {
            Integer var = used.get(used.size()-1);
            for (ArrayList<Integer> term: terms) {
                if (term.contains(var)) {
                    term.clear();
                }
            }
            var = -var;
            for (ArrayList<Integer> term: terms) {
                term.remove(var);
            }
        }
        
        //end condition: check if all terms are equal to 1
        if(allOnes(terms)) {
            return addSolutionOne(used, solutions);
        }
        
        //recursive routine to find all solutions
        for(int i=0; i<allLiterals.size(); i++) {
            Integer x = allLiterals.get(i);
            ArrayList<Integer> usedTemp = new ArrayList<>(used);
            usedTemp.add(x);
            ArrayList<Integer> allLiteralsTemp = new ArrayList<>(allLiterals);
            allLiteralsTemp.remove(x);           
            ArrayList<ArrayList<Integer>> termsTemp = copyOf(terms);
            solutions = addSolutionsMany(
                    evaluateRecursive(usedTemp, allLiteralsTemp, termsTemp), 
                    solutions);
        }        

        return solutions;
    }
    
    //finds the unique variables to begin the exhaustive branching
    //ie. for an expression like (A+B)(B+C+D) it will return A,B,C,and D
    private ArrayList<Integer> uniqueLiterals(ArrayList<ArrayList<Integer>> terms) {
        ArrayList<Integer> allLiterals = new ArrayList<>();

        for (ArrayList<Integer> term : terms) {
            for (Integer literal: term) {
                if (literal < 0) { literal = -literal; }
                if (!allLiterals.contains(literal)) {
                    allLiterals.add(literal);
                }
            }
        }
        return allLiterals;
    }
    
    //sort the solution and add it to 'solutions' only if it is unique
    private ArrayList<ArrayList<Integer>> addSolutionOne(
            ArrayList<Integer> used,
            ArrayList<ArrayList<Integer>> solutions) {
        
        Collections.sort(used);
        
        if (!solutions.contains(used)) {
            solutions.add(used);
        }
        return solutions;
    }
    
    //add all solutions from an array of solutions to 'solutions' if they are unique
    private ArrayList<ArrayList<Integer>> addSolutionsMany(
            ArrayList<ArrayList<Integer>> used, 
            ArrayList<ArrayList<Integer>> solutions) {
        
        for(ArrayList<Integer> s: used) {
            solutions = addSolutionOne(s, solutions);
        }
        return solutions;
    }
    

    private ArrayList<ArrayList<Integer>> copyOf(ArrayList<ArrayList<Integer>> srcList) {
        ArrayList<ArrayList<Integer>> destList = new ArrayList<>();
        for (ArrayList<Integer> srcItem:srcList) {
            ArrayList<Integer> destItem = new ArrayList<>();
            for (Integer x:srcItem) {
                destItem.add(x);
            }
            destList.add(destItem);
        }
        return destList;
    }
    
    private boolean allOnes(ArrayList<ArrayList<Integer>> terms) {
        
        for(ArrayList<Integer> t:terms) {
            if(allPositive(t)) {return false;} //the method will return true if every term contains a negated variable
        }
        return true;
    }
    
    private boolean allPositive(ArrayList<Integer> s) {
        if (s.isEmpty()) {return false;}
            
        for (Integer i : s) {
            if (i < 0) { return false; }
        }
        return true;
    }
    
    private boolean isSubsetOf(ArrayList<Integer> listX, ArrayList<Integer> listY) {
        if (listX.isEmpty()) {return false;}
        if (listX.size() > listY.size()) {return false;}
       
        for (Integer x : listX) {
            boolean found = false;
            for (Integer y: listY) {
                if (x == y) {
                    found = true;
                    break;
                }                
            }
            if (!found) {return false;}
        }
        return true;
    }

    // Print routines for debugging
    private void printList(String label, ArrayList<Integer> terms) {       
        System.out.print(label + "(" + terms.size() + "): ");
        for(Integer t: terms) {
            System.out.print(t + " ");
        }
        System.out.println("");
    }
    
    private void printListList(String label, ArrayList<ArrayList<Integer>> terms) {       
        System.out.print(label + "(" + terms.size() + "): ");
        for(ArrayList<Integer> t: terms) {
            if (t.isEmpty()) {
                System.out.print("null ");
                continue;
            }
            System.out.print("[");
            for (Integer x: t) {
                System.out.print(x + " ");
            }
            System.out.print("]");
        }
        System.out.println("");
    }    
}