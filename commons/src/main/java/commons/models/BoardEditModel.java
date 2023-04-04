package commons.models;

import java.util.Objects;

public class BoardEditModel {
    private String name;
    private String password;

    public BoardEditModel(String name, String password) {
        this.name = name;
        this.password = password;
    }

    public BoardEditModel() {
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public String toString() {
        return "BoardEditModel{" +
                "name='" + name + "\', " +
                "password='" + password + "\'" +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BoardEditModel that = (BoardEditModel) o;
        return Objects.equals(getName(), that.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName());
    }
}
