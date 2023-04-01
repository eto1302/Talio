package client.scenes;

import client.scenes.subtasks.SubTaskShapeCtrl;
import client.scenes.boards.AddBoardController;
import client.scenes.boards.BoardController;
import client.scenes.boards.BoardShape;
import client.scenes.boards.EditBoardController;
import client.scenes.lists.AddListController;
import client.scenes.lists.EditListController;
import client.scenes.lists.ListShapeCtrl;
import client.scenes.subtasks.AddSubTaskController;
import client.scenes.subtasks.EditSubTaskController;
import client.scenes.tags.AddTagController;
import client.scenes.tags.AddTagToTaskController;
import client.scenes.tags.TagOverviewController;
import client.scenes.tags.TagShapeController;
import client.scenes.tasks.AddTaskController;
import client.scenes.tasks.EditTaskController;
import client.scenes.tasks.TaskShape;
import client.user.UserData;
import commons.Board;
import commons.Subtask;
import commons.Tag;
import commons.Task;
import commons.mocks.IShowCtrl;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Pair;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static client.utils.Constants.FXML;

public class ShowCtrl implements IShowCtrl {

    private Stage primaryStage, secondaryStage, popUpStage;

    private HomeController homeCtrl;
    private Scene home, addList, yourBoards, search, board, taskOverview, connection,
            addBoard, editTask, errorScene, admin, editBoard, tagOverview, help;
    private AddListController addListCtrl;
    private YourBoardsController yourBoardsCtrl;
    private SearchCtrl searchCtrl;
    private BoardController boardController;
    private ConnectionCtrl connectionCtrl;
    private AddBoardController addBoardController;
    private EditTaskController editTaskController;
    private ErrorController errorController;
    private AdminController adminController;
    private EditBoardController editBoardController;
    private HelpCtrl helpCtrl;

    private TagOverviewController tagOverviewController;
    private Map<Integer, ListShapeCtrl> listControllers;

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
        boardController = (BoardController) loader.get(4).getKey();
        board = new Scene((Parent) loader.get(4).getValue());
        connectionCtrl=(ConnectionCtrl) loader.get(5).getKey();
        connection = new Scene((Parent)loader.get(5).getValue());
        addBoardController = (AddBoardController) loader.get(6).getKey();
        addBoard = new Scene((Parent) loader.get(6).getValue());
        errorController = (ErrorController) loader.get(7).getKey();
        errorScene = new Scene((Parent) loader.get(7).getValue());
        admin = new Scene((Parent) loader.get(8).getValue());
        adminController = (AdminController) loader.get(8).getKey();
        editBoard = new Scene((Parent) loader.get(9).getValue());
        editBoardController = (EditBoardController) loader.get(9).getKey();
        tagOverview = new Scene((Parent) loader.get(10).getValue());
        tagOverviewController = (TagOverviewController) loader.get(10).getKey();
        help = new Scene((Parent) loader.get(11).getValue());
        helpCtrl = (HelpCtrl) loader.get(11).getKey();

        listControllers = new HashMap<>();

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
        addTask.getKey().setup(controller, list);
        secondaryStage = new Stage();
        secondaryStage.setScene(addTaskScene);
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
        popUpStage = new Stage();
        var addTagPair = FXML.load(AddTagController.class,
                "client", "scenes", "AddTag.fxml");
        Scene addTagScene = new Scene(addTagPair.getValue());
        popUpStage.setScene(addTagScene);
        popUpStage.setTitle("Add a tag");
        popUpStage.show();
    }

    public void showAddSubTask(Task task) {
        popUpStage = new Stage();
        var addSubtaskPair = FXML.load(AddSubTaskController.class,
                "client", "scenes", "AddSubTask.fxml");
        Scene addSubtaskScene = new Scene(addSubtaskPair.getValue());
        addSubtaskPair.getKey().setup(task);

        popUpStage.setScene(addSubtaskScene);
        popUpStage.setTitle("Add a sub-task");
        popUpStage.show();
    }

    public void showEditSubTask(Subtask subtask) {
        popUpStage = new Stage();
        var editSubtaskPair = FXML.load(EditSubTaskController.class,
                "client", "scenes", "AddSubTask.fxml");
        Scene editSubtaskScene = new Scene(editSubtaskPair.getValue());
        editSubtaskPair.getKey().setup(subtask);

        popUpStage.setScene(editSubtaskScene);
        popUpStage.setTitle("Edit a sub-task");
        popUpStage.show();
    }

    public void showBoard(){
        primaryStage.setTitle("Board");
        boardController.setup(primaryStage);
        primaryStage.setScene(this.board);
    }

    /**
     * Shows the details of the task. First sets the information in the window according to
     * the task.
     */

    /**
     * Shows the window with options for the editing the list.
     * First sets up the scene to the list's information
     * @param list the list that contains the info
     */
    public void showEditList(commons.List list, Stage primaryStage){
        var editList = FXML.load(EditListController.class,
                "client", "scenes", "EditList.fxml");
        editList.getKey().setup(list, primaryStage);
        secondaryStage=new Stage();
        secondaryStage.setScene(new Scene(editList.getValue()));
        secondaryStage.setTitle("Edit your list");
        secondaryStage.show();
    }

    /**
     * Updates the window after editing the respective list.
     * @param list the updated list
     */
    public void editList(commons.List list) {
        ListShapeCtrl controller = listControllers.get(list.getId());
        Scene updated = controller.getSceneUpdated(list);
        primaryStage.setScene(updated);
    }

    /**
     * Deletes a list from the window
     * @param list the list to delete
     */
    public void deleteList(commons.List list) {
        ListShapeCtrl controller = listControllers.get(list.getId());
        if(controller != null) {
            listControllers.remove(list.getId());
            controller.deleteList();
        }
    }

    /**
     * Adds the list to the board and updates the scene
     *
     * @param list the list object whose attributes specify the visual of the list
     * @return the new list shape controller
     */
    public ListShapeCtrl addList(commons.List list) {
        var listShape = FXML.load(ListShapeCtrl.class, "client", "scenes", "List.fxml");
        Scene initializeList = new Scene(listShape.getValue());
        ListShapeCtrl listShapeCtrl = listShape.getKey();

        listShapeCtrl.set(list, primaryStage);
        Scene listScene = listShapeCtrl.getSceneUpdated(list);
        Scene scene = boardController.putList(listScene);
        primaryStage.setScene(scene);
        listControllers.put(list.getId(), listShapeCtrl);
        return listShapeCtrl;
    }

    /**
     * Adds a task to the list.
     * @param task the task with the info
     * @param list the list to add the task to
     */
    public void addTask(Task task, commons.List list) {
        var taskShape = FXML.load(TaskShape.class, "client", "scenes", "Task.fxml");
        Scene taskScene = new Scene(taskShape.getValue());
        TaskShape taskShapeCtrl = taskShape.getKey();
        ListShapeCtrl listShapeCtrl = listControllers.get(list.getId());

        taskShapeCtrl.set(task, primaryStage, listShapeCtrl);
        Scene updated = taskShapeCtrl.getSceneUpdated(task);
        Scene scene = listShapeCtrl.addTask(updated);
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
        popUpStage = new Stage();
        popUpStage.setScene(errorScene);
        popUpStage.setTitle("error");
        errorController.setErrorMessage(errorMessage);
        popUpStage.show();
    }

    public void closePopUp() {
        popUpStage.close();
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
        var subTaskShapePair = FXML.load(SubTaskShapeCtrl.class,
            "client", "scenes", "SubTaskShape.fxml");
        SubTaskShapeCtrl subTaskShapeCtrl = subTaskShapePair.getKey();
//        subTaskShapeCtrl.setup(subtask, editTaskController);
        Scene subTaskScene = subTaskShapeCtrl.getScene(subtask);
        editTaskController.putSubtask(subTaskScene);
    }

    public void addTag(Tag tag) {
        tagOverviewController.putTag(getTagScene(tag));
    }

    public void showAddTagToTask(EditTaskController c){
        popUpStage = new Stage();
        var tagToTaskPair = FXML.load(AddTagToTaskController.class, "client", "scenes", "AddTagToTask.fxml");
        AddTagToTaskController controller = tagToTaskPair.getKey();
        controller.setController(c);
        Scene addTagToTask = new Scene((Parent) tagToTaskPair.getValue());
        controller.setup();

        popUpStage.setScene(addTagToTask);
        popUpStage.setTitle("Add tag to task");
        popUpStage.show();
    }

    public void addTagToTask(Tag tag, EditTaskController controller){
        //add the tag to the task serverside
        //message update that applies the new tagmarkers
        controller.putTag(getTagScene(tag));
    }

    public Scene getTagScene(Tag tag){
        var tagPair = FXML.load(TagShapeController.class, "client", "scenes", "TagShape.fxml");
        Scene initializeTagShape = new Scene(tagPair.getValue());
        TagShapeController tagShapeController = tagPair.getKey();
        Scene tagScene = tagShapeController.getSceneUpdated(tag);
        return tagScene;
    }

    public Scene getTagSceneTask(Tag tag, EditTaskController c){
        var tagPair = FXML.load(TagShapeController.class, "client", "scenes", "TagShape.fxml");
        Scene initializeTagShape = new Scene(tagPair.getValue());
        TagShapeController tagShapeController = tagPair.getKey();
        tagShapeController.setTaskController(c);
        Scene tagScene = tagShapeController.getSceneUpdated(tag);
        return tagScene;
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
            stage.close();

            // verify the password and go to the admin board if correct
            if (adminController.verifyAdmin(text.getText())) {
                primaryStage.setTitle("Admin Board");
                adminController.setup();
                primaryStage.setScene(this.admin);
            } else {

                // show an error message if the password is wrong
                showError("Wrong password");
            }
        });
        text.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER)
                button.fire();
        });

        VBox layout = new VBox(10);
        layout.getChildren().addAll(label, text, button);
        layout.setAlignment(Pos.CENTER);
        Scene scene = new Scene(layout);
        stage.setScene(scene);

        stage.showAndWait();
    }

    public void refreshAdminBoards() {
        adminController.setup();
    }

    public void showEditBoard(BoardController controller) {
        secondaryStage = new Stage();
        secondaryStage.setTitle("Edit Board");
        secondaryStage.setScene(this.editBoard);
        editBoardController.setup(controller);
        secondaryStage.show();
    }
    public void refreshBoardCtrl() {
        boardController.refresh();
    }

    public void showTagOverview(Board board){
        assert board != null;
        secondaryStage = new Stage();
        secondaryStage.setTitle("Tag overview");
        secondaryStage.setScene(this.tagOverview);
        tagOverviewController.setup(board);
        secondaryStage.show();
    }


    public void refreshList(int listID) {
        ListShapeCtrl ctrl = listControllers.get(listID);
        if(ctrl != null)
            ctrl.refreshList();
    }

    public void showHelpMenu(){
        popUpStage=new Stage();
        help.setOnKeyReleased(event->{
            if (event.getCode()==KeyCode.Q)
                closePopUp();
        });
        popUpStage.setScene(help);
        popUpStage.setTitle("Help menu - Shortcuts");
        popUpStage.setResizable(false);

        popUpStage.show();
    }

}
