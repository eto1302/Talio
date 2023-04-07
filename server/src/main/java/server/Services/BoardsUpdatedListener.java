package server.Services;

import commons.Board;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.async.DeferredResult;
import server.database.BoardRepository;

import java.util.ArrayList;
import java.util.List;

@Component
public class BoardsUpdatedListener {

    private final List<DeferredResult<Board[]>>  deferredResults = new ArrayList<>();
    private final BoardRepository boardRepository;

    public BoardsUpdatedListener(BoardRepository boardRepository) {
        this.boardRepository = boardRepository;
    }

    /**
     * add the responses to be sent to a list
     * @param deferredResult the response to be sent
     */
    public void addDeferredResult(DeferredResult<Board[]> deferredResult){
        deferredResults.add(deferredResult);
    }

    /**
     * when the event is fired, activate all the deferred results and clear the list
     * @param event custom event
     */
    @EventListener
    public void onBoardsUpdated(BoardsUpdatedEvent event){
        Board[] boards = boardRepository.findAll().toArray(new Board[0]);
        for(DeferredResult<Board[]> deferredResult : deferredResults){
            deferredResult.setResult(boards);
        }
        deferredResults.clear();
    }
}
