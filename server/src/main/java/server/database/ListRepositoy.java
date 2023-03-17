package server.database;

import commons.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ListRepositoy extends JpaRepository<List, Integer> {

    @Query("SELECT l FROM List l WHERE id = (:id)")
    List getListByID(@Param("id") int id);
}
