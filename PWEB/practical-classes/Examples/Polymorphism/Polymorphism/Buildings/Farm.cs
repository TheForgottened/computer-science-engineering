using System;

namespace Polymorphism.Buildings
{
    class Farm : Building
    {

        public Farm(int level = 1) : base(level) { }

        public override void DoSomething(Game g)
        {
            Console.WriteLine("\nDo Something from Farm class!\n");
            g.Corn += (this.Level * 5);
        }

        public override string ToString()
        {
            return "Farm";
        }
    }
}
