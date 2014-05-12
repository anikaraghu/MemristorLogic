
package DecisionFunction;


import java.io.IOException;
import java.util.*;

/**
 *
 * @author Anika Raghuvanshi
 */
public class DecisionFunction {

    public static void main(String[] args) throws IOException {
        DecisionFunction f = new DecisionFunction();
        if(args.length > 0) {
            f.run(args[0], args[1]);
        } 
        else {
            //f.run("", "(A)(A+E)(B)(B+E)(C)(C+E)(D)(D+E)");
            f.run("", "(A+B)(C)");
        }
    }
    
    public void run(String method, String s) throws IOException {
        Scanner reader = new Scanner(System.in);
        String[] terms;
        DecisionFunctionMethod f = new DecisionFnMethod3(); //default
        if(method.equals("Method1String")) {f = new DecisionFnMethod1String();}
        if(method.equals("Method1")) {f = new DecisionFnMethod1();}
        if(method.equals("Method2String")) {f = new DecisionFnMethod2String();}
        if(method.equals("Method2")) {f = new DecisionFnMethod2();}
        if(method.equals("Method3String")) {f = new DecisionFnMethod3String();}
        if(method.equals("Method3")) {f = new DecisionFnMethod3();}
        
        System.out.println("************************");
        System.out.println("Expression: "+s);
        s = s.replaceAll("\\(", "");
        s = s.replaceAll(" ", "");
        s = s.replaceAll("\\+", "");
        terms = s.split("\\)"); //find all terms
        ArrayList<String> solutions = f.evaluate(terms); //see DecisionFnMethod classes
        System.out.print("Optimal solutions (" + solutions.size() + ") : ");
        System.out.println(solutions);
        System.out.println();
    }    
}
