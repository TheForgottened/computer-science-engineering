using System;
using System.Collections.Generic;

namespace Ex2 {
    class Program {
        static void Main(string[] args) {
            IEmployee employee = new BaseEmployee(
                "Ângelo Miguel", "Fernandes Paiva", 
                249725967, 5000000, 0.6
                );

            Console.WriteLine(employee);
            
            employee = new EmployeeFixedSalary(
                "Ângelo Miguel", "Fernandes Paiva", 
                249725967, 5000000, 0.6, 5
            );

            Console.WriteLine(employee);
        }
    }
}