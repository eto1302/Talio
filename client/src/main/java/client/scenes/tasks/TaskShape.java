package client.scenes.tasks;

import client.scenes.ShowCtrl;
import client.scenes.lists.ListShapeCtrl;
import client.scenes.tags.TagMarkerShapeController;
import client.scenes.tags.TagOverviewController;
import client.utils.ServerUtils;
import commons.List;
import commons.Tag;
import commons.Task;
import commons.models.TaskEditModel;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.image.WritableImage;
import javafx.scene.input.*;
import javafx.scene.layout.*;

import javax.inject.Inject;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

public class TaskShape {
    @FXML
    private GridPane grid;
    @FXML
    private Label plusSign, title, deleteX;
    @FXML
    private HBox tagMarkerContainer;
    private ShowCtrl showCtrl;
    private ServerUtils server;
    private ObjectProperty<GridPane> drag = new SimpleObjectProperty<>();
    private ListShapeCtrl controller;
    private commons.Task task;
    private Stage primaryStage;

    @Inject
    public TaskShape(ShowCtrl showCtrl, ServerUtils serverUtils){
        this.showCtrl=showCtrl;
        server=serverUtils;
    }

    public void setTaskUpdated(){
        task=server.getTask(task.getId());
    }

    /**
     * On double-click, this will show the window containing the overview (details of the task)
     */
    public void doubleClick (){
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
//        grid.setOnMouseEntered(new EventHandler<MouseEvent>() {
//            @Override
//            public void handle(MouseEvent event) {
//                if (event.getButton().equals(MouseButton.PRIMARY))
//                    if (event.getClickCount()==2) {
//                        setTaskUpdated();
//                        showCtrl.showEditTask(task, controller);
//                    }
//            }
//        });
    }

    /**
     * Sets the content of the task accordingly.
     * @param task the task whose information will be displayed
     * @return the new scene updated
     */
    public Scene getSceneUpdated(Task task){
        addTagMarkers(task);
        this.task = task;
        title.setText(task.getTitle());
        if (task.getDescription()==null || task.getDescription().equals("No description yet"))
            plusSign.setVisible(false);
        return grid.getScene();
    }

    private void addTagMarkers(Task task){
        if(task.getTags() == null || task.getTags().isEmpty()){
            return;
        }
        TagMarkerShapeController markerController = new TagMarkerShapeController();
        for(Tag t: task.getTags()){
            Node root = markerController.getSceneUpdated(t).getRoot();
            tagMarkerContainer.getChildren().add(root);
        }
    }

    /**
     * deletes the task
     */
    public void delete(){
        deleteX.setOnMouseClicked(event -> {
            VBox parent = (VBox) grid.getParent();
            parent.getChildren().remove(grid);
            server.removeTask(task.getId(), task.getListID());
        });

    }

    /**
     * Sets the information of the list and task. Sets the methods for the dragging and dropping
     * for the ordering tasks feature
//     * @param id the id of the task
//     * @param list the task's list
     */
    public void set(Task task, Stage primaryStage, ListShapeCtrl listShapeCtrl){
        this.task = task;
        this.primaryStage = primaryStage;
        this.controller = listShapeCtrl;
        addTagMarkers(task);
        if (task.getDescription().equals("No description yet"))
            plusSign.setVisible(false);

        grid.setOnDragDetected(this::dragDetected);
        grid.setOnDragOver(this::dragOver);
        grid.setOnDragDropped(this::dragDrop);
        grid.setOnDragDone(this::dragDone);
        grid.setOnMousePressed(event-> grid.setOpacity(0.4));
        grid.setOnMouseReleased(event-> grid.setOpacity(1));
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
        if (dragboard.hasString())
            clipboardContent.putString(task.getId()+"+"+ task1.getListID());
        else clipboardContent.putString(task.getId()+"+"+ task1.getListID());

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
                    previousTask.getDescription(), newIndex, currentlist);
            server.editTask(taskId, model);

            currentlist.getTasks().add(previousTask);
            java.util.List<Task> previousListTasks = server.getTasksOrdered(previousListId);
            reorderTasks(previousListTasks, previousList);
            done=true;
        }
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
                    taskIndex.getDescription(), i, list);
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
}