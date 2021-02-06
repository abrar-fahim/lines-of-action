public class Pair {

    int x;
    int y;

    public Pair(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public boolean equals(Object obj) {
        Pair other = (Pair) obj;
        return other.x == x && other.y == y;
    }
}
