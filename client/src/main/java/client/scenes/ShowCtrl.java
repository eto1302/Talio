package client.scenes;

import client.MyFXML;
import client.MyModule;
import com.google.inject.Injector;
import commons.Board;
import commons.Subtask;
import commons.Tag;
import commons.mocks.IShowCtrl;
import commons.Task;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Pair;

import java.util.List;

import static com.google.inject.Guice.createInjector;

public class ShowCtrl implements IShowCtrl {

    private static final Injector INJECTOR = createInjector(new MyModule());
    private static final MyFXML FXML = new MyFXML(INJECTOR);

    private Stage primaryStage, secondaryStage, popUpStage;

    private HomeController homeCtrl;
    private Scene home, addList, yourBoards, search, addTag, board, taskOverview, connection,
            addBoard, editTag, editTask, errorScene, addSubTask, editSubTask, admin;
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
    private AddSubTaskController addSubTaskController;
    private EditSubTaskController editSubTaskController;
    private AdminController adminController;


    public void initialize(Stage primaryStage, List<Pair> loader) {
        this.primaryStage = primaryStage;
        homeCtrl = (HomeController) loader.get(0).getKey();
        home = new Scene((Parent) loader.get(0).getValue());
        addListCtrl = (AddListController) loader.get(1).getKey();
        addList = new Scene((Parent) loader.get(1).getValue());
        yourBoardsCtrl = (YourBoardsController) loader.get(2).getKey();
        yourBoards = new Scene((Parent) loader.get(2).getValue());
        searchCtrl = (SearchCtrl) loader.get(3).getKey();
        search = new Scene((Parent) loader.get(3).getValue());
        addTagController = (AddTagController) loader.get(4).getKey();
        addTag = new Scene((Parent) loader.get(4).getValue());
        boardController = (BoardController) loader.get(5).getKey();
        board = new Scene((Parent) loader.get(5).getValue());
        connectionCtrl=(ConnectionCtrl) loader.get(6).getKey();
        connection = new Scene((Parent)loader.get(6).getValue());
        addBoardController = (AddBoardController) loader.get(7).getKey();
        addBoard = new Scene((Parent) loader.get(7).getValue());
        editTagController = (EditTagController) loader.get(8).getKey();
        editTag = new Scene((Parent) loader.get(8).getValue());
        editTaskController = (EditTaskController) loader.get(9).getKey();
        editTask = new Scene((Parent) loader.get(9).getValue());
        errorController = (ErrorController) loader.get(10).getKey();
        errorScene = new Scene((Parent) loader.get(10).getValue());
        addSubTaskController = (AddSubTaskController) loader.get(11).getKey();
        addSubTask = new Scene((Parent) loader.get(11).getValue());
        editSubTaskController = (EditSubTaskController) loader.get(12).getKey();
        editSubTask = new Scene((Parent) loader.get(12).getValue());
        admin = new Scene((Parent) loader.get(13).getValue());
        adminController = (AdminController) loader.get(13).getKey();

        showConnection();
        //showBoard();
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

    /**
     * Shows the window with options for adding a task in a list.
     *
     * @param controller   the list's controller
     * @param primaryStage the primary stage of our scenes.
     * @param list
     */
    public void showAddTask(ListShapeCtrl controller, Stage primaryStage, commons.List list){
        var addTask = FXML.load(AddTaskController.class, "client",
                "scenes", "AddTask.fxml");
        Scene addTaskScene = new Scene(addTask.getValue());
        addTask.getKey().setup(controller, primaryStage, list);
        secondaryStage = new Stage();
        secondaryStage.setScene(addTaskScene);
        secondaryStage.setTitle("Add a task");
        secondaryStage.show();
    }

    /**
     * Adds a task to the list.
     * @param task the task with the info
     * @param controller the list's controller
     * @param primaryStage the primary stage of the previous scene
     */
    public void addTask(Task task, ListShapeCtrl controller, Stage primaryStage){
        var taskShape = FXML.load(TaskShape.class, "client", "scenes", "Task.fxml");
        Scene taskScene = new Scene(taskShape.getValue());
        Scene updated = taskShape.getKey().getSceneUpdated(task);
        taskShape.getKey().set(task, primaryStage, controller);
        Scene finalScene = controller.addTask(updated);

        primaryStage.setScene(finalScene);
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

    public void showAddTag(Task task){
        popUpStage = new Stage();
        popUpStage.setScene(addTag);
        popUpStage.setTitle("Add a tag");
        popUpStage.show();
        addTagController.setup(task);
    }

    public void showAddSubTask(Task task) {
        popUpStage = new Stage();
        popUpStage.setScene(addSubTask);
        popUpStage.setTitle("Add a sub-task");
        popUpStage.show();
        addSubTaskController.setup(task);
    }

    public void showEditSubTask(Task task, int index) {
        popUpStage = new Stage();
        popUpStage.setScene(editSubTask);
        popUpStage.setTitle("Edit a sub-task");
        popUpStage.show();
        editSubTaskController.setup(task, index);
    }

    public void showBoard(){
        primaryStage.setTitle("Board");
        boardController.setup();
        primaryStage.setScene(this.board);
    }

    /**
     * Shows the details of the task. First sets the information in the window according to
     * the task.
     */
    public void showTaskOverview(Task task, ListShapeCtrl listShapeCtrl) {
        secondaryStage=new Stage();
        var taskOverview = FXML.load(TaskOverview.class, "client",
                "scenes", "TaskOverview.fxml");
        Scene initialize = new Scene(taskOverview.getValue());
        Scene updated = taskOverview.getKey().setup(task, listShapeCtrl);
        secondaryStage.setScene(updated);
        secondaryStage.setTitle("See your task details");
        secondaryStage.show();
    }

    /**
     * Shows the window with options for the editing the list.
     * First sets up the scene to the list's information
     * @param list the list that contains the info
     * @param controller the list's controller
     */
    public void showEditList(commons.List list, ListShapeCtrl controller, Stage primaryStage){
        var editList = FXML.load(EditListController.class,
                "client", "scenes", "EditList.fxml");
        editList.getKey().setup(list, controller, primaryStage);
        secondaryStage=new Stage();
        secondaryStage.setScene(new Scene(editList.getValue()));
        secondaryStage.setTitle("Edit your list");
        secondaryStage.show();
    }

    /**
     * Updates the window after editing the respective list.
     * @param list the updated list
     * @param controller the list's controller
     * @param primaryStage of our windows.
     */
    public void editList(commons.List list, ListShapeCtrl controller, Stage primaryStage) {
        Scene updated = controller.getSceneUpdated(list);
        primaryStage.setScene(updated);
    }

    /**
     * Adds the list to the board and updates the scene
     *
     * @param list the list object whose attributes specify the visual of the list
     * @return
     */
    public void addList(commons.List list) {
        var listShape = FXML.load(ListShapeCtrl.class, "client", "scenes", "List.fxml");
        Scene initializeList = new Scene(listShape.getValue());
        ListShapeCtrl listShapeCtrl = listShape.getKey();

        listShapeCtrl.set(list, primaryStage);
        Scene listScene = listShapeCtrl.getSceneUpdated(list);
        Scene scene = boardController.putList(listScene);
        primaryStage.setScene(scene);
    }

    public ListShapeCtrl addAndReturnList(commons.List list) {
        var listShape = FXML.load(ListShapeCtrl.class, "client", "scenes", "List.fxml");
        Scene initializeList = new Scene(listShape.getValue());
        ListShapeCtrl listShapeCtrl = listShape.getKey();

        listShapeCtrl.set(list, primaryStage);
        Scene listScene = listShapeCtrl.getSceneUpdated(list);
        Scene scene = boardController.putList(listScene);
        primaryStage.setScene(scene);
        return listShapeCtrl;
    }

    public void addTask(Task task, ListShapeCtrl listShapeCtrl) {
        var taskShape = FXML.load(TaskShape.class, "client", "scenes", "Task.fxml");
        Scene taskScene = new Scene(taskShape.getValue());
        TaskShape taskShapeCtrl = taskShape.getKey();

        taskShapeCtrl.set(task, primaryStage, listShapeCtrl);
        Scene updated = taskShapeCtrl.getSceneUpdated(task);
        Scene scene = listShapeCtrl.putTask(updated);
        primaryStage.setScene(scene);
    }

    /**
     * Adds the board and updates the scene
     * @param board the board object whose attributes specify the visual of the board
     */
    public void addBoard(Board board){
        var boardShape = FXML.load(BoardShape.class, "client", "scenes", "BoardShape.fxml");
        Scene initializeBoard = new Scene(boardShape.getValue());

        Scene boardScene = boardShape.getKey().getSceneUpdated(board);
        Scene scene = yourBoardsCtrl.putBoard(boardScene);
        primaryStage.setScene(scene);
    }
    public void showError(String errorMessage) {
        var error=FXML.load(ErrorController.class, "client", "scenes", "Error.fxml");
        errorController = error.getKey();
        errorScene =new Scene((Parent)error.getValue());
        popUpStage = new Stage();
        popUpStage.setScene(errorScene);
        popUpStage.setTitle("error");
        errorController.setErrorMessage(errorMessage);
        popUpStage.show();
    }

    public void closePopUp() {
        popUpStage.close();
    }

    public void addTag(Tag tag, TaskOverview controller, Stage primaryStage) {
    }

    public void showEditTask(Task task, ListShapeCtrl listShapeCtrl) {
        var editTaskPair = FXML.load(EditTaskController.class, "client", "scenes", "EditTask.fxml");
        editTaskController = editTaskPair.getKey();
        editTask = new Scene((Parent) editTaskPair.getValue());
        Scene updated = editTaskController.setup(task, listShapeCtrl, primaryStage);
        secondaryStage = new Stage();
        secondaryStage.setScene(editTask);
        secondaryStage.setTitle("Edit a task");
        secondaryStage.show();
    }

    public void addSubTask(Subtask subtask, EditTaskController editTaskController) {
        Scene subTaskScene = null;
        editTaskController.putSubtask(subTaskScene);
    }

    public void addTag(Tag tag, EditTaskController editTaskController) {
        Scene tagScene = null;
        editTaskController.putTag(tagScene);
    }

    // show a popup for the user to enter the admin password,
    public void showAdmin(){
        Stage stage = new Stage();
        stage.setTitle("Please enter admin password: ");

        Label label = new Label("Password: ");
        TextField text = new TextField();
        text.setPromptText("Enter password");

        Button button = new Button("Submit");
        button.setOnAction(e -> {

            // verify the password and go to the admin board if correct
            if (adminController.verifyAdmin(text.getText())) {
                stage.close();
                primaryStage.setTitle("Admin Board");
                adminController.setup();
                primaryStage.setScene(this.admin);
            } else {

                // show an error message if the password is wrong
                showError("Wrong password");
            }
        });

        VBox layout = new VBox(10);
        layout.getChildren().addAll(label, text, button);
        layout.setAlignment(Pos.CENTER);
        Scene scene = new Scene(layout);
        stage.setScene(scene);

        stage.showAndWait();
    }
}
