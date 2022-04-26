package model.entity;

import model.enums.TaskType;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class Task {
    private String name;
    private String description;
    private Long id;
    private String status;
    private LocalTime duration;
    private LocalDate startTime;

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yy");
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

    public Task(String name, String description, String status, String duration, String startTime) {
        this.name = name;
        this.description = description;
        this.status = status;
        this.duration = LocalTime.parse(duration, DATE_TIME_FORMATTER);
        this.startTime = LocalDate.parse(startTime, TIME_FORMATTER);
    }

    public Task(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Long getId() {
        return id;
    }

    public String getStatus() {
        return status;
    }

    public LocalDateTime getEndTime() {
        return LocalDateTime.of(startTime, duration);
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return String.join(",",
                getId().toString(),
                TaskType.TASK.name(),
                getName(),
                getStatus(),
                getDescription());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return Objects.equals(name, task.name)
               && Objects.equals(description, task.description)
               && Objects.equals(id, task.id)
               && Objects.equals(status, task.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, description, id, status);
    }
}
