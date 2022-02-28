using System;

namespace Ex3 {
    public enum Priority {
        Low,
        Normal,
        High
    }

    public enum Category {
        Personal,
        Work
    }

    public enum State {
        ToDo,
        Doing,
        Done
    }
    public class Task {
        public string Name { get; }
        public DateTime InputDateTime { get; }
        public DateTime DeadlineDateTime { get; }
        public Priority TaskPriority { get; }
        public Category TaskCategory { get; }
        public State TaskState { get; }

        public Task(
            string name,
            Priority taskPriority,
            Category taskCategory,
            State taskState,
            DateTime inputDateTime,
            DateTime deadlineDateTime
        ) {
            Name = name;
            InputDateTime = inputDateTime;
            DeadlineDateTime = deadlineDateTime;
            TaskPriority = taskPriority;
            TaskCategory = taskCategory;
            TaskState = taskState;
        }

        public override string ToString() {
            //string s = $"\n{{\n\tNome: \"{Nome}\", \n\tPrioridade: {Prioridade}, \n\tCategoria: {Categoria}, \n\tEstado: {Estado}, \n\tDataRegisto: {DataRegisto}, \n\tDataLimite: {DataLimite}\n}}\n";
            string s = $"Nome: \"{Name}\", Prioridade: {TaskPriority}, Categoria: {TaskCategory}, Estado: {TaskState}, DataRegisto: {InputDateTime}, DataLimite: {DeadlineDateTime}";
            return s;
        }

        public string MostrarTarefa() {
            string s = $"\n{{\n\tNome: \"{Name}\", \n\tPrioridade: {TaskPriority}, \n\tCategoria: {TaskCategory}, \n\tEstado: {TaskState}, \n\tDataRegisto: {InputDateTime}, \n\tDataLimite: {DeadlineDateTime}\n}}\n";
            return s;
        }
    }
}