using System.Collections.Generic;
using System.Linq;

namespace Ex2 {
    public class BaseEmployee : IEmployee {
        private string _name;
        private string _surname;
        private int _nif;

        private int _nrSales;
        private double _salesComission;

        public BaseEmployee(
            string name,
            string surname,
            int nif,
            int nrSales,
            double salesComission
        ) {
            _name = name;
            _surname = surname;
            _nif = nif;
            _nrSales = nrSales;
            _salesComission = salesComission;
        }
        public virtual double GetSalary() => _nrSales * _salesComission;

        public override string ToString() {
            return _name + ' ' + _surname + '\n'
                   + "NIF = " + _nif + '\n'
                   + "Nr. Sales = " + _nrSales + '\n'
                   + "Sales Comission = " + _salesComission + '\n'
                   + "Salary = " + GetSalary();
        }
    }
}