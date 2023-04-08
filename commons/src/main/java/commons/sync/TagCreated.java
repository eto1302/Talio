package commons.sync;

import commons.Board;
import commons.Tag;
import commons.mocks.IServerUtils;
import commons.mocks.IUserData;
import commons.models.IdResponseModel;

public class TagCreated extends BoardUpdate{

    private Tag tag;
    private Board board;

    public TagCreated(int boardID, Tag tag, Board board) {
        super(boardID);
        this.tag = tag;
        this.board = board;
        board.getTags().add(tag);
        tag.setBoard(board);
        tag.setBoardId(board.getId());
    }

    public TagCreated(){}

    public Tag getTag() {
        return tag;
    }

    public void setTag(Tag tag) {
        this.tag = tag;
    }

    public Board getBoard() {
        return board;
    }

    public void setBoard(Board board) {
        this.board = board;
    }

    @Override
    public IdResponseModel sendToServer(IServerUtils server) {
        IdResponseModel id = server.addTagToBoard(tag, board.getId());
        tag.setId(id.getId());
        return id;
    }

    @Override
    public void apply(IUserData data, IServerUtils serverUtils) {
        data.getShowCtrl().addTag(tag);
    }
}
