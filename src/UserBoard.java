public class UserBoard extends Board{
    public UserBoard(int boardHeight, int boardWidth) {
        super(boardHeight, boardWidth);
        board = generateBoard();
    }

    private int[][] generateBoard() {
        board = new int[boardHeight][boardWidth];

        for (int i = 0; i < boardHeight; i++) {
            for (int j = 0; j < boardWidth; j++) {
                board[i][j] = 9;
            }
        }
        return board;
    }
}
