package db.footballdb.football_d_b_mongo.service;

import db.footballdb.football_d_b_mongo.domain.Competitions;
import db.footballdb.football_d_b_mongo.domain.Country;
import db.footballdb.football_d_b_mongo.domain.Teams;
import db.footballdb.football_d_b_mongo.model.CompetitionsDTO;
import db.footballdb.football_d_b_mongo.repos.CompetitionsRepository;
import db.footballdb.football_d_b_mongo.repos.CountryRepository;
import db.footballdb.football_d_b_mongo.repos.TeamsRepository;
import db.footballdb.football_d_b_mongo.util.NotFoundException;
import db.footballdb.football_d_b_mongo.util.WebUtils;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class CompetitionsService {

    private final CompetitionsRepository competitionsRepository;
    private final CountryRepository countryRepository;
    private final TeamsRepository teamsRepository;

    public CompetitionsService(final CompetitionsRepository competitionsRepository,
            final CountryRepository countryRepository, final TeamsRepository teamsRepository) {
        this.competitionsRepository = competitionsRepository;
        this.countryRepository = countryRepository;
        this.teamsRepository = teamsRepository;
    }

    public List<CompetitionsDTO> findAll() {
        final List<Competitions> competitionses = competitionsRepository.findAll(Sort.by("id"));
        return competitionses.stream()
                .map(competitions -> mapToDTO(competitions, new CompetitionsDTO()))
                .toList();
    }

    public CompetitionsDTO get(final Long id) {
        return competitionsRepository.findById(id)
                .map(competitions -> mapToDTO(competitions, new CompetitionsDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final CompetitionsDTO competitionsDTO) {
        final Competitions competitions = new Competitions();
        mapToEntity(competitionsDTO, competitions);
        return competitionsRepository.save(competitions).getId();
    }

    public void update(final Long id, final CompetitionsDTO competitionsDTO) {
        final Competitions competitions = competitionsRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(competitionsDTO, competitions);
        competitionsRepository.save(competitions);
    }

    public void delete(final Long id) {
        final Competitions competitions = competitionsRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        // remove many-to-many relations at owning side
        countryRepository.findAllByToCompetitions(competitions)
                .forEach(country -> country.getToCompetitions().remove(competitions));
        teamsRepository.findAllByToTeamstoCompetitions(competitions)
                .forEach(teams -> teams.getToTeamstoCompetitions().remove(competitions));
        competitionsRepository.delete(competitions);
    }

    private CompetitionsDTO mapToDTO(final Competitions competitions,
            final CompetitionsDTO competitionsDTO) {
        competitionsDTO.setId(competitions.getId());
        competitionsDTO.setCompetitionName(competitions.getCompetitionName());
        return competitionsDTO;
    }

    private Competitions mapToEntity(final CompetitionsDTO competitionsDTO,
            final Competitions competitions) {
        competitions.setCompetitionName(competitionsDTO.getCompetitionName());
        return competitions;
    }

    public String getReferencedWarning(final Long id) {
        final Competitions competitions = competitionsRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        final Country toCompetitionsCountry = countryRepository.findFirstByToCompetitions(competitions);
        if (toCompetitionsCountry != null) {
            return WebUtils.getMessage("competitions.country.toCompetitions.referenced", toCompetitionsCountry.getId());
        }
        final Teams toTeamstoCompetitionsTeams = teamsRepository.findFirstByToTeamstoCompetitions(competitions);
        if (toTeamstoCompetitionsTeams != null) {
            return WebUtils.getMessage("competitions.teams.toTeamstoCompetitions.referenced", toTeamstoCompetitionsTeams.getId());
        }
        return null;
    }

}
