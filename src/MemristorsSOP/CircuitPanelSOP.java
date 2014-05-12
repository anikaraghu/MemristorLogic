
package MemristorsSOP;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;

/**
 *
 * @author Anika Raghuvanshi
 */
public class CircuitPanelSOP extends JPanel{
    
    private int x_start = 50;
    private int x_interval = 10;
    private int x_max = 900;  // make this dynamic, get from window params
    private int y_start = 50;
    private int y_interval = 10;
    private int y_max = 500; // make this dynamic, get from window params
    private int totalPulses = 0;
    private int zeroPulses = 0;
    
    private int y_sumLine;
    private int y_productLine;
    
    private List<String> drawItems = new ArrayList<>();
    String allVariables = "";
    
    public void setVariables(int num) {
        for (int i=0; i<num; i++) {
            allVariables += (char)('A'+i);
        }
    }
    
    public void addAND (String varnames) {
        drawItems.add(varnames);
        totalPulses += (varnames.length() + 1);
    }
    
    public void addOR () {
        drawItems.add("+");
        totalPulses += 1;  
    }
    
    public int getTotalPulses() {
        return totalPulses;
    }
    
    public int getHorizontalSize() {
        return x_max;
    }
        
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        this.setBackground(Color.WHITE);       
        Graphics2D g2d = (Graphics2D)g;
        
        drawVariables(g, allVariables);
      
        int step = 1;
        if (drawItems.size() > 0) {
            int verticalLines = 0;
            for (int i =0; i <drawItems.size(); i++) {
                verticalLines += drawItems.get(i).length();
            }
            x_interval = (x_max-x_start-200)/verticalLines;
            if (x_interval<20) {
                x_interval = 20;
                x_max = x_start + x_interval * verticalLines + 200;
            }
        }
        for (int i=0; i<drawItems.size(); i++) {
            if (drawItems.get(i).equals("+")) {
                drawSumLine(g, step++);
            }
            else {
                drawProductLine(g, step, drawItems.get(i));
                step = step + drawItems.get(i).length();
            }
        }
        
        printPulses(g);        
        printExpression(g);                        
    }
    

    private void drawVariables(Graphics g, String varnames) {
        int y1_center;
        int y2_center;
        int x_center;
        
        y_interval = (y_max-y_start-200)/varnames.length();
        if (y_interval < 40) {
            y_interval = 40;
            y_max = y_start + y_interval*varnames.length() + 100;
        }
        zeroPulses = 1;
        
        for (int i=0; i<varnames.length(); i++) {
            
            int index = varnames.charAt(i) - 'A';

            // Draw horizontal line denoting positive input variable
            y1_center = y_start + y_interval * index;
            g.drawChars(varnames.toCharArray(), i, 1, x_start, y1_center+5);
            g.drawLine(x_start + 50, y1_center, x_max, y1_center);
            
            // Draw second horizontal line denoting the negated input variable 
            y2_center = y1_center + y_interval/3;
            g.drawString("0", x_start+40, y2_center+5);
            //g.setColor(Color.LIGHT_GRAY);
            g.drawLine(x_start + 50, y2_center, x_max, y2_center); 
            //g.setColor(Color.BLACK);
         
            // Draw Imply Gate on second line, 
            // and connect from first line to show NOT
            x_center = x_start + 100;
            connectAndDrawImplyGate(g, x_center, y1_center, x_center, y2_center);
            
        }
        
        // Draw two lines for two working memristors (product line and sum line)
        y_sumLine = y_max;
        y_productLine = y_sumLine - 50;
       
        g.drawString("0", x_start, y_productLine+5);
        //g.setColor(Color.GREEN);
        g.drawLine(x_start + 50, y_productLine, x_max, y_productLine);        
        //g.setColor(Color.BLACK);
        
        g.drawString("0", x_start, y_sumLine+5);
        //g.setColor(Color.RED);
        g.drawLine(x_start + 50, y_sumLine, x_max, y_sumLine);
        //g.setColor(Color.BLACK);

    }
    
    private void drawProductLine(Graphics g, int step, String varnames) {
        int x_center = x_start + 100 + (x_interval * step);
        //g.drawLine(x_center, y_start-10, x_center, y_max);
        
        if (step > 1) {
            g.drawString("0", x_center-15, y_productLine-2);
            zeroPulses++;
        }

        for (int i=0; i<varnames.length(); i++) {
            char letter = varnames.charAt(i);
            int index;
            int y_center;
            
            if (letter >= 'A' && letter <= 'Z') {
                index = letter - 'A';
                y_center = y_start + y_interval * index;
            }        
            else if (letter >= 'a' && letter <= 'z') {
                index = letter - 'a';
                y_center = y_start + y_interval * index + y_interval/3;
            }
            else {
                return;
            }
                        
            connectAndDrawImplyGate(g, x_center, y_center, x_center, y_productLine);
            
            x_center = x_center + x_interval;
        }
    }
    
    private void drawSumLine(Graphics g, int step) {
        int x_center = x_start + 100 + (x_interval * step);
               
        connectAndDrawImplyGate(g, x_center, y_productLine, x_center, y_sumLine );

        g.drawString(Integer.toString(step+zeroPulses), x_center, y_sumLine+30);      
    }

    private void connectAndDrawImplyGate(Graphics g, int srcX, int srcY, 
                                                     int destX, int destY) {
        g.drawLine(srcX, srcY, destX, destY);
        g.fillOval(srcX-2, srcY-2, 4, 4);
        drawImplyGate(g, destX, destY);
    }
    
    private void drawImplyGate(Graphics g, int x, int y) {          
        g.setColor(Color.WHITE);
        g.fillRect(x-8, y-8, 16, 16);
        g.setColor(Color.BLACK);        
        g.drawRect(x-8, y-8, 16, 16);

        g.drawOval(x-4, y-16, 8, 8);
        g.fillOval(x-4, y-16, 8, 8);           
    }

    private void printPulses(Graphics g) {
        g.drawString("Total pulses: "+
                Integer.toString(totalPulses), x_start, y_max+40);
    }
    
    private void printExpression(Graphics g) {
        String s = "";
        for (int i=0; i<(drawItems.size()-1); i++) {
            //s = s + drawItems.get(i) + " ";
            String varnames = drawItems.get(i);
            for (int j=0; j<varnames.length(); j++) {
                char letter = varnames.charAt(j);
                
                if (letter >= 'a' && letter <= 'z') {
                    s = s + Character.toUpperCase(letter) + "\u0305";
                }
                else {
                    s = s + letter;
                }
            }
        }
        g.drawString("Expression: "+ s, x_start, y_max+100);
    }

}