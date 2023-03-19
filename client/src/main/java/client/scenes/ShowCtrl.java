package client.scenes;

import client.MyFXML;
import client.MyModule;
import com.google.inject.Injector;
import commons.Board;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Pair;

import java.util.List;

import static com.google.inject.Guice.createInjector;

public class ShowCtrl {
    private static final Injector INJECTOR = createInjector(new MyModule());
    private static final MyFXML FXML = new MyFXML(INJECTOR);
    private Stage primaryStage, secondaryStage, popUpStage;
    private HomeController homeCtrl;
    private Scene home, addTask, addList, yourBoards, search, addTag, board,
            taskOverview, connection, addBoard, editTag, editTask, error, editList;
    private AddListController addListCtrl;
    private AddTaskController addTaskCtrl;
    private YourBoardsController yourBoardsCtrl;
    private SearchCtrl searchCtrl;
    private AddTagController addTagController;
    private BoardController boardController;
    private TaskOverview taskOverviewCtrl;
    private ConnectionCtrl connectionCtrl;
    private AddBoardController addBoardController;
    private EditTagController editTagController;
    private EditTaskController editTaskController;
    private ErrorController errorController;
    private EditListController editListCtrl;


    public void initialize(Stage primaryStage, List<Pair> loader) {
        this.primaryStage = primaryStage;
        homeCtrl = (HomeController) loader.get(0).getKey();
        home = new Scene((Parent) loader.get(0).getValue());
        addListCtrl = (AddListController) loader.get(1).getKey();
        addList = new Scene((Parent) loader.get(1).getValue());
        yourBoardsCtrl = (YourBoardsController) loader.get(2).getKey();
        yourBoards = new Scene((Parent) loader.get(2).getValue());
        addTaskCtrl = (AddTaskController) loader.get(3).getKey();
        addTask = new Scene((Parent) loader.get(3).getValue());
        searchCtrl = (SearchCtrl) loader.get(4).getKey();
        search = new Scene((Parent) loader.get(4).getValue());
        addTagController = (AddTagController) loader.get(5).getKey();
        addTag = new Scene((Parent) loader.get(5).getValue());
        boardController = (BoardController) loader.get(6).getKey();
        board = new Scene((Parent) loader.get(6).getValue());
        taskOverviewCtrl = (TaskOverview) loader.get(7).getKey();
        taskOverview = new Scene((Parent) loader.get(7).getValue());
        connectionCtrl=(ConnectionCtrl) loader.get(8).getKey();
        connection = new Scene((Parent)loader.get(8).getValue());
        addBoardController = (AddBoardController) loader.get(9).getKey();
        addBoard = new Scene((Parent) loader.get(9).getValue());
        editTagController = (EditTagController) loader.get(10).getKey();
        editTag = new Scene((Parent) loader.get(10).getValue());
        editTaskController = (EditTaskController) loader.get(11).getKey();
        editTask = new Scene((Parent) loader.get(11).getValue());
        errorController = (ErrorController) loader.get(12).getKey();
        error = new Scene((Parent) loader.get(12).getValue());
        showConnection();
        primaryStage.show();
    }

    public void showAddBoard(){
        secondaryStage = new Stage();
        secondaryStage.setScene(addBoard);
        secondaryStage.setTitle("Add a board");
        secondaryStage.show();
    }

    public void showConnection(){
        this.primaryStage.setScene(connection);
        this.primaryStage.setTitle("Connect to a server!");
    }

    public void showAddList() {
        secondaryStage = new Stage();
        secondaryStage.setScene(addList);
        secondaryStage.setTitle("Add a list");
        secondaryStage.show();

    }

    public void showHome() {
        primaryStage.setTitle("Home");
        primaryStage.setScene(this.home);
    }

    public void showYourBoards(){
        primaryStage.setTitle("Your boards");
        this.yourBoardsCtrl.fillBoardBox();
        primaryStage.setScene(this.yourBoards);
    }

    public void showAddTask(){
        secondaryStage = new Stage();
        secondaryStage.setScene(addTask);
        secondaryStage.setTitle("Add a task");
        secondaryStage.show();
    }

    public void cancel() {
        secondaryStage.close();
    }

    public void showSearch() {
        secondaryStage = new Stage();
        secondaryStage.setScene(search);
        secondaryStage.setTitle("Search for a board");
        secondaryStage.show();
    }


    public void showAddTag(){
        secondaryStage = new Stage();
        secondaryStage.setScene(addTag);
        secondaryStage.setTitle("Add a tag");
        secondaryStage.show();
    }

    public void showBoard(){
        primaryStage.setTitle("Board");
        boardController.setup();
        primaryStage.setScene(this.board);
    }

    public void showTaskOverview() {
        secondaryStage=new Stage();
        secondaryStage.setScene(taskOverview);
        secondaryStage.setTitle("See your task details");
        secondaryStage.show();
    }

    public void showEditList(commons.List list, ListShapeCtrl controller){
        var editList = FXML.load(EditListController.class,
                "client", "scenes", "EditList.fxml");
        editList.getKey().setup(list, controller);

        secondaryStage=new Stage();
        secondaryStage.setScene(new Scene(editList.getValue()));
        secondaryStage.setTitle("Edit your list");
        secondaryStage.show();
    }

    /**
     * Adds the list to the board and updates the scene
     * @param list the list object whose attributes specify the visual of the list
     */
    public void addList(commons.List list){
        var listShape = FXML.load(ListShapeCtrl.class, "client", "scenes", "List.fxml");
        Scene initializeList = new Scene(listShape.getValue());
        ListShapeCtrl listShapeCtrl = listShape.getKey();

        listShapeCtrl.setId(list.getId());
        Scene listScene = listShapeCtrl.getSceneUpdated(list);
        Scene scene = boardController.putList(listScene);
        primaryStage.setScene(scene);
    }

    /**
     * Adds the board and updates the scene
     * @param board the board object whose attributes specify the visual of the board
     */
    public void addBoard(Board board){
        var boardShape = FXML.load(BoardShape.class, "client", "scenes", "BoardShape.fxml");
        Scene initializeBoard = new Scene(boardShape.getValue());

//        Scene boardScene = boardShape.getKey().getSceneUpdated(board);
//        Scene scene = yourBoardsCtrl.putBoard(boardScene);
//        primaryStage.setScene(scene);
    }
    public void showError(String errorMessage) {
        popUpStage = new Stage();
        popUpStage.setScene(error);
        popUpStage.setTitle("error");
        errorController.setErrorMessage(errorMessage);
        popUpStage.show();
    }

    public void closePopUp() {
        popUpStage.close();
    }
}
