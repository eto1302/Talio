package commons.models;

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
}
