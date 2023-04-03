package commons.models;

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
}
