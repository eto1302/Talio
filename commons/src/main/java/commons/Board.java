package commons;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.*;

@Entity
@Table(name = "Boards")
public class Board {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "integer")
    private int id;

    @Column(name = "name")
    @Size(max = 20)
    private String name;

    @Column(name = "password")
    @Size(max = 20)
    private String password;

    @Column(name = "fontColor")
    // regular expression that matches a valid color string in the format "#RRGGBB" or "#AARRGGBB"
    @Pattern(regexp = "^#([A-Fa-f0-9]{6}|[A-Fa-f0-9]{8})$")
    private String fontColor;

    @Column(name = "backgroundColor")
    @Pattern(regexp = "^#([A-Fa-f0-9]{6}|[A-Fa-f0-9]{8})$")
    private String backgroundColor;
    @JsonManagedReference
    @OneToMany()
    private Set<List> lists;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "tagId", referencedColumnName = "id")
    private Tag tag;

    /**
     * Creates a new Board object with the given name, password, and set of lists.
     *
     * @param name     The name of the board.
     * @param password The password of the board.
     * @param lists    The set of lists associated with the board.
     * @return A new Board object with the given name, password, and set of lists.
     */
    public static Board create(String name, String password, Set<List> lists,
                               String fontColor, String backgroundColor) {
        Board board = new Board();
        board.name = name;
        board.password = password;
        board.lists = lists;
        board.fontColor = fontColor;
        board.backgroundColor = backgroundColor;
        return board;
    }

    /**
     * Creates a new Board object.
     */
    public Board() {
    }

    /**
     * Returns the id of the board.
     *
     * @return The id of the board.
     */
    public int getId() {
        return id;
    }

    /**
     * Returns the name of the board.
     *
     * @return The name of the board.
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the password of the board.
     *
     * @return The password of the board.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Returns the set of lists associated with the board.
     *
     * @return The set of lists associated with the board.
     */
    public Set<List> getLists() {
        return lists;
    }

    /**
     * Sets the name of the board.
     *
     * @param name The new name of the board.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Sets the password of the board.
     *
     * @param password The new password of the board.
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Sets the lists of the board.
     *
     * @param lists The new lists of the board.
     */
    public void setLists(Set<List> lists) {
        this.lists = lists;
    }

    /**
     * Gets the font color of the board
     * @return the font color of the board
     */
    public String getFontColor() {
        return fontColor;
    }

    /**
     * Sets the font color of the board
     * @param fontColor to be set
     */
    public void setFontColor(String fontColor) {
        this.fontColor = fontColor;
    }

    /**
     * Gets the background color of the board
     * @return the background color of the board
     */
    public String getBackgroundColor() {
        return backgroundColor;
    }

    /**
     * Sets the background color
     * @param backgroundColor to be set
     */
    public void setBackgroundColor(String backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    /**
     * Returns true if the given object is equal to this board.
     * <p>
     * Two boards are considered equal if they have the same id, name, and password.
     *
     * @param o The object to compare to this board.
     * @return True if the given object is equal to this board.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Board board = (Board) o;
        return getId() == board.getId() && Objects.equals(getName(), board.getName()) &&
                Objects.equals(getPassword(), board.getPassword()) &&
                Objects.equals(getFontColor(), board.getFontColor()) &&
                Objects.equals(getBackgroundColor(), board.getBackgroundColor());
    }

    /**
     * Returns a hash code value for the Board object based on its id, name, and password fields.
     *
     * @return an int representing the hash code value for the Board object
     */
    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName(), getPassword(),
                getFontColor(), getBackgroundColor());
    }

    /**
     * Returns a string representation of the Board object,
     * including its id, name, password, lists, and tag fields.
     *
     * @return a String representation of the Board object
     */
    @Override
    public String toString() {
        return "Board{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", password='" + password + '\'' +
                ", lists=" + lists +
                ", tag=" + tag +
                ", fontColor=" + fontColor +
                ", backgroundColor=" + backgroundColor +
                '}';
    }
}
