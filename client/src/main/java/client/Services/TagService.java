package client.Services;

import client.user.UserData;
import client.utils.ServerUtils;
import commons.Tag;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TagService {
    private final ServerUtils serverUtils;
    private final UserData userData;

    public TagService(UserData userData, ServerUtils serverUtils) {
        this.userData = userData;
        this.serverUtils = serverUtils;
    }

    public List<Tag> getTagByTask(int id) {
        var response = this.serverUtils.getTagsByTask(id);
        if(!response.getStatusCode().is2xxSuccessful()){
            return new ArrayList<>();
        }
        return Arrays.asList(response.getBody());
    }

    public List<Tag> getTagByBoard() {
        var response = this.serverUtils.getTagsByTask(
                this.userData.getCurrentBoard().getId());
        if(!response.getStatusCode().is2xxSuccessful()){
            return new ArrayList<>();
        }
        return Arrays.asList(response.getBody());
    }
}
