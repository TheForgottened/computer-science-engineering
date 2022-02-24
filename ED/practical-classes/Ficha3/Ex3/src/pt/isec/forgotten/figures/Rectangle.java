package pt.isec.forgotten.figures;

public class Rectangle extends Figure {
    private final float height;
    private final float width;

    public Rectangle(float height, float width) {
        this.height = height;
        this.width = width;
    }

    @Override
    public float getArea() {
        return height * width;
    }

    @Override
    public float getPerimeter() {
        return (height * 2) + (width * 2);
    }
}
