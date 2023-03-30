package commons.models;

public class ColorEditModel {
    private String backgroundColor;
    private String fontColor;

    private boolean isDefault;

    public boolean isDefault() {
        return isDefault;
    }

    public void setDefault(boolean aDefault) {
        isDefault = aDefault;
    }

    public ColorEditModel(String backgroundColor, String fontColor, boolean isDefault) {
        this.backgroundColor = backgroundColor;
        this.fontColor = fontColor;
        this.isDefault = isDefault;
    }

    public String getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(String backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public String getFontColor() {
        return fontColor;
    }

    public void setFontColor(String fontColor) {
        this.fontColor = fontColor;
    }

    @Override
    public String toString() {
        return "ColorEditModel{" +
                "backgroundColor='" + backgroundColor + '\'' +
                ", fontColor='" + fontColor + '\'' +
                '}';
    }
}
