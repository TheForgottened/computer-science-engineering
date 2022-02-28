using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace HelloWorld.Math
{
    class Calculator
    {
        //static methods ou atributes are accessed by the class not an object

        public static int Add(int a, int b)
        {
            return a + b;
        }

        public static float Add(float a, float b)
        {
            return a + b;
        }

        public static double Add(double a, double b)
        {
            return a + b;
        }
    }
}
