public class Board {
    private int boardWidth;
    private int boardLength;
    private String[][] board;

    public Board(int boardWidth, int boardLength) {
        this.boardWidth = boardWidth;
        this.boardLength = boardLength;
    }

    public int getBoardWidth() {
        return boardWidth;
    }

    public int getBoardLength() {
        return boardLength;
    }

    private String[][] generateBoard() {
        board = new String[boardWidth][boardLength];

        for (int i = 0; i < boardWidth; i++) {
            for (int j = 0; j < boardLength; j++) {
                board[i][j] = "[]";
            }
        }
        return board;
    }

    public String toString() {
        String[][] board = generateBoard();

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < boardWidth; i++) {
            for (int j = 0; j < boardLength; j++) {
                sb.append(board[i][j]);
                sb.append(" ");
            }
            sb.append("\n");
        }
        return sb.toString();

    }
}
