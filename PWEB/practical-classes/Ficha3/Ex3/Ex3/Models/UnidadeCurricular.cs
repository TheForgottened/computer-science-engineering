using System.ComponentModel.DataAnnotations;

namespace Ex3.Models
{
    public class UnidadeCurricular
    {
        public string Nome { get; set; }
        public int ECTS { get; set; }

        [Display(Name = "Licenciatura")]
        public string Curso { get; set; }
        public int Semestre { get; set; }
    }
}