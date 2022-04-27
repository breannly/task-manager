package model.entity;

import model.enums.TaskType;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Optional;

public class Epic extends Task {
    private final HashMap<Long, Subtask> subtasks;
    private Optional<LocalDateTime> endTime;

    public Epic(String name, String description) {
        super(name, description);
        setDuration(Optional.empty());
        setStartTime(Optional.empty());
        setEndTime(Optional.empty());
        subtasks = new HashMap<>();
    }

    public void setEndTime(Optional<LocalDateTime> endTime) {
        this.endTime = endTime;
    }

    public HashMap<Long, Subtask> getSubtask() {
        return subtasks;
    }

    public void addSubtask(Subtask subtask) {
        subtasks.put(subtask.getId(), subtask);
    }

    public Optional<LocalDateTime> getEndTime() {
        if (endTime.isPresent() && getDuration().isPresent())
            return Optional.ofNullable(endTime.get().plus(getDuration().get()));
        else
            return Optional.empty();
    }

    @Override
    public String toString() {
        String durationString = getFromDurationString(getDuration());
        String startTimeString = getFromStartTimeString(getStartTime());
        String endTimeString = getFromEndTimeString(endTime);

        return String.join(",",
                getId().toString(),
                TaskType.EPIC.name(),
                getName(),
                getStatus(),
                getDescription(),
                durationString,
                startTimeString,
                endTimeString);
    }
}
