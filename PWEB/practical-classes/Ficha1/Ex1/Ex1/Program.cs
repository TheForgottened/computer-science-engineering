using System;
using System.Collections.Generic;

namespace Ex1 {
    class Program {
        static void Main(string[] args) {
            User u = new User("Exemplo", new List<Task>());

            //Mock Data
            u.AdicionarTarefa();
            u.AdicionarTarefa("Compras", category: Category.Personal);
            u.AdicionarTarefa("Exame", Priority.Normal, deadline: "16/08/2019 09:30");
            u.AdicionarTarefa("Recurso", Priority.Normal);
            u.AdicionarTarefa(deadline: "16/08/2019 10:30", priority: Priority.High, name: "acordar");
            u.AdicionarTarefa(deadline: "17/08/2019 15:30", state: State.Done);

            u.AdicionarTarefa(name: "Concluida", state: State.Done);
            u.AdicionarTarefa(name: "Pessoal", category: Category.Personal);
            u.AdicionarTarefa(name: "Baixa", priority: Priority.Low);

            //Mock Teste
            u.MostrarTarefas(u.Tasks, "\nTodas as Tarefas\n");
            
            u.DeleteTask();
            u.MostrarTarefas(u.Tasks, "\nTeste\n");
        }
    }
}