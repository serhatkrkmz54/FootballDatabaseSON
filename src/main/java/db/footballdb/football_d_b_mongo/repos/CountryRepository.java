package db.footballdb.football_d_b_mongo.repos;

import db.footballdb.football_d_b_mongo.domain.Competitions;
import db.footballdb.football_d_b_mongo.domain.Country;
import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;


public interface CountryRepository extends MongoRepository<Country, Long> {

    Country findFirstByToCompetitions(Competitions competitions);

    List<Country> findAllByToCompetitions(Competitions competitions);

}
