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

    public void setTasks(java.util.List<Task> tasks) {
        this.tasks = tasks;
    }

    @OneToMany
    private java.util.List<Task> tasks;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "tagId", referencedColumnName = "id")
    private Tag tag;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name="boardId", nullable=false)
    @JsonIgnore
    private Board board;

    @Column(name="background", columnDefinition = "varchar(255)")
    private String backgroundColor;
    @Column(name="font", columnDefinition = "varchar(255)")
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

    public List() {}

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public Board getBoard() {
        return board;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setBoard(Board board) {
        this.board = board;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        List list = (List) o;
        return getId() == list.getId() && Objects.equals(getName(), list.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName());
    }

    @Override
    public String toString() {
        return "List{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", tasks=" + tasks +
                '}';
    }
}
