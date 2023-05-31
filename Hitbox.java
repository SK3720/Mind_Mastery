/** 
Abstract class to handle interaction between a hitbox and the player

    <-------May 29------->
      > separated Obstacle class into separate child classes of Hitbox
      Contributor: Caleb Chue
*/ 

public abstract class Hitbox {
    public int x, y, w, h;
    private int pW, pH;
    
    public Hitbox(int xPos, int yPos, int wid, int hgt) {
        x = xPos;
        y = yPos;
        w = wid;
        h = hgt;
    }
    
    @Override
    public String toString() {
        return x + " " + y + " " + w + " " + h;
    }
    
    
    public void setPlayerSize(int[] arr) {
        pW = arr[0];
        pH = arr[1];
    }
    
    
    /** 
    Public method to determine whether the player is colliding
    with the current hitbox
    
    @param x The x-coordinate of the player
    @param y The y-coordinate of the player
    @return Whether the player collides with the obstacle
    */ 
    public boolean colliding(int pX, int pY) {
        for (int i = -1; i < 2; i++) {
            for (int j = -1; j < 2; j++) {
                if (collidePointRect(pX + pW/2*i, pY + pH/2*j, x, y, x+w, y+h)) {
                    System.out.println("Collision: " + (pX + pW/2*i) + " " + (pY + pH/2*j) + " " + x + " " + y + " " + (x+w) + " " + (y+h));
                    return true;
                }
            }
        }
        return false;
    }

    /** 
    Private method to check whether a point is within a box
    
    @param x The x coordinate of the point
    @param y The y coordinate of the point
    @param x1 The x coordinate of the top left corner of the box
    @param y1 The y coordinate of the top left corner of the box
    @param x2 The x coordinate of the bottom right corner of the box
    @param y2 THe y coordinate of the bottom right corner of the box
    @return Whether the point is within the bounds of the box
    
    <-------May 25------->
      > added method
      > moved method from nested class to main class
      Contributor: Caleb Chue
    
    */
    private boolean collidePointRect(int x, int y, int x1, int y1, int x2, int y2) {
        return x >= x1 && x <= x2 && y >= y1 && y <= y2;
    }
    
    
    public abstract void interactedBehaviour();
    public abstract void proximityBehaviour();
}