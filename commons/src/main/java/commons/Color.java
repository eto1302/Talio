package commons;

import javax.persistence.*;
import javax.validation.constraints.Pattern;
import java.util.Objects;

@Entity
@Table(name = "Colors")
public class Color {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "integer")
    private int id;

    @Column(name = "fontColor")
    // regular expression that matches a valid color string in the format "#RRGGBB" or "#AARRGGBB"
    @Pattern(regexp = "^#([A-Fa-f0-9]{6}|[A-Fa-f0-9]{8})$")
    private String fontColor;

    @Column(name = "backgroundColor")
    @Pattern(regexp = "^#([A-Fa-f0-9]{6}|[A-Fa-f0-9]{8})$")
    private String backgroundColor;

    @Column(name = "isDefault")
    private boolean isDefault;

    public boolean isDefault() {
        return isDefault;
    }

    public void setDefault(boolean aDefault) {
        isDefault = aDefault;
    }

    public Color() {
    }

    public static Color create(String fontColor, String backgroundColor){
        Color color = new Color();
        color.fontColor = fontColor;
        color.backgroundColor = backgroundColor;
        return color;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFontColor() {
        return fontColor;
    }

    public void setFontColor(String fontColor) {
        this.fontColor = fontColor;
    }

    public String getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(String backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Color)) return false;
        Color color = (Color) o;
        return id == color.id && Objects.equals(fontColor, color.fontColor)
                && Objects.equals(backgroundColor, color.backgroundColor);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, fontColor, backgroundColor);
    }

    @Override
    public String toString() {
        return "Color{" +
                "id=" + id +
                ", fontColor='" + fontColor + '\'' +
                ", backgroundColor='" + backgroundColor + '\'' +
                '}';
    }
}
