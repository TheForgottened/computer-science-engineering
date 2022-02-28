using System;

namespace Polymorphism.Buildings
{
    class LumberHouse : Building
    {

        public LumberHouse(int level = 1) : base(level) { }

        public override void DoSomething(Game g)
        {
            Console.WriteLine("\nDo Something from LumberHouse class!\n");
            g.Wood += (this.Level * 20);
        }

        public override string ToString()
        {
            return "LumberHouse";
        }
    }
}
