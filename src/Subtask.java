public class Subtask extends Task {
    private Long IdEpic;

    public Subtask(String name, String description, String status) {
        super(name, description, status);
    }

    public Long getIdEpic() {
        return IdEpic;
    }

    public void setIdEpic(Long idEpic) {
        IdEpic = idEpic;
    }
}
