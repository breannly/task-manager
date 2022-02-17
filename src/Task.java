public class Task {
    private String name;
    private String description;
    private Long id;
    private String status;

    public Task(String name, String description, String status) {
        this.name = name;
        this.description = description;
        this.status = status;
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

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Task{" +
                "name=" + getName() +
                ", status=" + getStatus() +
                ", id=" + getId() + "}";
    }
}
