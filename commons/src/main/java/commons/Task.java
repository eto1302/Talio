package commons;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Objects;

@Entity()
@Table(name="Task")
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "integer")
    private int id;
    @Column(name = "description", columnDefinition = "varchar(255)")
    private String description;
    @Column(name = "title", columnDefinition = "varchar(255)")
    private String title;

    @Column(name="index", columnDefinition = "integer")
    private int index;

    @Column(name = "l_id")
    @NotNull
    private int listID;

    @JsonBackReference
    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn(name="listId", nullable=false)
    private List list;

    @OneToMany (mappedBy = "task", cascade = CascadeType.REMOVE)
    @JsonManagedReference
    private java.util.List<Subtask> subtasks;

    @OneToMany(mappedBy = "task", cascade = CascadeType.REMOVE)
    private java.util.List<Tag> tags;

    @Column(name = "colorId")
    private int colorId;

    /**
     * Creates a new task with the specified description and title.
     * @param description the description of the task
     * @param title the title of the task
     * @return the newly created task
     */
    public static Task create(String description, String title, int listID,
                              java.util.List<Subtask> subtasks) {
        Task task = new Task();
        task.description = description;
        task.title = title;
        task.listID=listID;
        task.subtasks=subtasks;
        return task;
    }

    /**
     * Constructs an empty task.
     */
    public Task() {}

    /**
     * Gets the unique identifier of the task.
     * @return the task's identifier
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the unique identifier of the task.
     * @param id the task's identifier
     */
    public void setId(int id) {
        this.id = id;
    }

    public int getListID() {
        return listID;
    }

    public void setListID(int listID) {
        this.listID = listID;
    }

    public java.util.List<Subtask> getSubtasks() {
        return subtasks;
    }

    public void setSubtasks(java.util.List<Subtask> subtasks) {
        this.subtasks = subtasks;
    }

    /**
     * Gets the index of the task in the list
     * @return the index
     */
    public int getIndex() {
        return index;
    }

    /**
     * Sets the index of the task in its list
     * @param index the index to be set
     */
    public void setIndex(int index) {
        this.index = index;
    }

    /**
     * Gets the description of the task.
     * @return the task's description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the description of the task.
     * @param description the task's description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Gets the title of the task.
     * @return the task's title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets the title of the task.
     * @param title the task's title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Gets the list that the task belongs to.
     * @return the list that the task belongs to
     */
    public List getList() {
        return list;
    }

    /**
     * Sets the list that the task belongs to.
     * @param list the list that the task belongs to
     */
    public void setList(List list) {
        this.list = list;
    }

    /**
     * Gets the tags associated with the task.
     * @return the tags associated with the task
     */
    public java.util.List<Tag> getTags() {
        return tags;
    }

    /**
     * Sets the tags associated with the task.
     * @param tags the tags associated with the task
     */
    public void setTags(java.util.List<Tag> tags) {
        this.tags = tags;
    }

    public int getColorId() {
        return colorId;
    }

    public void setColorId(int colorId) {
        this.colorId = colorId;
    }

    /**
     * Determines if the task is equal to the specified object
     * @param o the object to compare to
     * @return is the object equal to this task
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return getId() == task.getId() &&
                Objects.equals(getDescription(), task.getDescription())
                && Objects.equals(getTitle(), task.getTitle())
                && Objects.equals(getListID(), task.getListID());
    }

    /**
     * Computes the hash code for this task.
     * @return the hash code for this task
     */
    @Override
    public int hashCode() {
        return Objects.hash(getId(), getDescription(), getTitle(), getListID());
    }

    /**
     * Gets a string representation of this task.
     * @return a string representation of this task
     */
    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", description='" + description + '\'' +
                ", title='" + title + '\'' +
                '}';
    }
}
