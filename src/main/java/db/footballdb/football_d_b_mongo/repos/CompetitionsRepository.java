package db.footballdb.football_d_b_mongo.repos;

import db.footballdb.football_d_b_mongo.domain.Competitions;
import org.springframework.data.mongodb.repository.MongoRepository;


public interface CompetitionsRepository extends MongoRepository<Competitions, Long> {
}
