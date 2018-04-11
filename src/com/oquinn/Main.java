package com.oquinn;
import java.util.*;

interface Sortable {
    List<Task> getTasks(int priority);
}

interface Priority {
    void listByPriority();
}

class TaskCollection implements Sortable {
    private List<Task> tasks;

    TaskCollection() {
        this.tasks = new ArrayList<>();
    }
    public void addTask(Task task) {
        this.tasks.add(task);
    }

    public void removeTask(int index) {
        if (this.tasks.size() > index) {
            this.tasks.remove(index);
        }
    }

    public void updateTask(int index, String name, String description, int priority) {
        this.tasks.get(index).setName(name);
        this.tasks.get(index).setDescription(description);
        this.tasks.get(index).setPriority(priority);
    }

    public List<Task> getTasks() {
        return this.tasks;
    }

    public List<Task> getTasks(int priority) {
        List<Task> matchedTasks = new LinkedList();
        for (Task task : this.tasks) {
            if (task.getPriority() == priority) {
                matchedTasks.add(task);
            }
        }
        matchedTasks.sort(Comparator.comparing(Task::getName));
        return matchedTasks;
    }
}

class Task {
    private String name;
    private String description;
    private int priority;

    Task(String name, String description, int priority) {
        setName(name);
        setDescription(description);
        setPriority(priority);
    }

    public String getName() {
        return this.name;
    }

    public int getPriority() {
        return this.priority;
    }

    public String getDescription() {
        return this.description;
    }


    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPriority(int priority) {
        if (priority < 0) {
            this.priority = 0;
        } else if (priority > 5) {
            this.priority = 5;
        } else {
            this.priority = priority;
        }
    }
}

class TaskView implements Priority {
    private TaskCollection taskCollection;
    private Scanner scanner = new Scanner(System.in);

    TaskView(TaskCollection collection) {
        this.taskCollection = collection;
    }

    private boolean isInteger(String string) {
        try {
            Integer.parseInt(string);
        }
        catch (NumberFormatException|NullPointerException e) {
            return false;
        }
        return true;
    }

    private String prompt(String message) {
        System.out.println(message);
        return this.scanner.nextLine();
    }

    private int promptInt(String message) {
        String response = prompt(message);

        boolean isInt = isInteger(response);
        while (!isInt) {
            response = prompt(message);
            isInt = isInteger(response);
        }
        return Integer.parseInt(response);
    }

    private void add() {
        Task buffer = new Task(prompt("Enter task name: "),
                prompt("Description: "),
                promptInt("Priority: "));
        this.taskCollection.addTask(buffer);
    }

    private void update() {
        int index = promptInt("Enter the index of the task to update.");
        String newName = prompt("Enter the new name.");
        String newDescription = prompt("Enter the new Description");
        int newPriority = promptInt("Enter the new priority");
        this.taskCollection.updateTask(index, newName, newDescription, newPriority);
    }

    private void remove() {
        int index = promptInt("Enter the index of the task to remove.");
        this.taskCollection.removeTask(index);
    }

    private void displayTasks(List<Task> tasks) {
        for (int i = 0; i < tasks.size(); i++) {
            Task task = (Task)tasks.get(i);
            System.out.println("Task index: " + i + ", Name: " + task
                    .getName() + ", Description: " + task
                    .getDescription() + ", Priority: " + task
                    .getPriority());
        }
    }

    private void list() {
        displayTasks(this.taskCollection.getTasks());
    }

    public void listByPriority() {
        for(int priority = 0; priority <= 5; priority++) {
            displayTasks(this.taskCollection.getTasks(priority));
        }
    }

    private void menu() {
        boolean endLoop = false;
        while (!endLoop) {
            System.out.println("1.) Add a task.");
            System.out.println("2.) Remove a task.");
            System.out.println("3.) Update a task.");
            System.out.println("4.) List all tasks.");
            System.out.println("5.) List all tasks by priority.");
            System.out.println("0.) Exit");

            int userInput = promptInt("Choose an option.");
            switch (userInput) {
                case 0:
                    endLoop = true;
                    break;
                case 1:
                    add();
                    break;
                case 2:
                    remove();
                    break;
                case 3:
                    update();
                    break;
                case 4:
                    list();
                    break;
                case 5:
                    listByPriority();
            }
        }
    }

    public void run() {
        menu();
    }
}

public class Main {
    public static void main(String[] args) {
        TaskCollection collection = new TaskCollection();
        TaskView view = new TaskView(collection);
        view.run();
    }
}