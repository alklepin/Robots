package robots.domain;


public record DoublePoint(double x, double y) {
    public Point toPoint() {
        return new Point((int) x, (int) y);
    }

    @Override
    public String toString() {
        return "x=" + x + ", y=" + y;
    }
}