package model.entity;

import controller.exception.FormatException;
import model.enums.TaskType;

import java.util.Objects;

public class Subtask extends Task {
    private final Long idEpic;

    public Subtask(String name, String description, String status, Long idEpic, long duration, String startTime)
            throws FormatException {
        super(name, description, status, duration, startTime);
        this.idEpic = idEpic;
    }

    public Long getIdEpic() {
        return idEpic;
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
        return getId().equals(subtask.getId());
    }
}
