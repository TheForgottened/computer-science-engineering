using System;

namespace Polymorphism.Buildings
{
    class Building
    {
        public int Level { get;}

        public Building(int level)
        {
            Level = level;
        }

        public virtual void DoSomething(Game g)
        {
            Console.WriteLine("Do Something from Building class!");
        }

        public override string ToString()
        {
            return "Building";
        }
    }
}
