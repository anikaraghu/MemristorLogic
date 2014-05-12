
package MemristorsSOPDF;

import DecisionFunction.DecisionFnMethod1;
import DecisionFunction.DecisionFnMethod2String;
import DecisionFunction.DecisionFnMethod2;
import DecisionFunction.DecisionFnMethod3;
import MemristorsSOP.DiagramSOP;
import java.util.*;


/**
 *
 * @author Anika Raghuvanshi
 */

/*******************************************************************************
 Internal representation
 15 14 13 12 11 10 09 08 07 06 05 04 03 02 01 00    //position number
  R  A  B  C  D  E  F  G  H  I  J  K  L  M  N  O    //position variable(first bit is reserved)
 11 11 11 01 11 10 11 11 11 11 11 11 11 11 11 11    //position values-this shows C!E or Ce
*******************************************************************************/
public class CircuitSOPDF {
    private String iEquation;
    private String minterm[];
    private List<Integer> onSet = new ArrayList<>();
    private List<Integer> offSet = new ArrayList<>();
    private List<Integer> dontCareSet = new ArrayList<>();
    private List<Integer> minTerms = new ArrayList<>();
    
    private String kMap4[] = {"abcd","abcD","abCD","abCd",
                              "aBcd","aBcD","aBCD","aBCd",
                              "ABcd","ABcD","ABCD","ABCd",
                              "Abcd","AbcD","AbCD","AbCd"};
    private int dimension;
    private List<Integer> kernel = new ArrayList<>();
    private int totalPulses = 0;
    
    //given the position variable, returns the position number
    private int varPosition(char c) {
        int position = 14 - (c - 'A'); 
        return position;
    }
    
    //reverse of previous method
    private char positionToVar(int x, int pos) {
        char var;
        if(getVarValueAtPosition(x, pos) == 1) {
            var = (char) ('A' + (char) (14 - pos));
        }
        else if(getVarValueAtPosition(x, pos) == 2) {
            var = (char)('a' + (char) (14 - pos));
        }
        else {var = 0;}
        return var;
    }
    
    //sets a bit equal to 01
    private int setPositiveValue(int x, int position) {
        return (x & ~(2 << position * 2));
    }
    
    private int setPositiveValue(int x, char c) {
        int position = varPosition(c);
        int element = setPositiveValue(x, position);
        return element;
    }
    
    
    //sets a bit equal to 10 assuming the initial values of the bits are 11
    private int setNegativeValue(int x, int position) {
        return (x & ~(1 << position * 2));
    }
    
    private int setNegativeValue(int x, char c) {
        int position = varPosition(c);
        int element = setNegativeValue(x, position);
        return element;
    }
    
    //returns the value(1, 2, or 3) of a bit given its position number
    private int getVarValueAtPosition (int x, int pos) {
        int mask = 3 << (pos *2);
        int value = (x & mask) >> (pos *2);
        return value;
    }
    
    //returns the value(1, 2, or 3) of a bit given its position variable
    private int getVarValue (int x, char c) {
        int pos = varPosition(c);
        return getVarValueAtPosition(x, pos);
    }
    
    //returns true if x is contained in y
    private boolean isXContainedInY (int x, int y) {
        if((x|y) == y) {return true;}
        return false;
    }
    
    private int binaryToElement(String s) {
        int element = 0xFFFFFFFF; //creates 32-bit int with all 1'
        char temp[] = s.toCharArray();
        for (int j=0; j<temp.length; j++) {
            int position = (14 - j);
            if (temp[j] == '0') {
                element = setNegativeValue(element, position);
            }
            else if (temp[j] == '1') {
                element = setPositiveValue(element, position);                  
            }
        }
        return element;
    }
    
    public void setEquation(String equation){
        iEquation = equation;  
        minterm = iEquation.split("\\+");
       
        for(int i=0; i<minterm.length; i++) {
            onSet.add(mintermToInt(minterm[i]));
        }
    }
    
    private int mintermToInt(String minterm) {
        int element = 0xFFFFFFFF;
        char temp[] = minterm.toCharArray(); //separates the characters
        for(int j=0; j<temp.length; j++) {
            if(temp[j] >= 'A' && temp[j] <= 'O'){
                setPositiveValue(element, temp[j]);
            }
            if(temp[j] >= 'a' && temp[j] <= 'o'){
                setNegativeValue(element, temp[j]);
            }
         }
         return element;
    }
    
    private String kernelToString(Integer element) {
        String strVal = "";
        for (int position=14; position >=0; position--) {
            if(positionToVar(element, position) != 0) {
                strVal = strVal + positionToVar(element, position);
            }
        }
        return strVal;
    }        
        
    public void printEquation() {
        for (int i=0; i<minterm.length; i++) {
            System.out.println(minterm[i]);
        }
    }
    
    public void printMap() {
        System.out.println(" This is the on-set. " + onSet.size() + " elements." );
        for (int i=0; i<onSet.size(); i++) {
            System.out.print(kernelToString(onSet.get(i)));
        }
        System.out.println();
        System.out.println(" This is the minTerms set. "+ minTerms.size() + " elements." );
        for (int i=0; i<minTerms.size(); i++) {
            System.out.print(kernelToString(minTerms.get(i)) + " ");
        }
        System.out.println();
        System.out.println(" This is the off-set. "+ offSet.size() + " elements." );
        
        for (int i=0; i<offSet.size(); i++) {
            System.out.print(kernelToString(offSet.get(i)) + " ");
        }
        System.out.println(); 
        System.out.println(" This is the dont Care set. "+ dontCareSet.size() + " elements." );
        for (int i=0; i<dontCareSet.size(); i++) {
            System.out.print(kernelToString(dontCareSet.get(i)) + " ");
        }
        System.out.println();
   
        System.out.println(" This is the kernel set. "+ kernel.size() + " elements." );
        for (int i=0; i<kernel.size(); i++) {
            System.out.print(kernelToString(kernel.get(i)) + " ");
        }
        System.out.println();
    }
    
    public void setKMap(int numVars, int kValues[]) {
        dimension = numVars*numVars;
        for(int i=0; i<dimension; i++) {
            if(kValues[i] == 0) {
                offSet.add(mintermToInt(kMap4[i])); //creates offSet array
            }
            if(kValues[i] == 1) {
                onSet.add(mintermToInt(kMap4[i])); //creates onSet array
            }
        }
    }
    
    public void setPLA (int numVars, List<String> stmts) {

        // Add elements to OnSet
        dimension= (int) Math.pow(2, numVars);
        for (int i=0; i<stmts.size(); i++) {
            int element = binaryToElement(stmts.get(i));
            onSet.add(element);
        }

        //Add elements to offSet[], if not covered by onSet
        int totVars = (int) Math.pow(2, numVars);
        for (int i=0; i< totVars; i++) {
            int x = i | (int) totVars;
            String s = Integer.toBinaryString(x).substring(1);
            int element = binaryToElement(s);
            
            // Now check if this value is not in OnSet
            boolean found = false;
            for (int j=0; j<onSet.size(); j++) {
                if (isXContainedInY(element, onSet.get(j))) {
                    found = true;
                }
            }
            if (found) {
                minTerms.add(element);
            } else {
                offSet.add(element);
            }
        }
        //printMap();
    }
        
    private int kernelLength (Integer k){
        int val = k.intValue();
        int size = 0;
        //count zeros
        for (int i=0; i<32; i++) {
            if (((val >>> i) & 0x1) == 0) {
                size++;
            }
        }
        return size;
    }
    
    private boolean isPositiveKernel (Integer k){        
        // Bitwise OR bytes with 10101010 (0xAA)
        // this changes positive variables (01) DBits to 11
        Integer temp = k | 0xAAAAAAAA;
        if (temp == 0xFFFFFFFF) {
            //System.out.println("Kernel "+ kernelToString(k) + " is positive.");
            return true;
        }
        //System.out.println("Kernel " + kernelToString(k) + " is NOT positive.");
        return false;
    }
    
    private boolean canCombine (Integer x, Integer y){ 
        // only the implicants of same size can be combined
        if (kernelLength(x) != kernelLength(y)) {
            return false;
        }
        
        int val = x.intValue() ^ y.intValue();
        // there must be exactly a single '11' for these to be combinable

        int index;
        //find a 11 pattern
        for (index=0; index<16; index++) {
            if ((val & 0x3) == 0x3) {
                break;
            }
            val = val >>> 2;
        }
        
        if (index < 16) {
            int mask = 0x3 << 2*index;
            if ((x.intValue() | mask) == (y.intValue() | mask)) {
                return true;
            }
        }    
        return false;        
    }
    
   
  
    private List<Integer> mergeImplicants(List<Integer> implicants) {
        List<Integer> combined = new ArrayList<>();
        boolean tryMore = false;
                      
        for(int i=0; i<implicants.size(); i++) {
            Integer temp1 = implicants.get(i); 
            
            // Combine with other elements in onSet
            for(int j=0; j<implicants.size(); j++) {
                if (i == j) {continue;}
                
                Integer temp2 = implicants.get(j);
               
                if (canCombine(temp1, temp2)) {
                    // These can be combined, add it to list
                    int temp3 = temp1 | temp2;
                    /* System.out.println("Combining " + kernelToString(temp1) +
                            " with " + kernelToString(temp2) + " = " +
                            kernelToString(temp3));*/
                    if (!combined.contains(temp3)) {
                        combined.add(temp3);
                    }
                    tryMore = true;
                }
            }
        }
        
        // now add remaining implicants from input set if not covered by Combined
        
        //System.out.println("Merged Implicants count: " + combined.size());
        for(int i=0; i<implicants.size(); i++) {
            Integer temp1 = implicants.get(i); 
            
            boolean covered = false;
            for(int j=0; j<combined.size(); j++) {
                Integer temp2 = combined.get(j);
                if (isXContainedInY(temp1, temp2)) {
                    covered = true;
                    break;
                }
            }
            if (!covered) {
                if (!combined.contains(temp1)) {combined.add(temp1);}
            }
        }
        //System.out.println("Merged+Extra Implicants count: " + combined.size());        
        if (tryMore) {
            // Did some merging at this level, Try next level
            return mergeImplicants(combined);
        } else {
            return combined;
        }
    }
    
    
    public boolean validKernel(int kern) { //is one kernel valid or not? return true or false
        for(int i=0; i<offSet.size(); i++) {
            if((offSet.get(i) | kern) == kern) {
                //System.out.println("not valid kernel");
                return false; 
            }
        }
        //System.out.println("valid kernel");
        return true;
    }
    
    private ArrayList<ArrayList<Integer>> coveringTable (List<Integer> minterms, List<Integer> implicants) {
        ArrayList<ArrayList<Integer>> terms = new ArrayList<>();
        
        for (int i=0; i<minterms.size(); i++) {
            ArrayList<Integer> term = new ArrayList<>();
            for (int j=0; j<implicants.size(); j++) {
                if (isXContainedInY(minterms.get(i), implicants.get(j))) {
                    term.add(j+1); //add the index of the implicant to term 
                }
            }
            terms.add(term);
        }
        return terms;
    }
    
    public void evaluateCircuit(boolean batchMode) {        
        List<Integer> implicants = mergeImplicants(minTerms);
        //printMap();
        
        ArrayList<ArrayList<Integer>> termsList = coveringTable(minTerms, implicants);
        // System.out.print("(" + minTerms.size() + "," + 
        //        implicants.size() + "," + termsList.size() + ") ");
        
        DecisionFnMethod3 f = new DecisionFnMethod3();
        ArrayList<ArrayList<Integer>> solutions = f.evaluate(termsList);
        
        
        DiagramSOP diag = new DiagramSOP((int) Math.sqrt(dimension), batchMode);
        
        if (solutions.isEmpty()) {
            System.out.println("Decision Function failure. No Solution");
            return;
        }
        
        for (int index: solutions.get(0)) {
            // find correct implicant in kernel list
            diag.addAND (kernelToString(implicants.get(index-1)));
            diag.addOR();
        }      
        
        //diag.print();
        totalPulses = diag.getTotalPulses();
    }
    
    public int getTotalPulses() { 
        return totalPulses; 
    }
}
