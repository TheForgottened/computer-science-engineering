using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Mvc;

namespace Ex1.Controllers
{
    public class HomeController : Controller
    {
        public IActionResult Index() {
            string html = "<ul>" +
                             "<li> </li>" +
                             "<li> </li>" +
                             "<li> </li>" +
                             "<li> </li>" +
                         "</ul>";
            
            return Content(html, "text/html", Encoding.UTF8);
        }
    }
}