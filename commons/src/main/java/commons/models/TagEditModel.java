package commons.models;

import java.util.Objects;

public class TagEditModel {
    private String name;
    private String color;

    public TagEditModel(String name, String color){
        this.name = name;
        this.color = color;
    }

    public TagEditModel(){}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    @Override
    public String toString() {
        return "TagEditModel{" +
                "name='" + name + '\'' +
                ", color='" + color + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TagEditModel that = (TagEditModel) o;
        return Objects.equals(getName(), that.getName()) && Objects.equals(getColor(), that.getColor());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getColor());
    }
}
