package server.database;

import commons.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TaskRepository extends JpaRepository<Task, Integer> {
    /*@Query("SELECT t FROM Task t WHERE id = (:id)")
    Task getTaskById(@Param("id") int id);*/
}
