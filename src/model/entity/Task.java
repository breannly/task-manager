package model.entity;

import controller.exception.FormatException;
import model.enums.TaskType;

import java.time.DateTimeException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.Optional;

public class Task {
    private String name;
    private String description;
    private Long id;
    private String status;
    private Optional<Duration> duration;
    private Optional<LocalDateTime> startTime;

    protected static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yy HH:mm");
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

    public Task(String name, String description, String status) {
        this.name = name;
        this.description = description;
        this.status = status;
    }

    public Task(String name, String description, String status, String duration, String startTime)
            throws FormatException {
        this.name = name;
        this.description = description;
        this.status = status;
        this.duration = getFromStringDuration(duration);
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

    public Optional<Duration> getDuration() {
        return duration;
    }

    public Optional<LocalDateTime> getStartTime() {
        return startTime;
    }

    protected Optional<LocalDateTime> getEndTime() {
        if (startTime.isPresent() && duration.isPresent())
            return Optional.ofNullable(startTime.get().plus(duration.get()));
        else
            return Optional.empty();
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

    public void setDuration(Optional<Duration> duration) {
        this.duration = duration;
    }

    public void setStartTime(Optional<LocalDateTime> startTime) {
        this.startTime = startTime;
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

    protected Optional<Duration> getFromStringDuration(String duration) throws FormatException {
        if (duration != null) {
            try {
                return Optional.of(Duration.between(LocalTime.MIN, LocalTime.parse(duration, TIME_FORMATTER)));
            } catch (DateTimeException exception) {
                throw new FormatException();
            }
        }

        return Optional.empty();
    }

    protected Optional<LocalDateTime> getFromStringStartTime(String startTime) throws FormatException {
        if (startTime != null) {
            try {
                return Optional.of(LocalDateTime.parse(startTime, DATE_TIME_FORMATTER));
            } catch (DateTimeException exception) {
                throw new FormatException();
            }
        }

        return Optional.empty();
    }

    protected String getFromStartTimeString(Optional<LocalDateTime> startTime) {
        return startTime.map(localDateTime -> localDateTime.format(DATE_TIME_FORMATTER)).orElse("null");
    }

    protected String getFromEndTimeString(Optional<LocalDateTime> endTime) {
        return endTime.map(localDateTime -> localDateTime.format(DATE_TIME_FORMATTER)).orElse("null");
    }

    protected String getFromDurationString(Optional<Duration> duration) {
        return duration.map(value -> LocalTime.of(value.toHoursPart(), value.toMinutesPart())
                        .format(TIME_FORMATTER)).orElse("null");
    }
}
