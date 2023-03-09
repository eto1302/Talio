package client.scenes;

import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.util.Pair;
import java.util.*;

public class ShowCtrl {
    private Stage primaryStage, secondaryStage;
    private HomeController homeCtrl;
    private Scene home, addTask, addList, yourBoards, search, taskOverview;
    private AddListController addListCtrl;

    private AddTaskController addTaskCtrl;
    private YourBoardsController yourBoardsCtrl;
    private SearchCtrl searchCtrl;
    private TaskOverview taskOverviewCtrl;

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
        taskOverviewCtrl = (TaskOverview) loader.get(5).getKey();
        taskOverview = new Scene((Parent) loader.get(5).getValue());

        showHome();
        primaryStage.show();
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

    public void showTaskOverview() {
        secondaryStage=new Stage();
        secondaryStage.setScene(taskOverview);
        secondaryStage.setTitle("See your task details");
        secondaryStage.show();
    }

    /**
     * Creates a scene with a new parent grid that will have every list and card of the user. Basically a refresh
     * after we add a list.
     * @param gridPane the grid pane that will be populated
     */

    public void addList(GridPane gridPane){
        double height=primaryStage.getHeight();
        double width = primaryStage.getWidth();
        setupGrid(gridPane);

        this.home=new Scene(gridPane);
        primaryStage.setScene(home);
        primaryStage.setWidth(width);
        primaryStage.setHeight(height);

        primaryStage.show();
    }

    /**
     * Sets up the grid (parent of the scene) to be identical to the one we started from
     * @param gridPane the grid that will contain all lists with tasks
     */
    public void setupGrid(GridPane gridPane){
        GridPane parent=(GridPane)home.getRoot();
        Iterator<Node> it = parent.getChildren().iterator();
        while (it.hasNext()){
            Node node = it.next();
            it.remove();
            if (node instanceof GridPane)
                gridPane.add(node, 0, 0);
            if (node instanceof VBox)
                gridPane.add(node, 0, gridPane.getRowCount());
        }

        ColumnConstraints col = new ColumnConstraints();
        col.setHgrow(Priority.SOMETIMES);
        RowConstraints row = new RowConstraints();
        row.setVgrow(Priority.NEVER);
        row.setMinHeight(35); row.setPrefHeight(35);
        gridPane.getColumnConstraints().add(0,col);
        gridPane.getRowConstraints().add(0,row);
    }


}
