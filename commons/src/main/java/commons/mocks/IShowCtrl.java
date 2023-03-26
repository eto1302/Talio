package commons.mocks;

import commons.*;

public interface IShowCtrl {

    Object addList(List list);

    void editList(List list);

    void deleteList(List list);

    void addTask(Task task, List list);

}
