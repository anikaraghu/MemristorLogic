
package MemristorsWM2;
import java.awt.BorderLayout;
import java.util.*;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/**
 *
 * @author Anika Raghuvanshi
 */
public class Diagram {
    private List<String> steps = new ArrayList<>();
    CircuitPanelISD p;
    boolean batchMode;
    int pulseCount = 0;
    
    public  Diagram (int numVars, boolean batchMode) {
        this.batchMode = batchMode;
        if (!batchMode) {
            JFrame j = new JFrame("MemristorCircuit");
            p = new CircuitPanelISD();
            p.setVariables(numVars);
            
            // Add two dummy text boxes to get horizontal/vertical scroll bars
            JTextArea t1 = new JTextArea(1, 1000); p.add(t1);
            JTextArea t2 = new JTextArea(1000, 1); p.add(t2);
            JScrollPane s = new JScrollPane(p);
            j.add(s, BorderLayout.CENTER);

            j.setSize(1000,700);
            j.setVisible(true);
        }
    }
   
    public void addNAND(String vars) {
        steps.add("Add NAND(" + vars + ") to O-input of IMPLY Gate");
        pulseCount += vars.length()+2;
        if (!batchMode) {p.addNAND(vars);}
    }
    
    public void addNOT() {
        steps.add("Add NOT Gate to open input");
        pulseCount += 2;
        if (!batchMode) {p.addNOT();}
    }
    
    public void addIMPLY() {
        steps.add("Add IMPLY Gate to open input");
    }
    
    public void print(){
        /*for (int i=0; i<steps.size(); i++) {
            System.out.println(steps.get(i));
        }
        p.repaint();
        */
        //System.out.println("Total pusles: "+p.getTotalPulses());
    }
    
    public int getTotalPulses() {
        return pulseCount;
    }
}
