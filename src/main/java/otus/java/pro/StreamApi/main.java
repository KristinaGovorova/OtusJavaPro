package otus.java.pro.StreamApi;

public class main {
    public static void main(String[] args) {
        TaskFactory taskFactory = new TaskFactory();
        System.out.printf("Tasks in progress: %s \n", taskFactory.tasksInProgress());
        System.out.printf("Tasks opened: %s \n", taskFactory.tasksOpened());
        System.out.printf("Tasks closed: %s \n", taskFactory.tasksClosed());
        System.out.printf("Task with id %s - %b \n", 13, taskFactory.isTaskWithIdExists(13));
        System.out.printf("Sorted tasks list Open>Closed>In progress: %s \n", taskFactory.sortTasksByStatus());
        System.out.printf("Tasks in \"In progress\" status count is %s \n", taskFactory.countTasksByStatus(Status.IN_PROGRESS));
        System.out.printf("Tasks in \"Closed\" count is %s \n", taskFactory.countTasksByStatus(Status.CLOSED));
        System.out.printf("Tasks in \"Open\" count is %s \n", taskFactory.countTasksByStatus(Status.OPEN));
    }
}
