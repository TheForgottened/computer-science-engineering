package pt.isec.forgotten.points;

public class Point<X extends Number, Y extends Number> {
    private X x;
    private Y y;

    public Point(X x, Y y) {
        this.x = x;
        this.y = y;
    }

    public void copy(Point<? extends X, ? extends Y> point) {
        this.x = point.getX();
        this.y = point.getY();
    }

    public X getX() { return x; }
    public Y getY() { return y; }
}
