
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
public class CircuitPanelRID extends JPanel{
    
    private int x_start = 50;
    private int x_interval = 10;
    private int x_max = 900;  // make this dynamic, get from window params
    private int y_start = 50;
    private int y_interval = 10;
    private int y_max = 500; // make this dynamic, get from window params
    private int totalPulses = 0;
    
    private List<String> drawItems = new ArrayList<>();
    String allVariables = "";
    
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        this.setBackground(Color.WHITE);       
        Graphics2D g2d = (Graphics2D)g;
        
        drawVariables(g, allVariables);
        
        int step = 1;
        if (drawItems.size() > 0) {
            x_interval = (x_max-x_start-100)/drawItems.size();
        }
        for (int i=0; i<drawItems.size(); i++) {
            if (drawItems.get(i).equals("NOT")) {
                drawNOT(g, step++);
            }
            else {
                drawNAND(g, step++, drawItems.get(i));
            }
        }
        
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
        drawItems.add("NOT");
        totalPulses += 2;  
    }
    
    public void drawVariables(Graphics g, String varnames) {
        int y_center = 0; 
        y_interval = (y_max-y_start-50)/varnames.length();
        for (int i=0; i<varnames.length(); i++) {
            
            int index = varnames.charAt(i) - 'A';

            y_center = y_start + y_interval * index;
            g.drawChars(varnames.toCharArray(), i, 1, x_start, y_center);
            g.drawLine(x_start + 50, y_center, x_max, y_center);
        }
        
        //y_max = y_center+100;
        
        g.drawString("0", x_start, y_max);
        g.drawLine(x_start + 50, y_max, x_max, y_max);
        
    }
    
    public void drawNAND(Graphics g, int step, String varnames) {
        int x_center = x_max - (x_interval * step);
        g.drawLine(x_center, y_start-10, x_center, y_max);
        
        for (int i=0; i<varnames.length(); i++) {
            int index = varnames.charAt(i) - 'A';
            
            int y_center = y_start + y_interval * index;
            
            g.drawOval(x_center-4, y_center-4, 8, 8);
            g.fillOval(x_center-4, y_center-4, 8, 8);
        }
        
        
        g.setColor(Color.WHITE);
        g.fillRect(x_center-10, y_max-10, 20, 20);
        g.setColor(Color.BLACK);        
        g.drawRect(x_center-10, y_max-10, 20, 20);
        
        g.drawOval(x_center-4, y_max-18, 8, 8);
        g.fillOval(x_center-4, y_max-18, 8, 8);
        
        g.drawString(Integer.toString(varnames.length()+2), x_center-4, y_start-30);
    }
    
    public void drawNOT(Graphics g, int step) {
        int x_center = x_max - (x_interval * step);
               
        int[] x = new int[]{x_center-10, x_center+10, x_center-10};
        int[] y = new int[]{y_max - 10, y_max, y_max+10};
    
        g.setColor(Color.WHITE);
        g.fillPolygon(x, y, 3);
        g.setColor(Color.BLACK); 
        
        g.drawPolygon(x, y, 3);

        g.drawOval(x_center-18, y_max-4, 8, 8);
        g.fillOval(x_center-18, y_max-4, 8, 8);  
        
        g.drawString("2", x_center-4, y_start-30);      
    }
    
    public void printPulses(Graphics g) {
        g.drawString("Total pulses: "+Integer.toString(totalPulses), x_start, y_max+40);
    }
    
    public int getTotalPulses() {
        return totalPulses;
    }
}

