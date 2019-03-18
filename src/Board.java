import java.util.ArrayList;

public class Board {
    private int boardWidth;
    private int boardHeight;
    private int[][] board;
    private ArrayList<ArrayList> bombCoordinates;

    public Board(int boardHeight, int boardWidth) {
        this.boardWidth = boardWidth;
        this.boardHeight = boardHeight;
        board = generateBoard();
        bombCoordinates = new ArrayList<>();
    }

    public int getBoardWidth() {
        return boardWidth;
    }

    public int getBoardHeight() {
        return boardHeight;
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
        for (int i = 0; i < coordinates.size(); i++) {
            for (int j = 0; j < boardHeight; j++) {
                for (int k = 0; k < boardWidth; k++) {
                    if((int)coordinates.get(i).get(0)==j && (int)coordinates.get(i).get(1)==k){
                        //System.out.println(j+"-"+k);
                        board[j][k]=-1;
                    }
                }
            }
        }
    }

    void setNumbers(){
        for (int i = 0; i < boardHeight; i++) {
            for (int j = 0; j < boardWidth; j++) {
                if(board[i][j]!=-1) {
                    if (i == 0 && j == 0) {
                        if (board[i + 1][j] == -1) board[i][j] += 1;
                        if (board[i + 1][j + 1] == -1) board[i][j] += 1;
                        if (board[i][j + 1] == -1) board[i][j] += 1;
                    } else if (i == 0 && j == boardWidth - 1) {
                        if (board[i][j-1] == -1) board[i][j] += 1;
                        if (board[i + 1][j -1] == -1) board[i][j] += 1;
                        if (board[i+1][j] == -1) board[i][j] += 1;
                    } else if (i == boardHeight - 1 && j == 0) {
                        if (board[i -1][j] == -1) board[i][j] += 1;
                        if (board[i - 1][j + 1] == -1) board[i][j] += 1;
                        if (board[i][j + 1] == -1) board[i][j] += 1;
                    } else if (i == boardHeight - 1 && j == boardWidth - 1) {
                        if (board[i ][j-1] == -1) board[i][j] += 1;
                        if (board[i - 1][j - 1] == -1) board[i][j] += 1;
                        if (board[i-1][j ] == -1) board[i][j] += 1;
                    } else if (i == 0) {
                        if (board[i ][j-1] == -1) board[i][j] += 1;
                        if (board[i + 1][j - 1] == -1) board[i][j] += 1;
                        if (board[i+1][j ] == -1) board[i][j] += 1;
                        if (board[i + 1][j+1] == -1) board[i][j] += 1;
                        if (board[i ][j + 1] == -1) board[i][j] += 1;
                    } else if (j == 0) {
                        if (board[i -1][j] == -1) board[i][j] += 1;
                        if (board[i - 1][j + 1] == -1) board[i][j] += 1;
                        if (board[i][j +1] == -1) board[i][j] += 1;
                        if (board[i + 1][j+1] == -1) board[i][j] += 1;
                        if (board[i +1][j ] == -1) board[i][j] += 1;
                    } else if (i == boardHeight - 1) {
                        if (board[i ][j-1] == -1) board[i][j] += 1;
                        if (board[i -1 ][j - 1] == -1) board[i][j] += 1;
                        if (board[i-1][j ] == -1) board[i][j] += 1;
                        if (board[i - 1][j+1] == -1) board[i][j] += 1;
                        if (board[i ][j + 1] == -1) board[i][j] += 1;
                    } else if (j == boardWidth - 1) {
                        if (board[i +1][j] == -1) board[i][j] += 1;
                        if (board[i + 1][j - 1] == -1) board[i][j] += 1;
                        if (board[i][j-1 ] == -1) board[i][j] += 1;
                        if (board[i - 1][j-1] == -1) board[i][j] += 1;
                        if (board[i -1][j ] == -1) board[i][j] += 1;
                    } else {
                        if (board[i ][j-1] == -1) board[i][j] += 1;
                        if (board[i - 1][j -1] == -1) board[i][j] += 1;
                        if (board[i-1][j ] == -1) board[i][j] += 1;
                        if (board[i - 1][j+1] == -1) board[i][j] += 1;
                        if (board[i ][j + 1] == -1) board[i][j] += 1;
                        if (board[i +1][j+1] == -1) board[i][j] += 1;
                        if (board[i + 1][j ] == -1) board[i][j] += 1;
                        if (board[i+1][j -1] == -1) board[i][j] += 1;
                    }
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
