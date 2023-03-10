package client.user;

import java.io.IOException;

/**
 * A subclass of IO exception used by the {@link UserData} class
 * to indicate that a poorly formatted file has caused reading the
 * information to fail.
 *
 * It is hence expected that the application handles this situation
 * accordingly, by either exiting the program or scrapping the data
 * and creating a new file.
 */
public class InvalidFormatException extends IOException {

    public InvalidFormatException() {
        super();
    }

    public InvalidFormatException(String message) {
        super(message);
    }

    public InvalidFormatException(Throwable cause) {
        super(cause);
    }

    public InvalidFormatException(String message, Throwable cause) {
        super(message, cause);
    }

}
