using System;
using System.Reflection;

namespace Ex1 {
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
        public DateTime Registration { get; }
        public DateTime Deadline { get; }
        public Priority Priority { get; }
        public Category Category { get; }
        public State State { get; }

        public Task(
            string name, 
            DateTime registration, 
            DateTime deadline, 
            Priority priority, 
            Category category,
            State state
        ) {
            Name = name;
            Registration = registration;
            Deadline = deadline;
            Priority = priority;
            Category = category;
            State = state;
        }
        
        public override string ToString() {
            return $"\n{{\n\tNome: \"{Name}\", \n\tPrioridade: {Priority}, \n\tCategoria: {Category}, \n\tEstado: {State}, \n\tDataRegisto: {Registration}, \n\tDataLimite: {Deadline}\n}}\n";
        }
    }
}