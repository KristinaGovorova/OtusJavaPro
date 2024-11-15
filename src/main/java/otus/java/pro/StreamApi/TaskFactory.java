package otus.java.pro.StreamApi;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TaskFactory {

    @Builder
    protected static class Task {

        @Getter
        private int taskId;

        @Getter
        @Setter
        private String taskName;

        @Getter
        private Status status;
    }

    protected Stream<Task> getTaskStream() {
        return Stream.of(
                Task.builder()
                .taskId(1)
                .taskName("Task 1")
                .status(Status.OPEN).build(),
                Task.builder()
                .taskId(2)
                .taskName("Task 2")
                .status(Status.IN_PROGRESS).build(),
                Task.builder()
                .taskId(3)
                .taskName("Task 3")
                .status(Status.CLOSED).build(),
                Task.builder()
                .taskId(4)
                .taskName("Task 4")
                .status(Status.CLOSED).build(),
                Task.builder()
                .taskId(5)
                .taskName("Task 5")
                .status(Status.CLOSED).build(),
                Task.builder()
                .taskId(6)
                .taskName("Task 6")
                .status(Status.CLOSED).build(),
                Task.builder()
                .taskId(7)
                .taskName("Task 7")
                .status(Status.CLOSED).build(),
                Task.builder()
                .taskId(8)
                .taskName("Task 8")
                .status(Status.CLOSED).build(),
                Task.builder()
                .taskId(9)
                .taskName("Task 9")
                .status(Status.IN_PROGRESS).build(),
                Task.builder()
                .taskId(10)
                .taskName("Task 10")
                .status(Status.IN_PROGRESS).build(),
                Task.builder()
                .taskId(11)
                .taskName("Task 11")
                .status(Status.IN_PROGRESS).build(),
                Task.builder()
                .taskId(12)
                .taskName("Task 12")
                .status(Status.OPEN).build()
        );
    }

    public List<String> tasksInProgress() {
        return getTaskStream().filter(task -> task.getStatus() == Status.IN_PROGRESS)
                .map(task -> task.getTaskName())
                .toList();
    }

    public List<String> tasksOpened() {
        return getTaskStream().filter(task -> task.getStatus() == Status.OPEN)
                .map(task -> task.getTaskName())
                .toList();
    }

    public List<String> tasksClosed() {
        return getTaskStream().filter(task -> task.getStatus() == Status.CLOSED)
                .map(task -> task.getTaskName())
                .toList();
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
