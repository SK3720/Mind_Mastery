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

    <-------June 4------->
      > finished agenda planning task
      Contributor: Caleb Chue

    <-------June 5------->
      > finished math homework task
      Contributor: Caleb Chue

    <-------June 6------->
      > started on sweeping task
      Contributor: Caleb Chue

*/ 

public class Task extends Hitbox {
    
    int type;
    JPanel content;
    JButton[] buttons;
    JLabel[] texts;
    int[] vars;
    int[][] floorGrid;
    JFrame frame;
    JButton checkButton;
    boolean complete;
    Drawing draw;
    WindowAdapter window;
    
    final int FRAME_WIDTH = 700, FRAME_HEIGHT = 500;
    
    final int boxW = 200, boxH = 50;
    
    final int BRUSH_SIZE = 5;
    
    public Task(int x, int y, int wdt, int hgt, int typ, WindowAdapter wind) {
        super(x,y,wdt,hgt);
        type = typ;
        content = new JPanel();
        complete = false;
        window = wind;
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
        if (type == 0) {
            /**
             drag and drop system
             (source: https://stackoverflow.com/questions/28844574/drag-and-drop-from-jbutton-to-jcomponent-in-java) 
             */
            frame = new JFrame("Ordering Tasks");
            
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
                c.insets = new Insets(i == 2 ? boxH : i > 0 ? boxH/3 : 0, 0, 0, 0);
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
            
        } else if (type == 1) {
            /**
             drag and drop system
             (source: https://stackoverflow.com/questions/28844574/drag-and-drop-from-jbutton-to-jcomponent-in-java) 
             */
            
            frame = new JFrame("Completing Math Homework");
        
            content = new JPanel();
            
            buttons = new JButton[5];
            JPanel leftPanel = new JPanel();
            leftPanel.setLayout(new GridBagLayout());
            JPanel topPanel = new JPanel();
            topPanel.setLayout(new GridBagLayout());
            GridBagConstraints c = new GridBagConstraints();
            c.gridx = 1;
            c.weightx = 0.5;
            c.fill = GridBagConstraints.HORIZONTAL;
            
            vars = new int[] {rand(30) + 15, rand(20) + 10, rand(2) - 1, rand(20) + 10, rand(10) + 3, rand(2) - 1, rand(4) + 3, rand(5) + 2, rand(3) - 1};
            
            JLabel title = new JLabel("Math Homework");
            title.setFont(new Font("Arial", Font.BOLD, 24));
            c.insets = new Insets(10, 300, 0, 0);
            topPanel.add(title);
            String[] labels = {String.format("1. %d + %d = %d", vars[0], vars[1], vars[0] + vars[1] + vars[2]), 
                               String.format("2. %d - %d = %d", vars[3], vars[4], vars[3] - vars[4] + vars[5]), 
                               String.format("3. %d x %d = %d", vars[6], vars[7], vars[6] * (vars[7] + vars[8])),
                               "True", "False"};
            for (int i = 0; i < labels.length; i++) {
                if (i >= 3) {
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
                } else {
                    c.insets = new Insets(0, 0, 0, 0);
                    c.gridx = i*2;
                    c.gridy = 1;
                    JLabel text = new JLabel(labels[i]);
                    text.setPreferredSize(new Dimension(boxW, boxH));
                    text.setMaximumSize(new Dimension(boxW, boxH));
                    
                    topPanel.add(text, c);
                }
            }
            
            texts = new JLabel[3];
            JPanel rightPanel = new JPanel();
            rightPanel.setLayout(new GridBagLayout());
            for (int i = 0; i < texts.length; i++) {
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
            
            content.add(topPanel, BorderLayout.PAGE_START);
            content.add(leftPanel, BorderLayout.LINE_START);
            content.add(filler, BorderLayout.CENTER);
            content.add(rightPanel, BorderLayout.LINE_END);
            content.add(checkButton, BorderLayout.PAGE_END);
            
        } else if (type == 2) {
            frame = new JFrame("Sweeping the Floor");
            content = new JPanel();

            draw = new Drawing();
            draw.setFocusable(true);
            MouseHandler m = new MouseHandler();
            draw.addMouseListener(m);
            draw.addMouseMotionListener(m);
            draw.setPreferredSize(new Dimension(FRAME_WIDTH, FRAME_HEIGHT));
            
            floorGrid = new int[50][35];
            for (int row = 0; row < floorGrid.length; row++) {
                for (int col = 0; col < floorGrid[row].length; col++) {
                    floorGrid[row][col] = 1+rand(3);
                }
            }
            
            content.add(draw);
            draw.setVisible(true);
            draw.repaint();

            frame.setContentPane(content);
        }

        frame.addWindowListener(window);
        frame.setSize(FRAME_WIDTH, FRAME_HEIGHT);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        
    }
    
    class Drawing extends JComponent {
        public void paint(Graphics g) {
            g.setColor(new Color(156, 90, 25));
            g.fillRect(0,0,700,500);
            
            if (type == 2) {
                for (int row = 0; row < floorGrid.length; row++) {
                    for (int col = 0; col < floorGrid[row].length; col++) {
                        g.setColor(new Color(223, 223, 223, 63*floorGrid[row][col]));
                        g.fillOval(100 + 10*row, 50 + 10*col, 7+rand(9), 10+rand(5));
                    }
                }
            }
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
        } else if (type == 1) {
            if (texts[0].getText().equals("True") == (vars[0] + vars[1] == vars[0] + vars[1] + vars[2])
             && texts[1].getText().equals("True") == (vars[3] - vars[4] == vars[3] - vars[4] + vars[5])
             && texts[2].getText().equals("True") == (vars[6] * vars[7] == vars[6] * (vars[7] + vars[8]))) {
                close();
                return;
             }
        } else if (type == 2) {
            for (int[] row : floorGrid) {
                for (int col : row) {
                    if (col > 0) return;
                }
            }
            
            try{Thread.sleep(400);}catch(Exception e){}
            close();
            return;
        }
        System.out.println("check failed");
    }
    
    
    /** 
     Private method to dispose of the frame once the task is closed
     */ 
    private void close() {
        complete = true;
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
            if (type == 0 || type == 1) {
                for (int i = 0; i < buttons.length; i++) {
                    if (e.getSource() == buttons[i]) {
                        TransferHandler handle = buttons[i].getTransferHandler();
                        handle.exportAsDrag(buttons[i], e, TransferHandler.COPY);
                    }
                }
            } else if (type == 2) {
                int tempX = e.getX();
                int tempY = e.getY();
                System.out.println(tempX + " " + tempY);
                tempX = (tempX - 100) / 10;
                tempY = (tempY - 50) / 10;
                int locX, locY;
                
                for (int i = -BRUSH_SIZE/2; i <= BRUSH_SIZE/2; i++) {
                    for (int j = -BRUSH_SIZE/2; j <= BRUSH_SIZE/2; j++) {
                        locX = tempX + i;
                        locY = tempY + j;
                        if (locX < 0 || locY < 0 || locX >= floorGrid.length || locY >= floorGrid[0].length) continue;
                        if (floorGrid[locX][locY] > 0) floorGrid[locX][locY] -= 1;
                    }
                }
                checkComplete();
            }
            draw.repaint();
        }
        public void mouseMoved(MouseEvent e) {}
        
        public void mousePressed(MouseEvent e) {
        
        }
        public void mouseReleased(MouseEvent e) {
            
        }
        public void mouseClicked(MouseEvent e) {
            System.out.println("click");
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
        if (complete) return "";
        load();
        return "";
    }
    public String proximityMessage() {
        if (complete) return "";
        if (type == 0) return "Open Agenda Planner";
        else if (type == 1) return "Do Math Homework";
        
        else return "Unnamed Task";
    }
    
    private int rand(int lim) {
        return (int)(Math.random()*lim);
    }
    
    public boolean isComplete() {
        return complete;
    }
    
    public JFrame getFrame() {
        return frame;
    }
    
    public static void main(String[] args) {
        Task t = new Task(0,0,0,0,2,new WindowAdapter() {
            @Override 
            public void windowClosing(WindowEvent e) {
                System.out.println("Print thing");
            }
        });
        t.interactedBehaviour();
    }
}