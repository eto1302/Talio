package client.scenes;


import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Pair;
import java.util.*;

public class ShowCtrl {

    private Stage primaryStage, secondaryStage;
    private HomeController homeCtrl;
    private Scene home, addTask, addCard, yourBoards;
    private AddCardController addCardCtrl;
    private AddTaskController addTaskCtrl;
    private YourBoardsController yourBoardsCtrl;

    public void initialize(Stage primaryStage, List<Pair> loader) {
        this.primaryStage = primaryStage;
        homeCtrl = (HomeController) loader.get(0).getKey();
        home = new Scene((Parent) loader.get(0).getValue());
        addCardCtrl = (AddCardController) loader.get(1).getKey();
        addCard = new Scene((Parent) loader.get(1).getValue());
        yourBoardsCtrl = (YourBoardsController) loader.get(2).getKey();
        yourBoards = new Scene((Parent) loader.get(2).getValue());
        addTaskCtrl = (AddTaskController) loader.get(3).getKey();
        addTask = new Scene((Parent) loader.get(3).getValue());

        showHome();
        primaryStage.show();
    }

    public void showAddCard() {
        secondaryStage = new Stage();
        secondaryStage.setScene(addCard);
        secondaryStage.setTitle("Add a card");
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


}
