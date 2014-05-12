
package MemristorsSOP;
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
public class CircuitSOP {
    private String iEquation;
    private String minterm[];
    private List<Integer> onSet = new ArrayList<>();
    private List<Integer> offSet = new ArrayList<>();    
    private String kMap4[] = {"abcd","abcD","abCD","abCd",
                              "aBcd","aBcD","aBCD","aBCd",
                              "ABcd","ABcD","ABCD","ABCd",
                              "Abcd","AbcD","AbCD","AbCd"};
    private int dimension = 36;
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
                element = setPositiveValue(element, temp[j]);
            }
            if(temp[j] >= 'a' && temp[j] <= 'o'){
                element = setNegativeValue(element, temp[j]);
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
        System.out.print("OnSet " + onSet.size() + " elements : " );
        for (int i=0; i<onSet.size(); i++) {
            System.out.print(kernelToString(onSet.get(i)) + " ");
        }
        System.out.println();
    }
    
    public void setKMap(int numVars, int kValues[]) {
        dimension = numVars*numVars;
        for(int i=0; i<dimension; i++) {
            if(kValues[i] == 1) {
                onSet.add(mintermToInt(kMap4[i])); //creates onSet array
            }
        }
    }
    
    public void setPLA (int numVars, List<String> stmts) {
       // Add elements to OnSet
        dimension= numVars*numVars;
        for (int i=0; i<stmts.size(); i++) {
            int element = binaryToElement(stmts.get(i));
            onSet.add(element);
        }        
    }
        

    public void evaluateCircuit(boolean batchMode) {
        DiagramSOP diag = new DiagramSOP((int) Math.sqrt(dimension), batchMode);
        
        for (int i=0; i<onSet.size(); i++) {
            diag.addAND (kernelToString(onSet.get(i)));
            diag.addOR();
        }      
        
        //diag.print();
        totalPulses = diag.getTotalPulses();
    }
    
    public int getTotalPulses() { 
        return totalPulses; 
    }
    
}