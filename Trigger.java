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
        if (id == 2) return "goto 11";
        if (id == 3) return "paragraph \nOh hi, didn't see you there. *sigh*\nSchool hasn't been too good for me...\nI can't focus in class and it's really bringing down my grades.\nI need a better way to focus in the future...";
        if (id == 4) return "goto 13";
        if (id == 5) return "goto 12";
        if (id == 6) return "goto 14";
        if (id == 10) return "goto 0";
        return "";
    }
    
    public String proximityMessage() {
        // learning sign 1
        if (id == 0) return "Welcome to the Learning Area! \n Head to the Red House to begin.";
        if (id == 1) return "wow";
        if (id == 2) return "Enter the Red House";
        if (id == 3) return "Talk to James";
        if (id == 4) return "Exit the Red House";
        if (id == 5) return "Enter the learning center";
        if (id == 6) return "Exit the learning center";
        
        
        return "";
    }
}
