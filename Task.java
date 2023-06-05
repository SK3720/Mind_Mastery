import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DnDConstants;import java.awt.event.*;
import java.awt.image.BufferedImage;
import javax.swing.*;
import javax.swing.border.*;
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

    <-------June 1------->
      > integrated drag and drop elements from Stack Overflow source
      Contributor: Caleb Chue


*/ 

public class Task extends Hitbox {
    
    int type;
    JPanel content;
    JButton[] buttons;
    JLabel[] texts;
    JFrame frame;
    JButton checkButton;
    final int boxW = 200, boxH = 50;
    
    public Task(int x, int y, int wdt, int hgt, int typ) {
        super(x,y,wdt,hgt);
        type = typ;
        content = new JPanel();
    }
    
    /** 
    Private method to load components for the given task
    
    <-------June 1------->
     > added loading drag and drop elements
     Contributor: Caleb Chue

    <-------June 2------->
     > finalized drag and drop elements
     Contributor: Caleb Chue

    */
    private void load() {
        frame = new JFrame();
        
        if (type == 0) {
            /**
             drag and drop system
             (source: https://stackoverflow.com/questions/28844574/drag-and-drop-from-jbutton-to-jcomponent-in-java) 
             */
        
            content = new JPanel();
            
            buttons = new JButton[4];
            JPanel leftPanel = new JPanel();
            leftPanel.setLayout(new GridBagLayout());
            GridBagConstraints c = new GridBagConstraints();
            c.gridx = 0;
            c.weightx = 0.5;
            c.fill = GridBagConstraints.HORIZONTAL;
            
            String[] labels = {"Doing Homework", "Playing Video Games", "Doing Laundry", "Checking Social Media"};
            for (int i = 0; i < labels.length; i++) {
                c.insets = new Insets(i > 0 ? boxH/2 : 0, 0, 0, 0);
                c.gridy = i*2;
                buttons[i] = new JButton(labels[i]);
                MouseHandler m = new MouseHandler();
                buttons[i].addMouseListener(m);
                buttons[i].addMouseMotionListener(m);
                buttons[i].setPreferredSize(new Dimension(boxW, boxH));
                buttons[i].setMaximumSize(new Dimension(boxW, boxH));
                buttons[i].setTransferHandler(new TransferExporter(labels[i]));
                
                leftPanel.add(buttons[i], c);
            }
            
            texts = new JLabel[4];
            JPanel rightPanel = new JPanel();
            rightPanel.setLayout(new GridBagLayout());
            for (int i = 0; i < labels.length; i++) {
                c.insets = new Insets(i > 0 ? boxH/3 : 0, 0, 0, 0);
                c.gridy = i*2;
                texts[i] = new JLabel("");
                MouseHandler m = new MouseHandler();
                texts[i].addMouseListener(m);
                texts[i].addMouseMotionListener(m);
                texts[i].setBorder(new CompoundBorder(new LineBorder(Color.DARK_GRAY), new EmptyBorder(boxH/2, boxW/2, boxH/2, boxW/2)));
                texts[i].setTransferHandler(new TransferImporter());
                texts[i].setMaximumSize(new Dimension(boxW, boxH));
                rightPanel.add(texts[i], c);
            }
            
            JPanel filler = new JPanel();
            Dimension fillerSize = new Dimension(100, 350);
            filler.setPreferredSize(fillerSize);
            filler.setMaximumSize(fillerSize);
            
            checkButton = new JButton("Check your answer");
            Dimension checkSize = new Dimension(250, 50);
            checkButton.setPreferredSize(checkSize);
            checkButton.setMaximumSize(checkSize);
            checkButton.addMouseListener(new MouseHandler());
            
            content.add(leftPanel, BorderLayout.LINE_START);
            content.add(filler, BorderLayout.CENTER);
            content.add(rightPanel, BorderLayout.LINE_END);
            content.add(checkButton, BorderLayout.PAGE_END);
            
            frame.add(content);
            frame.setSize(700, 500);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setVisible(true);
            
        }
    }
    
    class TransferExporter extends TransferHandler {
        private String cont;
        
        public TransferExporter(String val) {
            cont = val;
        }
    
        public String getString() {
            return cont;
        }
        
        @Override
        public int getSourceActions(JComponent c) {
            return DnDConstants.ACTION_COPY_OR_MOVE;
        }
        
        @Override
        protected Transferable createTransferable(JComponent c) {
            Transferable t = new StringSelection(getString());
            return t;
        }

        @Override
        protected void exportDone(JComponent source, Transferable data, int action) {
            super.exportDone(source, data, action);
        }
    }
    
    
    /** 
    Two classes taken from the above source
    */ 
    class TransferImporter extends TransferHandler {

        @Override
        public boolean canImport(TransferHandler.TransferSupport support) {
            return support.isDataFlavorSupported(DataFlavor.stringFlavor);
        }

        @Override
        public boolean importData(TransferHandler.TransferSupport support) {
            boolean accept = false;
            if (canImport(support)) {
                try {
                    Transferable t = support.getTransferable();
                    Object value = t.getTransferData(DataFlavor.stringFlavor);
                    if (value instanceof String) {
                        Component component = support.getComponent();
                        if (component instanceof JLabel) {
                            ((JLabel) component).setText(value.toString());
                            accept = true;
                        }
                    }
                } catch (Exception exp) {
                    exp.printStackTrace();
                }
            }
            return accept;
        }
    }
    
    
    /** 
     Private method to check the win conditions for each task
     */ 
    private void checkComplete() {
        System.out.println("check complete");
        if (type == 0) { // task sorting
            for (JLabel f : texts) {
                System.out.println(f.getText());
            }
            if (((texts[0].getText().equals("Doing Homework") && texts[1].getText().equals("Doing Laundry")) ||
                 (texts[0].getText().equals("Doing Homework") && texts[1].getText().equals("Doing Laundry")))
             && ((texts[2].getText().equals("Checking Social Media") && texts[3].getText().equals("Playing Video Games")) ||
                 (texts[3].getText().equals("Checking Social Media") && texts[2].getText().equals("Playing Video Games")))) {
                close();
                return;
            }
        }
        System.out.println("check failed");
    }
    
    
    /** 
     Private method to dispose of the frame once the task is closed
     */ 
    private void close() {
        content.setVisible(false);
        frame.dispose();
     }
    
    
    /** 
    Nested class to handle mouse events
    
    <-------June 1------->
      > updated methods
      Contributor: Caleb Chue

    */ 
    class MouseHandler implements MouseMotionListener, MouseListener {
    
        public void mouseDragged(MouseEvent e) {
            for (int i = 0; i < buttons.length; i++) {
                if (e.getSource() == buttons[i]) {
                    TransferHandler handle = buttons[i].getTransferHandler();
                    handle.exportAsDrag(buttons[i], e, TransferHandler.COPY);
                }
            }
        }
        public void mouseMoved(MouseEvent e) {}
        
        public void mousePressed(MouseEvent e) {
        
        }
        public void mouseReleased(MouseEvent e) {
            
        }
        public void mouseClicked(MouseEvent e) {
            if (e.getSource() == checkButton) {
                checkComplete();
            }
        }
        public void mouseEntered(MouseEvent e) {}
        public void mouseExited(MouseEvent e) {}
    }
    
    

    /** 
    Overridden methods to handle the specific behaviours of this class
    when interacted with and when approached by the player
    
    <-------May 29------->
      > added methods
      Contributor: Caleb Chue

    */ 
    public String interactedBehaviour() {
        load();
        return "";
    }
    public String proximityMessage() {
        if (type == 0) return "Open Agenda Planner";
        else if (type == 1) return "Do Math Homework";
        
        else return "Unnamed Task";
    }
    
    
    
    public JFrame getFrame() {
        return frame;
    }
}