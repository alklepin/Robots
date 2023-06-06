package robots.domain;

public record Point(int x, int y) {
    @Override
    public String toString() {
        return "x=" + x + ", y=" + y;
    }

    public DoublePoint toDoublePoint() {
        return new DoublePoint(x, y);
    }
}
