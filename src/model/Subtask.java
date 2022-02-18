package model;

import java.util.Objects;

public class Subtask extends Task {
    private Long IdEpic;

    public Subtask(String name, String description, String status, Long IdEpic) {
        super(name, description, status);
        this.IdEpic = IdEpic;
    }

    public Long getIdEpic() {
        return IdEpic;
    }

    public void setIdEpic(Long idEpic) {
        IdEpic = idEpic;
    }

    @Override
    public String toString() {
        return "model.Subtask{" +
                "name=" + getName() +
                ", id=" + getId() +
                ", status=" + getStatus() +
                ", id_epic=" + getIdEpic() + "}";
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
