public class Distraction extends Hitbox {
    int type;

    public Distraction(int xPos, int yPos, int wid, int hgt, int typ) {
        super(xPos, yPos, wid, hgt);
        type = typ;
    }
    
    public void interactedBehaviour() {
        
    }
    
    public String proximityMessage() {
        return "Click Me!";
    }
}
