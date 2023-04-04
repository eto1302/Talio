/*
 * Copyright 2021 Delft University of Technology
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package client;

import client.scenes.*;
import client.scenes.tags.*;
import client.user.UserData;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.util.Pair;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import static client.utils.Constants.FXML;
import static client.utils.Constants.INJECTOR;

public class Main extends Application {

    public static void main(String[] args) throws URISyntaxException, IOException {
        launch();
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        var userData = INJECTOR.getInstance(UserData.class);
        userData.initialize(new File(System.getProperty("user.dir") + "\\talioData.txt"));

        var showCtrl = INJECTOR.getInstance(ShowCtrl.class);
        List<Pair> loader = new ArrayList<>();
        setup(loader);
        showCtrl.initialize(primaryStage, loader);
    }

    public void setup(List<Pair> loader){
        var home = FXML.load(
                HomeController.class, "client", "scenes", "Home.fxml");
        var addList =FXML.load(
                AddListController.class, "client", "scenes", "AddList.fxml");
        var yourBoards=FXML.load(
                YourBoardsController.class, "client", "scenes", "YourBoards.fxml");
        var search = FXML.load(
                SearchCtrl.class, "client", "scenes","Search.fxml");
        var board = FXML.load(
                BoardController.class, "client", "scenes", "Board.fxml");
        var connection=FXML.load(
                ConnectionCtrl.class, "client", "scenes", "Connection.fxml");
        var addBoard=FXML.load(
                AddBoardController.class, "client", "scenes", "AddBoard.fxml");
        var error=FXML.load(
                ErrorController.class, "client", "scenes", "Error.fxml");
        var admin = FXML.load(
                AdminController.class, "client", "scenes", "Admin.fxml");
        var editBoard = FXML.load(
                EditBoardController.class, "client", "scenes", "EditBoard.fxml");
        var colorPicker = FXML.load(
                ColorPicker.class, "client", "scenes", "ColorPicker.fxml");
        var addTaskColor = FXML.load(
                AddTaskColor.class, "client", "scenes", "AddTaskColor.fxml");
        var help = FXML.load(
                HelpCtrl.class, "client", "scenes", "Help.fxml");
        var taskColorPicker= FXML.load(
                TaskColorPicker.class, "client", "scenes", "TaskColorPicker.fxml");
        var editColor= FXML.load(
                EditColor.class, "client", "scenes", "EditColor.fxml");
        var tagOverview = FXML.load(TagOverviewController.class,
            "client", "scenes", "TagOverview.fxml");
        loader.add(home);//0
        loader.add(addList);
        loader.add(yourBoards);
        loader.add(search);
        loader.add(board);
        loader.add(connection); //5
        loader.add(addBoard);
        loader.add(error);
        loader.add(admin);
        loader.add(editBoard);
        loader.add(help); //10
        loader.add(tagOverview);
        loader.add(colorPicker);
        loader.add(addTaskColor);
        loader.add(taskColorPicker);
        loader.add(editColor); //15
    }
}