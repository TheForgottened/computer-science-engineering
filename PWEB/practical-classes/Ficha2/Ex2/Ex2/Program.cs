using System;
using System.Collections.Generic;
using System.Linq;

namespace Ex2 {
    class Program {
        static void Main(string[] args) {
            string[] listaUm = { 
                "C#", "Aprender C#", "ASP.NET Core com C#",
                "Entity Framework", "Bootstrap", "Identity", "Lambda", "Delegates",
                "Linq", "POO com C# "
            };
            
            string[] listaDois = {"C#", "ASP.NET Core", "Linq", "Lambda e C#"};
            
            int[] numeros = {10, 23, 54, 77, 85, 12, 1, 4, 53};

            // a)
            Console.WriteLine("-- Query A --");
            var queryA = listaUm.OrderBy(s => s);
            queryA.ToList().ForEach(Console.WriteLine);
            Console.WriteLine("-- End Query --");
            
            // b)
            Console.WriteLine("-- Query B --");
            var queryB = listaUm.Where(s => s.Length < 6);
            queryB.ToList().ForEach(Console.WriteLine);
            Console.WriteLine("-- End Query --");
            
            // c)
            Console.WriteLine("-- Query C --");
            var queryC = listaUm.Count(s => s.Contains("C#"));
            Console.WriteLine(queryC);
            Console.WriteLine("-- End Query --");
            
            // d)
            Console.WriteLine("-- Query D --");
            var queryD = new List<int>(listaUm.Select(s => s.Split(' ').Length));
            queryD.ToList().ForEach(Console.WriteLine);
            Console.WriteLine("-- End Query --");
            
            // e)
            Console.WriteLine("-- Query E --");
            var queryE = numeros.Average();
            Console.WriteLine(queryE);
            Console.WriteLine("-- End Query --");
            
            // f)
            Console.WriteLine("-- Query F --");
            var queryF = numeros.Max(n => n);
            Console.WriteLine(queryF);
            Console.WriteLine("-- End Query --");
            
            // g)
            Console.WriteLine("-- Query G --");
            var queryG = numeros.Where(n => n is >= 1 and <= 25);
            queryG.ToList().ForEach(Console.WriteLine);
            Console.WriteLine("-- End Query --");
            
            // h)
            Console.WriteLine("-- Query H --");
            var queryH = listaUm.Intersect(listaDois);
            queryH.ToList().ForEach(Console.WriteLine);
            Console.WriteLine("-- End Query --");
            
            // i)
            Console.WriteLine("-- Query I --");
            var queryI = listaUm.Union(listaDois);
            queryI.ToList().ForEach(Console.WriteLine);
            Console.WriteLine("-- End Query --");
            
            // j)
            Console.WriteLine("-- Query J --");
            var queryJEven = numeros.Where(n => n % 2 == 0);
            var queryJOdd = numeros.Except(queryJEven);
            queryJEven.ToList().ForEach(Console.WriteLine);
            Console.WriteLine("----");
            queryJOdd.ToList().ForEach(Console.WriteLine);
            Console.WriteLine("-- End Query --");
            
            // k)
            Console.WriteLine("-- Query K --");
            var queryK = numeros.Where(n => n < 30).Aggregate((x, y) => x * y);
            Console.WriteLine(queryK);
            Console.WriteLine("-- End Query --");
            
            // l)
            Console.WriteLine("-- Query L --");
            var queryL = listaUm.Union(listaDois)
                .Where(s => s.Contains("C#"))
                .Select(s => new {
                    all = s, 
                    first = s.Trim().Split(' ').First(),
                    last = s.Trim().Split(' ').Length == 1 ? "NÃO TEM" : s.Trim().Split(' ').Last()
                });
            
            foreach (var s in queryL) {
                Console.WriteLine("Frase: " + s.all + "\n\tPrimeira Palavra: " + 
                                  s.first + "\n\tUltima Palavra: " + s.last);
            }
            
            Console.WriteLine("-- End Query --");
        }
    }
}