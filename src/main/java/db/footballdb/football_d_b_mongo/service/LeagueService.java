package db.footballdb.football_d_b_mongo.service;

import db.footballdb.football_d_b_mongo.domain.Country;
import db.footballdb.football_d_b_mongo.domain.League;
import db.footballdb.football_d_b_mongo.domain.Teams;
import db.footballdb.football_d_b_mongo.model.LeagueDTO;
import db.footballdb.football_d_b_mongo.repos.CountryRepository;
import db.footballdb.football_d_b_mongo.repos.LeagueRepository;
import db.footballdb.football_d_b_mongo.repos.TeamsRepository;
import db.footballdb.football_d_b_mongo.util.NotFoundException;
import db.footballdb.football_d_b_mongo.util.WebUtils;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class LeagueService {

    private final LeagueRepository leagueRepository;
    private final CountryRepository countryRepository;
    private final TeamsRepository teamsRepository;

    public LeagueService(final LeagueRepository leagueRepository,
            final CountryRepository countryRepository, final TeamsRepository teamsRepository) {
        this.leagueRepository = leagueRepository;
        this.countryRepository = countryRepository;
        this.teamsRepository = teamsRepository;
    }

    public List<LeagueDTO> findAll() {
        final List<League> leagues = leagueRepository.findAll(Sort.by("id"));
        return leagues.stream()
                .map(league -> mapToDTO(league, new LeagueDTO()))
                .toList();
    }

    public LeagueDTO get(final Long id) {
        return leagueRepository.findById(id)
                .map(league -> mapToDTO(league, new LeagueDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final LeagueDTO leagueDTO) {
        final League league = new League();
        mapToEntity(leagueDTO, league);
        return leagueRepository.save(league).getId();
    }

    public void update(final Long id, final LeagueDTO leagueDTO) {
        final League league = leagueRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(leagueDTO, league);
        leagueRepository.save(league);
    }

    public void delete(final Long id) {
        final League league = leagueRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        // remove many-to-many relations at owning side
        teamsRepository.findAllByLeaguesss(league)
                .forEach(teams -> teams.setLeaguesss(null));
        leagueRepository.delete(league);
    }

    private LeagueDTO mapToDTO(final League league, final LeagueDTO leagueDTO) {
        leagueDTO.setId(league.getId());
        leagueDTO.setLeagueName(league.getLeagueName());
        leagueDTO.setCountryLeague(league.getCountryLeague() == null ? null : league.getCountryLeague().getId());
        leagueDTO.setLigHangiUlkede(league.getCountryLeague() == null ? null : league.getCountryLeague().getCName());
        return leagueDTO;
    }

    private League mapToEntity(final LeagueDTO leagueDTO, final League league) {
        league.setLeagueName(leagueDTO.getLeagueName());
        final Country countryLeague = leagueDTO.getCountryLeague() == null ? null : countryRepository.findById(leagueDTO.getCountryLeague())
                .orElseThrow(() -> new NotFoundException("countryLeague not found"));
        league.setCountryLeague(countryLeague);
        return league;
    }

    public String getReferencedWarning(final Long id) {
        final League league = leagueRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        final Teams leaguesssTeams = teamsRepository.findFirstByLeaguesss(league);
        if (leaguesssTeams != null) {
            return WebUtils.getMessage("league.teams.leaguesss.referenced", leaguesssTeams.getId());
        }
        return null;
    }

}
