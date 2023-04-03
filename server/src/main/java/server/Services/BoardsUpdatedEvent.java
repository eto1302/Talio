package server.Services;

import commons.Board;
import org.springframework.context.ApplicationEvent;

/**
 * custom event class, an event is fired when a board is added/deleted/edited
 * when the event is fired, the listener will set the result of all deferred results
 */
public class BoardsUpdatedEvent extends ApplicationEvent {

    private final Board board;

    public BoardsUpdatedEvent(Object source, Board board) {
        super(source);
        this.board = board;
    }
}
