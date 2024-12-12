package ru.tele2.govorova.otus.java.pro.StreamApi;

import java.util.List;

public class main {
    public static void main(String[] args) {
        TaskWorker taskWorker = new TaskWorker();

        List<TaskFactory.Task> inProgressTasks = taskWorker.getTasksByStatus(Status.IN_PROGRESS);
        System.out.printf("Tasks in status \"%s\": %s \n", Status.IN_PROGRESS, inProgressTasks);
        List<TaskFactory.Task> openedTasks = taskWorker.getTasksByStatus(Status.OPEN);
        System.out.printf("Tasks in status \"%s\": %s \n", Status.OPEN, openedTasks);
        List<TaskFactory.Task> closedTasks = taskWorker.getTasksByStatus(Status.CLOSED);
        System.out.printf("Tasks in status \"%s\": %s \n", Status.CLOSED, closedTasks);

        System.out.printf("Task with id %s - %b \n", 13, taskWorker.isTaskWithIdExists(13));

        System.out.printf("Sorted tasks list Open>Closed>In progress: %s \n", taskWorker.sortTasksByStatus());

        System.out.printf("Tasks in \"In progress\" status count is %s \n", taskWorker.countTasksByStatus(Status.IN_PROGRESS));

        System.out.printf("Tasks in \"Closed\" count is %s \n", taskWorker.countTasksByStatus(Status.CLOSED));

        System.out.printf("Tasks in \"Open\" count is %s \n", taskWorker.countTasksByStatus(Status.OPEN));
    }
}
