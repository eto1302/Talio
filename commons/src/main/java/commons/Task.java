package commons;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class Task {
    @Id
    @SequenceGenerator(
            name = "task_sequence",
            sequenceName = "task_sequence"
    )
    @GeneratedValue(
            generator = "task_sequence",
            strategy = GenerationType.SEQUENCE
    )
    private int id;
    private String description;
    private String title;

    public Task(String description, String title) {
        this.description = description;
        this.title = title;
    }

    public Task() {}

    public int getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public String getTitle() {
        return title;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return getId() == task.getId() && Objects.equals(getDescription(), task.getDescription())
                && Objects.equals(getTitle(), task.getTitle());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getDescription(), getTitle());
    }

    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", description='" + description + '\'' +
                ", title='" + title + '\'' +
                '}';
    }
}
