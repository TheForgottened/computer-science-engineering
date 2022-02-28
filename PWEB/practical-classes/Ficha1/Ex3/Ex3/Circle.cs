using System;

namespace Ex3 {
    public class Circle : IGeometricShape {
        private readonly double _radius;

        public Circle(double radius) {
            _radius = radius;
        }

        public double GetArea() => Math.PI * _radius * _radius;
        public double GetPerimeter() => Math.PI * 2 * _radius;
        
        public override string ToString() {
            return "Circle r = " + _radius + '\n'
                   + "Area: " + GetArea() + " Perimeter: " + GetPerimeter();
        }
    }
}