package org.example;

public class HexGame {
    private static final int SIZE = 11;
    private char[][] board = new char[SIZE][SIZE];
    private Player player1;
    private Player player2;
    private boolean started;
    private long lastMoveTime;
    private Player currentPlayer;
    private boolean finished;

    public HexGame(Player player1) {
        this.player1 = player1;
        this.started = false;
        this.finished = false;
        for (int i = 0; i < SIZE; i++)
            for (int j = 0; j < SIZE; j++)
                board[i][j] = '.';
    }

    public boolean join(Player player2) {
        if (this.player2 == null) {
            this.player2 = player2;
            this.started = true;
            this.currentPlayer = player1;
            this.lastMoveTime = System.currentTimeMillis();
            return true;
        }
        return false;
    }

    public synchronized String submitMove(Player player, String move) {
        if (!started) return "Game not started yet.";
        if (finished) return "Game has already ended.";
        if (!player.equals(currentPlayer)) return "Not your turn.";

        long now = System.currentTimeMillis();
        long elapsed = now - lastMoveTime;
        player.deductTime(elapsed);

        if (player.getRemainingTime() <= 0) {
            finished = true;
            return player.getName() + " ran out of time! Game over.";
        }

        int row = move.charAt(0) - 'A';
        int col = Integer.parseInt(move.substring(1)) - 1;
        if (row < 0 || row >= SIZE || col < 0 || col >= SIZE || board[row][col] != '.') {
            return "Invalid move.";
        }

        char symbol = (player == player1) ? 'X' : 'O';
        board[row][col] = symbol;

        lastMoveTime = now;
        StringBuilder sb = new StringBuilder();
        sb.append("Move accepted: ").append(move).append("\n");
        sb.append(printBoard());

        if (checkWin(symbol)) {
            finished = true;
            sb.append(player.getName()).append(" wins!");
        } else {
            currentPlayer = (currentPlayer == player1) ? player2 : player1;
        }

        return sb.toString();
    }

    private boolean checkWin(char symbol) {
        boolean[][] visited = new boolean[SIZE][SIZE];
        if (symbol == 'X') {
            for (int i = 0; i < SIZE; i++) {
                if (board[i][0] == 'X' && dfs(i, 0, visited, 'X')) return true;
            }
        } else {
            for (int j = 0; j < SIZE; j++) {
                if (board[0][j] == 'O' && dfs(0, j, visited, 'O')) return true;
            }
        }
        return false;
    }

    private boolean dfs(int i, int j, boolean[][] visited, char symbol) {
        if (symbol == 'X' && j == SIZE - 1) return true;
        if (symbol == 'O' && i == SIZE - 1) return true;
        visited[i][j] = true;

        int[] dx = {-1, -1, 0, 0, 1, 1};
        int[] dy = {0, 1, -1, 1, -1, 0};

        for (int d = 0; d < 6; d++) {
            int ni = i + dx[d];
            int nj = j + dy[d];
            if (ni >= 0 && ni < SIZE && nj >= 0 && nj < SIZE && !visited[ni][nj] && board[ni][nj] == symbol) {
                if (dfs(ni, nj, visited, symbol)) return true;
            }
        }
        return false;
    }

    public boolean isFull() {
        return player1 != null && player2 != null;
    }

    public String printBoard() {
        StringBuilder sb = new StringBuilder();
        sb.append("  ");
        for (int j = 1; j <= SIZE; j++) sb.append(j).append(" ");
        sb.append("\n");
        for (int i = 0; i < SIZE; i++) {
            sb.append((char)('A' + i)).append(" ");
            for (int s = 0; s < i; s++) sb.append(" ");
            for (int j = 0; j < SIZE; j++) {
                sb.append(board[i][j]).append(" ");
            }
            sb.append("\n");
        }
        return sb.toString();
    }
}