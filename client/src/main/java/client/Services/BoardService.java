package client.Services;

import client.user.UserData;
import client.utils.ServerUtils;
import commons.Board;
import commons.models.BoardEditModel;
import commons.models.IdResponseModel;
import commons.sync.BoardDeleted;
import commons.sync.BoardEdited;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class BoardService {
    private UserData userData;
    private ColorService colorService;
    private ServerUtils serverUtils;

    public BoardService(UserData userData, ServerUtils serverUtils) {
        this.userData = userData;
        this.colorService = new ColorService(userData,serverUtils);
        this.serverUtils = serverUtils;
    }

    public IdResponseModel addBoard(String name) {
        commons.Color boardColor = new commons.Color("#000000", "#FFFFFF");
        commons.Color listColor = new commons.Color("#000000", "#FFFFFF");
        boardColor.setIsDefault(true);
        listColor.setIsDefault(true);

        IdResponseModel boardColorResponse = serverUtils.addColor(boardColor);
        if (boardColorResponse.getId() == -1) {
            return boardColorResponse;
        }

        IdResponseModel listColorResponse = serverUtils.addColor(listColor);
        if (listColorResponse.getId() == -1) {
            return listColorResponse;
        }

        boardColor.setId(boardColorResponse.getId());
        listColor.setId(listColorResponse.getId());
        List<commons.Color> colors = new ArrayList<>();
        colors.add(boardColor);
        colors.add(listColor);

        Board board = new Board(name, null,
                new ArrayList<>(), colors, new ArrayList<>());
        String inviteKey = generateInviteKey();
        board.setInviteKey(inviteKey);

        IdResponseModel response = serverUtils.addBoard(board);
        if (response.getId() == -1) {
            return response;
        }
        IdResponseModel colorResponse = serverUtils.setColorToBoard(boardColor, response.getId());
        if (colorResponse.getId() == -1) {
            return colorResponse;
        }

        colorResponse = serverUtils.setColorToBoard(listColor, response.getId());
        if (colorResponse.getId() == -1) {
            return colorResponse;
        }
        this.userData.joinBoard(response.getId(), board.getPassword());
        return response;
    }


    /**
     * Generates a random 15-character string to serve as the invite key
     *
     * @return the generated invite key
     */
    private String generateInviteKey() {
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 15; i++) {
            int randomInt = random.nextInt(26);
            char letter = (char) (randomInt + 'a');
            sb.append(letter);
        }

        return sb.toString();
    }

    public Board getBoard(int id) {
        return this.serverUtils.getBoard(id);
    }

    public Board getCurrentBoard() {
        return this.userData.getCurrentBoard();
    }

    public void refresh() {
        this.userData.refresh();
    }

    public IdResponseModel delete(int boardId) {
        this.userData.leaveBoard(boardId);
        this.userData.saveToDisk();
        BoardDeleted boardDeleted = new BoardDeleted(boardId);

        return userData.deleteBoard(boardDeleted);
    }

    public void enterBoard(int id) {
        this.userData.openBoard(id);
        this.userData.subscribeToBoard();
        this.userData.saveToDisk();
    }

    public void leaveBoard(int id) {
        this.userData.leaveBoard(id);
        this.userData.saveToDisk();
    }

    public void copyInviteLink(int id) {
        Board board = serverUtils.getBoard(id);
        String inviteKey = board.getInviteKey();

        StringSelection string = new StringSelection(inviteKey);
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(string, null);
    }

    public IdResponseModel editBoard(String title) {
        IdResponseModel response = this.userData.updateBoard(
                new BoardEdited(this.userData.getCurrentBoard().getId(),
                        new BoardEditModel(title, userData.getCurrentBoard().getPassword())));
        this.userData.refresh();
        return response;
    }

    public IdResponseModel search(String inviteKey) {
        Board board = serverUtils.getBoardByInviteKey(inviteKey);

        if (board == null) {
            return new IdResponseModel(-1, "Board not found!");
        }
        this.userData.joinBoard(board.getId(), board.getPassword());
        this.userData.saveToDisk();
        this.userData.openBoard(board.getId());
        return new IdResponseModel(1, null);
    }

    public Board[] getAllBoards() {
        return this.serverUtils.getAllBoards();
    }

    public Board[] getBoardsUpdated() {
        return this.serverUtils.getBoardsUpdated();
    }

    public Map<Integer, String> getJoinedBoards() {
        return this.userData.getBoards();
    }

    public boolean isCurrentBoardLocked() { return this.userData.isCurrentBoardLocked();}
}
