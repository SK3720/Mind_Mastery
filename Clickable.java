import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

/** 
Class to handle physical walls the player can bump into

    <-------May 29------->
      > separated Obstacle class into separate child classes of Hitbox
      Contributor: Caleb Chue

*/

public class Clickable extends Hitbox {
    int type;
    int[] vel;
    BufferedImage img;
    
    public Clickable(int xPos, int yPos, int wdt, int hgt, int typ, BufferedImage im) {
        super(xPos, yPos, wdt, hgt);
        
        type = typ;
        img = im;
        vel = vl;
    }
    
    public void handle() {
        move(vel[0], vel[1]);
    }
    
    public String interactedBehaviour() {
        if (type == 0) // task
            return "add";
        
        // distraction
        return "deduct";
    }
    public String proximityMessage() { return ""; }
}