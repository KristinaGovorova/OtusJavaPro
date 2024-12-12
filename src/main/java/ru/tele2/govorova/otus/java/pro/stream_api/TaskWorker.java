package ru.tele2.govorova.otus.java.pro.stream_api;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TaskWorker {
    protected Stream<TaskFactory.Task> getTaskStream() {
        return Stream.of(
                TaskFactory.Task.builder()
                .taskId(1)
                .taskName("Task 1")
                .status(Status.OPEN).build(),
                TaskFactory.Task.builder()
                .taskId(2)
                .taskName("Task 2")
                .status(Status.IN_PROGRESS).build(),
                TaskFactory.Task.builder()
                .taskId(3)
                .taskName("Task 3")
                .status(Status.CLOSED).build(),
                TaskFactory.Task.builder()
                .taskId(4)
                .taskName("Task 4")
                .status(Status.CLOSED).build(),
                TaskFactory.Task.builder()
                .taskId(5)
                .taskName("Task 5")
                .status(Status.CLOSED).build(),
                TaskFactory.Task.builder()
                .taskId(6)
                .taskName("Task 6")
                .status(Status.CLOSED).build(),
                TaskFactory.Task.builder()
                .taskId(7)
                .taskName("Task 7")
                .status(Status.CLOSED).build(),
                TaskFactory.Task.builder()
                .taskId(8)
                .taskName("Task 8")
                .status(Status.CLOSED).build(),
                TaskFactory.Task.builder()
                .taskId(9)
                .taskName("Task 9")
                .status(Status.IN_PROGRESS).build(),
                TaskFactory.Task.builder()
                .taskId(10)
                .taskName("Task 10")
                .status(Status.IN_PROGRESS).build(),
                TaskFactory.Task.builder()
                .taskId(11)
                .taskName("Task 11")
                .status(Status.IN_PROGRESS).build(),
                TaskFactory.Task.builder()
                .taskId(12)
                .taskName("Task 12")
                .status(Status.OPEN).build()
        );
    }

    public List<TaskFactory.Task> getTasksByStatus(Status status) {
        return getTaskStream()
                .filter(task -> task.getStatus() == status)
                .collect(Collectors.toList());
    }

    public boolean isTaskWithIdExists(int taskId) {
        return getTaskStream()
                .anyMatch(task -> task.getTaskId() == taskId);
    }

    public List<String> sortTasksByStatus() {
        List<Status> statusList = List.of(Status.OPEN, Status.CLOSED, Status.IN_PROGRESS);
        return getTaskStream()
                .sorted(Comparator.comparing(task -> statusList.indexOf(task.getStatus())))
                .map(task -> task.getTaskName())
                .collect(Collectors.toList());
    }

    public long countTasksByStatus(Status status) {
        return getTaskStream()
                .filter(task -> task.getStatus() == status)
                .count();
    }
}
