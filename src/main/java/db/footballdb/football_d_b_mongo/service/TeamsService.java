package db.footballdb.football_d_b_mongo.service;

import db.footballdb.football_d_b_mongo.domain.Competitions;
import db.footballdb.football_d_b_mongo.domain.Country;
import db.footballdb.football_d_b_mongo.domain.League;
import db.footballdb.football_d_b_mongo.domain.Players;
import db.footballdb.football_d_b_mongo.domain.Teams;
import db.footballdb.football_d_b_mongo.model.PlayersDTO;
import db.footballdb.football_d_b_mongo.model.TeamsDTO;
import db.footballdb.football_d_b_mongo.repos.CompetitionsRepository;
import db.footballdb.football_d_b_mongo.repos.CountryRepository;
import db.footballdb.football_d_b_mongo.repos.LeagueRepository;
import db.footballdb.football_d_b_mongo.repos.PlayersRepository;
import db.footballdb.football_d_b_mongo.repos.TeamsRepository;
import db.footballdb.football_d_b_mongo.util.NotFoundException;
import db.footballdb.football_d_b_mongo.util.WebUtils;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;


import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class TeamsService {

    private final TeamsRepository teamsRepository;
    private final CountryRepository countryRepository;
    private final LeagueRepository leagueRepository;
    private final CompetitionsRepository competitionsRepository;
    private final PlayersRepository playersRepository;
    private final PlayersService playersService;
    public TeamsService(final TeamsRepository teamsRepository,
            final CountryRepository countryRepository, final LeagueRepository leagueRepository,
            final CompetitionsRepository competitionsRepository,
            final PlayersRepository playersRepository, final PlayersService playersService) {
        this.teamsRepository = teamsRepository;
        this.countryRepository = countryRepository;
        this.leagueRepository = leagueRepository;
        this.competitionsRepository = competitionsRepository;
        this.playersRepository = playersRepository;
        this.playersService = playersService;
    }

    public List<TeamsDTO> findAll() {
        final List<Teams> teamses = teamsRepository.findAll(Sort.by("id"));
        return teamses.stream()
                .map(teams -> mapToDTO(teams, new TeamsDTO()))
                .toList();
    }

    public List<PlayersDTO> listPlayersInTeams(final Long id) {
        final Teams teams = teamsRepository.findById(id).get();
        final List<Players> players = playersRepository.findAllByToTeams(id);
        List <PlayersDTO> list = players.stream()
                .map(players1 -> {
                    PlayersDTO playersDTO = playersService.mapToDTO(players1, new PlayersDTO());
                    playersDTO.setOyuncuHangiTakimda(players1.getToTeams() == null ? null : players1.getToTeams().getTName());
                    return playersDTO;
                })
                .toList();
        return list;
    }

    public List<TeamsDTO> getByKeyword(final String keyword) {
        final List<Teams> teamses= teamsRepository.findAll();
        if(keyword != null) {
            List<Teams> byKeyword = teamsRepository.getByKeyword(keyword);
            return byKeyword.stream()
                    .map(teams -> mapToDTO(teams, new TeamsDTO()))
                    .toList();
        }
        return teamses.stream()
                .map(teams -> mapToDTO(teams, new TeamsDTO()))
                .toList();
    }
    public BigDecimal getToplamTakimDegeri() {
        List<Teams> takimListesi = teamsRepository.findAll();
        BigDecimal toplamDeger = takimListesi.stream()
                .map(Teams::getTValue)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        return toplamDeger;
    }
    public Page<TeamsDTO> findPage(int pageNumber) {
        Pageable pageable = PageRequest.of(pageNumber-1,10);
        return teamsRepository.findAll(pageable).map(teams -> mapToDTO(teams, new TeamsDTO()));
    }



    public TeamsDTO get(final Long id) {
        return teamsRepository.findById(id)
                .map(teams -> mapToDTO(teams, new TeamsDTO()))
                .orElseThrow(NotFoundException::new);
    }
    public Long create(final TeamsDTO teamsDTO) throws IOException {
        final Teams teams = new Teams();
        mapToEntity(teamsDTO, teams);
        return teamsRepository.save(teams).getId();
    }

    public void update(final Long id, final TeamsDTO teamsDTO) {
        final Teams teams = teamsRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        try {
            mapToEntity(teamsDTO, teams);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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
        teamsDTO.setFilePath((teams.getPathFile() == null || teams.getPathFile().isEmpty()) ? null : teams.getPathFile());
        teamsDTO.setToCountry(teams.getToCountry() == null ? null : teams.getToCountry().getId());
        teamsDTO.setTakimHangiUlkede(teams.getToCountry() == null ? null : teams.getToCountry().getCName());
        teamsDTO.setLeaguesss(teams.getLeaguesss() == null ? null : teams.getLeaguesss().getId());
        teamsDTO.setToTeamstoCompetitions(teams.getToTeamstoCompetitions().stream()
                .map(Competitions::getId)
                .toList());
        teamsDTO.setTakiminKatildigiMusabakalar(teams.getToTeamstoCompetitions().stream()
                .map(Competitions::getCompetitionName)
                .toList());
        return teamsDTO;
    }

    private Teams mapToEntity(final TeamsDTO teamsDTO, final Teams teams) throws IOException {
        teams.setTName(teamsDTO.getTName());
        teams.setTPoint(teamsDTO.getTPoint());
        teams.setTValue(teamsDTO.getTValue());
        if (teamsDTO.getPathFile()  != null && !teamsDTO.getPathFile().isEmpty()) {
            byte[] image = Base64.encodeBase64(teamsDTO.getPathFile().getBytes(), false);
            String result = new String(image);
            teams.setPathFile(result);
        } else {
            if (teamsDTO.getFilePath() == null || teamsDTO.getFilePath().isEmpty()){
                String base64Image = "src/main/resources/static/images/default-teams-logo.png";
                byte[] imageBytes = Base64.encodeBase64(Files.readAllBytes(Paths.get(base64Image)), false);
                String defaultResult = new String(imageBytes);
                teams.setPathFile(defaultResult);
            } else {
                teams.setPathFile(teamsDTO.getFilePath());
            }
        }
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
        iterable.forEach(list::add);
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
