namespace Ex2 {
    public class EmployeeFixedSalary : BaseEmployee {
        private double _fixedSalary;

        public EmployeeFixedSalary(
            string name,
            string surname,
            int nif,
            int nrSales,
            double salesComission,
            double fixedSalary
        ) : base(name, surname, nif, nrSales, salesComission) {
            _fixedSalary = fixedSalary;
        }

        public override double GetSalary() => _fixedSalary + base.GetSalary();
    }
}