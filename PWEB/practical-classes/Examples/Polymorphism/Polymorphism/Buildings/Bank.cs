using System;

namespace Polymorphism.Buildings
{
    class Bank: Building
    {

        public Bank(int level = 1) : base(level) { }

        public override void DoSomething(Game g)
        {
            Console.WriteLine("\nDo Something from Bank class!\n");
            g.Money += (this.Level * 10);
        }

        public override string ToString()
        {
            return "Bank";
        }
    }
}
