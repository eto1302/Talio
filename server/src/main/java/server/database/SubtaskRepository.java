package server.database;

import commons.Subtask;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SubtaskRepository extends JpaRepository <Subtask, Integer>{

    @Query("SELECT s FROM Subtask s WHERE s.id = (:id)")
    Subtask getSubtaskByID(@Param("id") int id);
    @Query("SELECT s FROM Subtask s WHERE s.taskID = :taskId ORDER BY s.index")
    List<Subtask> getSubtasksOrdered(@Param("taskId") int taskId);
}
