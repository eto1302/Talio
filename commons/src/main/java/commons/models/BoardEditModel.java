package commons.models;

import java.util.Objects;

public class BoardEditModel {
    private String name;

    public BoardEditModel(String name) {
        this.name = name;
    }

    public BoardEditModel() {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BoardEditModel that = (BoardEditModel) o;
        return Objects.equals(getName(), that.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName());
    }
}
