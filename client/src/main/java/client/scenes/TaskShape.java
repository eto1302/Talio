package client.scenes;

import client.user.UserData;
import client.utils.ServerUtils;
import commons.Color;
import commons.List;
import commons.Task;
import commons.models.TaskEditModel;
import commons.sync.TaskDeleted;
import commons.sync.TaskEdited;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.WritableImage;
import javafx.scene.input.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Collections;

public class TaskShape {
    @FXML
    private GridPane grid;
    @FXML
    private Label plusSign, title, deleteX;
    private ShowCtrl showCtrl;
    private ServerUtils server;
    private ObjectProperty<GridPane> drag = new SimpleObjectProperty<>();
    private ListShapeCtrl controller;
    private commons.Task task;
    private UserData userData;
    private boolean selected;
    private String style;
    private TextField text;

    @Inject
    public TaskShape(ShowCtrl showCtrl, ServerUtils serverUtils, UserData userData) {
        this.showCtrl = showCtrl;
        this.server = serverUtils;
        this.userData = userData;
    }

    public void setTaskUpdated() {
        task=server.getTask(task.getId());
    }

    /**
     * On double-click, this will show the window containing the overview (details of the task)
     */
    public void doubleClick (){
        TaskShape selectedTask = controller.getBoardController().find();
        if (selectedTask==null) {
            selected = true;
            grid.setStyle("-fx-border-color: rgba(14,27,111,1);" +
                    "-fx-border-width: 3px");
        }
        else{
            selected=false;
        }
        grid.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.getButton().equals(MouseButton.PRIMARY))
                    if (event.getClickCount()==2) {
                        setTaskUpdated();
                        showCtrl.showEditTask(task, controller);
                    }
            }
        });
    }

    public boolean isSelected() {
        return selected;
    }


    public Task getTask() {
        return task;
    }

    public ListShapeCtrl getController() {
        return controller;
    }

    public void setController(ListShapeCtrl controller) {
        this.controller = controller;
    }

    public void setStatus(boolean selected){
        this.selected=selected;
        if (selected)
            grid.setStyle("-fx-border-color: rgba(14,27,111,1);" +
                    "-fx-border-width: 3px");
        else grid.setStyle(style);
    }

    /**
     * Sets the content of the task accordingly.
     * @param task the task whose information will be displayed
     */
    public void updateScene(Task task){
        this.task = task;
        title.setText(task.getTitle());
        Color taskColor = this.server.getColor(task.getColorId());
        if(taskColor == null){
            int defaultColorId = this.userData.getCurrentBoard().getColors()
                    .stream().filter(Color::getIsDefault).findFirst().get().getId();
            this.task.setColorId(defaultColorId);

            List list = this.server.getList(this.task.getListID());
            TaskEditModel edit = new TaskEditModel(task.getTitle(), task.getDescription(),
                    task.getIndex(), list, task.getColorId());

            userData.updateBoard(new TaskEdited(list.getBoardId(), list.getId(),
                    task.getId(), edit));
            taskColor = server.getColor(defaultColorId);
        }
        title.setTextFill(javafx.scene.paint.Color.web(taskColor.getFontColor()));
        grid.setStyle("-fx-padding: 2px; -fx-border-color: gray; " +
                "-fx-background-color: " + taskColor.getBackgroundColor() +";");
        if (task.getDescription()==null || task.getDescription().equals("No description yet"))
            plusSign.setVisible(false);
    }

    /**
     * Adds the delete event to the controller
     */
    public void delete() {
        VBox parent = (VBox) grid.getParent();
        parent.getChildren().remove(grid);
        server.removeTask(task.getId(), task.getListID());
        controller.getTaskControllers().remove(this);
    }

    public void deleteEvent() {
        deleteX.setOnMouseClicked(event -> userData.updateBoard(new TaskDeleted(userData
                .getCurrentBoard().getId(), task.getId(), task.getListID())));
    }




    public void deleteOnKey(){
        VBox parent = (VBox) grid.getParent();
        parent.getChildren().remove(grid);
        userData.updateBoard(new TaskDeleted(userData
                .getCurrentBoard().getId(), task.getId(), task.getListID()));
        controller.getTaskControllers().remove(this);
    }
    /**
     * Sets the information of the list and task. Sets the methods for the dragging and dropping
     * for the ordering tasks feature
//     * @param id the id of the task
//     * @param list the task's list
     */
    public void set(Task task, ListShapeCtrl listShapeCtrl){
        this.task = task;
        this.controller = listShapeCtrl;
        if (task.getDescription().equals("No description yet"))
            plusSign.setVisible(false);
        this.style=grid.getStyle();

        grid.setOnDragDetected(this::dragDetected);
        grid.setOnDragOver(this::dragOver);
        grid.setOnDragDropped(this::dragDrop);
        grid.setOnDragDone(this::dragDone);

        grid.setOnMousePressed(event-> grid.setOpacity(0.4));
        grid.setOnMouseReleased(event-> grid.setOpacity(1));
        grid.setOnMouseExited(event-> {
            controller.getBoardController().reset();
            selected=false;
            grid.setStyle(style);
        });
    }

    /**
     * When dragging is detected, sets the drag board (a specific clipboard) to contain a string
     * to be recognized later. Makes a snapshot image of the task to be visible when dragging.
     * @param event the mouse event initiating the drag
     */
    private void dragDetected(MouseEvent event){
        Dragboard dragboard = grid.startDragAndDrop(TransferMode.MOVE);
        ClipboardContent clipboardContent = new ClipboardContent();
        SnapshotParameters snapshotParams = new SnapshotParameters();
        WritableImage image = grid.snapshot(snapshotParams, null);
        Task task1 =server.getTask(task.getId());

        clipboardContent.putString(task.getId()+"+"+ task1.getListID());

        drag.set(grid);
        dragboard.setDragView(image, event.getX(), event.getY());
        dragboard.setContent(clipboardContent);
        event.consume();
    }

    /**
     * Gets the drag board of the source and makes the event possible if it has the special
     * string
     * @param event the drag event
     */
    private void dragOver(DragEvent event){
        Dragboard dragboard = event.getDragboard();
        if (dragboard.hasString()){
            event.acceptTransferModes(TransferMode.MOVE);
            event.consume();
        }
    }

    /**
     * Method for dropping the task in the box; gets the children of the box (the other tasks) and
     * rotates (shifts) all the tasks that sit in-between the source and the target (inclusive).
     * Then puts all the children back in the box.
     * Also changes the index of all tasks after this update
     * @param event the rag event
     */
    private void dragDrop(DragEvent event){
        Dragboard dragboard = event.getDragboard();
        Object source = event.getGestureSource();
        boolean done=false;
        String identify = dragboard.getString();
        int taskId = Integer.parseInt(identify.split("\\+")[0].trim());
        int previousListId = Integer.parseInt(identify.split("\\+")[1].trim());
        task =server.getTask(task.getId());
        Task previousTask = server.getTask(taskId);
        List currentlist = server.getList(task.getListID());
        if (dragboard.hasString() && previousListId==task.getListID()){
            VBox parent = (VBox) grid.getParent();
            ArrayList<Node> children = new ArrayList<>(parent.getChildren());
            ArrayList<Task> orderedTasks=
                    (ArrayList<Task>) server.getTasksOrdered(task.getListID());

            rearrange(source, parent, children, orderedTasks);
            reorderTasks(orderedTasks, currentlist);

            parent.getChildren().clear();
            parent.getChildren().addAll(children);
            done=true;
        }
        else if (dragboard.hasString() && previousListId!=task.getListID()){
            List previousList = server.getList(previousListId);
            previousList.getTasks().remove(previousTask);
            VBox parent = (VBox) grid.getParent();

            parent.getChildren().add(((GridPane) source));
            int newIndex= parent.getChildren().indexOf((GridPane) source);

            TaskEditModel model = new TaskEditModel(previousTask.getTitle(),
                    previousTask.getDescription(), newIndex, currentlist,
                    previousTask.getColorId());
            server.editTask(taskId, model);

            currentlist.getTasks().add(previousTask);
            java.util.List<Task> previousListTasks = server.getTasksOrdered(previousListId);
            reorderTasks(previousListTasks, previousList);
            done=true;
        }
        if (done)
            controller.getBoardController().refresh();
        ((GridPane) source).setOpacity(1);

        event.setDropCompleted(done);
        event.consume();
    }

    /**
     * Makes the task's opacity back to normal even if the drag was unsuccessful
     * @param event the drag event
     */
    private void dragDone(DragEvent event){
        Object source = event.getGestureSource();
        ((GridPane) source).setOpacity(1);
        event.consume();
    }

    /**
     * Re-assigns the tasks' indexes after the drag event update
     * @param tasksToReorder the tasks to be reordered
     * @param list the list in which this happens
     */
    private void reorderTasks(java.util.List<Task> tasksToReorder, List list){
        for (int i=0; i<tasksToReorder.size(); i++){
            Task taskIndex = tasksToReorder.get(i);
            TaskEditModel model = new TaskEditModel(taskIndex.getTitle(),
                    taskIndex.getDescription(), i, list, taskIndex.getColorId());
            server.editTask(taskIndex.getId(), model);
        }
    }

    /**
     * Rearranges the tasks and the inside the current list when we perform drag and drop
     * between tasks
     * @param source the source task that is getting dragged
     * @param parent the parent of the current task that is getting dropped onto
     * @param children all the tasks in the current list, represented as grids
     * @param orderedTasks the ordered tasks of the current, which need to be reordered in
     *                     the database
     */
    private void rearrange(Object source, VBox parent, ArrayList<Node> children,
                           ArrayList<Task> orderedTasks){
        int sourceIndex = parent.getChildren().indexOf(source);
        int targetIndex = parent.getChildren().indexOf(grid);

        if (sourceIndex<targetIndex) {
            Collections.rotate(children.subList(sourceIndex, targetIndex + 1), -1);
            Collections.rotate(orderedTasks.subList(sourceIndex, targetIndex+1),-1);
        }
        else {
            Collections.rotate(children.subList(targetIndex, sourceIndex+1), 1);
            Collections.rotate(orderedTasks.subList(targetIndex, sourceIndex+1), 1);
        }
    }

    public void orderWithKeyEvent(int index, String direction){
        VBox parent = (VBox) grid.getParent();
        ArrayList<Node> children = new ArrayList<>(parent.getChildren());
        ArrayList<Task> orderedTasks =
                (ArrayList<Task>) server.getTasksOrdered(task.getListID());
        var controllers = controller.getTaskControllers();
        List list = server.getList(task.getListID());

        if (direction.equals("up") && index!=0){
            Collections.rotate(children.subList(index-1, index+1), 1);
            Collections.rotate(controllers.subList(index-1, index+1), 1);
            Collections.rotate(orderedTasks.subList(index-1, index+1), 1);
        }
        if (direction.equals("down") && index!=children.size()-1){
            Collections.rotate(children.subList(index, index+2), 1);
            Collections.rotate(controllers.subList(index, index+2), 1);
            Collections.rotate(orderedTasks.subList(index, index+2), 1);
        }
        reorderTasks(orderedTasks, list);
        parent.getChildren().clear();
        parent.getChildren().addAll(children);
    }


    public void editOnKey() {
        int index = ((VBox) grid.getParent()).getChildren().indexOf(grid);
        TaskEditModel model = new TaskEditModel(text.getText(), task.getDescription(),
                index, controller.getList(), task.getColorId());
        task.setTitle(model.getTitle());
        server.editTask(task.getId(), model);

        title.setGraphic(null);
        title.setText(model.getTitle());
    }

    public void makeEditable() {
        text = new TextField();
        text.setPrefWidth(80);

        text.setText(task.getTitle());
        title.setGraphic(text);
        title.setText("");

        text.end();
        text.requestFocus();
    }
}