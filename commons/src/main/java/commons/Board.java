package commons;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonView;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.*;

@Entity
@Table(name = "Boards")
public class Board {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "integer")
    @JsonView(BoardSummary.class)
    private int id;

    @Column(name = "name")
    @JsonView(BoardSummary.class)
    @Size(max = 20)
    private String name;

    @Column(name = "password")
    @JsonView(BoardSummary.class)
    @Size(max = 20)
    private String password;

    @Column(name="inviteKey")
    @Size(max=20)
    private String inviteKey;

    @JsonManagedReference
    @OneToMany(mappedBy = "board", cascade = CascadeType.REMOVE)
    private java.util.List<List> lists;

    @JsonManagedReference
    @OneToMany(mappedBy = "board")
    private java.util.List<Tag> tags;

    @JsonManagedReference
    @OneToMany(mappedBy = "board")
    private java.util.List<Color> colors;

    /**
     * Creates a new Board object with the given name, password, and set of lists.
     *
     * @param name     The name of the board.
     * @param password The password of the board.
     * @param lists    The set of lists associated with the board.
     * @return A new Board object with the given name, password, and set of lists.
     */
    public static Board create(String name, String password, java.util.List<List> lists,
                               java.util.List<Color> colors, java.util.List<Tag> tags) {
        Board board = new Board();
        board.name = name;
        board.password = password;
        board.lists = lists;
        board.colors = colors;
        board.tags = tags;
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
    public java.util.List<List> getLists() {
        return lists;
    }

    /**
     * Returns the list of tags associated with the board.
     *
     * @return The list of tags associated with the board.
     */
    public java.util.List<Tag> getTags() { return tags; }

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
    public void setLists(java.util.List<List> lists) {
        this.lists = lists;
    }

    /**
     * Gets the invite key of the board
     * @return the invite key
     */
    public String getInviteKey() {
        return inviteKey;
    }

    /**
     * Sets the invite key of the board
     * @param inviteKey the invite key to be set
     */
    public void setInviteKey(String inviteKey) {
        this.inviteKey = inviteKey;
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
                Objects.equals(getPassword(), board.getPassword());
    }

    /**
     * Returns a hash code value for the Board object based on its id, name, and password fields.
     *
     * @return an int representing the hash code value for the Board object
     */
    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName(), getPassword());
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
                ", inviteKey='" + inviteKey + '\'' +
                '}';
    }

    public Color getBoardColor() {
        return this.colors.get(0);
    }

    public Color getListColor() {
        return this.colors.get(1);
    }

    public void setBoardColor(Color color){
        this.colors.set(0, color);
    }

    public void setListColor(Color color){
        this.colors.set(1, color);
    }

    public java.util.List<Color> getColors(){
        return colors;
    }

    public void setColors(java.util.List<Color> colors) {
        this.colors = colors;
    }
}
