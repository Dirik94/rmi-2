
/**
 * Represents a Tic Tac Toe game with a 3x3 grid.
 */
public class TicTacToe {
    private final char[][] board;
    private char currentPlayer;
    private static final int BOARD_SIZE = 3;

    /**
     * Creates a new game with an empty board and X as the starting player.
     */
    public TicTacToe() {
        board = new char[BOARD_SIZE][BOARD_SIZE];
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                board[i][j] = ' ';
            }
        }
        currentPlayer = 'X';
    }

    /**
     * Attempts to make a move at the specified position.
     * 
     * @param row Row index (0-2)
     * @param col Column index (0-2)
     * @return true if the move was valid and made, false otherwise
     */
    public boolean makeMove(int row, int col) {
        if (!isValidPosition(row, col) || board[row][col] != ' ') {
            return false;
        }
        board[row][col] = currentPlayer;
        return true;
    }

    private boolean isValidPosition(int row, int col) {
        return row >= 0 && row < BOARD_SIZE && col >= 0 && col < BOARD_SIZE;
    }

    /**
     * Switches the current player from X to O or vice versa.
     */
    public void switchPlayer() {
        currentPlayer = (currentPlayer == 'X') ? 'O' : 'X';
    }

    /**
     * Gets the symbol of the current player.
     * 
     * @return 'X' or 'O'
     */
    public char getCurrentPlayer() {
        return currentPlayer;
    }

    /**
     * Returns a string representation of the current board state.
     */
    public String getBoardAsString() {
        StringBuilder sb = new StringBuilder();
        sb.append("-------------\n");
        for (int i = 0; i < BOARD_SIZE; i++) {
            sb.append("| ");
            for (int j = 0; j < BOARD_SIZE; j++) {
                sb.append(board[i][j]).append(" | ");
            }
            sb.append("\n-------------\n");
        }
        return sb.toString();
    }

    /**
     * Checks if the current player has won.
     */
    boolean checkWin() {
        // Check rows and columns
        for (int i = 0; i < BOARD_SIZE; i++) {
            if (checkLine(board[i][0], board[i][1], board[i][2]) ||
                    checkLine(board[0][i], board[1][i], board[2][i])) {
                return true;
            }
        }

        // Check diagonals
        return checkLine(board[0][0], board[1][1], board[2][2]) ||
                checkLine(board[0][2], board[1][1], board[2][0]);
    }

    private boolean checkLine(char a, char b, char c) {
        return a == currentPlayer && b == currentPlayer && c == currentPlayer;
    }

    /**
     * Checks if the board is full (draw condition).
     */
    boolean isBoardFull() {
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                if (board[i][j] == ' ') {
                    return false;
                }
            }
        }
        return true;
    }

    public static void main(String[] args) {
        // Removed call to play() since it no longer exists.
        TicTacToe game = new TicTacToe();
        System.out.println("Empty board:");
        System.out.println(game.getBoardAsString());
    }
}
