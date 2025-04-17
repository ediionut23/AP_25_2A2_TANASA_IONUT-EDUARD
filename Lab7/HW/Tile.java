class Tile {
    char letter;
    int points;

    public Tile(char letter, int points) {
        this.letter = letter;
        this.points = points;
    }

    @Override
    public String toString() {
        return letter + "(" + points + ")";
    }
}