package db.footballdb.football_d_b_mongo.repos;

import db.footballdb.football_d_b_mongo.domain.Country;
import db.footballdb.football_d_b_mongo.domain.Players;
import db.footballdb.football_d_b_mongo.domain.Teams;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;


public interface PlayersRepository extends MongoRepository<Players, Long> {

    Players findFirstByToTeams(Teams teams);
    Players findFirstByToCountryPlayers(Country country);

    List<Players> findAllByToTeams(Long teamsID);

}
