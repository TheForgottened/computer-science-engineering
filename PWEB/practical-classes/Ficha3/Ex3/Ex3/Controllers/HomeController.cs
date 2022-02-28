using System.Diagnostics;
using Microsoft.AspNetCore.Mvc;
using Ex3.Models;
using Microsoft.Extensions.Logging;

namespace Ex3.Controllers;

public class HomeController : Controller
{
    private readonly ILogger<HomeController> _logger;

    public HomeController(ILogger<HomeController> logger)
    {
        _logger = logger;
    }

    public IActionResult Index()
    {
        var uc = new UnidadeCurricular()
        {
            Nome = "PWEB",
            Curso = "Engenharia Informática",
            ECTS = 6,
            Semestre = 5
        };
        
        return View(uc);
    }
    
    public IActionResult Index2()
    {
        var uc = new UnidadeCurricular()
        {
            Nome = "PWEB",
            Curso = "Engenharia Informática",
            ECTS = 6,
            Semestre = 5
        };
        
        return View(uc);
    }

    public IActionResult Privacy()
    {
        return View();
    }

    [ResponseCache(Duration = 0, Location = ResponseCacheLocation.None, NoStore = true)]
    public IActionResult Error()
    {
        return View(new ErrorViewModel {RequestId = Activity.Current?.Id ?? HttpContext.TraceIdentifier});
    }
}