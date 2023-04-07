package client.Services;

import client.user.UserData;
import client.utils.ServerUtils;
import commons.List;
import commons.Task;
import commons.models.IdResponseModel;
import commons.models.ListEditModel;
import commons.sync.ListAdded;
import commons.sync.ListDeleted;
import commons.sync.ListEdited;

import java.util.ArrayList;

public class ListService {
    private final ServerUtils serverUtils;
    private final UserData userData;

    public ListService(UserData userData, ServerUtils serverUtils){
        this.userData = userData;
        this.serverUtils = serverUtils;
    }

    public IdResponseModel addList(String name) {
        List list = new List(name,this.userData.getCurrentBoard().getId(),
                new ArrayList<Task>());
        return userData.updateBoard(new
                ListAdded(userData.getCurrentBoard().getId(), list));
    }

    public IdResponseModel editList(List list, String name) {
        ListEditModel requestModel = new ListEditModel(name);
        return userData.updateBoard(new
                ListEdited(list.getBoardId(), list.getId(), requestModel));
    }

    public IdResponseModel deleteList(List list) {
        return userData.updateBoard(new
                ListDeleted(list.getBoardId(), list.getId()));
    }

    public List getList(int id) {
        return this.serverUtils.getList(id);
    }
}
