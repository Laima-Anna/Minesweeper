import java.util.ArrayList;

class Bombs {
    private ArrayList<Integer> location;
    private int boardHeight;
    private int boardWidth;


    Bombs(int boardHeight, int boardWidth) {
        this.boardHeight = boardHeight;
        this.boardWidth = boardWidth;
        this.location = new ArrayList<>();

        location=generateBomb(boardHeight,boardWidth);

    }

    ArrayList<Integer> getLocation() {
        return location;
    }

    private ArrayList<Integer> generateBomb(int boardHeight, int boardWidth){
        int x = (int)(Math.random()*boardHeight);
        int y = (int)(Math.random()*boardWidth);
        location.add(x);
        location.add(y);

        return location;
    }
}
