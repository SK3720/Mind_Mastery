/**
 * Authors: Focus Forge - Caleb Chue and Shiv Kanade
 * Teacher: V. Krasteva
 * Date: May 29, 2023
 * Description: The final version of Focus Forge's game, Mind Mastery.
 * This version contains completed menus, GUI, mouse and 
 * keyboard interactions, and three completed levels created
 * to guide the player through learning about and overcoming
 * attention disorders.
 */

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import javax.imageio.IIOException;
import javax.swing.*;
import javax.imageio.ImageIO;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.Timer;

public class Mind_Mastery extends TimerTask implements KeyListener, ActionListener, Runnable {
   // JFrame to hold all content
   private JFrame frame;
   Drawing draw;
   String username = "Shiv";

   /**
    * 
    * Instance Variable Declaration
    * <p>
    * <-------May 24------->
    * > deprecated screenSize in favour of a set size application
    * Contributor: Caleb Chue
    * <p>
    * <-------May 26------->
    * > changed player hitbox to align with the temporary sprite
    * Contributor: Caleb Chue
    * <p>
    * <-------June 2------->
    * > attempt to implement Runnable
    * Contributor: Caleb Chue
    */
   final int SCREEN_WIDTH = 1000, SCREEN_HEIGHT = 700;
   int locx, locy;

   /**
    * States:
    * 0: splashscreen
    * 1: main menu
    * 2: level select
    * 3: instructions
    * 4: credits
    * <p>
    * 10: learning level
    * 11: maze level
    * 12: action level
    */

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
   Leaderboard leaderboard;

   // credits
   JPanel credPanel, instrPanel;

   double[] player;
   ArrayList<Hitbox> obs;
   boolean[] keysPressed;
   public int[] playerSize;
   final double MOVE_DISTANCE = 3.5;
   final int NUM_CLICKABLES = 7, OBS_LIMIT = 18;

   int score, time;
   int interacting;
   Hitbox source;

   // action level


   // constructor
   public Mind_Mastery() {
      // loading all panels, buttons, content of the main menu
      load();
   
      // showing the splashscreen
      splashScreen();
   
      // displaying the main menu
      mainMenu();
   }


   /**
    * Private method to load content for the main menu, splashscreen,
    * level select, instruction, and credits screen, and buttons
    * for user navigation between pages
    * <p>
    * <-------May 17------->
    * > added loading of drawing and drawing panel, main menu and associated buttons
    * > added loading of main frame, sizing, and display
    * Contributor: Caleb Chue
    * <p>
    * <-------May 24------->
    * > added loading of level select buttons
    * > added outer JFrame loading certain panels
    * Contributor: Caleb Chue
    * <p>
    * <-------May 25------->
    * > added loading of keysPressed boolean array
    * Contributor: Caleb Chue
    * <p>
    * <-------May 31------->
    * > added leaderboard button to level select screen.
    * > formatted button positions and UI for level selection screen
    * Contributor: Shiv Kanade
    * <p>
    * <-------June 2------->
    * > added loading of thread
    * Contributor: Caleb Chue
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
   
   
      JLabel gameTitle = new JLabel("Mind Mastery");
      gameTitle.setFont(new Font("Verdana", Font.BOLD, 100));
      gameTitle.setForeground(Color.RED);
      gameTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
      
      mainMenuButtons.add(Box.createVerticalStrut(SCREEN_HEIGHT / 15));
      mainMenuButtons.add(gameTitle);
      mainMenuButtons.add(Box.createVerticalGlue());
      mainMenuButtons.add(Box.createVerticalStrut(2 * SCREEN_HEIGHT / 17));
   
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
   
      gameTitle = new JLabel("Level Select");
      gameTitle.setFont(new Font("Verdana", Font.BOLD, 60));
      gameTitle.setForeground(Color.RED);
      gameTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
      
      // aligning box elements (source: https://docs.oracle.com/javase/tutorial/uiswing/layout/box.html#alignment)
      levelMenuButtons.add(Box.createVerticalStrut(2 * SCREEN_HEIGHT / 17));
      levelMenuButtons.add(gameTitle);
      levelMenuButtons.add(Box.createVerticalGlue());
      levelMenuButtons.add(Box.createVerticalStrut(2 * SCREEN_HEIGHT / 17));
   
      for (int i = 0; i < 4; i++) loadLevelButton(i);
   
      levelPanel.add(levelMenuButtons);
      playerSize = new int[]{20, 50};
   
      interacting = -1;
   
      // movement keys
      keysPressed = new boolean[4];
   
      // finish setup
      frame = new JFrame("Mind Mastery");
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
   
      frame.add(drawPanel);
      frame.add(mainMenu);
      frame.add(levelPanel);
      frame.addKeyListener(this);
      frame.setFocusTraversalKeysEnabled(false);
   
      frame.setSize(SCREEN_WIDTH, SCREEN_HEIGHT);
      frame.setResizable(false);
      frame.setLocationRelativeTo(null);
      frame.setVisible(true);
      frame.setFocusable(true);
   
      reset();
   }


   /**
    * Private method to handle loading hitboxes from a file for
    * learning and action levels
    *
    * @param path The path of the file to load from
    * @return An ArrayList of Strings of each line in the file
    */
   private ArrayList<String> loadFromFile(String path) {
      ArrayList<String> arr = new ArrayList<String>();
      try {
         Scanner sc = new Scanner(new File(path));
         String temp;
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
    * Private method to load and return a list of obstacles for the
    * current stage of the action or learning level
    *
    * @return A List of Obstacle objects from the stage
    * <p>
    * <-------May 26------->
    * > created method
    * Contributor: Caleb Chue
    * <p>
    * <-------May 28------->
    * > changed file path
    * Contributor: Caleb Chue
    * <p>
    * <-------June 4------->
    * > changed file path again
    * > made method dependent on state
    * Contributor: Caleb Chue
    */
   private ArrayList<Hitbox> loadObstacles() {
      ArrayList<Hitbox> tobs = new ArrayList<Hitbox>();
      if (state == 31) 
         return tobs;
      ArrayList<String> strs = loadFromFile("leveldata/leveldata-" + (state / 10) + "-" + (state % 10) + ".txt");
      for (String s : strs) {
         String[] spl = s.strip().split(" ");
         if (spl.length == 0 || spl[0].equals("") || spl[0].equals("//") || spl[0].equals("#")) 
            continue;
         int[] dat = new int[spl.length];
         for (int i = 0; i < spl.length; i++) dat[i] = Integer.parseInt(spl[i]);
         Hitbox o;
         if (dat[dat.length - 1] == 0) {
            o = new Obstacle(dat[0], dat[1], dat[2], dat[3]);
         } else if (dat[dat.length - 1] == 1) {
            o = new Task(dat[0], dat[1], dat[2], dat[3], dat[4], new WindowDetector());
         } else if (dat[dat.length - 1] == 2) {
            o = new Distraction(dat[0], dat[1], dat[2], dat[3], dat[4]);
         } else {
            o = new Trigger(dat[0], dat[1], dat[2], dat[3], dat[4]);
         }
         o.setPlayerSize(playerSize);
         tobs.add(o);
      }
   
      return tobs;
   }


   /**
    * Private method to reset the frame, setting all panels invisible
    * <p>
    * <-------May 24------->
    * > created method, set main menu, level select, drawing,
    * learning level, and maze level panels to be invisible
    * Contributor: Caleb Chue
    */
   private void reset() {
      score = 0;
      mainMenu.setVisible(false);
      levelPanel.setVisible(false);
      drawPanel.setVisible(false);
   }


   /**
    * Private method to simplify loading new buttons on the main menu
    *
    * @param index The index of the button within the array of JButtons
    *              <p>
    *              <p>
    *              <-------May 18------->
    *              > added loading of JButtons for convenience
    *              Contributor: Caleb Chue
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
    * Private method to handle the displaying of the splashscreen, including the delays for it
    * <p>
    * <-------May 16------->
    * > added displaying of splashscreen
    * > added fade out code
    * Contributor: Caleb Chue
    * <p>
    * <-------May 18------->
    * > after integration to JPanel and individual panel, the
    * splashscreen wouldn't fade in and would not show on screen
    * > fixed issues, fades out properly
    * Contributor: Caleb Chue
    * <p>
    * <-------May 23------->
    * > changed background color of splashscreen
    * Contributor: Shiv Kanade
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
   }


   /**
    * Private method to handle the display of the main menu
    * <p>
    * <-------May 18------->
    * > added buttons and visibility
    * Contributor: Caleb Chue
    * <p>
    * <-------May 19------->
    * > changed background color of main menu
    * > made main menu panel appear on top as content pane
    * Contributor: Caleb Chue
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
    * Private method to set the level select as the main content
    */
   private void levelSelect() {
      frame.setContentPane(levelPanel);
      levelPanel.setVisible(true);
   }


   /**
    * Private method to handle the display of the credits screen
    */
   private void credits() {
      drawPanel.setVisible(true);
      draw.setVisible(true);
      frame.setContentPane(drawPanel);
      frame.getContentPane().setBackground(new Color(191, 215, 255));
      frame.setVisible(true);
   }


   /**
    * Private method to handle the display of the instructions screen
    */
   private void instructions() {
      drawPanel.setVisible(true);
      draw.setVisible(true);
      frame.setContentPane(drawPanel);
      frame.getContentPane().setBackground(new Color(191, 215, 255));
      frame.setVisible(true);
   }

   /**
    * Private method to handle the display of each maze level
    * <p>
    * <-------May 24------->
    * > created method
    * > added redrawing on clicking on a Drawing
    * Contributor: Caleb Chue
    */
   private void loadLevel() {
      if (state == 10) {
         player = new double[]{50, 175};
         playerSize = new int[]{20, 50};
      } else if (state == 11) {
         player = new double[]{475, 500};
         playerSize = new int[]{45, 120};
      } else if (state == 12) {
         player = new double[]{475, 500};
         playerSize = new int[]{45, 120};
         player = new double[]{475, 500};
         playerSize = new int[]{45, 120};
      } else if (state == 13) {
         player = new double[]{426, 170};
         playerSize = new int[]{20, 50};
      } else if (state == 14) {
         player = new double[]{445, 375};
         playerSize = new int[]{20, 50};
      } else if (state == 20) {
         player = new double[]{475, 579};
         playerSize = new int[]{20, 50};
      } else if (state == 30) {
         time = 1800;
      }
      if (state % 10 != 9) obs = loadObstacles();
      else obs = new ArrayList<Hitbox>();
      interacting = 0;
   
      frame.setContentPane(drawPanel);
      drawPanel.setVisible(true);
      draw.setVisible(true);
   
      // making the drawing the focus (source: https://docs.oracle.com/javase/tutorial/uiswing/misc/focus.html)
      draw.requestFocusInWindow();
      draw.repaint();
      
      run();
   }

   /** 
    * Private method to return an ArrayList of Hitboxes that the
    * player is currently colliding with
   
    * @return An ArrayList of all the Hitboxes the player
              is colliding with
   */
   private ArrayList<Hitbox> collidingHitboxes() {
      ArrayList<Hitbox> out = new ArrayList<Hitbox>();
      for (Hitbox h : obs) {
         if (h.colliding(player[0], player[1])) out.add(h);
      }
      return out;
   }
   
   
   /**
    * Private method to check whether the current state is a 
    * winning state, and to change states accordingly
    */ 
   private void checkWin() {
      int c = state/10;
      
      if (c == 2) {
         for (Hitbox h : obs) {
            if (h instanceof Task && h.getActive()) {
               return;
            }
         }
         state = 9;
         loadLevel();
      }
   }


   /** 
    * Private method to handle the player's actions
    */ 
   private void handlePlayer() {
      handleMovement();
   }

   /**
    * Overridden method to run the program thread
    */
   @Override
   public void run() {
      handlePlayer();
      draw.repaint();
      sleep(10);
      if (state == 31) { // action level
         while (obs.size() < OBS_LIMIT) {
            int[] params = {draw.rand(NUM_CLICKABLES)};
            try {
               BufferedImage img = draw.getImage("clickable-" + params[0] + ".png");
               Clickable c = new Clickable(draw.rand(2)*1000, draw.rand(350)+70, img.getWidth(), img.getHeight(), params[0], 0.5+2*Math.random(), img);
               c.setPlayerSize(new int[] {0,0});
               obs.add(c);
            } catch (IOException e) {
               System.out.println("Cannot load image from images/clickable-" + params[0]);
               e.printStackTrace();
            }
         }
         for (int c = 0; c < obs.size(); c++) {
            if (((Clickable)obs.get(c)).handle()) obs.remove(c--);
         }
      }
   }


   /**
    * Private method to simplify delaying the program by a certain amount of time
    *
    * @param ms The number of milliseconds to delay by
    *           <p>
    *           <-------May 16------->
    *           > added method to help sleep the program
    *           Contributor: Caleb Chue
    */
   public void sleep(int ms) {
      try {
         Thread.sleep(ms);
      } catch (Exception e) {
      }
   }

   /**
    * Public overridden method to handle events from various sources
    *
    * @param e An action event to handle
    *          <p>
    *          <-------May 16------->
    *          > added exit button
    *          > added redrawing on clicking on a Drawing
    *          Contributor: Caleb Chue
    *          <p>
    *          <-------May 17------->
    *          > added functionality to other buttons from main menu,
    *          such as level select, instructions, and credits
    *          Contributor: Caleb Chue
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
         instructions();
      } else if (e.getSource() == mainButtons[2]) { // credits
         state = 4;
         reset();
         credits();
      } else if (e.getSource() == mainButtons[3]) { // exit button
         frame.setVisible(false);
         frame.dispose();
         System.exit(0);
      
         // level select
      } else if (e.getSource() == levelButtons[0]) {
         state = 10;
         reset();
         loadLevel();
      } else if (e.getSource() == levelButtons[1]) {
         state = 20;
         reset();
         loadLevel();
      } else if (e.getSource() == levelButtons[2]) {
         state = 30;
         reset();
         loadLevel();
      } else if (e.getSource() == levelButtons[3]) {
         state = 40;
         loadLeaderboard();
      } else if (e.getSource() == draw) {
         draw.repaint();
      }
   }

   public void loadLeaderboard() {
      Leaderboard LeaderboardCurrent = new Leaderboard();
   }

      /**
       <-------Shiv Kanade------->
       * Created a method to write to the scores list file to keep track of the high scores.
       */
      private void dataListWriter() {
      File scoreFile = new File("leveldata/scoresList.txt");
      if (scoreFile.exists())
      //checks if the text file scoresList exists
      {
         try {
            FileWriter fileWrite = new FileWriter("leveldata/scoresList.txt", true);
            BufferedWriter output = new BufferedWriter(fileWrite);
            //If scoreList already exists, it does not create a new text file and moves on to adding the new scores and usernames
            output.write("\n");
            output.write(String.valueOf(score) + "\n");
            output.write(username + "\n");
            //This appends the scores and usernames of the two players who have just played the game to the end of the text file
            output.close();
            //closes file writer
         } catch (IOException e) {
         }
         //This catches exception e
      }
   }

   /**
    * Nested class to handle mouse events
    */
   class ClickHandler extends MouseAdapter {
   
      /**
       * Public overridden method to log the location of the last mouse press
       *
       * @param e The last mouse event
       */
      public void mousePressed(MouseEvent e) {
         locx = e.getX();
         locy = e.getY();
         if (state == 9 || state == 32) {
            state = 0;
            mainMenu();
         } else if (state == 30) {
            state = 31;
         } else if (state == 31) {
            for (int h = 0; h < obs.size(); h++) {
               Clickable temp;
               if (obs.get(h) instanceof Clickable && (temp = (Clickable)(obs.get(h))).colliding(locx, locy)) {
                  String[] ins = temp.interactedBehaviour().split(" ");
                  if (ins[0].equals("add")) score += Integer.parseInt(ins[1]);
                  else if (ins[0].equals("deduct")) score -= Integer.parseInt(ins[1]);
                  
                  if (!temp.getActive()) {
                     obs.remove(h--);
                  }
               }
            }
         }
      }
   }

   /**
    * Nested class to handle mouse events
    */
   class WindowDetector extends WindowAdapter {
   
      /**
       * Public overridden method to log the location of the last mouse click
       *
       * @param e The last mouse event
       */
      public void windowClosed(WindowEvent e) {
         JFrame fr;
         for (Hitbox h : collidingHitboxes()) {
            if (h instanceof Task && e.getSource() == ((Task) h).getFrame()) {
               if (!h.getActive()) { // reward for completing task
                  score += 500;
                  checkWin();
                  draw.repaint();
               }
            }
         }
      }
   }


   /**
    *
    */
   private void interact(Hitbox h) {
      for (int i = 0; i < keysPressed.length; i++) keysPressed[i] = false;
      String original = h.interactedBehaviour();
      String[] ins = original.split(" ");
      if (ins.length == 0) 
         return;
   
      if (ins[0].equals("goto")) {
         state = Integer.parseInt(ins[1]);
         loadLevel();
      } else if (ins[0].equals("paragraph")) {
         ins = original.split("\n");
         interacting = ins.length - 1;
         source = h;
      }
   }


   /**
    * Overridden methods to handle keyboard input
    * <p>
    * <-------May 25------->
    * > moved keyboard handling from inside Drawing class
    * to parent class as an implemented interface
    * Contributor: Caleb Chue
    */

   public void keyTyped(KeyEvent k) {
   }

   public void keyPressed(KeyEvent k) {
      if (state == 2 || state == 3 || state == 4) {
         if (k.getKeyCode() == KeyEvent.VK_ESCAPE) {
            reset();
            mainMenu();
         }
      } else if (state < 10) 
         return;
      char key = k.getKeyChar();
      handleKeys(key, true);
      if (key == 'e') {
         if (interacting > 0) interacting -= 1;
         else if (collidingHitboxes().size() > 0) interact(collidingHitboxes().get(0));
      }
      draw.repaint();
   }

   public void keyReleased(KeyEvent k) {
      char key = k.getKeyChar();
      handleKeys(key, false);
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
    * Private method to handle the movement of the player
    * <p>
    * <-------May 25------->
    * > added method
    * Contributor: Caleb Chue
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
   }

   /**
    * Private method to check whether the player's next move will result in a wall collision
    *
    * @param x The x coordinate of the player's location
    * @param y The y coordinate of the player's location
    * @return Whether the player's next move is legal
    * <p>
    * <-------May 25------->
    * > added method
    * > moved out of nested class to main class
    * Contributor: Caleb Chue
    */
   private boolean legalMove(double x, double y) {
      if (x < 0 || x > SCREEN_WIDTH || y < 0 || y > SCREEN_HEIGHT) 
         return false;
      for (int i = 0; i < obs.size(); i++) {
         if (obs.get(i) instanceof Obstacle && obs.get(i).colliding(x, y)) {
            return false;
         }
      }
      return true;
   }


   /**
    * Nested class to handle drawing on a canvas-like component
    * <p>
    * <-------May 24------->
    * > added implements KeyListener (keyboard input)
    * > added image drawing methods
    * Contributor: Caleb Chue
    */
   class Drawing extends JComponent {
   
      /**
       * <-------Shiv Kanade------->
       * Designed UI for levels, formatted text, and added clear instructions for users.
       *
       * Public overridden method to paint the component, drawing
       * different contents based on the state of the program
       *
       * @param g The graphics Object to draw on
       *          <p>
       *          <-------May 16------->
       *          > added drawing of splashscreen, including displaying image
       *          > added placeholder main menu graphic
       *          Contributor: Caleb Chue
       *          <p>
       *          <-------May 17------->
       *          > removed placeholder graphic
       *          Contributor: Caleb Chue
       *          <p>
       *          <-------May 18------->
       *          > added placeholder graphic for all states not handled by logic structure
       *          Contributor: Caleb Chue
       *          <p>
       *          <-------May 24------->
       *          > working on the drawing of the maze level
       *          Contributor: Caleb Chue
       */
      public void paint(Graphics g) {
         if (state == 0) { // splashscreen
            image("FocusForge Icon.png", g);
            g.setColor(new Color(0, 0, 0, drawState));
            g.fillRect(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);
            
         } else if (state == 3) { // instructions
            Color mainMenuBG = new Color(40, 87, 173);
            g.setColor(mainMenuBG);
            g.fillRoundRect(50, 30, 900, 600, 50, 50);
            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.BOLD, 22));
            g.drawString("Learning Level: ", 75, 85);
            g.setFont(new Font("Arial", Font.PLAIN, 20));

            g.drawString("Press Escape to return to the Main Menu", 315, 610);

            g.drawString("The Learning Level allows you to navigate the map (Using keyboard controls", 250, 83);
            g.drawString("W,A,S, & D). By interacting with characters on screen, an opportunity to", 250, 111);
            g.drawString("learn about the impacts of Attention Disorders is presented. Signs will also", 250, 139);
            g.drawString("be available around the map as a guide to locations as well as for instructions.", 250, 166);

            g.setFont(new Font("Arial", Font.BOLD, 22));
            g.drawString("Maze Level: ", 75, 274);
            g.setFont(new Font("Arial", Font.PLAIN, 20));
            g.drawString("The Maze Level provides you with an interesting and engaging way to apply", 250, 273);
            g.drawString("the knowledge gained throughout the learning level. By navigating a maze", 250, 300);
            g.drawString("completed. Arrows will indicate the tasks within and completing tasks which", 250, 327);
            g.drawString("test knowledge acquired, the maze level can be the maze.", 250, 354);

            g.setFont(new Font("Arial", Font.BOLD, 22));
            g.drawString("Escape Level: ", 75, 464);
            g.setFont(new Font("Arial", Font.PLAIN, 20));
            g.drawString("The Escape Level offers an exciting and challenging gameplay session where", 250, 463);
            g.drawString("you must gain as many points as possible under a time limit. Objects will be", 250, 490);
            g.drawString("travelling across the screen at different speeds, and the objects which are", 250, 517);
            g.drawString("beneficial should be selected, such as 'Creating a Schedule'. Objects such as", 250, 544);
            g.drawString("Objects such as a Phone will result in points being deducted.", 250, 571);
            
         } else if (state == 4) { // credits
            Color mainMenuBG = new Color(40, 87, 173);
            g.setColor(mainMenuBG);
            g.fillRoundRect(50, 30, 900, 600, 50, 50);
            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.BOLD, 22));
            g.drawString("Mind Mastery", 425, 255);
            g.setFont(new Font("Arial", Font.PLAIN, 20));

            g.drawString("Press Escape to return to the Main Menu", 315, 610);

            g.drawString("Created by Caleb Chue and Shiv Kanade", 295, 293);
            g.drawString("Caleb Chue: Program Development and Graphics", 261, 321);
            g.drawString("Shiv Kanade: Program Development and Graphics", 260, 349);
         } else if (state == 9 || state == 32) { // level complete screen
            g.setColor(new Color(127,127,255));
            g.drawRect(0,0,SCREEN_WIDTH,SCREEN_HEIGHT);
            g.setColor(Color.BLACK);
            g.setFont(new Font("Arial", Font.BOLD, 32));
            g.drawString("Level Complete!", 375, 200);
            g.drawString("Click anywhere to continue...", 275, 450);
            
         } else if (state >= 10 && state < 30) { // levels
            if (state < 20) {
               if (state == 10 || state == 13 || state == 14)
                  image("LearningLevelMap.png", SCREEN_WIDTH / 2, SCREEN_HEIGHT / 2, 1000, 700, g);
               if (state == 11)
                  image("FocusForgeLearningLevelHouseRoom.png", SCREEN_WIDTH / 2, SCREEN_HEIGHT / 2 - 25, 1000, 675, g);
               if (state == 12)
                  image("FocusForgeLearningLevelRoom.png", SCREEN_WIDTH / 2, SCREEN_HEIGHT / 2 - 25, 1000, 675, g);
               drawPlayer(g);
            } else if (state < 30) {
               if (state == 20) {
                  image("FocusForgeMazeLevel.png", SCREEN_WIDTH / 2, SCREEN_HEIGHT / 2 - 20, 1000, 700, g);
                  drawPlayer(g);
               }
            }
         
            ArrayList<Hitbox> playerColliding = collidingHitboxes();
         
            for (Hitbox ob : obs) {
            
               if (playerColliding.contains(ob) && interacting == 0) {
                  g.setColor(Color.WHITE);
                  g.setFont(new Font("Arial", Font.BOLD, 16));
                  String[] mess = ob.proximityMessage().split("\n");
                  for (int i = 0; i < mess.length; i++) {
                     g.drawString(mess[i], ob.x + ob.w / 2 - 4 * mess[i].length(), ob.y + ob.h - 60 + 20 * i);
                  }
               }
            }
         } else if (state == 30) { // action level instructions screen
            // instructions here
            Color mainMenuBG = new Color(40, 87, 173);
            g.setColor(mainMenuBG);
            g.fillRoundRect(50, 30, 900, 600, 50, 50);
            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.BOLD, 30));
            g.drawString("Action Level Instructions", 325, 75);
            g.setFont(new Font("Arial", Font.PLAIN, 18));
            g.drawString("Level Goal: In this level, you must apply effective decision and focus", 100, 175);
            g.drawString("and focus skills in order to select certain objects. These objects,",100, 200);
            g.drawString("shown on the right, represent beneficial activities, such as 'Creating",100,225);
            g.drawString("a Schedule'. By selecting these objects, points are added to the ",100,250);
            g.drawString("counter located at the top right. Remember,there is a timer counting",100,275);
            g.drawString("down! Make sure to select as many correct object as possible.", 100, 300);
            image("clickable-4.png",730, 255, g);
            image("clickable-2.png", 835, 190, g);
            image("clickable-5.png",840,275,g);
            g.drawString("Controls: To select a certain object, simply click on the object using a mouse pointer.", 135, 425);
            g.drawString("Click anywhere to continue to the Action Level.", 315, 610);   
                  
         } else if (state == 31) {
            for (Hitbox ob : obs) {
               if (ob instanceof Clickable) {
                  BufferedImage img = ((Clickable)ob).getImage();
                  int[] loc = ob.getPos();
                  image(img, loc[0], loc[1], g);
               }
            }
            image("FocusForgeActionLevel.png", SCREEN_WIDTH / 2, SCREEN_HEIGHT / 2 - 20, 1000, 700, g);
            g.setColor(Color.RED);
            g.setFont(new Font("Arial", Font.BOLD, 30));
            g.drawString("" + (time / 60), 350, 28);
            g.drawString("" + score, 785, 28);
            time--;
            if (time <= 0) {
               state = 32;
               username = JOptionPane.showInputDialog(null, "Enter your username: ", "Score Entry", JOptionPane.INFORMATION_MESSAGE);
               dataListWriter();
               return;
            }
         }
         
         if (state == 32) {
            g.drawString("Score: " + score, 400, SCREEN_HEIGHT/2);
         }
         
         if (interacting > 0) {
            String[] message = source.toString().split(" ");
            int[] messageLoc = new int[4];
            for (int i = 0; i < 4; i++) messageLoc[i] = Integer.parseInt(message[i]);
         
            message = source.interactedBehaviour().split("\n");
         
            String[] splitMessage = message[message.length - interacting].split(";");
            
            g.setColor(new Color(0,0,0,127));
            g.fillRect((messageLoc[0] + messageLoc[2])/2 - 45, messageLoc[1] - splitMessage.length*10-40, 475, 80);
            
            for (int part = 0; part < splitMessage.length; part++) {    
               g.setColor(Color.WHITE);
               g.setFont(new Font("Arial", Font.BOLD, 18));
               g.drawString(splitMessage[part], (messageLoc[0] + messageLoc[2] - splitMessage[part].length() * 9) / 2 + 200, messageLoc[1] + (part-splitMessage.length/2)*20);
            }
         }
      }
   
   
      public int rand(int lim) {
         return (int) (Math.random() * lim);
      }
   
      /**
       * Private method to draw the player on screen
       */
      private void drawPlayer(Graphics g) {
         image("FocusForgeMainCharacter.png", player[0], player[1], playerSize[0], playerSize[1], g);
      }
   
   
      /**
       * Private method to make drawing centered images more convenient
       * <p>
       * Contributor: Caleb Chue
       *
       * @param path The file path to the image
       * @param g    The Graphics image to draw with
       *             <p>
       *             <-------May 24------->
       *             > added method
       *             Contributor: Caleb Chue
       */
      private void image(String path, Graphics g) {
         image(path, SCREEN_WIDTH / 2, SCREEN_HEIGHT / 2, -1, -1, g);
      }
   
      private void image(String path, double x, double y, Graphics g) {
         image(path, x, y, -1, -1, g);
      }
      
      public void image(BufferedImage img, int x, int y, Graphics g) {
         g.drawImage(img, x, y, null);
      }
      
      /** 
       * Private method to return an image object from the specified path
       */ 
      private BufferedImage getImage(String path) throws IOException {
         return ImageIO.read(new File("images/" + path));
      }
   
      /**
       * Overloaded private method to make drawing images more convenient
       * <p>
       * Contributor: Caleb Chue
       *
       * @param path The file path to the image
       * @param x    The x coordinate of the image
       * @param y    The y coordinate of the image
       * @param g    The Graphics image to draw with
       *             <p>
       *             <-------May 24------->
       *             > added method
       *             Contributor: Caleb Chue
       *             <p>
       *             <-------May 26------->
       *             > fixed method drawing in the wrong place
       *             Contributor: Caleb Chue
       */
      private void image(String path, double x, double y, double wd, double ht, Graphics g) {
         // drawing image (source: https://stackoverflow.com/questions/17865465/how-do-i-draw-an-image-to-a-jpanel-or-jframe)
         try {
            BufferedImage im = getImage(path);
            if (wd == -1) {
               wd = im.getWidth();
               ht = im.getHeight();
            }
            g.drawImage(im, (int) (x - wd / 2), (int) (y - ht / 2), (int) wd, (int) ht, null);
         } catch (IIOException e) {
            System.out.println("Cannot load image from images/" + path);
            e.printStackTrace();
         
         } catch (IOException e) {
            e.printStackTrace();
         }
      }
   }


   /**
    * Main method
    *
    * @param args Arguments for the application
    */
   public static void main(String[] args) {
      Timer timer = new Timer();
      timer.schedule(new Mind_Mastery(), 0, 1);
   }
}