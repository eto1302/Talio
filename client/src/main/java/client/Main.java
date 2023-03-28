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

import static com.google.inject.Guice.createInjector;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import client.scenes.*;
import client.user.UserData;
import com.google.inject.Injector;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.util.*;
import java.util.*;

public class Main extends Application {

    private static final Injector INJECTOR = createInjector(new MyModule());
    private static final MyFXML FXML = new MyFXML(INJECTOR);


    public static void main(String[] args) throws URISyntaxException, IOException {
        launch();
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        var userData = INJECTOR.getInstance(UserData.class);
        userData.initialize(new File("%userprofile%\\applicationData.txt"));

        var showCtrl = INJECTOR.getInstance(ShowCtrl.class);
        List<Pair> loader = new ArrayList<>();
        setup(loader);
        showCtrl.initialize(primaryStage, loader);
    }

    public void setup(List<Pair> loader){
        var home = FXML.load(HomeController.class, "client", "scenes", "Home.fxml");
        var addList =FXML.load(AddListController.class, "client", "scenes", "AddList.fxml");
        var yourBoards=FXML.load(YourBoardsController.class, "client", "scenes", "YourBoards.fxml");
        var search = FXML.load(SearchCtrl.class, "client", "scenes","Search.fxml");
        var board = FXML.load(BoardController.class, "client", "scenes", "Board.fxml");
        var connection=FXML.load(ConnectionCtrl.class, "client", "scenes", "Connection.fxml");
        var addBoard=FXML.load(AddBoardController.class, "client", "scenes", "AddBoard.fxml");
        var error=FXML.load(ErrorController.class, "client", "scenes", "Error.fxml");
        var admin = FXML.load(AdminController.class, "client", "scenes", "Admin.fxml");

        loader.add(home);
        loader.add(addList);
        loader.add(yourBoards);
        loader.add(search);
        loader.add(board);
        loader.add(connection);
        loader.add(addBoard);
        loader.add(error);
        loader.add(admin);

    }
}