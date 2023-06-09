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
    final int SPEED = 8;
    
    public Clickable(int xPos, int yPos, int wdt, int hgt, int typ, BufferedImage im) {
        super(xPos, yPos, wdt, hgt);
        if (xPos < 500) vel = new int[] {SPEED, 0};
        else vel = new int[] {-SPEED, 0};
        type = typ;
        img = im;
    }
    
    public boolean handle() {
        move(vel[0], vel[1]);
        int[] pos = super.getPos();
        return (pos[0] < 0 || pos[0] > 1000 || pos[1] < 0 || pos[1] > 700);
    }
    
    public BufferedImage getImage() {
        return img;
    }
    
    public String interactedBehaviour() {
        if (type == 0) // task
            return "add";
        
        // distraction
        return "deduct";
    }
    public String proximityMessage() { return ""; }
}