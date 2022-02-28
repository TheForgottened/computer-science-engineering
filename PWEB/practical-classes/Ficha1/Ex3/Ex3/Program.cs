using System;

namespace Ex3 {
    class Program {
        static void Main(string[] args) {
            IGeometricShape geometricShape = new Circle(3);
            Console.WriteLine(geometricShape);
            Console.WriteLine();
            
            geometricShape = new Rectangle(10, 5);
            Console.WriteLine(geometricShape);
            Console.WriteLine();
            
            geometricShape = new Triangle(3, 4, 5);
            Console.WriteLine(geometricShape);
            Console.WriteLine();
        }
    }
}