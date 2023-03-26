package commons;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Objects;

@Entity
@Table(name = "Lists")
public class List {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "integer")
    private int id;

    @Column(name = "name")
    @Size(max = 20)
    private String name;

    @Column(name="background")
    @Pattern(regexp = "^#([A-Fa-f0-9]{6}|[A-Fa-f0-9]{8})$")
    private String backgroundColor;

    @Column(name="font")
    @Pattern(regexp = "^#([A-Fa-f0-9]{6}|[A-Fa-f0-9]{8})$")
    private String fontColor;

    @Column(name = "b_id")
    @NotNull
    private int boardId;

    @OneToMany
    @JsonManagedReference
    private java.util.List<Task> tasks;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="boardId", nullable=false)
    private Board board;


    public static List create(String name, String backgroundColor,
                              String fontColor, int boardId, java.util.List<Task> tasks) {
        List list = new List();
        list.name = name;
        list.backgroundColor=backgroundColor;
        list.boardId = boardId;
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

    public int getBoardId() {
        return boardId;
    }

    /**
     * Sets the name of the list
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    public void setId(int id) {
        this.id = id;
    }

    /**
     * Sets the board of the list
     * @param board the board to set
     */
    public void setBoard(Board board) {
        this.board = board;
    }

    public void setBoardId(int boardId) {
        this.boardId = boardId;
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
        return id == list.id && boardId == list.boardId
                && Objects.equals(name, list.name)
                && Objects.equals(backgroundColor, list.backgroundColor)
                && Objects.equals(fontColor, list.fontColor)
                && Objects.equals(tasks, list.tasks)
                && Objects.equals(board, list.board);
    }

    /**
     * Computes the hash code for this list.
     * @return the hash code for this list
     */
    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName(), getTasks(), getBoard(),
                getBackgroundColor(), getFontColor());
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
                ", backgroundColor='" + backgroundColor + '\'' +
                ", fontColor='" + fontColor + '\'' +
                ", boardId=" + boardId;
    }
}
