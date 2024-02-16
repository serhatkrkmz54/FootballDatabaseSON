package db.footballdb.football_d_b_mongo.repos;

import db.footballdb.football_d_b_mongo.domain.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface AuthorityRepository extends MongoRepository<User, Long> {

}
