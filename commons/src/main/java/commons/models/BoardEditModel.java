package commons.models;

public class BoardEditModel {
    private String name;
    private String backgroundColor;
    private String fontColor;

    public BoardEditModel(String name, String backgroundColor, String fontColor) {
        this.name = name;
        this.backgroundColor = backgroundColor;
        this.fontColor = fontColor;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getBackgroundColor() {
        return backgroundColor;
    }

    public String getFontColor() {
        return fontColor;
    }

    public void setBackgroundColor(String backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public void setFontColor(String fontColor) {
        this.fontColor = fontColor;
    }

    @Override
    public String toString() {
        return "BoardEditModel{" +
                "name='" + name + '\'' +
                ", backgroundColor='" + backgroundColor + '\'' +
                ", fontColor='" + fontColor + '\'' +
                '}';
    }
}
