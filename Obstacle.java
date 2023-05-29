public abstract class Obstacle {
    public int x, y, w, h;
    
    public Obstacle(int xPos, int yPos, int wid, int hgt) {
        x = xPos;
        y = yPos;
        w = wid;
        h = hgt;
    }
    
    public abstract void interactedBehaviour();
    public abstract void proximityBehaviour();
}
