package SOPCount;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 *
 * @author Anika Raghuvanshi
 */
public class SOPCount {
    
     public static void main(String[] args) throws IOException {
        Scanner reader = new Scanner(System.in);        
        
         if (args.length > 0) {
            int pulses = readSOPFile(args[0]);
            //System.out.println("Number of pulses for " + args[0] + " = " + pulses);
            System.out.println(args[0] + "," + pulses);
            return;
        }
        String equation;
        while(true) {
            System.out.print("Enter Filename (*.pla): ");
            equation = reader.nextLine();
            if (equation.endsWith(".pla")) {
                int pulses = readSOPFile(equation);
                System.out.println("Number of pulses for " + equation + " = " + pulses);
                //break;
            }
            else {System.out.println("Enter filename ending with *.pla");}
        }
     }
     
     public static int readSOPFile(String fname) throws IOException {
         Scanner fReader = new Scanner(new File(fname));
         int numVars = 0;
         ArrayList<String> stmts = new ArrayList<>();
         
         while (fReader.hasNext()) {
            String line = fReader.nextLine();
            if(line.startsWith("#") || line.startsWith(".o") || 
                    line.startsWith(".p") || line.startsWith(".type")) {
                continue;
            }
            else if (line.startsWith(".i")) {
                numVars = Integer.parseInt(line.substring(3));           
            }
            else if(line.startsWith(".e")) {
                break;
            }
            else {stmts.add(line.substring(0, numVars));}
            
         }
         return countPulses(stmts);
     }
         
     public static int countPulses(ArrayList<String> stmts) {
         int pulses = 0;
         int numORs = stmts.size();
         pulses += 2 * numORs; 
         for(int i=0; i<stmts.size(); i++) {
             char[] charArray = stmts.get(i).toCharArray();
             for(int j=0; j<charArray.length; j++) {
                if(charArray[j] == '0') {pulses += 3;}
                if(charArray[j] == '1') {pulses += 1;}             
             }
         }
         return pulses;
     }    
}
