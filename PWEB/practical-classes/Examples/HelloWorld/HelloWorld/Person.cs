using System;

namespace HelloWorld {
    class Person {
        public string FirstName;
        public string LastName;
        
        public void Introduce() {
            Console.WriteLine("My name is " + FirstName + " " + LastName);
        }

        public override string ToString() {
            return "My name is " + FirstName + " " + LastName;
        }
    }
}
