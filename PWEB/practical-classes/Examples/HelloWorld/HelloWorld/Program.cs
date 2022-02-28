using System;
using System.Collections.Generic;
using System.Linq;

// ctrl + alt + j => object browser view
// cw => console.writeline shortcut (ReSharper)

namespace HelloWorld {

    class Program {
        private int myproperty;

        public int MyProperty { 
            get { return myproperty; } 
            set { myproperty = value; } 
        }

        public int getMyProperty() {
            return myproperty;
        }

        public void setMyProperty(int i) {
            myproperty = i;
        }
        
        public static void HelloWorld() {
            Console.ForegroundColor = ConsoleColor.Red;
            Console.WriteLine("Hello World");
            Console.ReadKey();
        }

        public static void VariablesExample() {
            // sigle line comment
            /*
              Multi-line comment
             */

            // primitive types

            byte pNum = 1;
            char letra = 'A';
            double altura = 1.80;
            int idade = 10;
            const float Pi = 3.14f;
            var numero = 30;

            pNum = 2;
            pNum += 1;
            ++pNum;

            Console.WriteLine("altura = {0}, idade = {1}, idade novamente = {1}", altura, idade);
            Console.WriteLine($"char = '{letra}'");
            Console.WriteLine($"Pi = {Pi}, numero = {numero}, pNum = {pNum}");
        }

        public static void ConversionExample() {
            int a = 1;
            float b = a; // Ok

            float c = 1.5f;
            //int d = c; // Error
            //int d = (int) c;
            int d = Convert.ToInt32(c);

            //int f;
            //Int32.TryParse("21312", out f);
            
            Console.WriteLine($"b = {b}, d = {d}");
        }

        public static void UnaryOperatorsExample() {
            int a = 1;
            int b = a++;
            int c = ++a;
            Console.WriteLine($"b = {b} , c = {c}");
        }

        public static void OperatorsExample() {
            int a = 2;
            int b = 3;
            Console.WriteLine(a/b);
            //Console.WriteLine((float)a / b);
            //Console.WriteLine(a > b);
        }

        public static void StringExample() {
            string s1 = "Ola mundo";
            string s2 = "Segunda String";
            Console.WriteLine(s1 + " " + s2);

            //using placeholders
            Console.WriteLine(string.Format("{0} {1}", s1, s2));

            Console.WriteLine(s1.Contains("Ola"));

            s1 = s1 + "AAA";

            Console.WriteLine(s1);

            Console.WriteLine(s1.ToLower());
            Console.WriteLine(s1.ToUpper());
            Console.WriteLine(s1.Substring(3));
            Console.WriteLine(s1.Substring(3,4));

            Console.WriteLine($"Primeiro char: '{s1[0]}'");

            //s1[0] = 'T'; // strings are immutable
            ///s1.ToCharArray()
            ///StringBuilder class

            string toSplit = "Frase para separar por espaços";
            string[] splitArray = toSplit.Split(' ');
            for(int i = 0; i < splitArray.Length; i++)
                Console.WriteLine($"Palavra {i} -> {splitArray[i]}");

            //string.IsNullOrEmpty()
            //string.IsNullOrWhiteSpace()

            string toJoin = string.Join(";", splitArray);
            Console.WriteLine(toJoin);
        }

        public static void PersonClassExample() {
            Person p = new Person();
            //var p = new Person();
            p.FirstName = "John";
            p.LastName = "Doe";
            p.Introduce();
            Console.WriteLine(p);
        }
        
        public static void SimpleCalculatorExample() {
            int a = 3;
            int b = 4;
            Console.WriteLine(Math.Calculator.Add(a,b));

            //using HelloWorld.Math;
            //Console.WriteLine(Calculator.Add(a, b));
        }

        public static void ArraysExample() {
            // array => data struture to store a collection of variables
            // of the same type

            // an array is an object and we need to allocate memory for it
            int[] numbers = new int[3];
            //var numbers = new int[3];

            numbers[0] = 1;

            // por defeito os valores guardados no array são os valores
            // por defeito do tipo de dados armazenado no array
            // bool -> false
            // int -> 0

            for(int i = 0; i < numbers.Length; i++)
                Console.WriteLine(numbers[i]);

            string[] names = new string[] { "Jack", "John", "Mary" };

            foreach(var a in names)
                Console.WriteLine(a);
        }

        public static void EnumsExample() {
            // enums are a set of name/value pairs(constants)
            // they are also a class

            Enum naipe = Enums.Naipes.Copas;
            Console.WriteLine(naipe);

            var @int = (int)Enums.Naipes.Copas;
            Console.WriteLine(@int);

            foreach(var i in Enum.GetNames(typeof(Enums.Naipes)))
                Console.WriteLine(i);
        }

        public static void ReferenceTypeValueTypeExample() {
            //structs are value type and all objects are reference type
            //arrays are fix sized, once created the size cannot be changed

            int a = 10;
            int b = a;
            a++;
            Console.WriteLine(string.Format("a: {0}, b: {1}", a, b));

            var array1 = new int[] { 1, 2, 3 };
            var array2 = array1;
            array2[0] = 100;

            foreach(var i in array1)
                Console.WriteLine(i);
        }

        public static void ListsExample() {
            // list is a generic type
            // it's not fix sized 
            // you can create a list of any type

            var numbers = new List<int>();
            var strings = new List<string>() { "John", "Doe"};

            strings.Add("novo item");
            foreach(var i in strings)
                Console.WriteLine(i);

            // não se pode remover um item de uma lista dentro de um foreach 
            // não se pode modificar uma colection dentro de um foreach loop
            // se usarmos um for loop normal já podemos
            
            /*
            strings.Count;
            strings.RemoveAt();
            strings.Clear();
            strings.IndexOf(0);
            strings.ToArray();
            */
        }

        public static void DatesExample() {
            // struct DateTime
            DateTime dateTime = new DateTime(2015, 1, 1);
            var now = DateTime.Now;
            var today = DateTime.Today;

            Console.WriteLine("Hour: " + now.Hour);
            Console.WriteLine("Minute: " + now.Minute);

            // DateTime são imutáveis, ou seja, assim que criamos o objecto
            // já não o podemos modificar. No entanto, podemos utilizar funções
            // que nos são oferecidos pelo objecto

            var tomorrow = now.AddDays(1);
            //var tomorrow = now;
            var yesterday = now.AddDays(-1);
            

            Console.WriteLine(now.ToLongDateString());
            Console.WriteLine(now.ToShortDateString());
            Console.WriteLine(now.ToLongTimeString());
            Console.WriteLine(now.ToShortTimeString());
            Console.WriteLine(now.ToString("dd-MM-yyyy HH:mm"));

            if (tomorrow > now)
                Console.WriteLine("Maior");
            else if(tomorrow == now)
                Console.WriteLine("Iguais");
            else
                Console.WriteLine("Menor");
        }

        
        public static void Swap(ref int a, ref int b) {
            int t = a;
            a = b;
            b = t;
        }

        public static void OutExemplo(out int a) {
            a = 5;
        }
        

        static void Main(string[] args) {
            //HelloWorld();
            //VariablesExample();
            //ConversionExample();
            //UnaryOperatorsExample();
            //OperatorsExample();
            //StringExample();
            //PersonClassExample();
            //SimpleCalculatorExample();
            //ArraysExample();
            //EnumsExample();
            //ReferenceTypeValueTypeExample();
            //ListsExample();
            //DatesExample();

            Program program = new Program();

            program.getMyProperty();

            
            int a = 2, b = 3;
            int x = 1;
            Swap(ref a, ref b);
            Console.WriteLine("A: {0}, B: {1}", a, b);
            OutExemplo(out x);
            Console.WriteLine("X: " + x);
            

            /*Program a = new Program();
            a.getMyProperty();
            a.setMyProperty(5);
            Console.WriteLine(a.MyProperty);
            a.MyProperty = 5;*/

        }
    }
}
