namespace Ex3 {
    public class Rectangle : IGeometricShape {
        private double _length;
        private double _width;

        public Rectangle(double length, double width) {
            _length = length;
            _width = width;
        }

        public double GetArea() => _length * _width;
        public double GetPerimeter() => (_length * 2) + (_width * 2);

        public override string ToString() {
            return "Rectangle " + _width + "x" + _length + '\n'
                   + "Area: " + GetArea() + " Perimeter: " + GetPerimeter();
        }
    }
}