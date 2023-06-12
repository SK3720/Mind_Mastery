import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;

public class Leaderboard implements ActionListener
{
    static String name1, name2, name3, name4, name5, name6;
    String username[] = new String[6];
    double scores[] = new double [6];
    static double score1, score2, score3, score4, score5, score6;

    Drawing draw = new Drawing();
    public Leaderboard()
    {
        JFrame frame = new JFrame("Leaderboard");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        JPanel panel = new JPanel();
        frame.add(draw);
        frame.setSize(800,500);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        frame.setFocusable(true);

    }
    public void actionPerformed(ActionEvent e)
    {
    }
    class Drawing extends JComponent
    {
        public void paint(Graphics g)
        {
            //Sets background
            Color mainMenuBG = new Color(40, 87, 173);
            Color splashScreenColor = new Color(191,215,255);
            g.setColor(splashScreenColor);
            g.fillRect(0,0,800,500);
            g.setColor(mainMenuBG);
            g.fillRoundRect (25, 55, 750, 400,50, 50);
            //Creates empty boxes outlining filled boxes
            g.setColor (Color.white);
            for(int i = 0; i<6; i++) {
                g.fillRoundRect(103, 80+i*65, 600, 30, 25, 25);
            }
            g.setColor(mainMenuBG);
            g.fillRect(397, 120, 5, 320);
            g.setColor(Color.black);
            g.setFont(new Font("Serif", Font.BOLD, 30));
            g.drawString("Player                         Score", 225, 108);
            dataListReader();
            scoreSort (scores, username); //runs scoreSort for array scores and username
            printArray (g, scores, username); //runs printArray for array scores and username
            arrayStorer (scores, username); //runs arrayStorer for array scores and username
        }
    }

    public void dataListReader ()
    {
        File scoreFile = new File ("leveldata/scoresList.txt");
        if (scoreFile.exists ())
        {
            try
            {
                //Checks if scoresList exists, and if it does, it moves on to reading the text file
                BufferedReader text = new BufferedReader (new FileReader ("leveldata/scoresList.txt"));
                scores[0] = Double.parseDouble (text.readLine ());
                username[0] = String.valueOf (text.readLine ());
                scores[1] = Double.parseDouble (text.readLine ());
                username[1] = String.valueOf (text.readLine ());
                scores[2] = Double.parseDouble (text.readLine ());
                username[2] = String.valueOf (text.readLine ());
                scores[3] = Double.parseDouble (text.readLine ());
                username[3] = String.valueOf (text.readLine ());
                scores[4] = Double.parseDouble (text.readLine ());
                username[4] = String.valueOf (text.readLine ());
                String possibleNull = text.readLine();
                if(possibleNull != null) {
                    scores[5] = Double.parseDouble(possibleNull);
                    username[5] = String.valueOf(text.readLine());
                }
                //Reads lines from the text file, storing the scores and usernames in variables
                //declares array username
            }
            catch (IOException e)
            {
            }
            //catches exception e
        }
    }

    void scoreSort (double[] arr1, String[] arr2)
    {
        for (int i = 0 ; i < 5 ; i++)
            // loop one, which runs the code sorter
            for (int j = 0 ; j < 5 - i ; j++)
                // loop two, which runs through the array elements, and goes through each element depending on what run it is, example i = 4
                if (arr1 [j] < arr1 [j + 1])
                {
                    double scoreHolder = arr1 [j];
                    arr1 [j] = arr1 [j + 1];
                    arr1 [j + 1] = scoreHolder;
                    //This part swaps two elements in the array for scores if one is smaller than the other
                    String nameHolder = arr2 [j];
                    arr2 [j] = arr2 [j + 1];
                    arr2 [j + 1] = nameHolder;
                    //This part swaps two elements in the array for usernames if two elements in the scores array are swapped
                    //The swapping is synchronized between both arrays, keeping it organized.
                }
    }

    void arrayStorer (double[] arr1, String[] arr2)
    {
        try
        {
            FileWriter fileWipe1 = new FileWriter ("leveldata/scoresList.txt", false);
            BufferedWriter output1 = new BufferedWriter (fileWipe1);
            output1.write ("");
            output1.close ();
            //Wipes the text file and closes file writer used to wipe
            FileWriter fileRewrite1 = new FileWriter ("leveldata/scoresList.txt", true);
            BufferedWriter output2 = new BufferedWriter (fileRewrite1);
            //Creates second file writer to append to the text file
            output2.write (arr1 [0] + "\n");
            output2.write (arr2 [0]);
            for (int i = 1 ; i < 5 ; i++)
            {
                output2.write ("\n" + (String.valueOf (arr1 [i])));
                output2.write ("\n" + (String.valueOf (arr2 [i])));
            }
            output2.close ();
            //It then writes the top the scores and the usernames with them, which gets rid of the bottom 2 scores and username
        }
        catch (IOException e)
        {
        }
        //catches exception e
    }

    void printArray (Graphics g, double[] arr1, String[] arr2)
    {
        for (int i = 0 ; i < 5 ; ++i)
        {
            g.drawString (arr2 [i], 120, 170 + 65 * i);
            g.drawString (String.valueOf (arr1 [i]), 415, 170 + 65 * i);
        }
        //This section of my code prints the arrays on the leaderboard, and displays the data
    }
}