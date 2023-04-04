package commons.mocks;

import commons.*;

public interface IShowCtrl {

    Object addList(List list);

    void editList(List list);

    void deleteList(List list);

    void addTask(Task task, List list);

    void deleteTask(Task task);

    void refreshAdminBoards();

    void refreshBoardCtrl();

    Object addTaskColor(Color color);

    void deleteTaskColor(Color color);

    void editColor(Color color);

    void showBoard();

    void showColorPicker();

//    void addTask(Task task);
}
