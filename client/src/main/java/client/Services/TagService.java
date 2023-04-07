package client.Services;

import client.user.UserData;
import client.utils.ServerUtils;
import commons.Board;
import commons.Tag;
import commons.models.IdResponseModel;
import commons.sync.TagCreated;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TagService {
    private final ServerUtils serverUtils;
    private final UserData userData;
    private final ColorService colorService;

    public TagService(UserData userData, ServerUtils serverUtils) {
        this.userData = userData;
        this.serverUtils = serverUtils;
        this.colorService = new ColorService(userData, serverUtils);
    }

    public List<Tag> getTagByTask(int id) {
        var response = this.serverUtils.getTagsByTask(id);
        if(!response.getStatusCode().is2xxSuccessful()){
            return new ArrayList<>();
        }
        return Arrays.asList(response.getBody());
    }

    public List<Tag> getTagByBoard() {
        var response = this.serverUtils.getTagByBoard(
                this.userData.getCurrentBoard().getId());
        if(!response.getStatusCode().is2xxSuccessful()){
            return new ArrayList<>();
        }
        return Arrays.asList(response.getBody());
    }

    public IdResponseModel addTag(String text, Color background, Color font) {
        String textColor = colorService.colorToHex(font);
        String backgroundColor = colorService.colorToHex(background);
        commons.Color color = new commons.Color(textColor, backgroundColor);

        Tag tag = new Tag(text, color);

        Board current = userData.getCurrentBoard();
        return userData.updateBoard(new TagCreated(current.getId(), tag, current));
    }
}
