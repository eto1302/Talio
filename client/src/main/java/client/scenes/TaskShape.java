package client.scenes;

import client.Services.*;
import client.user.UserData;
import client.utils.ServerUtils;
import commons.*;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.WritableImage;
import javafx.scene.input.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Collections;

public class TaskShape {

    @Inject
    private ServerUtils server;
    @Inject
    private UserData userData;
    @FXML
    private GridPane grid;
    @FXML
    private Label plusSign, title, deleteX, subtaskProgress;
    @FXML
    private HBox tagMarkerContainer;
    @FXML
    private ScrollPane markerScroll;
    private ShowCtrl showCtrl;
    private ObjectProperty<GridPane> drag = new SimpleObjectProperty<>();
    private ListShapeCtrl controller;
    private commons.Task task;
    private boolean selected;
    private String style;
    private TextField text;
    private TaskService taskService;
    private ColorService colorService;
    private BoardService boardService;
    private ListService listService;
    private TagService tagService;
    private Color taskColor;


    @Inject
    public TaskShape(ShowCtrl showCtrl, ServerUtils serverUtils, UserData userData) {
        this.showCtrl = showCtrl;
        this.taskService = new TaskService(userData, serverUtils);
        this.colorService = new ColorService(userData, serverUtils);
        this.boardService = new BoardService(userData, serverUtils);
        this.listService = new ListService(userData, serverUtils);
        this.tagService = new TagService(userData, serverUtils);
    }

    public void setTaskUpdated() {
        task = taskService.getTask(task.getId());
    }

    /**
     * On double-click, this will show the window containing the overview (details of the task)
     */
    public void doubleClick (){
        TaskShape selectedTask = controller.getBoardController().find();
        if (selectedTask==null) {
            selected = true;
            grid.setStyle("-fx-border-color: rgba(0, 0, 0, 1);" +
                "-fx-border-width: 3px;" +
                "-fx-background-color: " + taskColor.getBackgroundColor());
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
                        showCtrl.showEditTask(task, controller, TaskShape.this);
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

    public void setTask(Task task) {
        this.task = task;
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
            grid.setStyle("-fx-border-color: rgba(0, 0, 0, 1);" +
                "-fx-border-width: 3px;"+
                "-fx-background-color: " + taskColor.getBackgroundColor());
        else grid.setStyle(style);
    }

    /**
     * Sets the content of the task accordingly.
     * @param task the task whose information will be displayed
     */
    public void updateScene(Task task){
        this.task = task;
        title.setText(task.getTitle());
        taskColor = this.colorService.getColor(task.getColorId());

        java.util.List<Subtask> subtasks = task.getSubtasks();
        int done = 0;
        for(Subtask subtask: subtasks){
            if(subtask.isChecked()){
                ++done;
            }
        }
        subtaskProgress.setText(done + "/" + subtasks.size());
        Color taskColor = colorService.getColor(task.getColorId());
        if(taskColor == null){
            int defaultColorId = this.boardService.getCurrentBoard().getColors()
                .stream().filter(Color::getIsDefault).findFirst().get().getId();
            this.task.setColorId(defaultColorId);
            List list = this.listService.getList(this.task.getListID());

            this.taskService.editTask(task, list, task.getIndex());
            taskColor = colorService.getColor(defaultColorId);
        }
        title.setTextFill(javafx.scene.paint.Color.web(taskColor.getFontColor()));
        grid.setStyle("-fx-padding: 2px; -fx-border-color: gray; " +
            "-fx-background-color: " + taskColor.getBackgroundColor() +";");
        style = grid.getStyle();
        refreshTagMarkers(task);
        if (task.getDescription()==null || task.getDescription().equals("No description yet")
                || task.getDescription().isEmpty())
            plusSign.setVisible(false);
    }

    //TODO: refactor to service
    public void refreshTagMarkers(Task task){
        java.util.List<Tag> tags = this.tagService.getTagByTask(task.getId());
        tagMarkerContainer.getChildren().remove(0, tagMarkerContainer.getChildren().size());
        if(tags == null || tags.isEmpty()){
            return;
        }
        for (Tag t: tags){
            Scene scene = showCtrl.getTagMarker(t, this);
            tagMarkerContainer.getChildren().add(scene.getRoot());
        }
    }

    /**
     * Adds the delete event to the controller
     */
    public void delete() {
        VBox parent = (VBox) grid.getParent();
        parent.getChildren().remove(grid);
        controller.getTaskControllers().remove(this);
    }

    public void deleteEvent() {
        deleteX.setOnMouseClicked(event -> taskService.deleteTask(task));
    }




    public void deleteOnKey(){
        VBox parent = (VBox) grid.getParent();
        parent.getChildren().remove(grid);
        taskService.deleteTask(task);;
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
        if (task.getDescription()==null || task.getDescription().equals("No description yet"))
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
                    new ArrayList<>(taskService.getTasksOrdered(task.getListID()));

            rearrange(source, parent, children, orderedTasks);
            reorderTasks(orderedTasks, currentlist);

            parent.getChildren().clear();
            parent.getChildren().addAll(children);
            done=true;
        }
        else if (dragboard.hasString() && previousListId!=task.getListID()){
            List previousList = server.getList(previousListId);
            VBox parent = (VBox) grid.getParent();

            parent.getChildren().add(((GridPane) source));
            int newIndex= parent.getChildren().indexOf((GridPane) source);

            taskService.editTask(previousTask, currentlist, newIndex);

            java.util.List<Task> previousListTasks = taskService.getTasksOrdered(previousListId);
            previousListTasks.remove(previousTask);
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
            taskService.editTask(taskIndex, list, i);
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
                new ArrayList<>(taskService.getTasksOrdered(task.getListID()));
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
        task.setTitle(text.getText());
        taskService.editTask(task, controller.getList(), index);

        title.setGraphic(null);
        title.setText(text.getText());
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