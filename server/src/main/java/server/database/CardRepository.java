package server.database;

import commons.Card;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CardRepository extends JpaRepository<Card, Integer> {
    /*@Query("SELECT c FROM Card c WHERE id = (:id)")
    Card getCardById(@Param("id") int id);*/
}
