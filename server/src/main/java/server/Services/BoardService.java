package server.Services;

import commons.Board;
import commons.List;
import org.springframework.stereotype.Service;
import server.database.BoardRepository;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

@Service
public class BoardService {
    BoardRepository boardRepository;
    public BoardService(BoardRepository boardRepository){
        this.boardRepository = boardRepository;
    }
    public Board getBoard(){
        List firstList = List.create("To Do", new ArrayList<>());
        Set<List> lists = new HashSet<>();
        lists.add(firstList);

        return Board.create("TEAM", "12345", lists);
    }

    public Board getBoardById(int id){
        return boardRepository.getById(id);
    }
}
