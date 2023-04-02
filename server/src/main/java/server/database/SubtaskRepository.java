package server.database;

import commons.Subtask;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface SubtaskRepository extends JpaRepository <Subtask, Integer>{

    @Query("SELECT s FROM Subtask s WHERE id = (:id)")
    Subtask getSubtaskByID(@Param("id") int id);
}
