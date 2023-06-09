package client.scenes;

import client.Services.SubtaskService;
import client.user.UserData;
import client.utils.ServerUtils;
import commons.Subtask;
import commons.models.IdResponseModel;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
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
    private SubtaskService subtaskService;

    @Inject
    public SubTaskShapeCtrl(ShowCtrl showCtrl, ServerUtils serverUtils, UserData userData){
        this.showCtrl = showCtrl;
        this.serverUtils = serverUtils;
        this.subtaskService = new SubtaskService(userData, serverUtils);
    }

    public void setup(Subtask subtask){
        this.subtask = subtask;
        if(subtask.getDescription() == null) subtask.setDescription("");
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
        subtaskService.setDescription(subtask, text.getText(),
                ((VBox) grid.getParent()).getChildren().indexOf(grid));

        description.setGraphic(null);
        description.requestFocus();
    }

    private void editSubtask(MouseEvent event){
        if (event.getButton().equals(MouseButton.PRIMARY))
            if (event.getClickCount()==2) {
                text = new TextField();
                text.setPrefWidth(320);

                text.setText(subtask.getDescription());
                description.setGraphic(text);
                description.setText("");

                text.end();
                text.requestFocus();
            }
    }

    public void changeSelected(){
        IdResponseModel id = subtaskService.setChecked(subtask, !subtask.isChecked(),
                ((VBox) grid.getParent()).getChildren().indexOf(grid));
        if(id.getId() < 0){
            showCtrl.showError(id.getErrorMessage());
        }
    }

    public void remove(){
        IdResponseModel model = subtaskService.delete(subtask);
        if(model.getId() == -1){
            showCtrl.showError(model.getErrorMessage());
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
            ArrayList<Subtask> orderedSubtasks= new ArrayList<>(
                    this.subtaskService.getSubtasksOrdered(
                    subtask.getTaskID()));


            if (sourceIndex<targetIndex) {
                Collections.rotate(children.subList(sourceIndex, targetIndex + 1), -1);
                Collections.rotate(orderedSubtasks.subList(sourceIndex, targetIndex+1),-1);
            }
            else {
                Collections.rotate(children.subList(targetIndex, sourceIndex+1), 1);
                Collections.rotate(orderedSubtasks.subList(targetIndex, sourceIndex+1), 1);
            }

            subtaskService.writeSubtaskOrder(orderedSubtasks);
            done=true;
        }
        ((GridPane) source).setOpacity(1);

        event.setDropCompleted(done);
        event.consume();
    }

}
