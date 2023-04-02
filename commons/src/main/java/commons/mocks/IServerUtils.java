package commons.mocks;

import commons.Color;
import commons.List;
import commons.Tag;
import commons.Task;
import commons.models.BoardEditModel;
import commons.models.ColorEditModel;
import commons.models.IdResponseModel;
import commons.models.ListEditModel;

public interface IServerUtils {

    IdResponseModel addList(List list, int id);

    IdResponseModel deleteList(int boardId, int listId);

    IdResponseModel editList(int boardId, int listId, ListEditModel model);

    IdResponseModel addTask(Task task, int listID);

    IdResponseModel removeTask(int taskID, int listID);

    IdResponseModel editTask(int taskID, commons.models.TaskEditModel model);

    IdResponseModel deleteBoard(int boardID);

    IdResponseModel editBoard(int boardId, BoardEditModel edit);

    IdResponseModel deleteColor(int boardID, int colorId);

    IdResponseModel addColor(Color color);

    IdResponseModel setColorToBoard(Color color, int boardID);

    IdResponseModel editColor(int colorId, ColorEditModel edit);

    IdResponseModel addTagToTask(Tag tag, int taskId);

    IdResponseModel addTagToBoard(Tag tag, int boardId);
}
