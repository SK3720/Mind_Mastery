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


    /** 
    <-------May 24------->
      > deprecated screenSize in favour of a set size application
      Contributor: Caleb Chue

    */
    final int SCREEN_WIDTH = 1000, SCREEN_HEIGHT = 650;
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
    JButton learningLevelButton, mazeLevelButton, actionLevelButton;
    
    // credits
    JPanel credPanel;
    
    // learning/maze level
    int[] player;
    int[][] obs;
    final int[] playerSize = {50, 50};
    
    // action level
    
    
    
    // constructor
    public Mind_Mastery() {
        frame = new JFrame("Mind Mastery");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

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
    
    <-------May 24------->
      > added loading of level select buttons
      > added outer JFrame loading certain panels
      Contributor: Caleb Chue

    
    */ 
    private void load() {
        
        // drawing 
        draw = new Drawing();
        draw.addMouseListener(new ClickHandler());
        drawPanel = new JPanel();
        draw.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        drawPanel.add(draw);
        
        // main menu
        mainMenu = new JPanel();
        mainMenuButtons = new JPanel();
        mainMenuButtons.setLayout(new BoxLayout(mainMenuButtons, BoxLayout.Y_AXIS));
        
        mainButtons = new JButton[] {new JButton("Begin Adventure"), 
                                     new JButton("How To Play"), 
                                     new JButton("Credits"), 
                                     new JButton("Exit")};

        mainMenuButtons.add(Box.createVerticalStrut(6*SCREEN_HEIGHT/17));
        for (int i = 0; i < mainButtons.length; i++) loadMainButton(i);
        
        mainMenu.add(mainMenuButtons);
        
        // level select
        levelPanel = new JPanel();
        learningLevelButton = new JButton("Learning");
        mazeLevelButton = new JButton("Maze");
        actionLevelButton = new JButton("Action");
        levelPanel.add(learningLevelButton);
        levelPanel.add(mazeLevelButton);
        levelPanel.add(actionLevelButton);
        learningLevelButton.addActionListener(this);
        mazeLevelButton.addActionListener(this);
        actionLevelButton.addActionListener(this);
        

        // finish setup        
        frame.add(drawPanel);
        frame.add(mainMenu);
        frame.add(levelPanel);
        
        frame.setSize(SCREEN_WIDTH, SCREEN_HEIGHT);
        frame.setVisible(true);
        
        reset();
    }
    
    
    /** 
    Private method to reset the frame, setting all panels invisible
    
    <-------May 24------->
      > created method, set main menu, level select, drawing, 
        learning level, and maze level panels to be invisible
      Contributor: Caleb Chue

    */
    private void reset() {
        mainMenu.setVisible(false);
        levelPanel.setVisible(false);
        drawPanel.setVisible(false);
        
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
        Dimension size = new Dimension(SCREEN_WIDTH/3, SCREEN_HEIGHT/10);
        mainButtons[index].setPreferredSize(size);
        mainButtons[index].setMaximumSize(size);
        mainButtons[index].setFont(new Font("Courier New", Font.PLAIN, 32));
        mainMenuButtons.add(mainButtons[index]);
        mainMenuButtons.add(Box.createVerticalStrut(SCREEN_HEIGHT/18));
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
        frame.setVisible(true);
        
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
    Private method
    
    */
    private void levelSelect() {
        frame.setContentPane(levelPanel);
        levelPanel.setVisible(true);
        
        
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
    Private method to handle the display of the learning level
    */
    private void learningLevel() {
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
        
        frame.setContentPane(drawPanel);
        drawPanel.setVisible(true);
        draw.setVisible(true);
        draw.repaint();
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
        if (e.getSource() == mainButtons[0]) { // level select
            state = 2;
            reset();
            levelSelect();
        } else if (e.getSource() == mainButtons[1]) { // how to play
            state = 3;
            reset();
        } else if (e.getSource() == mainButtons[2]) { // credits
            state = 4;
            reset();
            mazeLevel();
        } else if (e.getSource() == mainButtons[3]) { // exit button
            frame.setVisible(false);
            frame.dispose();
            System.exit(0);
            
        // level select
        } else if (e.getSource() == learningLevelButton) {
            state = 10;
            reset();
            learningLevel();
            System.out.println("Learning trigger");
        } else if (e.getSource() == mazeLevelButton) {
            state = 11;
            reset();
            mazeLevel();
            System.out.println("Maze trigger");
        } else if (e.getSource() == actionLevelButton) {
            state = 12;
            reset();
            // actionLevel();
            System.out.println("Action trigger");
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
        
        <-------May 24------->
          > working on the drawing of the maze level
          Contributor: Caleb Chue


        */ 
        public void paint(Graphics g) {
            if (state == 0) { // splashscreen
                image("FocusForge Icon.png", g);
                g.setColor(new Color(0, 0, 0, drawState));
                g.fillRect(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);
            } else if (state == 11) {
                g.fillRect(-100, -100, SCREEN_WIDTH+100, SCREEN_HEIGHT+100);
                
                // drawing player
                
            }
        }
        
        /** 
        Private method to make drawing centered images more convenient
        
        Contributor: Caleb Chue
        
        @param path The file path to the image
        @param g The Graphics image to draw with
        */ 
        private void image(String path, Graphics g) {
            // drawing image (source: https://stackoverflow.com/questions/17865465/how-do-i-draw-an-image-to-a-jpanel-or-jframe)
            try {
                BufferedImage im = ImageIO.read(new File(path));
                int wd = im.getWidth(), ht = im.getHeight();
                g.drawImage(im, (SCREEN_WIDTH-wd)/2, (SCREEN_HEIGHT-ht)/2, null);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        /** 
        Overloaded private method to make drawing images more convenient
        
        Contributor: Caleb Chue
        
        @param path The file path to the image
        @param x The x coordinate of the image
        @param y The y coordinate of the image
        @param g The Graphics image to draw with
        */ 
        private void image(String path, int x, int y, Graphics g) {
            // drawing image (source: https://stackoverflow.com/questions/17865465/how-do-i-draw-an-image-to-a-jpanel-or-jframe)
            try {
                BufferedImage im = ImageIO.read(new File(path));
                int wd = im.getWidth(), ht = im.getHeight();
                g.drawImage(im, (SCREEN_WIDTH-wd)/2, (SCREEN_HEIGHT-ht)/2, null);
            } catch (IOException e) {
                e.printStackTrace();
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
