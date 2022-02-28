using Ex4.Models;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;

namespace Ex4.Controllers
{
    public class UCsController : Controller
    {
        // GET: UCsController
        public ActionResult Index()
        {
            return View(UCsMockData.UCList);
        }

        // GET: UCsController/Details/5
        public ActionResult Details(int id)
        {
            var uc = UCsMockData.UCList.Find(uc => uc.Id == id);

            if (uc == null) return RedirectToAction("Index");

            return View(uc);
        }

        // GET: UCsController/Create
        public ActionResult Create()
        {
            return View();
        }

        // POST: UCsController/Create
        [HttpPost]
        [ValidateAntiForgeryToken]
        public ActionResult Create(IFormCollection collection)
        {
            try
            {
                var newUC = new UC();
                int maxId = UCsMockData.UCList.Max(uc => uc.Id);
                
                newUC.Id = maxId + 1;
                newUC.Nome = collection["Nome"];
                newUC.Licenciatura = collection["Licenciatura"];
                newUC.Ramo = collection["Ramo"];
                int ects;
                int.TryParse(collection["ECTS"], out ects);
                newUC.ECTS = ects;
                int semestre;
                int.TryParse(collection["Semestre"], out semestre);
                newUC.Semestre = semestre;

                return RedirectToAction(nameof(Index));
            }
            catch
            {
                return View();
            }
        }

        // GET: UCsController/Edit/5
        public ActionResult Edit(int id)
        {
            return View();
        }

        // POST: UCsController/Edit/5
        [HttpPost]
        [ValidateAntiForgeryToken]
        public ActionResult Edit(int id, IFormCollection collection)
        {
            try
            {
                return RedirectToAction(nameof(Index));
            }
            catch
            {
                return View();
            }
        }

        // GET: UCsController/Delete/5
        public ActionResult Delete(int id)
        {
            return View();
        }

        // POST: UCsController/Delete/5
        [HttpPost]
        [ValidateAntiForgeryToken]
        public ActionResult Delete(int id, IFormCollection collection)
        {
            try
            {
                return RedirectToAction(nameof(Index));
            }
            catch
            {
                return View();
            }
        }
    }
}
