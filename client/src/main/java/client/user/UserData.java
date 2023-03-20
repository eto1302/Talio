package client.user;

import client.messageClients.MessageAdmin;
import client.messageClients.MessageSender;
import client.sync.BoardUpdate;
import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Board;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * The UserData class is a singleton dependency that stores all the
 * boards that this client is connected to including their passwords,
 * as well as a path in which to store this data when the application
 * is closed (including functionality to read and write to said path)
 *
 * In the future, this class may also store information about user
 * preferences, such as background color/image, which will be stored
 * in the same configuration file as well.
 */
public class UserData {

    /**
     * File in which to save the user's data
     */
    private File savePath;

    /**
     * User's joined boards: a mapping of integers (board ID) to
     * strings (board passwords)
     */
    private Map<Integer, String> boards;

    /**
     * Current opened board, imperative for synchronization
     */
    private Board currentBoard;

    /**
     * Message sender used for synchronization
     * Injected by guice
     */
    @Inject
    private MessageSender messageSender;

    /**
     * Message admin used for synchronization
     * Injected by guice
     */
    @Inject
    private MessageAdmin messageAdmin;

    /**
     * Server utils object used for sending board requests and updates
     * to the server
     * Injected by guice
     */
    @Inject
    private ServerUtils serverUtils;

    /**
     * Initializes the UserData class with a given filepath for the datafile. If this file
     * exists already, then it will be read and all user information imported into the fields
     * of this class. Otherwise, no action will be taken, and data will only be written when
     * the data is explicitly saved (refer to {@link UserData#saveToDisk()}).
     *
     * @param savePath the file to use for data saving (may or may not exist)
     * @throws IOException if any IO error occurred, including a poorly formatted datafile
     */
    public void initialize(File savePath) throws IOException {
        assert savePath != null;
        this.savePath = savePath;
        this.boards = new HashMap<>();

        if(savePath.exists()) {
            assert !savePath.isDirectory();
            loadFromDisk();
        }
    }

    /**
     * @return the current filepath to the datafile
     */
    public File getSavePath() {
        return savePath;
    }

    /**
     * Assigns a new filepath to the datafile, note that no action is taken until
     * data is explicitly loaded or saved.
     *
     * @param savePath the new filepath
     */
    public void setSavePath(File savePath) {
        this.savePath = savePath;
    }

    /**
     * @return the current ID to password board mapping
     */
    public Map<Integer, String> getBoards() {
        return boards;
    }

    /**
     * Joins a new board given the necessary information by adding it to the mapping.
     * This will not be saved to the datafile until done explicitly.
     *
     * @param identifier the board ID of the new board to join
     * @param password the board's password
     */
    public void joinBoard(int identifier, String password) {
        boards.put(identifier, password);
    }

    /**
     * Leaves a board already present in the current mapping (no action is taken if
     * it does not exist). This will not be saved to the datafile until done explicitly.
     *
     * @param identifier the board's ID
     */
    public void leaveBoard(int identifier) {
        boards.remove(identifier);
    }

    /**
     * [WORK IN PROGRESS, to integrate with server and messaging system]
     *
     * Fetches a board with a given identifier, returning a board object
     * containing the full details of said board. This method asserts that
     * the identifier of the board must be one of the joined boards,
     * throwing an {@link AssertionError} otherwise.
     *
     * @param identifier the ID of the board to fetch
     * @return the full detailing of the fetched board
     */
    public Board openBoard(int identifier) {
        assert boards.containsKey(identifier);

        this.currentBoard = serverUtils.getBoard(identifier);
        this.messageAdmin.subscribe(BoardUpdate.QUEUE + currentBoard.getId());
        return currentBoard;
    }

    /**
     * @return the current board (since last synchronization), or null if no board has been opened
     */
    public Board getCurrentBoard() {
        return currentBoard;
    }

    /**
     * Uses a {@link BoardUpdate} object to update the board in a particular way. This includes
     * updating the current board locally, sending the update to the server, and messaging all
     * other clients about the update.
     *
     * @param boardUpdate the update to apply
     * @return true if the board update was successful, false otherwise (with no changes made)
     */
    public boolean updateBoard(BoardUpdate boardUpdate) {
        if(!boardUpdate.sendToServer(serverUtils))
            return false;

        if(currentBoard != null && currentBoard.getId() == boardUpdate.getBoardID())
            boardUpdate.apply(this);
        messageSender.send(boardUpdate.getQueue(), boardUpdate);
        return true;
    }

    /**
     * Reads the file at the current save path and overwrites the board
     * mapping of this object with the information retrieved from said
     * file.
     *
     * @throws IOException if any IO error occurred while reading,
     * including a poorly formatted file
     */
    public void loadFromDisk() throws IOException {
        boards.clear();
        FileReader fr = new FileReader(savePath);
        BufferedReader br = new BufferedReader(fr);

        String line;
        while((line = br.readLine()) != null) {
            line = line.trim();
            if(line.length() == 0)
                continue;

            String[] items = line.split("\\\\");
            if(items[0].trim().equals("b")) {
                if(items.length != 3)
                    throw new InvalidFormatException("On line: " + line);

                int identifier;
                try {
                    identifier = Integer.parseInt(items[1].trim());
                }
                catch(NumberFormatException ex) {
                    throw new InvalidFormatException(ex);
                }

                boards.put(identifier, items[2]);
            }
            // Future directives can be inserted here
            else throw new
                    InvalidFormatException(
                            String.format("No such directive '%s' on line: %s", items[0], line));
        }

        br.close();
        fr.close();
    }

    /**
     * Saves the current board mapping of this object into the file
     * specified by the save path. The contents of this file will be
     * overwritten entirely to reflect the new data.
     *
     * @throws IOException if any IO error occurred while writing.
     */
    public void saveToDisk() throws IOException {
        FileWriter fw = new FileWriter(savePath, false);
        BufferedWriter bw = new BufferedWriter(fw);

        for(Map.Entry<Integer, String> boardEntry : boards.entrySet()) {
            String line = "b\\" +
                    boardEntry.getKey() +
                    "\\" +
                    boardEntry.getValue();
            bw.write(line);
        }

        bw.close();
        fw.close();
    }

}
