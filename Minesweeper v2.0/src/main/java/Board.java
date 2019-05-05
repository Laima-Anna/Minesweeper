import java.util.ArrayList;
import java.util.List;

public class Board {
    int boardWidth;
    int boardHeight;
    int[][] board;
    private List<ArrayList> bombCoordinates;
    int bombCount;

    Board(int boardHeight, int boardWidth, int bombCount) {
        this.boardWidth = boardWidth;
        this.boardHeight = boardHeight;
        board = generateBoard();
        bombCoordinates = new ArrayList<>();
        this.bombCount = bombCount;
    }

    public int[][] getBoard() {
        return board;
    }

    public int getBombCount() {
        return bombCount;
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

    List<ArrayList> setBombCoordinates(int bombCount) {

        for (int i = 0; i < bombCount; i++) {
            Bombs bomb = new Bombs(boardHeight, boardWidth);
            if (!bombCoordinates.contains(bomb.getLocation()))
                bombCoordinates.add(bomb.getLocation());
            else {
                return setBombCoordinates(bombCount - i);
            }
        }
        return bombCoordinates;
    }

    void setBomb(List<ArrayList> coordinates) {
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

    int countOpened() {
        int count = 0;
        for (int i = 0; i < boardHeight; i++) {
            for (int j = 0; j < boardWidth; j++) {
                if (board[i][j] >= 0) count += 1;
            }
        }
        return count;
    }

    int[] getNumbersAround(int x, int y) {
        int[] numbers = new int[8];

        int a = 0;

        for (int row = x - 1; row < x + 2; row++) {
            for (int col = y - 1; col < y + 2; col++) {
                if (!(row ==x && col == y)) {
                    if (checkIndexNotOutOfBounds(row, col, boardHeight, boardWidth)) {
                        numbers[a] = board[row][col];

                        //if index is out of bounds, add -9 to the array
                    } else {
                        numbers[a] = -9;
                    }
                    a++;
                }
            }
        }
        return numbers;
    }

    private boolean checkIndexNotOutOfBounds(int i, int j, int height, int width) {
        return i >= 0 && j >= 0 && i < height && j < width;
    }

    private int countBombs(int i, int j) {
        int counter = 0;
        for (int row = i - 1; row < i + 2; row++) {
            for (int col = j - 1; col < j + 2; col++) {
                if (checkIndexNotOutOfBounds(row, col, boardHeight, boardWidth) && board[row][col] == -1) {
                    counter++;
                }
            }
        }
        return counter;
    }

    void setNumbers() {

        for (int i = 0; i < boardHeight; i++) {
            for (int j = 0; j < boardWidth; j++) {
                if (board[i][j] != -1) {
                    board[i][j] = countBombs(i, j);
                }
            }
        }
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < boardHeight; i++) {
            for (int j = 0; j < boardWidth; j++) {
                sb.append(String.format("%1$" + 3 + "s", board[i][j]));
                sb.append(" ");
            }
            sb.append("\n");
        }
        return sb.toString();
    }
}
