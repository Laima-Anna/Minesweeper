import java.util.ArrayList;

public class Bombs {
    private ArrayList<Integer> location;
    private int boardHeight;
    private int boardWidth;

    public Bombs(int boardHeight, int boardWidth) {
        this.boardHeight = boardHeight;
        this.boardWidth = boardWidth;
        this.location = new ArrayList<Integer>();

        location=generateBomb(boardHeight,boardWidth);
    }


    public ArrayList<Integer> getLocation() {
        return location;
    }

    ArrayList<Integer> generateBomb(int boardHeight, int boardWidth){
        int x = (int)(Math.random()*boardHeight);
        int y = (int)(Math.random()*boardWidth);
        location.add(x);
        location.add(y);

        return location;
    }
}
