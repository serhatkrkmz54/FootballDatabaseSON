package db.footballdb.football_d_b_mongo.service;

import db.footballdb.football_d_b_mongo.domain.Competitions;
import db.footballdb.football_d_b_mongo.domain.Country;
import db.footballdb.football_d_b_mongo.domain.League;
import db.footballdb.football_d_b_mongo.domain.Players;
import db.footballdb.football_d_b_mongo.domain.Teams;
import db.footballdb.football_d_b_mongo.model.TeamsDTO;
import db.footballdb.football_d_b_mongo.repos.CompetitionsRepository;
import db.footballdb.football_d_b_mongo.repos.CountryRepository;
import db.footballdb.football_d_b_mongo.repos.LeagueRepository;
import db.footballdb.football_d_b_mongo.repos.PlayersRepository;
import db.footballdb.football_d_b_mongo.repos.TeamsRepository;
import db.footballdb.football_d_b_mongo.util.NotFoundException;
import db.footballdb.football_d_b_mongo.util.WebUtils;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class TeamsService {

    private final TeamsRepository teamsRepository;
    private final CountryRepository countryRepository;
    private final LeagueRepository leagueRepository;
    private final CompetitionsRepository competitionsRepository;
    private final PlayersRepository playersRepository;

    public TeamsService(final TeamsRepository teamsRepository,
            final CountryRepository countryRepository, final LeagueRepository leagueRepository,
            final CompetitionsRepository competitionsRepository,
            final PlayersRepository playersRepository) {
        this.teamsRepository = teamsRepository;
        this.countryRepository = countryRepository;
        this.leagueRepository = leagueRepository;
        this.competitionsRepository = competitionsRepository;
        this.playersRepository = playersRepository;
    }

    public List<TeamsDTO> findAll() {
        final List<Teams> teamses = teamsRepository.findAll(Sort.by("id"));
        return teamses.stream()
                .map(teams -> mapToDTO(teams, new TeamsDTO()))
                .toList();
    }

    public TeamsDTO get(final Long id) {
        return teamsRepository.findById(id)
                .map(teams -> mapToDTO(teams, new TeamsDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final TeamsDTO teamsDTO) {
        final Teams teams = new Teams();
        mapToEntity(teamsDTO, teams);
        return teamsRepository.save(teams).getId();
    }

    public void update(final Long id, final TeamsDTO teamsDTO) {
        final Teams teams = teamsRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(teamsDTO, teams);
        teamsRepository.save(teams);
    }

    public void delete(final Long id) {
        teamsRepository.deleteById(id);
    }

    public TeamsDTO mapToDTO(final Teams teams, final TeamsDTO teamsDTO) {
        teamsDTO.setId(teams.getId());
        teamsDTO.setTName(teams.getTName());
        teamsDTO.setTPoint(teams.getTPoint());
        teamsDTO.setTValue(teams.getTValue());
        teamsDTO.setToCountry(teams.getToCountry() == null ? null : teams.getToCountry().getId());
        teamsDTO.setTakimHangiUlkede(teams.getToCountry() == null ? null : teams.getToCountry().getCName());
        teamsDTO.setLeaguesss(teams.getLeaguesss() == null ? null : teams.getLeaguesss().getId());
        teamsDTO.setToTeamstoCompetitions(teams.getToTeamstoCompetitions().stream()
                .map(competitions -> competitions.getId())
                .toList());
        teamsDTO.setTakiminKatildigiMusabakalar(teams.getToTeamstoCompetitions().stream()
                .map(competitions -> competitions.getCompetitionName())
                .toList());
        return teamsDTO;
    }

    private Teams mapToEntity(final TeamsDTO teamsDTO, final Teams teams) {
        teams.setTName(teamsDTO.getTName());
        teams.setTPoint(teamsDTO.getTPoint());
        teams.setTValue(teamsDTO.getTValue());
        final Country toCountry = teamsDTO.getToCountry() == null ? null : countryRepository.findById(teamsDTO.getToCountry())
                .orElseThrow(() -> new NotFoundException("toCountry not found"));
        teams.setToCountry(toCountry);
        final League leaguesss = teamsDTO.getLeaguesss() == null ? null : leagueRepository.findById(teamsDTO.getLeaguesss())
                .orElseThrow(() -> new NotFoundException("leaguesss not found"));
        teams.setLeaguesss(leaguesss);
        final List<Competitions> toTeamstoCompetitions = iterableToList(competitionsRepository.findAllById(
                teamsDTO.getToTeamstoCompetitions() == null ? Collections.emptyList() : teamsDTO.getToTeamstoCompetitions()));
        if (toTeamstoCompetitions.size() != (teamsDTO.getToTeamstoCompetitions() == null ? 0 : teamsDTO.getToTeamstoCompetitions().size())) {
            throw new NotFoundException("one of toTeamstoCompetitions not found");
        }
        teams.setToTeamstoCompetitions(toTeamstoCompetitions.stream().collect(Collectors.toSet()));
        return teams;
    }

    private <T> List<T> iterableToList(final Iterable<T> iterable) {
        final List<T> list = new ArrayList<T>();
        iterable.forEach(item -> list.add(item));
        return list;
    }

    public String getReferencedWarning(final Long id) {
        final Teams teams = teamsRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        final Players toTeamsPlayers = playersRepository.findFirstByToTeams(teams);
        if (toTeamsPlayers != null) {
            return WebUtils.getMessage("teams.players.toTeams.referenced", toTeamsPlayers.getId());
        }
        return null;
    }

}
