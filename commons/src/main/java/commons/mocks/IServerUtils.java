package commons.mocks;

import commons.List;
import commons.Subtask;
import commons.Task;
import commons.models.BoardEditModel;
import commons.models.IdResponseModel;
import commons.models.ListEditModel;
import commons.models.SubtaskEditModel;

public interface IServerUtils {

    IdResponseModel addList(List list, int id);

    IdResponseModel deleteList(int boardId, int listId);

    IdResponseModel editList(int boardId, int listId, ListEditModel model);

    IdResponseModel addTask(Task task, int listID);

    IdResponseModel removeTask(int taskID, int listID);

    IdResponseModel editTask(int taskID, commons.models.TaskEditModel model);

    IdResponseModel deleteBoard(int boardID);

    IdResponseModel editBoard(int boardId, BoardEditModel edit);

    IdResponseModel addSubtask(Subtask subtask, int taskID);

    IdResponseModel deleteSubtask(int taskID, int subtaskID);

    IdResponseModel editSubtask(int subtaskID, SubtaskEditModel model);
}
