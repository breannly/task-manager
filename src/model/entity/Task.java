package model.entity;

import controller.exception.FormatException;
import model.enums.TaskType;

import java.time.DateTimeException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class Task {
    private String name;
    private String description;
    private Long id;
    private String status;
    private long duration;
    private LocalDateTime startTime;

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yy HH:mm");

    public Task(String name, String description, String status, long duration, String startTime)
            throws FormatException {
        this.name = name;
        this.description = description;
        this.status = status;
        this.duration = duration;
        this.startTime = getFromStringStartTime(startTime);
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

    public long getDuration() {
        return duration;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public LocalDateTime getEndTime() {
        if (startTime != null)
            return startTime.plus(Duration.ofMinutes(duration));
        else
            return null;
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

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public void setStartTime(String startTime) throws FormatException {
        this.startTime = getFromStringStartTime(startTime);
    }

    @Override
    public String toString() {
        String durationString = getFromDurationString(duration);
        String startTimeString = getFromStartTimeString(startTime);
        String endTimeString = getFromEndTimeString(getEndTime());

        return String.join(",",
                getId().toString(),
                TaskType.TASK.name(),
                getName(),
                getStatus(),
                getDescription(),
                durationString,
                startTimeString,
                endTimeString);
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

    protected LocalDateTime getFromStringStartTime(String startTime) throws FormatException {
        if (startTime != null) {
            try {
                return LocalDateTime.parse(startTime, DATE_TIME_FORMATTER);
            } catch (DateTimeException exception) {
                throw new FormatException("Неверный формат данных");
            }
        }

        return null;
    }

    protected String getFromStartTimeString(LocalDateTime startTime) {
        return startTime != null ? startTime.format(DATE_TIME_FORMATTER) : null;
    }

    protected String getFromEndTimeString(LocalDateTime endTime) {
        return endTime != null ? endTime.format(DATE_TIME_FORMATTER) : null;
    }

    protected String getFromDurationString(long duration) {
        return Long.toString(duration);
    }
}
