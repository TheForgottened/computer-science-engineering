using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.Linq;

namespace Ex3 {
    public class User {
        public string Name { get; }
        public List<Task> Tasks { get; }

        public User(string name, List<Task> tasks = null) {
            Name = name;
            Tasks = tasks ?? new List<Task>();
        }

        public void AddTask(
            string name = null,
            Priority taskPriority = Priority.High,
            Category taskCategory = Category.Work,
            State taskState = State.ToDo,
            string deadlineDate = null
        ) {
            DateTime inputDateTime = DateTime.Now;
            DateTime deadlineDateTime;

            if (string.IsNullOrEmpty(name) || string.IsNullOrWhiteSpace(name))
                name = "Tarefa";

            if (!DateTime.TryParse(deadlineDate, out deadlineDateTime)) {
                deadlineDateTime = new DateTime(inputDateTime.Year, inputDateTime.Month, inputDateTime.Day, inputDateTime.Hour, inputDateTime.Minute,
                    inputDateTime.Second);
                deadlineDateTime.AddHours(24);
            }

            Tasks.Add(new Task(name, taskPriority, taskCategory, taskState, inputDateTime, deadlineDateTime));
        }

        public List<Task> OverdueTasks(DateTime date) => Tasks
            .Where(t => t.DeadlineDateTime < date && t.TaskState != State.Done).ToList();

        public List<Task> TaskList(State taskState) => Tasks.Where(t => t.TaskState == taskState).ToList();
        public List<Task> TaskList(Priority taskPriority) => Tasks.Where(t => t.TaskPriority == taskPriority).ToList();
        public List<Task> TaskList(Category taskCategory) => Tasks.Where(t => t.TaskCategory == taskCategory).ToList();

        public void RemoveTasks(State taskState) => Tasks.RemoveAll(t => t.TaskState == taskState);
        public void RemoveTasks(Priority taskPriority) => Tasks.RemoveAll(t => t.TaskPriority == taskPriority);
        public void RemoveTasks(Category taskCategory) => Tasks.RemoveAll(t => t.TaskCategory == taskCategory);

        public string ToStringOverdueTasks() => string.Join('\n', OverdueTasks(DateTime.Now));

        public string ToStringOrderBy<TKey>(Func<Task, TKey> keySelector, bool descending = false) {
            var outputTasks =
                (descending ? Tasks.OrderByDescending(keySelector) : Tasks.OrderBy(keySelector)).ToList();
            return string.Join('\n', outputTasks);
        }

        public override string ToString() => string.Join('\n', Tasks);
    }
}