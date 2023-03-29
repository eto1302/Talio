package commons.models;

public class ListEditModel {
    private String name;

    public ListEditModel(String name) {
        this.name = name;
    }

    public ListEditModel() {}

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "ListEditModel{" +
                "name='" + name + '\'' +
                '}';
    }
}
