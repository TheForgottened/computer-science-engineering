using System;
using System.Collections.Generic;

namespace Ex3 {
    class Program {
        static void Main(string[] args) {
            var u = new User("Exemplo", new List<Task>());

            //Mock Data
            u.AddTask();
            u.AddTask("Compras", taskCategory: Category.Personal);
            u.AddTask("Exame", Priority.Normal, deadlineDate: "15/10/2021 09:30");
            u.AddTask("Recurso", Priority.Normal);
            u.AddTask(deadlineDate: "14/10/2021 10:30", taskPriority: Priority.High, name: "acordar");
            u.AddTask(deadlineDate: "15/10/2021 15:30", taskState: State.Done);
            u.AddTask(name: "Concluida", taskState: State.Done);
            u.AddTask(name: "Pessoal", taskCategory: Category.Personal);
            u.AddTask(name: "Baixa", taskPriority: Priority.Low);

            //Mock Test
            Console.WriteLine(u);
            Console.WriteLine("OVERDUE TASKS:");
            Console.WriteLine(u.ToStringOverdueTasks());
            Console.WriteLine("ORDER BY PRIORITY TASKS:");
            Console.WriteLine(u.ToStringOrderBy(t => t.TaskPriority));
            Console.WriteLine("ORDER BY DESCENDING PRIORITY TASKS:");
            Console.WriteLine(u.ToStringOrderBy(t => t.TaskPriority, true));
            Console.WriteLine("ORDER BY STATE TASKS:");
            Console.WriteLine(u.ToStringOrderBy(t => t.TaskState));
        }
    }
}