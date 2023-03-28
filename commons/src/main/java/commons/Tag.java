package commons;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "Tags")
public class Tag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "integer")
    private int id;
    @Column(name = "name", columnDefinition = "varchar(255)")
    private String name;

    @Column(name = "color", columnDefinition = "varchar(7) default '#000000'")
    private String color;

    @JsonBackReference
    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn(name="taskId")
    private Task task;


    /**
     * Creates a new tag object with the given name and color.
     *
     * @param name     The name of the tag.
     * @param color    The color of the tag.
     * @return A new Board object with the given name, password, and set of lists.
     */
    public static Tag create(String name, String color) {
        Tag tag = new Tag();
        tag.name = name;
        tag.color = color;
        return tag;
    }

    /**
     * Creates a new Tag object.
     */
    public Tag(){}

    /**
     * Returns the id of the tag.
     *
     * @return The id of the tag.
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the id of the tag
     * @param id to be set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Returns the name of the tag.
     *
     * @return The name of the tag.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the tag
     * @param name to be set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns the color of the tag
     * @return the color of the tag
     */
    public String getColor() {
        return color;
    }

    /**
     * Sets the color of the tag
     * @param color to be set
     */
    public void setColor(String color) {
        this.color = color;
    }

    /**
     * Indicates whether some other object is "equal to" this one.
     * @param o The object to compare to this one.
     * @return True if the objects are equal; false otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Tag)) return false;
        Tag tag = (Tag) o;
        return id == tag.id && Objects.equals(name, tag.name) && Objects.equals(color, tag.color);
    }

    /**
     * Returns a hash code value for the object.
     * @return A hash code value for the object.
     */
    @Override
    public int hashCode() {
        return Objects.hash(id, name, color);
    }

    /**
     * Returns a string representation of the object.
     * @return A string representation of the object.
     */
    @Override
    public String toString() {
        return "Tag{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", color='" + color + '\'' +
                '}';
    }
}
