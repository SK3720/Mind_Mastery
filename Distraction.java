public class Distraction extends Hitbox {
    int type;

    public Distraction(int xPos, int yPos, int wid, int hgt) {
        super(xPos, yPos, wid, hgt);
    }
    
    public void interactedBehaviour() {
        
    }
    
    public String proximityMessage() {
        return "Click Me!";
    }
}
