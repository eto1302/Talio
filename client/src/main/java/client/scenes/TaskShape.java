package client.scenes;

import client.utils.ServerUtils;
import commons.Task;
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
    private Stage primaryStage;

    @Inject
    public TaskShape(ShowCtrl showCtrl, ServerUtils serverUtils){
        this.showCtrl=showCtrl;
        server=serverUtils;
    }

    /**
     * On double-click, this will show the window containing the overview (details of the task)
     */
    public void doubleClick (){
        grid.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.getButton().equals(MouseButton.PRIMARY))
                    if (event.getClickCount()==2)
                        showCtrl.showEditTask(task, controller);
            }
        });
    }

    /**
     * Sets the content of the task accordingly.
     * @param task the task whose information will be displayed
     * @return the new scene updated
     */
    public Scene getSceneUpdated(Task task){
        this.task = task;
        title.setText(task.getTitle());
        if (task.getDescription()==null)
            plusSign.setVisible(false);
        return grid.getScene();
    }

    /**
     * deletes the task
     */
    public void delete(){
        deleteX.setOnMouseClicked(event -> {
            VBox parent = (VBox) grid.getParent();
            parent.getChildren().remove(grid);
        });

        server.removeTask(task.getId(), task.getListID());
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
        if (task.getDescription().equals("No description yet"))
            plusSign.setVisible(false);
        grid.setOnDragDetected(this::dragDetected);
        grid.setOnDragOver(this::dragOver);
        grid.setOnDragDropped(this::dragDrop);
        grid.setOnMousePressed(event->{
            grid.setOpacity(0.4);
        });
        grid.setOnMouseReleased(event->{
            grid.setOpacity(1);
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
        clipboardContent.putString(task.getId()+"+"+ task.getListID());

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
     * @param event the rag event
     */
    private void dragDrop(DragEvent event){
        Dragboard dragboard = event.getDragboard();
        boolean done = false;
        Object source = event.getGestureSource();

        if (dragboard.hasString()){
            VBox parent = (VBox) grid.getParent();

            int sourceIndex = parent.getChildren().indexOf(source);
            int targetIndex = parent.getChildren().indexOf(grid);
            ArrayList<Node> children = new ArrayList<>(parent.getChildren());

            if (sourceIndex<targetIndex) {
                Collections.rotate(children.subList(sourceIndex, targetIndex + 1), -1);
            }
            else {
                Collections.rotate(children.subList(targetIndex, sourceIndex+1), 1);
            }

            parent.getChildren().clear();
            parent.getChildren().addAll(children);
            done = true;
        }
        ((GridPane) source).setOpacity(1);

        event.setDropCompleted(done);
        event.consume();
    }




}