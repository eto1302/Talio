package commons.models;

public class BoardEditModel {
    private String name;

    public BoardEditModel(String name) {
        this.name = name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "BoardEditModel{" +
                "name='" + name + '\'' +
                '}';
    }
}
