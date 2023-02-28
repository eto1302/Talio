package server.card;

import server.task.Task;

import javax.persistence.*;
import java.util.*;

@Entity
//@Table
public class Card {
    @Id
    @SequenceGenerator(
            name="card_sequence",
            sequenceName = "card_sequence"
    )
    @GeneratedValue(
            generator = "card_sequence",
            strategy = GenerationType.SEQUENCE
    )
    private int id;
    private String name;
    @OneToMany
    List<Task> tasks;

    public Card(String name, List<Task> tasks) {
        this.name = name;
        this.tasks=tasks;
    }

    public Card() {}

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Card card = (Card) o;
        return getId() == card.getId() && Objects.equals(getName(), card.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName());
    }
}
