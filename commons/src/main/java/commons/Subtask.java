package commons;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

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

    @Column(name = "t_id")
    @NotNull
    private int taskID;

    @JsonBackReference
    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn(name="taskId", nullable=false)
    private Task task;



}
