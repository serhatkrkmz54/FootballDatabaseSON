package db.footballdb.football_d_b_mongo.repos;

import db.footballdb.football_d_b_mongo.domain.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends MongoRepository<User, Long> {
    @EntityGraph(type = EntityGraph.EntityGraphType.FETCH,
                    attributePaths = {"authorities"})
    Optional<User> findByUsername(String username);

    @EntityGraph(type = EntityGraph.EntityGraphType.FETCH,
                    attributePaths = {"authorities"})
    List<User> findAll();

    Optional<User> findLoginUser();

}
