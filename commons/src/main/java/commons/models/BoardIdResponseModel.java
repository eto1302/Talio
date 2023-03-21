package commons.models;

import java.util.Objects;

public class BoardIdResponseModel {
    private int boardId;
    private String errorMessage;

    public BoardIdResponseModel(int boardId, String errorMessage) {
        this.boardId = boardId;
        this.errorMessage = errorMessage;
    }

    public BoardIdResponseModel(){}

    public int getBoardId() {
        return boardId;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setBoardId(int boardId) {
        this.boardId = boardId;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BoardIdResponseModel model = (BoardIdResponseModel) o;
        return boardId == model.boardId && Objects.equals(errorMessage, model.errorMessage);
    }

    @Override
    public int hashCode() {
        return Objects.hash(boardId, errorMessage);
    }
}
