import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;
public class Leaderboard implements ActionListener
{
    static String name1, name2, name3, name4, name5, name6;
    static String username[];
    static double score1, score2, score3, score4, score5, score6;

    Drawing draw = new Drawing();
    public Leaderboard()
    {
        JFrame frame = new JFrame("Leaderboard");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel panel = new JPanel();
        frame.add(draw);
        frame.setSize(1000,650);
        frame.setVisible(true);
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
            g.fillRect(0,0,1000,650);
            g.setColor(mainMenuBG);
            g.fillRoundRect (50, 50, 900, 525,50, 50);
            //Creates empty boxes outlining filled boxes
            g.setColor (Color.white);
            for(int i = 0; i<6; i++) {
                g.fillRoundRect(250, 90+i*80, 500, 40, 25, 25);
            }
            g.setColor(mainMenuBG);
            g.fillRect(497, 70, 5, 500);
            g.setColor(Color.black);
            g.setFont(new Font("Serif", Font.BOLD, 30));
            g.drawString("Player                     Score", 340, 118);
            double[] test1 = {5,3,1,4,2};
            String[] test2 = {"bob", "bob", "bob", "bob", "bob"};
            //dataListWriter();
            //dataListReader();
            scoreSort(test1,test2);
            printArray(g, test1,test2);
        }
    }

    void dataListWriter ()
    {
        File scoreFile = new File ("scoresList.txt");
        if (scoreFile.exists ())
        //checks if the text file scoresList exists
        {
            try
            {
                FileWriter fileWrite = new FileWriter ("scoresList.txt", true);
                BufferedWriter output = new BufferedWriter (fileWrite);
                //If scoreList already exists, it does not create a new text file and moves on to adding the new scores and usernames
                output.write ("");
                //This appends the scores and usernames of the two players who have just played the game to the end of the text file
                output.close ();
                //closes file writer
            }
            catch (IOException e)
            {
            }
            //This catches exception e
        }
        else
        {
            try
            {
                scoreFile.createNewFile ();
                FileWriter fileWrite = new FileWriter ("scoresList.txt", true);
                BufferedWriter output = new BufferedWriter (fileWrite);
                output.close ();
                //closes the file writer
            }
            catch (IOException e)
            {
            }
            //catches exception e
        }
    }


    public void dataListReader ()
    {
        File scoreFile = new File ("scoresList.txt");
        if (scoreFile.exists ())
        {
            try
            {
                //Checks if scoresList exists, and if it does, it moves on to reading the text file
                BufferedReader text = new BufferedReader (new FileReader ("scoresList.txt"));
                score1 = Double.parseDouble (text.readLine ());
                name1 = String.valueOf (text.readLine ());
                score2 = Double.parseDouble (text.readLine ());
                name2 = String.valueOf (text.readLine ());
                score3 = Double.parseDouble (text.readLine ());
                name3 = String.valueOf (text.readLine ());
                score4 = Double.parseDouble (text.readLine ());
                name4 = String.valueOf (text.readLine ());
                score5 = Double.parseDouble (text.readLine ());
                name5 = String.valueOf (text.readLine ());
                score6 = Double.parseDouble (text.readLine ());
                name6 = String.valueOf (text.readLine ());

                //Reads 24 lines from the text file, storing the scores and usernames in variables
                double scores[] = {score1, score2, score3, score4, score5, score6};
                String username[] = {name1, name2, name3, name4, name5, name6};
                //declares array username
                //scoreSort (scores, username); //runs scoreSort for array scores and username
                //printArray (scores, username); //runs printArray for array scores and username
                //arrayStorer (scores, username); //runs arrayStorer for array scores and username
            }
            catch (IOException e)
            {
            }
            //catches exception e
        }
        else
        {
            try
            {
                scoreFile.createNewFile ();
                //If scoresList is not created, it creates it.
                BufferedReader text = new BufferedReader (new FileReader ("scoresList.txt"));
                score1 = Double.parseDouble (text.readLine ());
                name1 = String.valueOf (text.readLine ());
                score2 = Double.parseDouble (text.readLine ());
                name2 = String.valueOf (text.readLine ());
                score3 = Double.parseDouble (text.readLine ());
                name3 = String.valueOf (text.readLine ());
                score4 = Double.parseDouble (text.readLine ());
                name4 = String.valueOf (text.readLine ());
                score5 = Double.parseDouble (text.readLine ());
                name5 = String.valueOf (text.readLine ());
                score6 = Double.parseDouble (text.readLine ());
                name6 = String.valueOf (text.readLine ());
                //reads first 24 lines from the text file
                double scores[] = {score1, score2, score3, score4, score5, score6};
                String username[] = {name1, name2, name3, name4, name5, name6};
                //declares arrays scores and username
                //scoreSort (scores, username); //runs scoreSort for array scores and username
                //printArray (scores, username); //runs printArray for array scores and username
                //arrayStorer (scores, username); //runs arrayStorer for array scores and username
            }
            catch (IOException e)
            {
            }
            //catches exception e
        }
    }

    void scoreSort (double[] arr1, String[] arr2)
    {
        for (int i = 0 ; i < 4 ; i++)
            // loop one, which runs the code sorter 11 times
            for (int j = 0 ; j < 4 - i ; j++)
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
            FileWriter fileWipe1 = new FileWriter ("scoresList.txt", false);
            BufferedWriter output1 = new BufferedWriter (fileWipe1);
            output1.write ("");
            output1.close ();
            //Wipes the text file and closes file writer used to wipe
            FileWriter fileRewrite1 = new FileWriter ("scoresList.txt", true);
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
            g.drawString (arr2 [i], 265, 200 + 80 * i);
            g.drawString (String.valueOf (arr1 [i]), 515, 200 + 80 * i);
        }
        //This section of my code prints the arrays on the leaderboard, and displays the data
    }

    public static void main(String[] args)
    {
        new Leaderboard();

    }
}