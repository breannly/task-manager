package controller.generator;

public class IdGenerator {
    private Long id;

    public IdGenerator() {
        id = 0L;
    }

    public Long getId() {
        return id;
    }

    public Long generateID() {
        id++;
        return id;
    }
}
