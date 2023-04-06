package commons;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Objects;

@Entity
@Table (name = "Subtasks")
public class Subtask {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "integer")
    private int id;

    @Column(name = "description", columnDefinition = "varchar(255)")
    private String description;
    @Column(name = "checked", columnDefinition = "boolean")
    private boolean checked;
    @Column(name="index", columnDefinition = "integer")
    private int index;

    @Column(name = "t_id")
    @NotNull
    private int taskID;

    @JsonBackReference
    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn(name="taskId", nullable=false)
    private Task task;

    public static Subtask create(String description, boolean checked, int taskID){
        Subtask subtask = new Subtask();
        subtask.description=description;
        subtask.checked=checked;
        subtask.taskID=taskID;
        return  subtask;
    }

    /**
     * creates empty subtask
     */
    public Subtask(){}

    /**
     * Gets the id of the subtask
     * @return the id of the subtask
     */
    public int getId() {
        return id;
    }

    /**
     * Gets the description of the subtask
     * @return the description of the subtask
     */
    public String getDescription() {
        return description;
    }

    /**
     * Gets the status of the subtask (checked or unchecked)
     * @return the status of the subtask
     */
    public boolean isChecked() {
        return checked;
    }

    /**
     * Gets the ID of the Task in which the subtask is
     * @return the ID of the Task
     */
    public int getTaskID() {
        return taskID;
    }

    /**
     * Gets the Task in which the subtask is
     * @return the Task of the subtask
     */
    public Task getTask() {
        return task;
    }

    /**
     * Sets the id of the subtask
     * @param id to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Sets the description of the subtask
     * @param description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * sets the status of the subtask
     * @param checked status to be set
     */
    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    /**
     * Sets the ID of the task the subtask is in
     * @param taskID ID of the task to be set
     */
    public void setTaskID(int taskID) {
        this.taskID = taskID;
    }

    /**
     * Sets the task in which the subtask is in
     * @param task to be set
     */
    public void setTask(Task task) {
        this.task = task;
    }

    /**
     * Checks whether another object is equal to this one
     * @param o the object that is being compared
     * @return true if they are of the same type and have the same id, description, status
     *         and task, false otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Subtask subtask = (Subtask) o;
        return getId() == subtask.getId() && isChecked() == subtask.isChecked() &&
                getIndex()==subtask.getIndex() &&
                getTaskID() == subtask.getTaskID() &&
                Objects.equals(getDescription(), subtask.getDescription());
    }

    /**
     * Computes a hash code value for this object
     * @return the hash value
     */
    @Override
    public int hashCode() {
        return Objects.hash(getId(), getDescription(), isChecked(),
                getTaskID(), getTask(), getIndex());
    }

    /**
     * Makes a human-readable representation of this object
     * @return the string representing the object
     */
    @Override
    public String toString() {
        return "Subtask{" +
                "id=" + id +
                "index=" + index+
                ", description='" + description + '\'' +
                ", checked=" + checked +
                ", taskID=" + taskID +
                '}';
    }
}
