package commons.mocks;

import commons.Board;
import commons.models.IdResponseModel;

public interface IUserData {

    Board getCurrentBoard();

    IShowCtrl getShowCtrl();

    void refresh();

}
