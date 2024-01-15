package db.footballdb.football_d_b_mongo.repos;

import db.footballdb.football_d_b_mongo.domain.Country;
import db.footballdb.football_d_b_mongo.domain.League;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;


public interface LeagueRepository extends MongoRepository<League, Long> {

    League findFirstByCountryLeague(Country country);


}
