
package MemristorsSOP;

import java.awt.BorderLayout;
import java.util.*;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/**
 *
 * @author Anika Raghuvanshi
 */
public class DiagramSOP {
    private List<String> steps = new ArrayList<>();
    private String equation = "";
    
    CircuitPanelSOP p;
    boolean batchMode;
    int pulseCount = 0;
    
    public  DiagramSOP (int numVars, boolean batchMode) {
        this.batchMode = batchMode;
        
        if (!batchMode) {
            JFrame j = new JFrame("MemristorCircuit");

            p = new CircuitPanelSOP();
            p.setVariables(numVars);
            
            JTextArea t1 = new JTextArea(1, 10000);
            p.add(t1);
            JTextArea t2 = new JTextArea(1000, 1);
            p.add(t2);

            JScrollPane s = new JScrollPane(p);

            j.add(s, BorderLayout.CENTER);
            
            //j.add(p);
            j.setSize(p.getHorizontalSize()+100,700);
            j.setVisible(true);
            
            //this.add(scrollFrame);

            
        }
    }
   
    public void addAND(String vars) {
        equation += vars;
        pulseCount += vars.length()+1;
        if (!batchMode) {p.addAND(vars);}
    }
     
    public void addOR() {
        equation += "+";
        pulseCount++;
        if (!batchMode) {p.addOR();}   
    }
    
    public void print(){
        System.out.println(equation);
    }
    
    public int getTotalPulses() {
        return pulseCount;
    }
}
