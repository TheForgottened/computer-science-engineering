using System;
using System.Collections.Generic;
using Polymorphism.Buildings;

namespace Polymorphism
{
    class Game
    {
        public List<Building> Buildings { get; }
        public int Money { get; set; }
        public int Wood { get; set; }
        public int Corn { get; set; } 
        
        public Game(int money = 0, int wood = 0, int corn = 0, List<Building> buildings = null)
        {
            if (buildings == null)
                Buildings = new List<Building>();

            Money = money;
            Wood = wood;
            Corn = corn;
        }

        public bool AddBuilding(Building b)
        {
            if(b != null)
            {
                Buildings.Add(b);
                return true;
            }

            return false;
        }

        public void Run()
        {
            int opt = -1;

            do
            {
                try
                {
                    Console.Write(this.ToString());

                    Console.Write(Menu.PrintMenu());
                    opt = Convert.ToInt32(Console.ReadLine());

                    switch (opt)
                    {
                        case (int)MenuOpts.Iterate:
                            Iterate();
                            break;

                        case (int)MenuOpts.Exit:
                            Console.WriteLine("\nGood Bye\n");
                            break;

                        default:
                            Console.WriteLine("\nInvalid Option\n");
                            break;
                    }
                }
                catch (Exception e)
                {
                    Console.WriteLine($"\nInvalid Option - {e.Message}\n");
                }

            } while (opt != (int)MenuOpts.Exit);
        }

        public void Iterate()
        {
            foreach (var i in Buildings)
                i.DoSomething(this);
        }

        public override string ToString()
        {
            return $"Game data: {{Money: {this.Money}, Wood: {this.Wood}, Corn: {this.Corn}}}\n";
        }
    }
}
