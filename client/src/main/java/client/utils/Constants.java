package client.utils;

import client.MyFXML;
import client.MyModule;
import com.google.inject.Injector;
import javafx.scene.image.Image;

import static com.google.inject.Guice.createInjector;

/**
 * Holds various application constants, including the FXML loader
 * and Guice injector
 */
public class Constants {

    public static final Injector INJECTOR = createInjector(new MyModule());

    public static final MyFXML FXML = new MyFXML(INJECTOR);

    public static final Image LOCKED_IMG = new Image(
            "file:client/build/resources/main/icons/lock.png");

    public static final Image UNLOCKED_IMG = new Image(
            "file:client/build/resources/main/icons/unlock.png");

}
