package ru.tele2.govorova.otus.java.pro.stream_api;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

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
