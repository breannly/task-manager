package model.entity;

import controller.exception.FormatException;
import model.enums.TaskType;

import java.util.Objects;

public class Subtask extends Task {
    private final Long IdEpic;

    public Subtask(String name, String description, String status, Long IdEpic) {
        super(name, description, status);
        this.IdEpic = IdEpic;
    }

    public Subtask(String name, String description, String status, Long IdEpic, String duration, String startTime)
            throws FormatException {
        super(name, description, status, duration, startTime);
        this.IdEpic = IdEpic;
    }

    public Long getIdEpic() {
        return IdEpic;
    }

    @Override
    public String toString() {
        String durationString = getFromDurationString(getDuration());
        String startTimeString = getFromStartTimeString(getStartTime());
        String endTimeString = getFromEndTimeString(getEndTime());

        return String.join(",",
                getId().toString(),
                TaskType.SUBTASK.name(),
                getName(),
                getStatus(),
                getDescription(),
                getIdEpic().toString(),
                durationString,
                startTimeString,
                endTimeString);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Subtask subtask = (Subtask) o;
        return getId() == subtask.getId()
                && Objects.equals(getName(), subtask.getName())
                && Objects.equals(getDescription(), subtask.getDescription())
                && Objects.equals(getStatus(), subtask.getStatus())
                && Objects.equals(getIdEpic(), subtask.getIdEpic());
    }
}
