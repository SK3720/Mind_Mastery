public abstract class Obstacle {
    int[] pos;
    
    public Obstacle(int x, int y, int wid, int hgt) {
        pos = new int[] {x,y,wid,hgt};
    }
    
    public int[] loc() {
        return pos;
    }
    
    public abstract void interactedBehaviour();
    public abstract void proximityBehaviour();
}
