package db.footballdb.football_d_b_mongo.repos;

import db.footballdb.football_d_b_mongo.domain.Competitions;
import db.footballdb.football_d_b_mongo.domain.Country;
import db.footballdb.football_d_b_mongo.domain.League;
import db.footballdb.football_d_b_mongo.domain.Teams;
import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;


public interface TeamsRepository extends MongoRepository<Teams, Long> {

    Teams findFirstByToCountry(Country country);

    Teams findFirstByLeaguesss(League league);

    Teams findFirstByToTeamstoCompetitions(Competitions competitions);

    List<Teams> findAllByLeaguesss(League league);
    List<Teams> findByLeaguesssId(Long leagueId);
    List<Teams> findAllByToTeamstoCompetitions(Competitions competitions);

    @Query("{'tName': {$regex: ?0, $options: 'i'}}")
    List<Teams> getByKeyword(String keyword);

}
