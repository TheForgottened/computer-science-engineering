using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Polymorphism
{
    enum MenuOpts
    {
        Iterate,
        Exit
    }

    class Menu
    {
        public static string PrintMenu()
        {
            StringBuilder s = new StringBuilder("\n*** MENU ****");

            foreach(var a in Enum.GetValues(typeof(MenuOpts)))
                s.Append($"\n{(int)a} - {a.ToString()}");
            
            return s.Append("\nOption: ").ToString();
        }
    }
}
