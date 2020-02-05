package by.mkwt.webquiz.dao;

import by.mkwt.webquiz.entity.GameArchive;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GameArchiveRepository extends JpaRepository<GameArchive, Long> {

    @Query(value = "SELECT * FROM games_info g JOIN users u ON g.user_id = u.user_id WHERE u.username = ?1", nativeQuery = true)
    List<GameArchive> findAllByUsername(String username);

}
