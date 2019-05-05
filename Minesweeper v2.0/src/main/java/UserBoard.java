public class UserBoard extends Board{
    public UserBoard(int boardHeight, int boardWidth, int bombCount) {
        super(boardHeight, boardWidth, bombCount);
        board = generateBoard();
    }

    private int[][] generateBoard() {
        board = new int[boardHeight][boardWidth];

        for (int i = 0; i < boardHeight; i++) {
            for (int j = 0; j < boardWidth; j++) {
                board[i][j] = -7;
            }
        }
        return board;
    }
}
