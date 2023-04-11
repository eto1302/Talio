package client.utils;

import client.MyFXML;
import client.MyModule;
import com.google.inject.Injector;

import static com.google.inject.Guice.createInjector;

/**
 * Holds various application constants, including the FXML loader
 * and Guice injector
 */
public class Constants {

    public static final Injector INJECTOR = createInjector(new MyModule());

    public static final MyFXML FXML = new MyFXML(INJECTOR);

}
