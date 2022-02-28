using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading;

namespace FirstProject
{
    class Program
    {
        static void Main(string[] args)
        {
            Console.ForegroundColor = ConsoleColor.DarkMagenta;
            
            //Grasshoper();
            //TetrisSong();

            Console.WriteLine("Hello World!");

            List<string> stringList = new List<string>();
            stringList.Add("isto1");
            stringList.Add("isto2");
            stringList.Add("isto3");
            stringList.Add("isto4");
            stringList.Add("isto5");
            stringList.Add("isto6");
            stringList.Add("isto7");
            stringList.Add("isto8");
            stringList.Add("isto9");
            stringList.Add("isto0");
            stringList.Add("isto10");
            stringList.Add("isto11");
            stringList.Add("isto12");
            stringList.Add("isto13");
            stringList.Add("isto14");
            
            stringList.ForEach(Console.WriteLine);

            for (int i = 0; i < stringList.Count; i++) {
                stringList.Remove(stringList[i]);
            }
            
            Console.WriteLine("---------");
            stringList.ForEach(Console.WriteLine);
            Console.WriteLine("END");
        }
        
        static void TetrisSong()
        {
            Console.Beep(1320,500);Console.Beep(990,250);Console.Beep(1056,250);Console.Beep(1188,250);Console.Beep(1320,125);Console.Beep(1188,125);Console.Beep(1056,250);Console.Beep(990,250);Console.Beep(880,500);Console.Beep(880,250);Console.Beep(1056,250);Console.Beep(1320,500);Console.Beep(1188,250);Console.Beep(1056,250);Console.Beep(990,750);Console.Beep(1056,250);Console.Beep(1188,500);Console.Beep(1320,500);Console.Beep(1056,500);Console.Beep(880,500);Console.Beep(880,500);System.Threading.Thread.Sleep(250);Console.Beep(1188,500);Console.Beep(1408,250);Console.Beep(1760,500);Console.Beep(1584,250);Console.Beep(1408,250);Console.Beep(1320,750);Console.Beep(1056,250);Console.Beep(1320,500);Console.Beep(1188,250);Console.Beep(1056,250);Console.Beep(990,500);Console.Beep(990,250);Console.Beep(1056,250);Console.Beep(1188,500);Console.Beep(1320,500);Console.Beep(1056,500);Console.Beep(880,500);Console.Beep(880,500);System.Threading.Thread.Sleep(500);
        }
        
        private static void Grasshoper()
        {
            Thread.Sleep(2000);
            Console.Beep(264, 125);
            Thread.Sleep(250);
            Console.Beep(264, 125);
            Thread.Sleep(125);
            Console.Beep(297, 500);
            Thread.Sleep(125);
            Console.Beep(264, 500);
            Thread.Sleep(125);
            Console.Beep(352, 500);
            Thread.Sleep(125);
            Console.Beep(330, 1000);
            Thread.Sleep(250);
            Console.Beep(264, 125);
            Thread.Sleep(250);
            Console.Beep(264, 125);
            Thread.Sleep(125);
            Console.Beep(297, 500);
            Thread.Sleep(125);
            Console.Beep(264, 500);
            Thread.Sleep(125);
            Console.Beep(396, 500);
            Thread.Sleep(125);
            Console.Beep(352, 1000);
            Thread.Sleep(250);
            Console.Beep(264, 125);
            Thread.Sleep(250);
            Console.Beep(264, 125);
            Thread.Sleep(125);
            Console.Beep(2642, 500);
            Thread.Sleep(125);
            Console.Beep(440, 500);
            Thread.Sleep(125);
            Console.Beep(352, 250);
            Thread.Sleep(125);
            Console.Beep(352, 125);
            Thread.Sleep(125);
            Console.Beep(330, 500);
            Thread.Sleep(125);
            Console.Beep(297, 1000);
            Thread.Sleep(250);
            Console.Beep(466, 125);
            Thread.Sleep(250);
            Console.Beep(466, 125);
            Thread.Sleep(125);
            Console.Beep(440, 500);
            Thread.Sleep(125);
            Console.Beep(352, 500);
            Thread.Sleep(125);
            Console.Beep(396, 500);
            Thread.Sleep(125);
            Console.Beep(352, 1000);
        }
    }
}