/** 
Class to handle physical walls the player can bump into

    <-------May 29------->
      > separated Obstacle class into separate child classes of Hitbox
      Contributor: Caleb Chue

*/

public class Obstacle extends Hitbox {

    public Obstacle(int xPos, int yPos, int wid, int hgt) {
        super(xPos, yPos, wid, hgt);
    }
    
    public String interactedBehaviour() { return ""; }
    public String proximityMessage() {
        return null;
    }
}
