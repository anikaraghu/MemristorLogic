
package MemristorsWM2;

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
public class CircuitPanelISD extends JPanel{
    
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
    
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        this.setBackground(Color.WHITE);       
        Graphics2D g2d = (Graphics2D)g;
        
        drawVariables(g, allVariables);
        
        int step = 1;
        if (drawItems.size() > 0) {
            int verticalLines = 0;
            for (int i =0; i <drawItems.size(); i++) {
                if (drawItems.get(i).equals("!")) {
                    verticalLines++;
                } else {               
                    verticalLines += drawItems.get(i).length()+1;
                }
            }
            x_interval = (x_max-x_start-200)/verticalLines;
            if (x_interval<20) {
                x_interval = 20;
                x_max = x_start + x_interval * verticalLines + 200;
            }
        }
        for (int i = (drawItems.size()-1); i >= 0;  i--) {
            if (drawItems.get(i).equals("!")) {
                drawNOT(g, step++);
            }
            else {
                drawNAND(g, step, drawItems.get(i));
                step = step + drawItems.get(i).length() + 1;    
            }
            drawPulses(g, step-1);
        }
        
        g.drawString("f ()", x_start + 100 + (x_interval * step), y_sumLine-2);
        
        printPulses(g);
        /*System.out.println(x_start + "," + x_interval + "," + x_max + "," + 
                y_start + "," + y_interval + "," + y_max);*/
                        
    }
    
    public void setVariables(int num) {
        for (int i=0; i<num; i++) {
            allVariables += (char)('A'+i);
        }
    }
    
    public void addNAND (String varnames) {
        drawItems.add(varnames);
        totalPulses += (varnames.length()+2);
    }
    
    public void addNOT () {
        drawItems.add("!");
        totalPulses += 2;  
    }

    
    private void drawVariables(Graphics g, String varnames) {
        int y1_center;
        int y2_center;
        int x_center;
        
        y_interval = (y_max-y_start-200)/varnames.length();
        
        
        for (int i=0; i<varnames.length(); i++) {
            
            int index = varnames.charAt(i) - 'A';

            // Draw horizontal line denoting positive input variable
            y1_center = y_start + y_interval * index;
            g.drawChars(varnames.toCharArray(), i, 1, x_start, y1_center+5);
            g.drawLine(x_start + 50, y1_center, x_max, y1_center);
           
        }
        
        // Draw two lines for two working memristors (product line and sum line)
        y_sumLine = y_max;
        y_productLine = y_sumLine - 50;
       
        g.drawString("WM1", x_start, y_productLine+5);
        //g.setColor(Color.GREEN);
        g.drawLine(x_start + 50, y_productLine, x_max, y_productLine);        
        //g.setColor(Color.BLACK);
        g.drawString("0", x_start + 55, y_productLine-2);
        
        g.drawString("WM2", x_start, y_sumLine+5);
        //g.setColor(Color.RED);
        g.drawLine(x_start + 50, y_sumLine, x_max, y_sumLine);
        //g.setColor(Color.BLACK);
        g.drawString("0", x_start + 55, y_sumLine-2);
        
        zeroPulses = 1;
    }
    
    private void drawNOT(Graphics g, int step) {
        int x_center = x_start + 100 + (x_interval * step);
                
        // zero out the product line, we will reuse this, and switch WMs
        g.drawString("0", x_center-15, y_productLine-2);
        zeroPulses++;
            
        // Draw an imply gate from sum Line to Product Line
        connectAndDrawImplyGate(g, x_center, y_sumLine, x_center, y_productLine);
        x_center = x_center + x_interval;
        
        // Now switch productLine and sumLine
        int tmp = y_sumLine;
        y_sumLine = y_productLine;
        y_productLine = tmp;
    }
    
    private void drawNAND(Graphics g, int step, String varnames) {
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
        
        connectAndDrawImplyGate(g, x_center, y_productLine, x_center, y_sumLine);
        x_center = x_center + x_interval;
        

    }
    
    private void drawPulses(Graphics g, int step) {
        int x_center = x_start + 100 + (x_interval * step);

        g.drawString(Integer.toString(step + zeroPulses), x_center, y_max+30);      
    }

    private void connectAndDrawImplyGate(Graphics g, int srcX, int srcY, int destX, int destY) {
        g.drawLine(srcX, srcY, destX, destY);
        g.fillOval(srcX-2, srcY-2, 4, 4);
        if (destY > srcY ) {
            drawImplyGateDown(g, destX, destY);
        }
        else {
            drawImplyGateUp(g, destX, destY);
        }
    }
    
    private void drawImplyGateDown(Graphics g, int x, int y) {          
        g.setColor(Color.WHITE);
        g.fillRect(x-8, y-8, 16, 16);
        g.setColor(Color.BLACK);        
        g.drawRect(x-8, y-8, 16, 16);

        g.drawOval(x-4, y-16, 8, 8);
        g.fillOval(x-4, y-16, 8, 8);           
    }
    
    private void drawImplyGateUp(Graphics g, int x, int y) {          
        g.setColor(Color.WHITE);
        g.fillRect(x-8, y-8, 16, 16);
        g.setColor(Color.BLACK);        
        g.drawRect(x-8, y-8, 16, 16);

        g.drawOval(x-4, y+8, 8, 8);
        g.fillOval(x-4, y+8, 8, 8);           
    }
    
    public void printPulses(Graphics g) {
        g.drawString("Total pulses: "+Integer.toString(totalPulses), x_start, y_max+50);
    }
    
    public int getTotalPulses() {
        return totalPulses;
    }
}

