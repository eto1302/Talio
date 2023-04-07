package client.scenes;

import client.scenes.tags.*;
import client.user.UserData;
import com.google.inject.Inject;
import commons.*;
import commons.mocks.IShowCtrl;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
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
    private Scene home, addList, yourBoards, search, board, connection,
            addBoard, editTask, errorScene, admin, editBoard, tagOverview,colorPicker,
            addTaskColor, help, taskColorPicker, editColor, unlockBoard, lockBoard;
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
    private TagOverviewController tagOverviewController;
    private ColorPicker colorPickerController;
    private TaskColorPicker taskColorPickerController;
    private AddTaskColor addTaskColorController;
    private Map<Integer, ListShapeCtrl> listControllers;
    private HelpCtrl helpCtrl;
    private EditColor editColorController;
    private UnlockBoardController unlockBoardController;
    private LockBoardController lockBoardController;

    @Inject
    private UserData userData;

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
        help = new Scene((Parent) loader.get(10).getValue());
        helpCtrl = (HelpCtrl) loader.get(10).getKey();
        tagOverview = new Scene((Parent) loader.get(11).getValue());
        tagOverviewController = (TagOverviewController) loader.get(11).getKey();
        colorPicker = new Scene((Parent) loader.get(12).getValue());
        colorPickerController = (ColorPicker) loader.get(12).getKey();
        addTaskColor = new Scene((Parent) loader.get(13).getValue());
        addTaskColorController = (AddTaskColor) loader.get(13).getKey();
        taskColorPicker = new Scene((Parent) loader.get(14).getValue());
        taskColorPickerController = (TaskColorPicker) loader.get(14).getKey();
        editColor = new Scene((Parent) loader.get(15).getValue());
        editColorController = (EditColor) loader.get(15).getKey();
        unlockBoard = new Scene((Parent) loader.get(16).getValue());
        unlockBoardController = (UnlockBoardController) loader.get(16).getKey();
        lockBoard = new Scene((Parent) loader.get(17).getValue());
        lockBoardController = (LockBoardController) loader.get(17).getKey();


        setUpKeys();
        listControllers = new HashMap<>();

        showConnection();
        primaryStage.show();
    }

    private void keyRelease(KeyEvent event) {
        if(event.isShiftDown()){
            if (event.getCode()==KeyCode.SLASH)
                showHelpMenu();
        }
    }

    public void setUpKeys(){
        yourBoards.setOnKeyReleased(this::keyRelease);
        search.setOnKeyReleased(this::keyRelease);
        board.setOnKeyReleased(this::keyRelease);
        admin.setOnKeyReleased(this::keyRelease);
    }


    public void showAddBoard(){
        secondaryStage = new Stage();
        secondaryStage.setScene(addBoard);
        secondaryStage.setTitle("Add a board");
        secondaryStage.show();
    }

    public void showConnection(){
        this.primaryStage.setScene(connection);
        connectionCtrl.setup();
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


    public void cancel() {
        secondaryStage.close();
    }

    public void showSearch() {
        secondaryStage = new Stage();
        secondaryStage.setScene(search);
        secondaryStage.setTitle("Search for a board");
        secondaryStage.show();
    }

    public void showAddSubTask(EditTaskController controller, Task task) {
        popUpStage = new Stage();
        var addSubtaskPair = FXML.load(AddSubTaskController.class,
                "client", "scenes", "AddSubTask.fxml");
        Scene addSubtaskScene = new Scene(addSubtaskPair.getValue());
        addSubtaskPair.getKey().setup(controller, task);

        popUpStage.setScene(addSubtaskScene);
        popUpStage.setTitle("Add a sub-task");
        popUpStage.show();
    }

    public void showBoard(){
        primaryStage.setTitle("Board");
        boardController.setup();
        primaryStage.setScene(this.board);
    }

    /**
     * Updates the window after editing the respective list.
     * @param list the updated list
     */
    public void editList(commons.List list) {
        ListShapeCtrl controller = getListController(list.getId());
        controller.updateScene(list, boardController);
    }

    /**
     * Deletes a list from the window
     * @param list the list to delete
     */
    public void deleteList(commons.List list) {
        ListShapeCtrl controller = getListController(list.getId());
        if(controller != null) {
            boardController.deleteList(controller);
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
        ListShapeCtrl listShapeCtrl = listShape.getKey();

        listShapeCtrl.updateScene(list, boardController);
        boardController.putList(listShape.getValue(), listShapeCtrl);
        return listShapeCtrl;
    }

    /**
     * Adds the taskColor to the ColorPicker and updates the scene
     *
     * @param color the color object whose attributes specify the visual of the shape
     * @return the new TaskColorShape shape controller
     */
    public TaskColorShape addTaskColor(Color color) {
        var taskColorShape = FXML.load(
                TaskColorShape.class, "client", "scenes", "TaskColorShape.fxml");
        Scene initializeTaskColor = new Scene(taskColorShape.getValue());
        TaskColorShape taskColorShapeController = taskColorShape.getKey();

        taskColorShapeController.set(color);
        Scene taskColorScene = taskColorShapeController.getSceneUpdated(color);
        Scene scene = colorPickerController.putColor(taskColorScene);
        secondaryStage.setScene(scene);
        return taskColorShapeController;
    }

    /**
     * Adds the taskColor to the ColorPicker and updates the scene
     *
     * @param color the color object whose attributes specify the visual of the shape
     * @return the new TaskColorShape shape controller
     */
    public SelectTaskColorController addSelectTaskColor(Color color, Task task) {
        var selectTaskColorShape = FXML.load(
                SelectTaskColorController.class, "client", "scenes", "SelectTaskColorShape.fxml");
        Scene initializeTaskColor = new Scene(selectTaskColorShape.getValue());
        SelectTaskColorController selectTaskColorController = selectTaskColorShape.getKey();

        selectTaskColorController.set(color, task);
        Scene taskColorScene = selectTaskColorController.getSceneUpdated(color);
        Scene scene = taskColorPickerController.putColor(taskColorScene);
        secondaryStage.setScene(scene);
        return selectTaskColorController;
    }

    /**
     * Adds a task to the list.
     * @param task the task with the info
     * @param list the list to add the task to
     */
    public void addTask(Task task, commons.List list) {
        var taskShape = FXML.load(TaskShape.class, "client", "scenes", "Task.fxml");
        TaskShape taskShapeCtrl = taskShape.getKey();
        ListShapeCtrl listShapeCtrl = getListController(list.getId());

        taskShapeCtrl.set(task, listShapeCtrl);
        taskShapeCtrl.updateScene(task);
        listShapeCtrl.addTask(taskShape.getValue(), taskShapeCtrl, task);
    }

    public void deleteTask(Task task) {
        ListShapeCtrl listShapeCtrl = getListController(task.getListID());
        if(listShapeCtrl != null)
            listShapeCtrl.removeTask(task.getId());
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
        if(popUpStage != null){
            closePopUp();
        }
        popUpStage = new Stage();
        popUpStage.setScene(errorScene);
        popUpStage.setTitle("error");
        errorController.setErrorMessage(errorMessage);
        popUpStage.show();
        this.cancel();
    }

    public void closePopUp() {
        popUpStage.close();
    }


    public void showEditTask(Task task, ListShapeCtrl listShapeCtrl, TaskShape taskShape) {
        var editTaskPair = FXML.load(EditTaskController.class, "client", "scenes", "EditTask.fxml");
        editTaskController = editTaskPair.getKey();
        editTask = new Scene((Parent) editTaskPair.getValue());
        editTask.setOnKeyReleased(this::keyRelease);

        editTask.setOnKeyPressed(event -> {
            if (event.getCode()==KeyCode.ESCAPE)
                cancel();
        });

        Scene updated = editTaskController.setup(task, listShapeCtrl, taskShape);
        secondaryStage = new Stage();
        secondaryStage.setScene(updated);
        secondaryStage.setTitle("Edit a task");
        secondaryStage.show();
    }

    public void addSubTask(Subtask subtask, EditTaskController editTaskController) {
        var subTaskShapePair = FXML.load(SubTaskShapeCtrl.class,
                "client", "scenes", "SubTaskShape.fxml");
        SubTaskShapeCtrl subTaskShapeCtrl = subTaskShapePair.getKey();
        Scene subTaskScene = new Scene((Parent) subTaskShapePair.getValue());

        subTaskShapeCtrl.setup(subtask);
        Scene updated = subTaskShapeCtrl.getScene(subtask);
        editTaskController.putSubtask(subTaskScene, subtask);
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

    public void showEditTag(Tag tag){
        popUpStage = new Stage();
        var editTagPair = FXML.load(EditTagController.class,
                "client", "scenes", "EditTag.fxml");
        Scene editTagScene = new Scene(editTagPair.getValue());
        EditTagController ctrl = editTagPair.getKey();
        ctrl.setTag(tag);

        popUpStage.setScene(editTagScene);
        popUpStage.setTitle("edit a tag");
        popUpStage.show();
    }

    public void showTagOverview(Board board){
        assert board != null;
        secondaryStage = new Stage();
        secondaryStage.setTitle("Tag overview");
        secondaryStage.setScene(this.tagOverview);
        tagOverviewController.refresh();
        secondaryStage.show();
    }

    public void addTag(Tag tag) {
        tagOverviewController.refresh();
    }

    public void deleteTag(Tag tag) {
        refreshTags(tag);
    }

    public void editTag(Tag tag){
        refreshTags(tag);
    }

    private void refreshTags(Tag tag){
        tagOverviewController.refresh();
        if(editTaskController != null){
            editTaskController.refresh();
        }
        refreshAllTasks();
        //TODO: add or empty check when changed to list
//        if(tag.getTask() != null){
//            //TODO: change to list impl.
//            TaskShape ts = boardController.findTaskController(tag.getTask());
//            ts.updateScene(tag.getTask());
//        }
    }

    public void removeTagFromTask(Tag tag, Task task){
        TaskShape ts = boardController.findTaskController(task);
        ts.refreshTagMarkers(task);
        editTaskController.refresh();
    }

    private void refreshAllTasks(){
        //TODO: change to better impl, only way to do this for now
        // since there is no way to get a task from a tag
        boardController.getListControllers().stream()
                .flatMap(l -> l.getTaskControllers().stream())
                .forEach(t -> t.refreshTagMarkers(t.getTask()));
    }

    public void showAddTagToTask(Task task){
        if(popUpStage != null){
            closePopUp();
        }
        popUpStage = new Stage();
        var tagToTaskPair = FXML.load(AddTagToTaskController.class,
            "client", "scenes", "AddTagToTask.fxml");
        AddTagToTaskController controller = tagToTaskPair.getKey();
        controller.setTask(task);
        Scene addTagToTask = new Scene((Parent) tagToTaskPair.getValue());
        controller.refresh();

        popUpStage.setScene(addTagToTask);
        popUpStage.setTitle("Add tag to task");
        popUpStage.show();
    }

    @Override
    public void addTagToTask(Tag tag, Task task){
        //TODO: change to use new impl
        TaskShape taskController = boardController.findTaskController(task);
        taskController.refreshTagMarkers(task);
        if(editTaskController != null) {editTaskController.refresh();}
    }

    public void putTagSceneOverview(Tag tag, TagOverviewController cntr){
        var tagPair = FXML.load(TagShapeController.class,
                "client", "scenes", "TagShape.fxml");
//        Scene initializeTagShape = new Scene(tagPair.getValue());
        TagShapeController tagShapeController = tagPair.getKey();
        tagShapeController.updateScene(tag);

        cntr.putTag(tagPair.getValue());
    }

    public void putTagSceneEditTask(Tag tag){
        var tagPair = FXML.load(TagShapeController.class, "client", "scenes", "TagShape.fxml");
//        Scene initializeTagShape = new Scene(tagPair.getValue());
        TagShapeController tagShapeController = tagPair.getKey();
        tagShapeController.updateScene(tag);
        tagShapeController.setTask(editTaskController.getTask());

        editTaskController.putTag(tagPair.getValue());
    }

    public void putTagSceneAddToTask(Tag tag, AddTagToTaskController cntrl){
        var tagPair = FXML.load(TagShapeController.class, "client", "scenes", "TagShape.fxml");
//        Scene initializeTagShape = new Scene(tagPair.getValue());
        TagShapeController tagShapeController = tagPair.getKey();
        tagShapeController.updateScene(tag);
        tagShapeController.setTask(cntrl.getTask());
        cntrl.putTag(tagPair.getValue());
    }

//    public Scene getTagScene(Tag tag){
//        var tagPair = FXML.load(TagShapeController.class, "client", "scenes", "TagShape.fxml");
////        Scene initializeTagShape = new Scene(tagPair.getValue());
//        TagShapeController tagShapeController = tagPair.getKey();
//        tagShapeController.getSceneUpdated(tag);
//        return tagPair.getValue();
//    }
//
//
//    public Scene getTagSceneTask(Tag tag, EditTaskController c){
//        var tagPair = FXML.load(TagShapeController.class, "client", "scenes", "TagShape.fxml");
////        Scene initializeTagShape = new Scene(tagPair.getValue());
//        TagShapeController tagShapeController = tagPair.getKey();
//        tagShapeController.setTaskController(c);
//        Scene tagScene = tagShapeController.getSceneUpdated(tag);
//        return tagScene;
//    }

    public Scene getTagMarker(Tag tag, TaskShape taskController) {
        var markerPair = FXML.load(TagMarkerShapeController.class,
            "client", "scenes", "TagMarkerShape.fxml");
        Scene initializeTagMarker = new Scene(markerPair.getValue());
        TagMarkerShapeController controller = markerPair.getKey();
        Scene markerScene = controller.getSceneUpdated(tag);
        return markerScene;
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

    public void showEditBoard() {
        secondaryStage = new Stage();
        secondaryStage.setTitle("Edit Board");
        secondaryStage.setScene(this.editBoard);
        editBoardController.setup();
        secondaryStage.show();
    }

    public void showUnlockBoard() {
        String password = userData.getCurrentBoard().getPassword();
        unlockBoardController.setCurrentPassword(password == null ? "" : password);
        secondaryStage = new Stage();
        secondaryStage.setTitle("Unlock Board");
        secondaryStage.setScene(this.unlockBoard);
        secondaryStage.setResizable(false);
        secondaryStage.show();
    }

    public void showLockBoard(Board adminModeBoard) {
        lockBoardController.reset(adminModeBoard);
        secondaryStage = new Stage();
        secondaryStage.setTitle("Lock Board");
        secondaryStage.setScene(this.lockBoard);
        secondaryStage.setResizable(false);
        secondaryStage.show();
    }

    public void updateBoardLockIcon(boolean locked) {
        boardController.updateLockIcon(locked);
    }

    public void refreshBoardCtrl() {
        boardController.refresh();
    }

    public void deleteTaskColor(Color color) {

    }

    @Override
    public void editColor(Color color) {
        this.boardController.refresh();
    }

    public void showColorPicker() {
        secondaryStage = new Stage();
        secondaryStage.setTitle("Color Picker");
        secondaryStage.setScene(this.colorPicker);
        this.colorPickerController.setup();
        secondaryStage.show();
    }

    public boolean isColorPickerOpen() {
        return secondaryStage.isShowing() && secondaryStage.getScene() == colorPicker;
    }

    public void showTaskColorPicker(Task task) {
        secondaryStage = new Stage();
        secondaryStage.setTitle("Task Color Picker");
        secondaryStage.setScene(this.taskColorPicker);
        this.taskColorPickerController.setup(task);
        secondaryStage.show();
    }

    public void showAddTaskColor() {
        secondaryStage = new Stage();
        secondaryStage.setTitle("AddTaskColor");
        secondaryStage.setScene(this.addTaskColor);
        secondaryStage.show();
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

    private ListShapeCtrl getListController(int listId) {
        ListShapeCtrl controller = boardController.getListControllers().stream()
                .filter(e -> e.getList().getId() == listId).findFirst().orElse(null);
        return controller;
    }

    public void showEditColor(Color color) {
        secondaryStage = new Stage();
        secondaryStage.setTitle("Edit Color");
        secondaryStage.setScene(this.editColor);
        this.editColorController.setup(color);
        secondaryStage.show();
    }

    public Scene getEditTask(){
        return editTask;
    }

    public Stage getSecondaryStage(){
        return secondaryStage;
    }

}
