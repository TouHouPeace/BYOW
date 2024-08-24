package byow.Core;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Out;
import edu.princeton.cs.algs4.StdDraw;

import byow.TileEngine.*;
import java.awt.*;
import java.util.*;
import java.io.*;
import java.text.SimpleDateFormat;

public class Engine {
    TERenderer ter = new TERenderer();
    private Random rand;
    private Avatar avatar;
    private ArrayList<Room> rooms;
    private String playerInput;
    private String seed;
    private TETile[][] world;

    public static final int WIDTH = 80;
    public static final int HEIGHT = 30;

    public Engine() {
        //default constructor
        this.avatar = null;
        this.rand = null;
        this.rooms = new ArrayList<Room>();
        this.seed = null;
        this.playerInput = null;
        this.world = new TETile[WIDTH][HEIGHT];
    }

    public Position randomPosition(){
        //generate random positions that would be used to generate random rooms
        //the bound is 1-13 because the room could have dimensions from 6-12
        int x = 1 + rand.nextInt(WIDTH - 13);
        int y = 1 + rand.nextInt(HEIGHT - 13);
        return new Position(x, y);
    }

    public Room generateRoom() {
        //generate rooms using random locations
        //checks whether there is overlap
        //recursively calls the function if there is overlap
        Position startPosition = randomPosition();
        //get the random location
        int width = 6 + rand.nextInt(6);
        int height = 6 + rand.nextInt(6);
        //get random dimensions
        Room newRoom = new Room(startPosition, width, height);
        for (Room r : rooms) {
            //checks if there is overlap in the list of rooms
            if (overlaps(r, newRoom)) {
                return generateRoom();
                //recursively calls the function if there is
            }
        }
        return newRoom;
    }

    public void drawRoom(Room r) {
        for (Position p : r.getSpan()) {
            //get the list of spans for the room, and have them all changed to floors
            world[p.getX()][p.getY()] = Tileset.FLOOR;
        }
        for (Position p : r.getWall()) {
            //get the list of walls for the room and have them all changed to walls
            world[p.getX()][p.getY()] = Tileset.WALL;
        }
    }

    public boolean overlaps(Room a, Room b) {
        //checks if the spans of two rooms overlaps or not
        for (Position p1 : b.getSpan()) {
            //span of room one
            for (Position p2 : a.getSpan()) {
                //span of room two
                if (Math.abs(p1.getX() - p2.getX()) <= 3 && Math.abs(p1.getY() - p2.getY()) <= 3) {
                    return true;
                }
            }
        }
        return false;
    }

    public void resetWorld(){
        //resets the world
        this.rand = null;
        this.rooms = new ArrayList<Room>();
        this.avatar = null;
        this.seed = "";
        this.playerInput = null;
    }

    public void DrawIntroFrame(){
        //draws the intro frame
        StdDraw.clear();
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.text(WIDTH * 8, HEIGHT * 12, "2D TILE GAME");
        StdDraw.text(WIDTH * 8, HEIGHT * 5, "NEW GAME (N/n)");
        StdDraw.text(WIDTH * 8, HEIGHT * 4, "LOAD GAME (L/l)");
        StdDraw.text(WIDTH * 8, HEIGHT * 3, "REPLAY GAME (R/r)");
        StdDraw.text(WIDTH * 8, HEIGHT * 2, "QUIT GAME (Q/q)");
        StdDraw.show();
    }

    public void DrawSeedFrame(String seed){
        //Draws the screen for the seed input
        StdDraw.clear();
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.text(WIDTH * 8, HEIGHT * 12, "Enter your seed: ");
        StdDraw.text(WIDTH * 8, HEIGHT * 10, "Seed: " + seed);
        StdDraw.show();
    }

    public void DrawHUD(String status, int statusX){
        //draws the HUD for the game, including information such as:
        //Player coordinates, the type of tile which the cursor is pointing at
        //Functionality of the game, Instructions
        StdDraw.setPenColor(StdDraw.WHITE);
        StdDraw.rectangle(5, HEIGHT + 2, WIDTH, 2);
        //Box for the HUD

        StdDraw.text(3, HEIGHT + 2, "Status: ");
        StdDraw.text(statusX, HEIGHT + 2, status);
        //Shows the status

        StdDraw.text(19, HEIGHT + 2, "PLAYER COORDS: ");
        StdDraw.text(25, HEIGHT + 2, "(" + avatar.getX() + ", " + avatar.getY() + ")");
        //Shows the location of the avatar

        int mouseX = Math.min((int) StdDraw.mouseX(), WIDTH - 1);
        int mouseY = Math.min((int) StdDraw.mouseY(), HEIGHT - 1);
        //Get the location of the mouse
        TETile mouseTile = this.world[mouseX][mouseY];
        //Get the tile that the mouse is pointing at
        StdDraw.text(4, HEIGHT + 1, "MOUSE OVER: ");
        StdDraw.text(9, HEIGHT + 1, mouseTile.description());
        //Shows the tile that the mouse is pointing at

        StdDraw.text(35.5, HEIGHT + 2, "INSTRUCTIONS: ");
        //Shows the instructions
        if (status.equals("REPLAY")) {
            StdDraw.text(48, HEIGHT + 2, "Press [SPACE] to advance one frame");
        }
        else if (status.equals("REPLAY OVER")) {
            StdDraw.text(46.5, HEIGHT + 2, "Type :r to replay from start");
        }
        else if (status.equals("PLAY")) {
            StdDraw.text(47, HEIGHT + 2, "Use [W][A][S][D] to move around");
        }
        StdDraw.line(13, HEIGHT, 13, HEIGHT + 3);
        StdDraw.line(29.5, HEIGHT, 29.5, HEIGHT + 3);

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
        Date date = new Date(System.currentTimeMillis());
        StdDraw.text(WIDTH - 8, HEIGHT + 2, formatter.format(date));
        StdDraw.show();
        StdDraw.pause(50);
        //Shows the Date
    }

    public String gameOption(){
        //respond to the inputs of the user in the main screen
        while(true){
            if(StdDraw.hasNextKeyTyped()){
                char key = StdDraw.nextKeyTyped();
                if(key == 'n' || key == 'l' || key == 'q' || key == 'r'){
                    //detects if the user use any of the commands of the main screen
                    return Character.toString(key);
                }
            }
        }
    }

    public String gameSeed(){
        //respond to the keys input for seed
        String s = "";
        char lastChar = 0;
        while(lastChar != 's'){
            DrawSeedFrame(s);
            if(StdDraw.hasNextKeyTyped()){
                lastChar = StdDraw.nextKeyTyped();
                s += lastChar;
            }
        }
        return s.substring(0, s.length() - 1);
    }


    public void interactWithKeyboard() {
        StdDraw.setCanvasSize(WIDTH * 16, HEIGHT * 16);
        //The Starting Screen
        Font font = new Font("Arial", Font.BOLD, 30);
        //Choose a font that we would like
        StdDraw.setFont(font);
        StdDraw.setXscale(0, WIDTH * 16);
        StdDraw.setYscale(0, HEIGHT * 16);
        StdDraw.clear(Color.BLACK);
        StdDraw.enableDoubleBuffering();

        DrawIntroFrame();
        String opt = gameOption();
        //opt would be the input of the player on the starting screen
        if (opt.equals("n")) {
            //if they choose new game
            String inputString = opt + gameSeed() + "s";
            startGame(inputString);
        }
        else if (opt.equals("l")) {
            //if they choose load game
            startGame(opt);
        }
        else if (opt.equals("r")) {
            //if they choose replay game
            replayGame();
        }
        else if (opt.equals("q")) {
            //if they choose quit
            System.exit(0);
        }
    }

    public void startGame(String input){
        //function to start the game
        TERenderer tr = new TERenderer();
        //initialize the new TERenderer object
        tr.initialize(WIDTH, HEIGHT + 3, 0, 0);
        //initialize the game frame with the preset height and width

        boolean isActive = true;
        //set the boolean variable that determines whether the game is active to true

        while(isActive){
            if(StdDraw.hasNextKeyTyped()){
                char key = StdDraw.nextKeyTyped();
                isActive = UpdateWorld(key);
            }

            DrawHUD("PLAY", 6);
            tr.renderFrame(this.world);
        }
    }

    public void replayGame(){
        //restart the game with a new map
        TERenderer tr = new TERenderer();
        tr.initialize(WIDTH, HEIGHT + 3, 0, 0);
        String input = "";

        try {
            //try and read a existing data file
            FileReader reader = new FileReader("data.txt");
            BufferedReader bufferedReader = new BufferedReader(reader);
            input = bufferedReader.readLine();
            reader.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        int divide = input.indexOf("s") + 1;
        String savedMoves = input.substring(divide);
        interactWithInputString(input.substring(0, divide));

        int index = 0;
        String keyLog = "";
        boolean IsActive = true;

        while (IsActive && index < savedMoves.length()) {
            DrawHUD("REPLAY", 7);
            //Draw the replay screen

            if (StdDraw.hasNextKeyTyped()) {
                char key = StdDraw.nextKeyTyped();
                keyLog += key;
                if (key == ' ') {
                    UpdateWorld(savedMoves.charAt(index));
                    index++;
                }
            }
            if (keyLog.length() > 1 && keyLog.substring(keyLog.length() - 2).equals(":q")) {
                //If the command is to quit the game
                IsActive = false;
            }

            if (keyLog.length() > 1 && keyLog.substring(keyLog.length() - 2).equals(":r")) {
                //If the command is to restart the game, reset everything
                seed = "";
                playerInput = "";
                rooms = new ArrayList<Room>();
                replayGame();
            }

            if (keyLog.length() > 1 && keyLog.substring(keyLog.length() - 2).equals(":h")) {
                //reset the world
                resetWorld();
                interactWithKeyboard();
            }
            tr.renderFrame(this.world);
        }

        StdDraw.clear();

        while (IsActive) {
            DrawHUD("REPLAY OVER", 8);
            ter.renderFrame(this.world);

            if (StdDraw.hasNextKeyTyped()) {
                char key = StdDraw.nextKeyTyped();
                keyLog += key;
            }

            if (keyLog.length() > 1 && keyLog.substring(keyLog.length() - 2).equals(":q")) {
                //If the command is to quit
                IsActive = false;
            }

            if (keyLog.length() > 1 && keyLog.substring(keyLog.length() - 2).equals(":r")) {
                //If the command to is to restart
                seed = "";
                playerInput = "";
                rooms = new ArrayList<Room>();
                replayGame();
            }

            if (keyLog.length() > 1 && keyLog.substring(keyLog.length() - 2).equals(":h")) {
                resetWorld();
                interactWithKeyboard();
            }
        }
        System.exit(0);

    }

    public boolean UpdateWorld(char key) {
        if (key != 'a' && key != 'd' && key != 's' && key != 'w' && key != ':' && key != 'q') {
            //these are valid keys that will update the world
            //wasd represents the movement, and :q represents quiting the game
            return true;
        }

        if (playerInput.length() > 0) {
            //If there is still player input
            char lastChar = playerInput.charAt(playerInput.length() - 1);
            if (lastChar != ':' && key == 'q') {
                return true;
            }
            if (lastChar == ':' && key == 'q') {
                //if the given command (:q) signifies quitting the game
                try {
                    //try and save this current game into a file called data.txt
                    //saves the seed and the entirety of player inputs into the data file
                    FileWriter writer = new FileWriter("data.txt");
                    int last = this.playerInput.length() - 1;
                    writer.write(this.seed + this.playerInput.substring(0, last));
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return false;
            }
            if (lastChar == ':' && key != 'q') {
                playerInput = playerInput.substring(0, playerInput.length() - 1);
            }
        }

        //Now deal with the inputs that moves the player avatar
        int x = this.avatar.getX();
        int y = this.avatar.getY();
        int changeX = 0;
        int changeY = 0;
        if (key == 'a') {
            //move left
            changeX = -1;
        }
        else if (key == 'd') {
            //move right
            changeX = 1;
        }
        else if (key == 's') {
            //move down
            changeY = -1;
        }
        else if (key == 'w') {
            //move up
            changeY = 1;
        }

        if (!world[x + changeX][y + changeY].equals(Tileset.WALL)) {
            //if the movement input doesn't move into a wall, enter this if statement
            world[x][y] = Tileset.FLOOR;
            //set the tile to a floor, since the avatar could be on it
            world[x + changeX][y + changeY] = this.avatar.getTile();
            //the tile that the avatar moves to is also a floor
            this.avatar.setX(x + changeX);
            //change the x coordinate of the avatar
            this.avatar.setY(y + changeY);
            //change the y coordinate of the avatar
            this.playerInput += key;
        } else if (key == ':' || key == 'q') {
            this.playerInput += key;
        }
        return true;
    }

    public void connect(Room roomA, Room roomB) {
        //connects the two rooms
        ArrayList<Position> spanA = roomA.getSpan();
        ArrayList<Position> spanB = roomB.getSpan();
        //get the spans of the two rooms
        Position pointA = spanA.get(rand.nextInt(spanA.size()));
        Position pointB = spanB.get(rand.nextInt(spanB.size()));
        //get two random points in the two rooms respective spans
        Position start = Position.CompareX(pointA, pointB) < 0 ? pointA : pointB;
        //returns pointA if the x value of pointA is smaller than pointB, and vice versa
        Position end = start == pointA ? pointB : pointA;
        //returns point B is point A is the start, vice versa

        for (int col = start.getX() - 1; col < end.getX() + 2; col++) {
            for (int row = start.getY() - 1; row < start.getY() + 2; row++) {
                if (row == start.getY() && col >= start.getX() && col <= end.getX()) {
                    world[col][row] = Tileset.FLOOR;
                } else if (world[col][row] != Tileset.FLOOR) {
                    world[col][row] = Tileset.WALL;
                }
            }
        }

        Position corner = new Position(end.getX(), start.getY());
        start = Position.CompareY(corner, end) < 0 ? corner : end;
        end = start == corner ? end : corner;

        for (int row = start.getY() - 1; row < end.getY() + 2; row++) {
            for (int col = start.getX() - 1; col < end.getX() + 2; col++) {
                if (col == start.getX() && row >= start.getY() && row <= end.getY()) {
                    world[col][row] = Tileset.FLOOR;
                } else if (world[col][row] != Tileset.FLOOR) {
                    world[col][row] = Tileset.WALL;
                }
            }
        }

    }

    public TETile[][] interactWithInputString(String input) {
        //this method handles all inputs

        if (input.charAt(0) == 'l' || input.charAt(0) == 'L') {
            //if the input string means load
            try {
                //try and load from the data.txt file
                FileReader reader = new FileReader("data.txt");
                BufferedReader bufferedReader = new BufferedReader(reader);
                input = bufferedReader.readLine() + input.substring(1);
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        int index = 0;
        while(input.charAt(index) != 'n' || input.charAt(index) != 'N')
        {
            index ++;
            if(index == input.length()){
                break;
            }
        }
        //loop through the input until 'n' is met
        index++;
        //now the index is at the index after n

        while(input.charAt(index) != 's' || input.charAt(index) != 'S'){
            this.seed += input.charAt(index);
            index ++;
            if(index < input.length()){
                break;
            }
        }
        //now the index is at the last char if the string is not s, or it is at s

        Long seed = Long.parseLong(this.seed);
        //parse the seed into a long variable
        if(index == input.length()){
            ;
        }
        rand = new Random(seed);
        //generate a new random number with the parsed seed.

        //generate rooms with all tiles starting as void
        int numRooms = 5 + rand.nextInt(5);
        //generate 5-10 rooms at random
        for (int i = 0; i < numRooms; i++) {
            Room r = generateRoom();
            rooms.add(r);
            drawRoom(r);
        }

        //generate the hallways that connects the rooms
        UnionFind uf = new UnionFind(rooms.size());
        for (int i = 0; i < 50; i++) {
            int a = rand.nextInt(rooms.size());
            int b = rand.nextInt(rooms.size());
            Room roomA = rooms.get(a);
            Room roomB = rooms.get(b);
            if (!uf.isConnected(a, b)) {
                uf.connect(a, b);
                connect(roomA, roomB);
            }
        }

        //connect the rooms
        for (int i = 0; i < numRooms; i++) {
            for (int j = 0; j < numRooms; j++) {
                if (!uf.isConnected(i, j)) {
                    uf.connect(i, j);
                    connect(rooms.get(i), rooms.get(j));
                }
            }
        }

        //Get a random starting position for the player avatar
        Room startRoom = rooms.get(rand.nextInt(rooms.size()));
        ArrayList<Position> roomSpan = startRoom.getSpan();
        //span is all the free space
        Position startPosition = roomSpan.get(rand.nextInt(roomSpan.size()));
        //get a random number in all the free spaces
        avatar = new Avatar(startPosition, Tileset.AVATAR);
        world[avatar.getX()][avatar.getY()] = avatar.getTile();

        //execute saved player moves
        while (index < input.length()) {
            UpdateWorld(input.charAt(index));
            index++;
        }
        return world;
    }
}
