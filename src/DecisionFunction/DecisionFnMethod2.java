
package DecisionFunction;

import java.util.ArrayList;
import java.util.Collections;

/**
 *
 * @author Anika Raghuvanshi
 */
//Method 2 branches based on the most occuring variable(s)
public class DecisionFnMethod2 extends DecisionFunctionMethod{
    private int stats_rcnt = 0;
    private int stats_rlevel = 0;
    private int currentBestLength = Integer.MAX_VALUE;
    private int maxSelections = 10;
    private int limitSolutions = 100;
    private int currentSolutions = 0;
    
    //main entry point method
    //given an array of terms, returns all optimal solutions
    public ArrayList<ArrayList<Integer>> evaluate(ArrayList<ArrayList<Integer>> terms) {
        ArrayList<ArrayList<Integer>> solutions;
        ArrayList<Integer> used = new ArrayList<>();
        solutions = evaluateRecursive(used, terms);

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
            ArrayList<ArrayList<Integer>> terms) {
        
        stats_rcnt++;
        stats_rlevel++;
        
        ArrayList<ArrayList<Integer>> solutions = new ArrayList<>();
                
        // see if there are any terms that are just single positive literals
        // if so, these must be removed from terms and added to used array
        for (ArrayList<Integer> term:terms) {
            if (term.size() == 1) {
                Integer literal = term.get(0);
                if (literal > 0) {
                    if (!used.contains(literal)) {
                        used.add(literal);
                    }
                }
            }
        }
                        
        for(Integer var:used) {
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
                
        /* for debugging 
        int tcount = 0;
        for (ArrayList<Integer> term:terms) {
            if (!term.isEmpty()) {tcount++;}
        }
        
        System.out.println(stats_rcnt + ", " + stats_rlevel + ", " + 
             used.size() + ", " + tcount + ", " + solutions.size());
        */
                 
        //end condition: check if all terms are equal to 1
        if(allOnes(terms)) {
            stats_rlevel--;
            solutions =  addSolutionOne(used, solutions);
            //System.out.print("Solutions (" + solutions.size() + "): ");
            //System.out.println(solutions);
            return solutions;
        }
        
        // if the length of used is already equal to the best solution so 
        // far, then there is no point of going further
        if (used.size() >= currentBestLength) {
            //System.out.println("Pruning.");
            return solutions;
        }
        
        ArrayList<Integer> bestLiterals = findBestLiterals(terms);
        for(int i=0; i<bestLiterals.size(); i++) {
            if (i > maxSelections) {break;}
            Integer x = bestLiterals.get(i);
            ArrayList<Integer> usedTemp = new ArrayList<>(used);
            usedTemp.add(x);
            ArrayList<ArrayList<Integer>> termsTemp = copyOf(terms);
            solutions = addSolutionsMany(
                    evaluateRecursive(usedTemp,termsTemp), 
                    solutions);
            //System.out.println(stats_rlevel + ", " + bestLiterals.size() + 
            //        ":Solutions (" + solutions.size() + "): ");
            if (currentSolutions >= limitSolutions) {
                break;
            }
        }        
        stats_rlevel--;
        return solutions;
    }
    
    //finds the unique variables
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
            currentSolutions++;
            if (used.size() < currentBestLength) {
                currentBestLength = used.size();
            }
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
    
    //Unique to DecisionFnMethod2 (in relation to DecisionFnMethod1)
    private ArrayList<Integer> findBestLiterals (ArrayList<ArrayList<Integer>> terms) {
        //Find all unique literals remaining in the expression 
        ArrayList<Integer> allLiterals = uniqueLiterals(terms);
           
        //Find the variable that occurs the most
        int bestCount = 0;
        ArrayList<Integer> bestLiteral = new ArrayList<>();
        
        for (int i: allLiterals) {
            int count = 0;
            for (ArrayList<Integer> term: terms) {
                for(int j: term) {
                    if(j == i) {count++;}
                }           
            } 
            if (count > bestCount) {
                bestCount = count;
                bestLiteral.clear();
                bestLiteral.add(i);
            }
            if (count == bestCount) {
                bestCount = count;
                bestLiteral.add(i);
            }            
        }
        return bestLiteral;
    }
}