import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import javax.swing.*;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.*;

/** 
Class to handle the hitboxes of tasks

    <-------May 29------->
      > separated Obstacle class into separate child classes of Hitbox
      Contributor: Caleb Chue
      
    <-------May 30------->
      > worked on loading new Task panel
      Contributor: Caleb Chue

    <-------May 31------->
      > 
      Contributor: Caleb Chue


*/ 

public class Task extends Hitbox {
    
    int type;
    JPanel content;
    JButton[] buttons;
    JTextField[] texts;
    
    public Task(int x, int y, int wdt, int hgt, int typ) {
        super(x,y,wdt,hgt);
        type = typ;
        content = new JPanel();
    }
    
    
    private void load() {
        if (type == 0) {
            texts = new JTextField[8];
            JPanel leftText = new JPanel();
            leftText.setLayout(new BoxLayout(leftText, BoxLayout.PAGE_AXIS));
            
            String[] labels = {"Doing Homework", "Playing Video Games", "Doing Laundry", "Checking Social Media"};
            for (int i = 0; i < labels.length; i++) {
                texts[i] = new JTextField(labels[i]);
                MouseHandler m = new MouseHandler();
                texts[i].addMouseListener(m);
                texts[i].addMouseMotionListener(m);
            }
        }
    }
    
    
    
    class MouseHandler implements MouseMotionListener, MouseListener {
    
        public void mouseDragged(MouseEvent e) {
            
        }
        
        public void mouseMoved(MouseEvent e) {}
        
        public void mousePressed(MouseEvent e) {
        
        }
        public void mouseReleased(MouseEvent e) {
            
        }
        public void mouseClicked(MouseEvent e) {}
        public void mouseEntered(MouseEvent e) {}
        public void mouseExited(MouseEvent e) {}
    }


    public void interactedBehaviour() {
        if (type == 0) { // drag and drop minigame
            
            
            
        }
    }
    
    public void proximityBehaviour() {
        
    }
    
    
    public JPanel getPanel() {
        return content;
    }
    
    public static void main(String[] args) {
        JFrame frame = new JFrame("test");
    
        Task t = new Task(100, 100, 200, 300, 0);
        t.interactedBehaviour();
        
        frame.add(t.getPanel());
        frame.setSize(1000, 700);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}