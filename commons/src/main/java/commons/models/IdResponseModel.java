package commons.models;

import java.util.Objects;

public class IdResponseModel {
    private int id;
    private String errorMessage;

    public IdResponseModel(int id, String errorMessage) {
        this.id = id;
        this.errorMessage = errorMessage;
    }

    public IdResponseModel(){}

    public int getId() {
        return id;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        IdResponseModel model = (IdResponseModel) o;
        return id == model.id && Objects.equals(errorMessage, model.errorMessage);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, errorMessage);
    }
}
