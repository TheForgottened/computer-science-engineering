using System;

namespace Ex3 {
    public class Triangle : IGeometricShape {
        private readonly double _sideA;
        private readonly double _sideB;
        private readonly double _sideC;

        public Triangle(double sideA, double sideB, double sideC) {
            _sideA = sideA;
            _sideB = sideB;
            _sideC = sideC;
        }

        public double GetArea() {
            var semiPerimeter = GetPerimeter() / 2;

            return Math.Sqrt(
                semiPerimeter
                * (semiPerimeter - _sideA)
                * (semiPerimeter - _sideB)
                * (semiPerimeter - _sideC)
            );
        }
        public double GetPerimeter() => _sideA + _sideB + _sideC;

        public override string ToString() {
            return "Triangle a = " + _sideA + ", b = " + _sideB + ", c = " + _sideC + '\n'
                   + "Area: " + GetArea() + " Perimeter: " + GetPerimeter();
        }
    }
}