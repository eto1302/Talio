package commons;

import javax.persistence.*;
//import java.util.ArrayList;
import java.util.Objects;

@Entity
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "integer")
    private int id;
    @Column(name = "description", columnDefinition = "varchar(255)")
    private String description;
    @Column(name = "title", columnDefinition = "varchar(255)")
    private String title;

    @ManyToOne
    @JoinColumn(name="listId", nullable=false)
    private List list;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "tagId", referencedColumnName = "id")
    private Tag tag;

//    private java.util.List<String> subTasks;

    /**
     * Creates a new task with the specified description and title.
     * @param description the description of the task
     * @param title the title of the task
     * @return the newly created task
     */
    public static Task create(String description, String title) {
        Task task = new Task();
        task.description = description;
        task.title = title;
        return task;
    }

//    public void makeSubtaskList(){
//        this.subTasks = new ArrayList<>();
//    }
//
//    public java.util.List<String> getSubTasks(){
//        return this.subTasks;
//    }

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
     * Gets the tag associated with the task.
     * @return the tag associated with the task
     */
    public Tag getTag() {
        return tag;
    }

    /**
     * Sets the tag associated with the task.
     * @param tag the tag associated with the task
     */
    public void setTag(Tag tag) {
        this.tag = tag;
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
                && Objects.equals(getTitle(), task.getTitle());
    }

    /**
     * Computes the hash code for this task.
     * @return the hash code for this task
     */
    @Override
    public int hashCode() {
        return Objects.hash(getId(), getDescription(), getTitle());
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
