import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import javax.swing.*;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class Mind_Mastery implements ActionListener {
    private JFrame frame;
    Drawing draw;
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
    
    
    public Mind_Mastery() {
        frame = new JFrame("Mind Mastery");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        // getting the size of the screen (source: https://stackoverflow.com/questions/8141865/java-how-to-get-the-screen-dimensions-from-a-graphics-object)
        screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        load();
        
        splashScreen();
        
        mainMenu();
    }
    
    
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
        
        mainButtons = new JButton[] {new JButton("Begin Adventure"), new JButton("How To Play"), new JButton("Credits"), new JButton("Exit")};
//         begin = new JButton("Begin Adventure");
//         instr = new JButton("How to Play");
//         cred = new JButton("Credits");
//         exit = new JButton("Exit");
//         loadButton(begin);
//         loadButton(instr);
//         loadButton(cred);
//         loadButton(exit);

        mainMenuButtons.add(Box.createVerticalStrut(6*screenSize.height/17));
        for (int i = 0; i < mainButtons.length; i++) loadMainButton(i);
        
        mainMenu.add(mainMenuButtons);
        mainMenu.setVisible(false);
        
        frame.add(drawPanel);
        frame.add(mainMenu);
        
        frame.setExtendedState(Frame.MAXIMIZED_BOTH);
        frame.setSize(screenSize.width, screenSize.height);
        frame.setVisible(true);
        
    }
    
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
    
    
    private void splashScreen() {
        // splashscreen
        state = 0;
        drawPanel.setVisible(true);
        draw.setVisible(true);
        frame.setContentPane(drawPanel);
        
        draw.repaint();
        sleep(1000);
        for (; drawState < 255; drawState++) {
            draw.repaint(); // fading out the screen
            sleep(5);
        }
        sleep(1000);
        System.out.println("splash finished");
    }
    
    
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
    
    
    private void credits() {
        frame.setContentPane(credPanel);
    }
    
    
    private void instructions() {
        
    }
    
    
    public void sleep(int ms) {
        try{
            Thread.sleep(ms);
        } catch(Exception e){}
    }
    
    public void actionPerformed(ActionEvent e) {
        // main menu
        if (e.getSource() == mainButtons[0]) {
            state = 2;
            mainMenuButtons.setVisible(false);
        } else if (e.getSource() == mainButtons[1]) {
            state = 3;
            mainMenuButtons.setVisible(false);
        } else if (e.getSource() == mainButtons[2]) {
            state = 4;
            mainMenuButtons.setVisible(false);
        } else if (e.getSource() == mainButtons[3]) {
            System.out.println("please exit");
            frame.setVisible(false);
            frame.dispose();
            System.exit(0);
        } else if (e.getSource() == draw) {
            draw.repaint();
        }
        
        
        
        // draw.repaint();
    }
    
    class ClickHandler extends MouseAdapter {
        public void mouseClicked(MouseEvent e) {
            locx = e.getX();
            locy = e.getY();
            // draw.repaint();
        }
    }
    
    class Drawing extends JComponent {
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
            } else if (state == 1) { // main menu
                g.setColor(new Color(61, 68, 128));
                g.fillRect(0, 0, screenSize.width, screenSize.height);
                g.setColor(new Color(255,0,0));
                g.fillRect(100, 100, 500, 500);
                g.fillOval(locx,locy,10,10);
                System.out.println("Should draw main menu: " + locx + " " + locy);
            } else {
                g.setColor(new Color(255, 0, 255));
                g.fillRect(0, 0, screenSize.width, screenSize.height);
                System.out.println("should draw other >:(");
            }
        }
    }
    
    
    public static void main(String args[]) {
        new Mind_Mastery();
    }
}
 