package commons.mocks;

import commons.List;
import commons.models.IdResponseModel;
import commons.models.ListEditModel;

public interface IServerUtils {

    IdResponseModel addList(List list, int id);

    IdResponseModel deleteList(int id, int boardID);

    IdResponseModel editList(int boardId, int listId, ListEditModel model);

}
