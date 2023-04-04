package client.scenes;

import client.scenes.ShowCtrl;
import client.utils.ServerUtils;
import commons.Subtask;
import commons.models.IdResponseModel;
import commons.models.SubtaskEditModel;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.WritableImage;
import javafx.scene.input.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import javax.inject.Inject;

import java.util.ArrayList;
import java.util.Collections;

public class SubTaskShapeCtrl {
    @FXML
    private GridPane grid;
    @FXML
    private CheckBox checkbox;
    @FXML
    private Label description;
    private ShowCtrl showCtrl;
    private ServerUtils serverUtils;
    private Subtask subtask;
    private TextField text;
    private ObjectProperty<GridPane> drag = new SimpleObjectProperty<>();

    @Inject
    public SubTaskShapeCtrl(ShowCtrl showCtrl, ServerUtils serverUtils){
        this.showCtrl = showCtrl;
        this.serverUtils = serverUtils;
    }

    public void setup(Subtask subtask){
        this.subtask = subtask;
        description.setText(subtask.getDescription());
        checkbox.setSelected(subtask.isChecked());

        grid.setOnDragDetected(this::dragDetected);
        grid.setOnDragOver(this::dragOver);
        grid.setOnDragDropped(this::dragDrop);
        grid.setOnDragDone(this::dragCompleted);
        grid.setOnMouseClicked(this::editSubtask);
        grid.addEventFilter(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (event.getCode()==KeyCode.ENTER) {
                    edit();
                }
            }
        });
    }

    private void edit(){
        int index = ((VBox) grid.getParent()).getChildren().indexOf(grid);
        SubtaskEditModel model = new SubtaskEditModel(text.getText(), subtask.isChecked(), index);
        subtask.setDescription(model.getDescription());
        subtask.setChecked(model.isChecked());
        serverUtils.editSubtask(subtask.getId(), model);

        description.setGraphic(null);
        description.setText(model.getDescription());
        description.requestFocus();
    }

    private void editSubtask(MouseEvent event){
        if (event.getButton().equals(MouseButton.PRIMARY))
            if (event.getClickCount()==1) {
                text = new TextField();
                text.setPrefWidth(320);

                text.setText(subtask.getDescription());
                description.setGraphic(text);
                description.setText("");

                text.end();
                text.requestFocus();
            }
    }

    public Scene getScene(Subtask subtask){
        description.setText(subtask.getDescription());
        checkbox.setSelected(subtask.isChecked());
        return grid.getScene();
    }

    public void changeSelected(){
        int index = ((VBox) grid.getParent()).getChildren().indexOf(grid);
        if(subtask.isChecked()){
            subtask.setChecked(false);
            serverUtils.editSubtask(subtask.getId(),
                    new SubtaskEditModel(subtask.getDescription(), false, index));
        }
        else{
            subtask.setChecked(true);
            serverUtils.editSubtask(subtask.getId(),
                    new SubtaskEditModel(subtask.getDescription(), true, index));
        }
    }

    public void remove(){
        IdResponseModel model = serverUtils.deleteSubtask(subtask.getTaskID(), subtask.getId());
        if(model.getId() == -1){
            showCtrl.showError(model.getErrorMessage());
        }
        else{
            VBox parent = (VBox) grid.getParent();
            parent.getChildren().remove(grid);
        }
    }

    /**
     * When dragging is detected, sets the drag board (a specific clipboard) to contain a string
     * to be recognized later. Makes a snapshot image of the subtask to be visible when dragging.
     * @param event the mouse event initiating the drag
     */
    private void dragDetected(MouseEvent event){
        Dragboard dragboard = grid.startDragAndDrop(TransferMode.MOVE);
        ClipboardContent clipboardContent = new ClipboardContent();
        SnapshotParameters snapshotParams = new SnapshotParameters();
        WritableImage image = grid.snapshot(snapshotParams, null);

        subtask =serverUtils.getSubtask(subtask.getId());
        clipboardContent.putString(String.valueOf(subtask.getId()));


        drag.set(grid);
        dragboard.setDragView(image, event.getX(), event.getY());
        dragboard.setContent(clipboardContent);
        event.consume();
    }

    private void dragOver (DragEvent event){
        Dragboard dragboard = event.getDragboard();
        if (dragboard.hasString()){
            event.acceptTransferModes(TransferMode.MOVE);
            event.consume();
        }
    }

    /**
     * Makes the subtask's (the grid representing it) opacity back to normal even
     * if the drag is unsuccessful
     * @param event the drag event
     */
    private void dragCompleted(DragEvent event){
        Object source = event.getGestureSource();
        ((GridPane) source).setOpacity(1);
        event.consume();
    }

    /**
     * Drops the subtask (the source) on another subtask and puts it in between. Reorders
     * the subtasks visually by rotating all subtasks
     * as well as in the database (updates the index after this event)
     * @param event the drag event
     */
    private void dragDrop(DragEvent event){
        Dragboard dragboard = event.getDragboard();
        Object source = event.getGestureSource();
        boolean done=false;

        if (dragboard.hasString()){
            VBox parent = (VBox) ((GridPane) source).getParent();
            int sourceIndex = parent.getChildren().indexOf(source);
            int targetIndex = parent.getChildren().indexOf(grid);

            ArrayList<Node> children = new ArrayList<>(parent.getChildren());
            ArrayList<Subtask> orderedSubtasks=
                    (ArrayList<Subtask>) serverUtils.getSubtasksOrdered(subtask.getTaskID());

            if (sourceIndex<targetIndex) {
                Collections.rotate(children.subList(sourceIndex, targetIndex + 1), -1);
                Collections.rotate(orderedSubtasks.subList(sourceIndex, targetIndex+1),-1);
            }
            else {
                Collections.rotate(children.subList(targetIndex, sourceIndex+1), 1);
                Collections.rotate(orderedSubtasks.subList(targetIndex, sourceIndex+1), 1);
            }

            reorderSubtasks(orderedSubtasks);

            parent.getChildren().clear();
            parent.getChildren().addAll(children);
            done=true;
        }
        ((GridPane) source).setOpacity(1);

        event.setDropCompleted(done);
        event.consume();
    }

    /**
     * Rearranges the subtasks' indexes after this drag event
     * @param subtasksToReorder the subtasks to reorder
     */
    private void reorderSubtasks(java.util.List<Subtask> subtasksToReorder){
        for (int i=0; i<subtasksToReorder.size(); i++){
            Subtask subtaskIndex = subtasksToReorder.get(i);
            SubtaskEditModel model = new SubtaskEditModel(subtaskIndex.getDescription(),
                    subtaskIndex.isChecked(), i);
            serverUtils.editSubtask(subtaskIndex.getId(), model);
        }
    }
}
