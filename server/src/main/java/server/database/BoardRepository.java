package server.database;

import commons.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BoardRepository extends JpaRepository<Board, Integer> {
    @Query("SELECT b FROM Board b WHERE id = (:id)")
    Board getBoardByID(@Param("id") int id);

    @Query("SELECT b FROM Board b WHERE b.inviteKey=:inviteKey")
    Board getBoardByInviteKey(@Param("inviteKey") String inviteKey);
}
