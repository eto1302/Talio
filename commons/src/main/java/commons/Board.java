package commons;

import javax.persistence.*;
import java.util.*;

@Entity
//@Table(name="Board")
public class Board {

    @Id
    @SequenceGenerator(
            name = "board_sequence",
            sequenceName = "board_sequence"
    )
    @GeneratedValue(
            generator = "board_sequence",
            strategy = GenerationType.SEQUENCE
    )
    @Column(name = "id", columnDefinition = "integer")
    private int id;

    @Column(name = "name", columnDefinition = "varchar(255)")
    private String name;

    @Column(name = "password", columnDefinition = "varchar(255)")
    private String password;

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL)
    Set<Card> cards;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "tagId", referencedColumnName = "id")
    private Tag tag;

    public Board(String name, String password, Set<Card> cards) {
        this.name = name;
        this.password = password;
        this.cards=cards;
    }

    public Board() {

    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public Set<Card> getCards() {
        return cards;
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
                ", cards=" + cards +
                '}';
    }
}
