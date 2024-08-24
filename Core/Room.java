package byow.Core;
import java.util.*;

public class Room {
    private Position pos;
    private int height;
    private int width;
    private ArrayList<Position> wall;
    private ArrayList<Position> span;

    public Room(Position startingPos, int h, int w){
        this.height = h;
        this.width = w;
        this.pos = startingPos;
        this.span = new ArrayList<Position>();
        this.wall = new ArrayList<Position>();

        int rowEnd = pos.getY() + h;
        int colEnd = pos.getX() + w;

        //add walls to all the edges
        for(int r = pos.getY() - 1; r < rowEnd + 1; r++){
            for(int c = pos.getX() - 1; c < colEnd + 1; c++){
                if(r == pos.getY() - 1 || r == rowEnd || c == pos.getX() - 1 || c == colEnd){
                    wall.add(new Position(c, r));
                }
                else{
                    span.add(new Position(c, r));
                }
            }
        }
    }

    public ArrayList<Position> getWall(){
        return this.wall;
    }

    public ArrayList<Position> getSpan(){
        return this.span;
    }

    public int getHeight(){
        return this.height;
    }

    public int getWidth(){
        return this.width;
    }
}
