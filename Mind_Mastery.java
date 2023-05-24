/** 
Authors: Focus Forge - Caleb Chue and Shiv Kanade
Teacher: V. Krasteva
Date: May 23, 2023
Description: The first version of Focus Forge's game, Mind Mastery. 
             It includes a fading splashscreen, and a few buttons on
             the main menu for user navigation. More will be implemented
             in the next version of Mind Mastery.
*/ 

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import javax.swing.*;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class Mind_Mastery implements ActionListener {
    // JFrame to hold all content
    private JFrame frame;
    Drawing draw;
    
    // variable to hold screen size
    Dimension screenSize;
    int locx, locy;
    
    /** 
        States: 
    0: splashscreen
    1: main menu
    2: level select
    3: instructions
    4: credits
    
    10: learning level
    11: maze level
    12: action level */
    
    int state;
    
    // splashscreen
    JPanel drawPanel;
    int drawState;
    
    // main menu buttons
    JButton[] mainButtons;
    JButton begin, instr, cred, exit;
    JPanel mainMenu, mainMenuButtons;
    
    // level select
    JPanel levelPanel;
    
    // credits
    JPanel credPanel;
    
    // maze level
    int[][] obs;
    JPanel mazePanel;
    int[] player;
    final int[] playerSize = {50, 50};
    
    // constructor
    public Mind_Mastery() {
        frame = new JFrame("Mind Mastery");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        // getting the size of the screen (source: https://stackoverflow.com/questions/8141865/java-how-to-get-the-screen-dimensions-from-a-graphics-object)
        screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        // loading all panels, buttons, content of the main menu
        load();
        
        // showing the splashscreen
        splashScreen();
        
        // displaying the main menu
        mainMenu();
    }
    
    
    /** 
    Private method to load content for the main menu, splashscreen,
    level select, instruction, and credits screen, and buttons
    for user navigation between pages
    
    <-------May 17------->
      > added loading of drawing and drawing panel, main menu and associated buttons
      > added loading of main frame, sizing, and display
      Contributor: Caleb Chue
    
    */ 
    private void load() {
        // drawing 
        draw = new Drawing();
        draw.addMouseListener(new ClickHandler());
        drawPanel = new JPanel();
        draw.setPreferredSize(new Dimension(screenSize.width, screenSize.height));
        drawPanel.add(draw);
        drawPanel.setVisible(false);
        
        // main menu
        mainMenu = new JPanel();
        mainMenuButtons = new JPanel();
        mainMenuButtons.setLayout(new BoxLayout(mainMenuButtons, BoxLayout.Y_AXIS));
        
        mainButtons = new JButton[] {new JButton("Begin Adventure"), 
                                     new JButton("How To Play"), 
                                     new JButton("Credits"), 
                                     new JButton("Exit")};

        mainMenuButtons.add(Box.createVerticalStrut(6*screenSize.height/17));
        for (int i = 0; i < mainButtons.length; i++) loadMainButton(i);
        
        mainMenu.add(mainMenuButtons);
        mainMenu.setVisible(false);
        
        // maze level
        mazePanel = new JPanel();


        // finish setup        
        frame.add(drawPanel);
        frame.add(mainMenu);
        
        frame.setExtendedState(Frame.MAXIMIZED_BOTH);
        frame.setSize(screenSize.width, screenSize.height);
        frame.setVisible(true);
        
    }
    
    
    /** 
    Private method to simplify loading new buttons on the main menu
    
    @param index The index of the button within the array of JButtons
    

    <-------May 18------->
      > added loading of JButtons for convenience
      Contributor: Caleb Chue
    
    */ 
    private void loadMainButton(int index) {
        mainButtons[index].addActionListener(this);
        mainButtons[index].setAlignmentX(Component.CENTER_ALIGNMENT);
        Dimension size = new Dimension(screenSize.width/3, screenSize.height/10);
        mainButtons[index].setPreferredSize(size);
        mainButtons[index].setMaximumSize(size);
        mainButtons[index].setFont(new Font("Courier New", Font.PLAIN, 32));
        mainMenuButtons.add(mainButtons[index]);
        mainMenuButtons.add(Box.createVerticalStrut(screenSize.height/18));
    }
    
    
    /** 
    Private method to handle the displaying of the splashscreen, including the delays for it

    <-------May 16------->
      > added displaying of splashscreen
      > added fade out code
      Contributor: Caleb Chue
    
    <-------May 18------->
      > after integration to JPanel and individual panel, the 
        splashscreen wouldn't fade in and would not show on screen
      > fixed issues, fades out properly
      Contributor: Caleb Chue
    
    <-------May 23------->
      > changed background color of splashscreen
      Contributor: Shiv Kanade
    
    
    */
    private void splashScreen() {
        // splashscreen
        state = 0;
        drawPanel.setVisible(true);
        draw.setVisible(true);
        frame.setContentPane(drawPanel);
        frame.getContentPane().setBackground(new Color(191,215,255));
        
        draw.repaint();
        sleep(1000);
        for (; drawState < 255; drawState++) {
            draw.repaint(); // fading out the screen
            sleep(5);
        }
        sleep(1000);
        // System.out.println("splash finished");
    }
    
    
    /** 
    Private method to handle the display of the main menu
    
    <-------May 18------->
      > added buttons and visibility
      Contributor: Caleb Chue
    
    <-------May 19------->
      > changed background color of main menu
      > made main menu panel appear on top as content pane
      Contributor: Caleb Chue
    
    
    */ 
    private void mainMenu() {
        frame.setContentPane(mainMenu);
        frame.revalidate();
        drawPanel.setVisible(false);
        mainMenu.setVisible(true);
        
        // main menu bg col
        Color mainMenuBG = new Color(61, 68, 128);
        frame.getContentPane().setBackground(mainMenuBG);
        mainMenuButtons.setBackground(mainMenuBG);
    }
    
    
    /** 
    Private method to handle the display of the credits screen
    */ 
    private void credits() {
        frame.setContentPane(credPanel);
    }
    
    
    /** 
    Private method to handle the display of the instructions screen
    */ 
    private void instructions() {
        
    }
    
    
    /** 
    Private method to handle the display of the maze level
    
    <-------May 24------->
      > created method
      > added redrawing on clicking on a Drawing
      Contributor: Caleb Chue
    
    */
    private void mazeLevel() {
        obs = new int[][] {{100, 100, 200, 200}};
        player = new int[] {50, 50};
    }
    
    
    /** 
    Private method to simplify delaying the program by a certain amout of time
    
    @param ms The number of milliseconds to delay by

    <-------May 16------->
      > added method to help sleep the program
      Contributor: Caleb Chue
    
    */ 
    public void sleep(int ms) {
        try{
            Thread.sleep(ms);
        } catch(Exception e){}
    }
    
    
    /** 
    Public overridden method to handle events from various sources
    
    @param e An action event to handle
    
    <-------May 16------->
      > added exit button
      > added redrawing on clicking on a Drawing
      Contributor: Caleb Chue
    
    <-------May 17------->
      > added functionality to other buttons from main menu, 
        such as level select, instructions, and credits
      Contributor: Caleb Chue
    
    */ 
    public void actionPerformed(ActionEvent e) {
        // main menu
        if (e.getSource() == mainButtons[0]) {
            state = 2;
            mainMenuButtons.setVisible(false);
        } else if (e.getSource() == mainButtons[1]) {
            state = 3;
            mainMenuButtons.setVisible(false);
        } else if (e.getSource() == mainButtons[2]) {
            state = 11;
            mazeLevel();
            mainMenuButtons.setVisible(false);
        } else if (e.getSource() == mainButtons[3]) {
            frame.setVisible(false);
            frame.dispose();
            System.exit(0);
        } else if (e.getSource() == draw) {
            draw.repaint();
        }
        
        
        // draw.repaint();
    }
    
    
    /** 
    Nested class to handle mouse events
    */
    class ClickHandler extends MouseAdapter {
    
        /** 
        Public overridden method to log the location of the last mouse click
        
        @param e The last mouse event
        */
        public void mouseClicked(MouseEvent e) {
            locx = e.getX();
            locy = e.getY();
            // draw.repaint();
        }
    }
    
    /** 
    Nested class to handle drawing on a canvas-like component
    */ 
    class Drawing extends JComponent {
    
        /** 
        Public overridden method to paint the component, drawing
        different contents based on the state of the program
        
        @param g The graphics Object to draw on

        <-------May 16------->
          > added drawing of splashscreen, including displaying image
          > added placeholder main menu graphic
          Contributor: Caleb Chue
    
        <-------May 17------->
          > removed placeholder graphic
          Contributor: Caleb Chue
    
        <-------May 18------->
          > added placeholder graphic for all states not handled by logic structure
          Contributor: Caleb Chue
        

        */ 
        public void paint(Graphics g) {
            if (state == 0) { // splashscreen
                try {
                    // drawing image (source: https://stackoverflow.com/questions/17865465/how-do-i-draw-an-image-to-a-jpanel-or-jframe)
                    BufferedImage im = ImageIO.read(new File("FocusForge Icon.png"));
                    int wd = im.getWidth(), ht = im.getHeight();
                    g.drawImage(im, (screenSize.width-wd)/2, (screenSize.height-ht)/2, null);
                    
                    g.setColor(new Color(0, 0, 0, drawState));
                    g.fillRect(0, 0, screenSize.width, screenSize.height);
                } catch (IOException e) {
                    System.out.println("Could not draw image: " + e);
                }
            } else if (state == 11) {
                g.setColor(new Color(255, 0, 255));
                g.fillRect(0, 0, screenSize.width, screenSize.height);
                // System.out.println("should draw other >:(");
            }
        }
    }
    
    
    /** 
    Main method
    
    @param args Arguments for the application
    */ 
    public static void main(String[] args) {
        new Mind_Mastery();
    }
}
 
