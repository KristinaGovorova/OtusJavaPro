package ru.tele2.govorova.otus.java.pro.StreamApi;

public enum Status {
    OPEN("Open"),
    IN_PROGRESS("In progress"),
    CLOSED("Closed");

    private String value;

    Status(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
