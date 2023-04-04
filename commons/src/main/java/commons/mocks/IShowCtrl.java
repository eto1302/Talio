package commons.mocks;

import commons.*;

public interface IShowCtrl {

    Object addList(List list);

    void editList(List list);

    void deleteList(List list);

    void addTask(Task task, List list);

    void deleteTask(Task task);

    void refreshAdminBoards();

    void refreshList(int listID);

    Object addTaskColor(Color color);

    void deleteTaskColor(Color color);

    void editColor(Color color);

//    void addTask(Task task);
    void addTagToTask(Tag tag,Task task);

    void addTag(Tag tag);

    void deleteTag(Tag tag);

    void editTag(Tag tag);

    void removeTagFromTask(Tag tag, Task task);
}
