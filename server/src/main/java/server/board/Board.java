package server.board;

import server.card.Card;

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
    private int id;
    @Column
    private String name;
    @Column
    private String password;
    @OneToMany
    Set<Card> cards;

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
}
