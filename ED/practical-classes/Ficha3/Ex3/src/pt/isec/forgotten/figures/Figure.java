package pt.isec.forgotten.figures;

public abstract class Figure implements Comparable<Figure> {
    protected Figure() {}

    @Override
    public int compareTo(Figure figure) {
        return Math.round(this.getArea() - figure.getArea());
    }

    public abstract float getArea();
    public abstract float getPerimeter();
}
