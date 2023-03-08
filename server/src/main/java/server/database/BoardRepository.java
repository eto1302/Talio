package server.database;

import commons.Board;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository extends JpaRepository<Board, Integer> {
    /*@Query("SELECT b FROM Board b WHERE id = (:id)")
    Board getBoardByID(@Param("id") int id);*/
}
