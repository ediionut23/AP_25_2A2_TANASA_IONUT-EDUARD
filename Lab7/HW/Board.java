class Board {
    public synchronized void submitWord(String playerName, String word, int score) {
        System.out.println(playerName + " submitted word '" + word + "' for " + score + " points.");
    }
}