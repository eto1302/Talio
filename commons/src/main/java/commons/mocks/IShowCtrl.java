package commons.mocks;

import commons.*;

public interface IShowCtrl {

    Object addList(List list);

    void editList(List list);

    void deleteList(List list);

    void addTask(Task task, List list);

    void refreshAdminBoards();

    void refreshList(int listID);

    Object addTaskColor(Color color);

    void deleteTaskColor(Color color);

//    void addTask(Task task);
}
