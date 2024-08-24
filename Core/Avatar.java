package byow.Core;

import byow.TileEngine.TETile;

public class Avatar {
    private Position pos;
    private TETile tile;

    public Avatar(Position p, TETile t) {
        this.pos = p;
        this.tile = t;
    }

    public int getX(){
        return this.pos.getX();
    }

    public int getY(){
        return this.pos.getY();
    }

    public void setX(int x){
        this.pos.setX(x);
    }

    public void setY(int y){
        this.pos.setY(y);
    }

    public Position getPos() {
        return this.pos;
    }

    public TETile getTile() {
        return this.tile;
    }
}
