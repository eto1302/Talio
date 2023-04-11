package commons.mocks;

import commons.*;

public interface IShowCtrl {

    Object addList(List list);

    void editList(List list);

    void deleteList(List list);

    void addTask(Task task, List list);

    void deleteTask(int listId, int taskId);

    void refreshAdminBoards();

    void refreshBoardCtrl();

    Object addTaskColor(Color color);

    void deleteTaskColor(Color color);

    void editColor(Color color);

    void showBoard();

    void showColorPicker();

//    void addTask(Task task);
    void addTagToTask(Tag tag,Task task);

    void addTag(Tag tag);

    void deleteTag(Tag tag);

    void editTag(Tag tag);

    void removeTagFromTask(Tag tag, Task task);

    boolean isColorPickerOpen();

    void cancel();

    void refreshSubtasks();

}
