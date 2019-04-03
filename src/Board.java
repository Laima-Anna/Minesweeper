import java.util.ArrayList;

public class Board {
    int boardWidth;
    int boardHeight;
    int[][] board;
    private ArrayList<ArrayList> bombCoordinates;

    public Board(int boardHeight, int boardWidth) {
        this.boardWidth = boardWidth;
        this.boardHeight = boardHeight;
        board = generateBoard();
        bombCoordinates = new ArrayList<>();
    }

    public int[][] getBoard() {
        return board;
    }

    private int[][] generateBoard() {
        board = new int[boardHeight][boardWidth];

        for (int i = 0; i < boardHeight; i++) {
            for (int j = 0; j < boardWidth; j++) {
                board[i][j] = 0;
            }
        }
        return board;
    }



    ArrayList<ArrayList> setBombCoordinates(int bombCount) {

        for (int i = 0; i < bombCount; i++) {
            Bombs bomb = new Bombs(boardHeight,boardWidth);
            if(!bombCoordinates.contains(bomb.getLocation()))
                bombCoordinates.add(bomb.getLocation());
            else {
                return setBombCoordinates(bombCount - i);
            }
        }
        return bombCoordinates;
    }




    void setBomb(ArrayList<ArrayList> coordinates){
        for (ArrayList coordinate : coordinates) {
            for (int j = 0; j < boardHeight; j++) {
                for (int k = 0; k < boardWidth; k++) {
                    if ((int) coordinate.get(0) == j && (int) coordinate.get(1) == k) {
                        //System.out.println(j+"-"+k);
                        board[j][k] = -1;
                    }
                }
            }
        }
    }

    int countOpened(){
        int count=0;
        for (int i = 0; i < boardHeight; i++) {
            for (int j = 0; j < boardWidth; j++) {
                if(board[i][j]>=0)count+=1;
            }
        }
        return count;
    }

    int[] getNumbersAround(int x, int y){ //TODO better way of doing? --Eva
        int[] numbers = new int[8];
        if(x==0&&y==0){
            numbers[0] = -9;
            numbers[1] = -9;
            numbers[2] = -9;
            numbers[3] = board[x][y + 1];
            numbers[4] = board[x + 1][y + 1];
            numbers[5] = board[x + 1][y];
            numbers[6] = -9;
            numbers[7] = -9;
        } else if(x==0&&y==boardWidth-1){
            numbers[0] = -9;
            numbers[1] = -9;
            numbers[2] = -9;
            numbers[3] = -9;
            numbers[4] = -9;
            numbers[5] = board[x + 1][y];
            numbers[6] = board[x + 1][y - 1];
            numbers[7] = board[x][y - 1];
        } else if (x == boardHeight - 1 && y == 0) {
            numbers[0] = -9;
            numbers[1] = board[x - 1][y];
            numbers[2] = board[x - 1][y + 1];
            numbers[3] = board[x][y + 1];
            numbers[4] = -9;
            numbers[5] = -9;
            numbers[6] = -9;
            numbers[7] = -9;
        } else if (x == boardHeight - 1 && y == boardWidth - 1) {
            numbers[0] = board[x - 1][y - 1];
            numbers[1] = board[x-1][y];
            numbers[2] = -9;
            numbers[3] = -9;
            numbers[4] = -9;
            numbers[5] = -9;
            numbers[6] = -9;
            numbers[7] = board[x][y - 1];
        } else if (x == 0) {
            numbers[0] = -9;
            numbers[1] = -9;
            numbers[2] = -9;
            numbers[3] = board[x][y + 1];
            numbers[4] = board[x + 1][y + 1];
            numbers[5] = board[x + 1][y];
            numbers[6] = board[x + 1][y - 1];
            numbers[7] = board[x][y - 1];
        } else if (y == 0) {
            numbers[0] = -9;
            numbers[1] = board[x - 1][y];
            numbers[2] = board[x - 1][y + 1];
            numbers[3] = board[x][y + 1];
            numbers[4] = board[x + 1][y + 1];
            numbers[5] = board[x + 1][y];
            numbers[6] = -9;
            numbers[7] = -9;
        } else if (x == boardHeight - 1) {
            numbers[0] = board[x - 1][y - 1];
            numbers[1] = board[x - 1][y];
            numbers[2] = board[x - 1][y + 1];
            numbers[3] = board[x][y + 1];
            numbers[4] = -9;
            numbers[5] = -9;
            numbers[6] = -9;
            numbers[7] = board[x][y - 1];
        } else if (y == boardWidth - 1) {
            numbers[0] = board[x - 1][y - 1];
            numbers[1] = board[x - 1][y];
            numbers[2] = -9;
            numbers[3] = -9;
            numbers[4] = -9;
            numbers[5] = board[x + 1][y];
            numbers[6] = board[x + 1][y - 1];
            numbers[7] = board[x][y - 1];
        } else {
            numbers[0] = board[x - 1][y - 1];
            numbers[1] = board[x - 1][y];
            numbers[2] = board[x - 1][y + 1];
            numbers[3] = board[x][y + 1];
            numbers[4] = board[x + 1][y + 1];
            numbers[5] = board[x + 1][y];
            numbers[6] = board[x + 1][y - 1];
            numbers[7] = board[x][y - 1];
        }

        return numbers;
    }

    boolean checkIndexOutOfBounds(int i, int j, int height, int width) {
        if (i < 0 || j < 0 || i >= height || j >= width)
            return false;
        return true;
    }

    int countBombs(int i, int j) {
        int counter = 0;
        for (int row = i-1; row < i+2; row++) {
            for (int col = j-1; col < j+2; col++) {
                if (checkIndexOutOfBounds(row, col, boardHeight, boardWidth) && board[row][col] == -1) {
                    counter++;
                }
            }
        }
        return counter;
    }

    void setNumbers(){

        for (int i = 0; i < boardHeight; i++) {
            for (int j = 0; j < boardWidth; j++) {
                if(board[i][j]!=-1) {
                    board[i][j] = countBombs(i, j);
                }
            }
        }
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < boardHeight; i++) {
            for (int j = 0; j < boardWidth; j++) {
                sb.append(String.format("%1$"+3+ "s", board[i][j]));
                sb.append(" ");
            }
            sb.append("\n");
        }
        return sb.toString();
    }
}
