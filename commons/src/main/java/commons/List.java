package commons;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "Lists")
public class List {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "integer")
    private int id;

    @Column(name = "name", columnDefinition = "varchar(255)")
    private String name;

    @OneToMany
    private java.util.List<Task> tasks;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name="boardId", nullable=false)
    @JsonIgnore
    private Board board;

    @Column(name="background", columnDefinition = "varchar(7)")
    private String backgroundColor;
    @Column(name="font", columnDefinition = "varchar(7)")
    private String fontColor;

    public static List create(String name, String backgroundColor,
                              String fontColor, java.util.List<Task> tasks) {
        List list = new List();
        list.name = name;
        list.backgroundColor=backgroundColor;
        list.fontColor=fontColor;
        list.tasks=tasks;
        return list;
    }

    /**
     * Creates an empty list
     */
    public List() {}

    /**
     * Gets the name of the list
     * @return the name of the list
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the id of the list
     * @return the id of the list
     */
    public int getId() {
        return id;
    }

    /**
     * Gets the board of the list
     * @return the board of the list
     */
    public Board getBoard() {
        return board;
    }

    /**
     * Sets the name of the list
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Sets the board of the list
     * @param board the board to set
     */
    public void setBoard(Board board) {
        this.board = board;
    }

    /**
     * Gets the tasks
     * @return the tasks in the list
     */
    public java.util.List<Task> getTasks(){return this.tasks;}

    /**
     * Sets the tasks
     * @param tasks to be set
     */
    public void setTasks(java.util.List<Task> tasks) {
        this.tasks = tasks;
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

    /**
     * Determines if the list is equal to the specified object
     * @param o the object to compare to
     * @return is the object equal to this list
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        List list = (List) o;
        return getId() == list.getId() && Objects.equals(getName(), list.getName());
    }

    /**
     * Computes the hash code for this list.
     * @return the hash code for this list
     */
    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName());
    }

    /**
     * Gets a string representation of this list.
     * @return a string representation of this list
     */
    @Override
    public String toString() {
        return "List{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", tasks=" + tasks +
                '}';
    }
}
