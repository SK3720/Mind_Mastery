/** 
Class to handle physical walls the player can bump into

    <-------May 29------->
      > separated Obstacle class into separate child classes of Hitbox
      Contributor: Caleb Chue

*/

public class Trigger extends Hitbox {
    int id;

    public Trigger(int xPos, int yPos, int wid, int hgt, int i) {
        super(xPos, yPos, wid, hgt);
        id = i;
    }
    
    public String interactedBehaviour() {
    
        if (id == 5) return "goto 11";
        else if (id == 10) return "goto 0";
        return "";
    }
    public String proximityMessage() {
        // learning sign 1
        if (id == 0) return "Welcome to the Learning Area";
        
        if (id == 1) return "wow";
        
        
        if (id == 5) return "Enter the learning center";
        return null;
    }
}
