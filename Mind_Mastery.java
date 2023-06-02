/**
 * Authors: Focus Forge - Caleb Chue and Shiv Kanade
 * Teacher: V. Krasteva
 * Date: May 29, 2023
 * Description: The second version of Focus Forge's game, Mind Mastery.
 * This updated version holds more of a maze level, as
 * a moving player and obstacle system have been implemented
 */

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import javax.imageio.IIOException;
import javax.swing.*;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.*;

public class Mind_Mastery implements KeyListener, ActionListener, Runnable {
    // JFrame to hold all content
    private JFrame frame;
    Drawing draw;


    /**
     Instance Variable Declaration

     <-------May 24------->
       > deprecated screenSize in favour of a set size application
       Contributor: Caleb Chue

     <-------May 26------->
       > changed player hitbox to align with the temporary sprite
       Contributor: Caleb Chue

     <-------June 2------->
       > attempt to implement Runnable
       Contributor: Caleb Chue


     */
    final int SCREEN_WIDTH = 1000, SCREEN_HEIGHT = 700;
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
    JButton[] mainButtons, levelButtons;
    JButton begin, instr, cred, exit;
    JPanel mainMenu, mainMenuButtons, levelMenuButtons;

    // level select
    JPanel levelPanel;
    JButton learningLevelButton, mazeLevelButton, actionLevelButton, LeaderboardButton;

    // credits
    JPanel credPanel;

    Thread thread;

    // learning/maze level
    int[] player;
    ArrayList<Hitbox> obs;
    boolean[] keysPressed;
    public final int[] playerSize = {18, 38};
    final int MOVE_DISTANCE = 5;

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

     <-------May 25------->
     > added loading of keysPressed boolean array
     Contributor: Caleb Chue

     <-------May 31------->
     > added leaderboard button to level select screen.
     > formatted button positions and UI for level selection screen
     Contributor: Shiv Kanade
     
     <-------June 2------->
     > added loading of thread
     Contributor: Caleb Chue


     */
    private void load() {

        // drawing 
        draw = new Drawing();
        draw.addMouseListener(new ClickHandler());
        draw.addKeyListener(this);

        // focusing on the drawing (source: https://stackoverflow.com/questions/10876491/how-to-use-keylistener)
        draw.setFocusable(true);

        drawPanel = new JPanel();
        draw.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        drawPanel.add(draw);

        // main menu
        mainMenu = new JPanel();
        mainMenuButtons = new JPanel();
        mainMenuButtons.setLayout(new BoxLayout(mainMenuButtons, BoxLayout.Y_AXIS));

        mainButtons = new JButton[]{new JButton("Begin Adventure"),
                new JButton("How To Play"),
                new JButton("Credits"),
                new JButton("Exit")};

        mainMenuButtons.add(Box.createVerticalStrut(6 * SCREEN_HEIGHT / 17));

        for (int i = 0; i < mainButtons.length; i++) loadMainButton(i);

        mainMenu.add(mainMenuButtons);

        // level selection menu
        levelPanel = new JPanel();
        levelMenuButtons = new JPanel();
        levelMenuButtons.setLayout(new BoxLayout(levelMenuButtons, BoxLayout.Y_AXIS));
        Color levelMenuBG = new Color(61, 68, 128);
        levelMenuButtons.setBackground(levelMenuBG);
        levelPanel.setBackground(levelMenuBG);

        levelButtons = new JButton[]{new JButton("Learning"),
                new JButton("Maze"),
                new JButton("Action"),
                new JButton("Leaderboard")};

        levelMenuButtons.add(Box.createVerticalStrut(6 * SCREEN_HEIGHT / 17));

        for (int i = 0; i < 4; i++) loadLevelButton(i);

        levelPanel.add(levelMenuButtons);

        // movement keys
        keysPressed = new boolean[4];
        thread = new Thread(this);

        // finish setup        
        frame.add(drawPanel);
        frame.add(mainMenu);
        frame.add(levelPanel);
        frame.addKeyListener(this);

        frame.setSize(SCREEN_WIDTH, SCREEN_HEIGHT);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        reset();
    }


    /** 
    Private method to handle loading hitboxes from a file for
    learning and action levels
    
    @param path The path of the file to load from
    @return An ArrayList of Strings of each line in the file
    
    
    */ 
    private ArrayList<String> loadFromFile(String path) {
        ArrayList<String> arr = new ArrayList<String>();
        try {
            Scanner sc = new Scanner(new File(path));
            String temp = "";
            while (sc.hasNext()) {
                temp = sc.nextLine();
                arr.add(temp);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return arr;
    }


    /**
     Private method to load and return a list of obstacles for the
     current stage of the action or learning level

     @param stage The stage of the level to load
     @return A List of Obstacle objects from the stage
     
     <-------May 26------->
       > created method
       Contributor: Caleb Chue
    
     <-------May 28------->
       > changed file path
       Contributor: Caleb Chue


     */
    private ArrayList<Hitbox> loadObstacles(int stage) {
        ArrayList<Hitbox> obs = new ArrayList<Hitbox>();
        ArrayList<String> strs = loadFromFile("leveldata-" + stage + ".txt");
        for (String s : strs) {
            String[] spl = s.split(" ");
            int[] dat = new int[spl.length];
            for (int i = 0; i < spl.length; i++) dat[i] = Integer.parseInt(spl[i]);
            Hitbox o;
            if (dat[4] == 0) {
                o = new Obstacle(dat[0], dat[1], dat[2], dat[3]);
            } else if (dat[4] >= 1) {
                o = new Task(dat[0], dat[1], dat[2], dat[3], dat[4]);
            } else {
                o = new Distraction(dat[0], dat[1], dat[2], dat[3], dat[4]);
            }
            o.setPlayerSize(playerSize);
            obs.add(o);
        }

        return obs;
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
        Dimension size1 = new Dimension(SCREEN_WIDTH / 3, SCREEN_HEIGHT / 12);
        mainButtons[index].setPreferredSize(size1);
        mainButtons[index].setMaximumSize(size1);
        mainButtons[index].setFont(new Font("Courier New", Font.PLAIN, 32));
        mainMenuButtons.add(mainButtons[index]);
        mainMenuButtons.add(Box.createVerticalStrut(SCREEN_HEIGHT / 16));
    }

    private void loadLevelButton(int index) {
        levelButtons[index].addActionListener(this);
        levelButtons[index].setAlignmentX(Component.CENTER_ALIGNMENT);
        Dimension size2 = new Dimension(SCREEN_WIDTH / 3, SCREEN_HEIGHT / 12);
        levelButtons[index].setPreferredSize(size2);
        levelButtons[index].setMaximumSize(size2);
        levelButtons[index].setFont(new Font("Courier New", Font.PLAIN, 32));
        levelMenuButtons.add(levelButtons[index]);
        levelMenuButtons.add(Box.createVerticalStrut(SCREEN_HEIGHT / 16));
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
        frame.getContentPane().setBackground(new Color(191, 215, 255));
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
        //levelPanel.add();
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
        obs = loadObstacles(0);
        player = new int[]{150, 150};

        frame.setContentPane(drawPanel);
        drawPanel.setVisible(true);
        draw.setVisible(true);

        // making the drawing the focus (source: https://docs.oracle.com/javase/tutorial/uiswing/misc/focus.html)
        draw.requestFocusInWindow();
        draw.repaint();
    }


    /**
     Private method to handle the display of the maze level

     <-------May 24------->
     > created method
     > added redrawing on clicking on a Drawing
     Contributor: Caleb Chue

     */
    private void mazeLevel() {
        thread.start();
        obs = loadObstacles(1);
        player = new int[]{150, 150};

        frame.setContentPane(drawPanel);
        drawPanel.setVisible(true);
        draw.setVisible(true);

        // making the drawing the focus (source: https://docs.oracle.com/javase/tutorial/uiswing/misc/focus.html)
        draw.requestFocusInWindow();
        draw.repaint();
    }
    
    
    @Override
    public void run() {
        handleMovement();
    }


    /**
     Private method to simplify delaying the program by a certain amout of time

     @param ms The number of milliseconds to delay by

     <-------May 16------->
     > added method to help sleep the program
     Contributor: Caleb Chue

     */
    public void sleep(int ms) {
        try {
            Thread.sleep(ms);
        } catch (Exception e) {
        }
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
        } else if (e.getSource() == levelButtons[0]) {
            state = 10;
            reset();
            learningLevel();
            System.out.println("Learning trigger");
        } else if (e.getSource() == levelButtons[1]) {
            state = 11;
            reset();
            mazeLevel();
            System.out.println("Maze trigger");
        } else if (e.getSource() == levelButtons[2]) {
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
     Overridden methods to handle keyboard input

     <-------May 25------->
     > moved keyboard handling from inside Drawing class
     to parent class as an implemented interface
     Contributor: Caleb Chue

     */

    public void keyTyped(KeyEvent k) {
    }

    public void keyPressed(KeyEvent k) {
        char key = k.getKeyChar();
        handleKeys(key, true);
        // System.out.println("Pressed: " + (keysPressed[0] ? "W " : "") + (keysPressed[1] ? "A " : "") + (keysPressed[2] ? "S " : "") + (keysPressed[3] ? "D " : ""));
        if (state == 10 || state == 11) handleMovement();
        draw.repaint();
    }

    public void keyReleased(KeyEvent k) {
        char key = k.getKeyChar();
        handleKeys(key, false);
        draw.repaint();
    }

    private void handleKeys(char key, boolean newState) {
        if (key == 'w' || key == 'W') {
            keysPressed[0] = newState;
        } else if (key == 'a' || key == 'A') {
            keysPressed[1] = newState;
        } else if (key == 's' || key == 'S') {
            keysPressed[2] = newState;
        } else if (key == 'd' || key == 'D') {
            keysPressed[3] = newState;
        }
    }


    /**
     Private method to handle the movement of the player

     <-------May 25------->
     > added method
     Contributor: Caleb Chue

     */
    private void handleMovement() {
        if (keysPressed[0]) { // forward
            if (legalMove(player[0], player[1] - MOVE_DISTANCE)) {
                player[1] -= MOVE_DISTANCE;
            }
        }
        if (keysPressed[1]) { // left
            if (legalMove(player[0] - MOVE_DISTANCE, player[1])) {
                player[0] -= MOVE_DISTANCE;
            }
        }
        if (keysPressed[2]) { // down
            if (legalMove(player[0], player[1] + MOVE_DISTANCE)) {
                player[1] += MOVE_DISTANCE;
            }
        }
        if (keysPressed[3]) { // right
            if (legalMove(player[0] + MOVE_DISTANCE, player[1])) {
                player[0] += MOVE_DISTANCE;
            }
        }
        draw.repaint();
    }

    /**
     Private method to check whether the player's next move will result in a wall collision

     @param x The x coordinate of the player's location
     @param y The y coordinate of the player's location
     @return Whether the player's next move is legal

     <-------May 25------->
     > added method
     > moved out of nested class to main class
     Contributor: Caleb Chue

     */
    private boolean legalMove(int x, int y) {
        if (x < 0 || x > SCREEN_WIDTH || y < 0 || y > SCREEN_HEIGHT) return false;
        for (int i = 0; i < obs.size(); i++) {
            if (obs.get(i) instanceof Obstacle && obs.get(i).colliding(x, y)) {
                return false;
            }
        }
        return true;
    }


    /**
     Nested class to handle drawing on a canvas-like component

     <-------May 24------->
     > added implements KeyListener (keyboard input)
     > added image drawing methods
     Contributor: Caleb Chue

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
                g.fillRect(-100, -100, SCREEN_WIDTH + 100, SCREEN_HEIGHT + 100);

                // drawing player
                g.setColor(new Color(12, 50, 101));
                g.fillRect(player[0] - playerSize[0] / 2, player[1] - playerSize[1] / 2, playerSize[0], playerSize[1]);
                image("player.png", player[0], player[1], g);

                g.setColor(new Color(255, 0, 0));
                for (Hitbox ob : obs) {
                    // System.out.println(ob.x + " " + ob.y + " " + ob.w + " " + ob.h);
                    g.fillRect(ob.x, ob.y, ob.w, ob.h);
                }

                if (keysPressed[0] || keysPressed[1] || keysPressed[2] || keysPressed[3]) handleMovement();
            } else if (state == 10){
                image("images/LearningLevelMap.png", SCREEN_WIDTH / 2, SCREEN_HEIGHT / 2, 1000, 700, g);
            }
        }

        /**
         Private method to make drawing centered images more convenient

         Contributor: Caleb Chue

         @param path The file path to the image
         @param g The Graphics image to draw with

         <-------May 24------->
         > added method
         Contributor: Caleb Chue

         */
        private void image(String path, Graphics g) {
            image(path, SCREEN_WIDTH / 2, SCREEN_HEIGHT / 2, -1, -1, g);
        }

        private void image(String path, int x, int y, Graphics g) {
            image(path, x, y, -1, -1, g);
        }

        /**
         Overloaded private method to make drawing images more convenient

         Contributor: Caleb Chue

         @param path The file path to the image
         @param x The x coordinate of the image
         @param y The y coordinate of the image
         @param g The Graphics image to draw with

         <-------May 24------->
         > added method
         Contributor: Caleb Chue

         <-------May 26------->
         > fixed method drawing in the wrong place
         Contributor: Caleb Chue

         */
        private void image(String path, int x, int y, int wd, int ht, Graphics g) {
            // drawing image (source: https://stackoverflow.com/questions/17865465/how-do-i-draw-an-image-to-a-jpanel-or-jframe)
            try {
                BufferedImage im = ImageIO.read(new File("images/" + path));
                if (wd == -1) {
                    wd = im.getWidth();
                    ht = im.getHeight();
                }
                g.drawImage(im, x - wd / 2, y - ht / 2, wd, ht, null);
            } catch (IIOException e) {
                System.out.println("Cannot load image from images/" + path);
                e.printStackTrace();

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