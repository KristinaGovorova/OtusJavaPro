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
    public static class Task {

        @Getter
        private int taskId;

        @Getter
        @Setter
        private String taskName;

        @Getter
        private Status status;

        @Override
        public String toString() {
            return taskName;
        }
    }
}
