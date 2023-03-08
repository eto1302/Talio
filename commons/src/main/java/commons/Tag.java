package commons;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "tags")
public class Tag {
    @Id
    @SequenceGenerator(
            name="tag_sequence",
            sequenceName = "tag_sequence"
    )
    @GeneratedValue(
            generator = "tag_sequence",
            strategy = GenerationType.SEQUENCE
    )
    @Column(name = "id", columnDefinition = "integer")
    private int id;


    @Column(name = "name", columnDefinition = "varchar(255)")
    private String name;

    @Column(name = "color", columnDefinition = "varchar(7) default '#000000'")
    private String color;

    public Tag(int id, String name, String color) {
        this.id = id;
        this.name = name;
        this.color = color;
    }

    public Tag(){}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Tag)) return false;
        Tag tag = (Tag) o;
        return id == tag.id && Objects.equals(name, tag.name) && Objects.equals(color, tag.color);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, color);
    }

    @Override
    public String toString() {
        return "Tag{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", color='" + color + '\'' +
                '}';
    }
}
