using System;
using System.Collections.Generic;
using System.Linq;

namespace Ex1 {
    class User {
        public string Name { get; }
        public List<Task> Tasks { get; }

        public User(string name, List<Task> tasks = null) {
            Name = name;
            if (tasks == null)
                Tasks = new List<Task>();
            else
                this.Tasks = tasks;
        }

        public void AdicionarTarefa(
            string name = null,
            Priority priority = Priority.High,
            Category category = Category.Work,
            State state = State.ToDo,
            string deadline = null
        ) {
            DateTime datareg = DateTime.Now;
            DateTime datalim;

            if (string.IsNullOrEmpty(name) || string.IsNullOrWhiteSpace(name))
                name = "Task";

            if (!DateTime.TryParse(deadline, out datalim))
                datalim = datareg.AddHours(24);

            Tasks.Add(new Task(name, datareg, datalim, priority, category, state));
        }

        public void MostrarTarefas(List<Task> list, string title = null) {
            if (title != null)
                Console.WriteLine(title);

            foreach (Task task in list) {
                Console.WriteLine(task);
                //Console.WriteLine(tarefa.ToString());
                //Console.WriteLine(tarefa.MostrarTarefa());
            }
        }

        public void DeleteTask() {
            foreach (var t in Tasks.ToList()) {
                Tasks.Remove(t);
            }
        }
    }
}