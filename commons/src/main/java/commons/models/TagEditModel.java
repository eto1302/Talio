package commons.models;

import commons.Color;

public class TagEditModel {
    private String name;
    private Color color;

    public TagEditModel(String name, Color color){
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

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    @Override
    public String toString() {
        return "TagEditModel{" +
                "name='" + name + '\'' +
                ", color='" + color.toString() + '\'' +
                '}';
    }
}
