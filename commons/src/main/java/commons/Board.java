package commons;

import javax.persistence.*;
import java.util.*;

@Entity
@Table(name="Boards")
public class Board {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "integer")
    private int id;

    @Column(name = "name", columnDefinition = "varchar(255)")
    private String name;

    @Column(name = "password", columnDefinition = "varchar(255)")
    private String password;

    public void setLists(Set<List> lists) {
        this.lists = lists;
    }

    @OneToMany
    private Set<List> lists;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "tagId", referencedColumnName = "id")
    private Tag tag;

    public static Board create(String name, String password, Set<List> lists) {
        Board board = new Board();
        board.name = name;
        board.password = password;
        board.lists=lists;
        return board;
    }

    public Board() {}

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public Set<List> getLists() {
        return lists;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Board board = (Board) o;
        return getId() == board.getId() && Objects.equals(getName(), board.getName()) &&
                Objects.equals(getPassword(), board.getPassword());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName(), getPassword());
    }

    @Override
    public String toString() {
        return "Board{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", password='" + password + '\'' +
                ", lists=" + lists +
                ", tag=" + tag +
                '}';
    }
}
