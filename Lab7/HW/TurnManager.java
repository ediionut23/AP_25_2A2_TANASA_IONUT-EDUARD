class TurnManager {
    private int currentPlayerIndex = 0;
    private final int totalPlayers;

    public TurnManager(int totalPlayers) {
        this.totalPlayers = totalPlayers;
    }

    public synchronized void waitForTurn(int playerIndex) throws InterruptedException {
        while (currentPlayerIndex != playerIndex) {
            wait();
        }
    }

    public synchronized void endTurn() {
        currentPlayerIndex = (currentPlayerIndex + 1) % totalPlayers;
        notifyAll();
    }
}

