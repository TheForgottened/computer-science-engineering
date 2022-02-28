using Polymorphism.Buildings;

namespace Polymorphism
{
    class Program
    {
        static void Main(string[] args)
        {
            Game g = new Game(0, 0, 0);
            
            g.AddBuilding(new Bank());
            g.AddBuilding(new Farm());
            g.AddBuilding(new LumberHouse());

            g.Run();
        }
    }
}
