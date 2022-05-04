package model.entity;

import model.enums.TaskType;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class Epic extends Task {
    private final HashMap<Long, Subtask> subtasks;
    private LocalDateTime endTime;

    public Epic(String name, String description) {
        super(name, description);
        subtasks = new HashMap<>();
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public Map<Long, Subtask> getSubtask() {
        return subtasks;
    }

    public void addSubtask(Subtask subtask) {
        subtasks.put(subtask.getId(), subtask);
    }

    @Override
    public LocalDateTime getEndTime() {
        return endTime != null ? endTime.plus(Duration.ofMinutes(getDuration())) : null;
    }

    @Override
    public String toString() {
        String durationString = getFromDurationString(getDuration());
        String startTimeString = getFromStartTimeString(getStartTime());
        String endTimeString = getFromEndTimeString(getEndTime());

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
