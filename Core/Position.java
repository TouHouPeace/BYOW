package byow.Core;


public class Position {
    private int x;
    private int y;

    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX(){
        return this.x;
    }

    public int getY(){
        return this.y;
    }

    public void setX(int x){
        this.x = x;
    }

    public void setY(int y){
        this.y = y;
    }

    public static int CompareX(Position p1, Position p2){
        return p1.getX() - p2.getX();
    }

    public static int CompareY(Position p1, Position p2){
        return p1.getY() - p2.getY();
    }
}
