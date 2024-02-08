package db.footballdb.football_d_b_mongo.service;

import db.footballdb.football_d_b_mongo.domain.Competitions;
import db.footballdb.football_d_b_mongo.domain.Country;
import db.footballdb.football_d_b_mongo.domain.League;
import db.footballdb.football_d_b_mongo.domain.Players;
import db.footballdb.football_d_b_mongo.domain.Teams;
import db.footballdb.football_d_b_mongo.model.CountryDTO;
import db.footballdb.football_d_b_mongo.model.PlayersDTO;
import db.footballdb.football_d_b_mongo.repos.CompetitionsRepository;
import db.footballdb.football_d_b_mongo.repos.CountryRepository;
import db.footballdb.football_d_b_mongo.repos.LeagueRepository;
import db.footballdb.football_d_b_mongo.repos.PlayersRepository;
import db.footballdb.football_d_b_mongo.repos.TeamsRepository;
import db.footballdb.football_d_b_mongo.util.NotFoundException;
import db.footballdb.football_d_b_mongo.util.WebUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class CountryService {

    private final CountryRepository countryRepository;
    private final CompetitionsRepository competitionsRepository;
    private final TeamsRepository teamsRepository;
    private final PlayersRepository playersRepository;
    private final LeagueRepository leagueRepository;

    public CountryService(final CountryRepository countryRepository,
                          final CompetitionsRepository competitionsRepository,
                          final TeamsRepository teamsRepository, final PlayersRepository playersRepository,
                          final LeagueRepository leagueRepository) {
        this.countryRepository = countryRepository;
        this.competitionsRepository = competitionsRepository;
        this.teamsRepository = teamsRepository;
        this.playersRepository = playersRepository;
        this.leagueRepository = leagueRepository;
    }

    public List<CountryDTO> findAll() {
        final List<Country> countries = countryRepository.findAll(Sort.by("id"));
        return countries.stream()
                .map(country -> mapToDTO(country, new CountryDTO()))
                .toList();
    }
    public BigDecimal getUlkeDegeri() {
        List<Country> ulkeListesi = countryRepository.findAll();
        BigDecimal ulkelerToplamDeger = ulkeListesi.stream()
                .map(Country::getCValue)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        return ulkelerToplamDeger;
    }
    public Page<CountryDTO> findPage(int pageNumber) {
        Pageable pageable = PageRequest.of(pageNumber-1,10);
        return countryRepository.findAll(pageable).map(country -> mapToDTO(country, new CountryDTO()));
    }
    public CountryDTO get(final Long id) {
        return countryRepository.findById(id)
                .map(country -> mapToDTO(country, new CountryDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final CountryDTO countryDTO) {
        final Country country = new Country();
        mapToEntity(countryDTO, country);
        return countryRepository.save(country).getId();
    }

    public void update(final Long id, final CountryDTO countryDTO) {
        final Country country = countryRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(countryDTO, country);
        countryRepository.save(country);
    }

    public void delete(final Long id) {
        countryRepository.deleteById(id);
    }

    private CountryDTO mapToDTO(final Country country, final CountryDTO countryDTO) {
        countryDTO.setId(country.getId());
        countryDTO.setCName(country.getCName());
        countryDTO.setCValue(country.getCValue());
        countryDTO.setCPoint(country.getCPoint());
        countryDTO.setToCompetitions(country.getToCompetitions().stream()
                .map(competitions -> competitions.getId())
                .toList());
        return countryDTO;
    }

    private Country mapToEntity(final CountryDTO countryDTO, final Country country) {
        country.setCName(countryDTO.getCName());
        country.setCValue(countryDTO.getCValue());
        country.setCPoint(countryDTO.getCPoint());
        final List<Competitions> toCompetitions = iterableToList(competitionsRepository.findAllById(
                countryDTO.getToCompetitions() == null ? Collections.emptyList() : countryDTO.getToCompetitions()));
        if (toCompetitions.size() != (countryDTO.getToCompetitions() == null ? 0 : countryDTO.getToCompetitions().size())) {
            throw new NotFoundException("one of toCompetitions not found");
        }
        country.setToCompetitions(toCompetitions.stream().collect(Collectors.toSet()));
        return country;
    }

    private <T> List<T> iterableToList(final Iterable<T> iterable) {
        final List<T> list = new ArrayList<T>();
        iterable.forEach(item -> list.add(item));
        return list;
    }

    public String getReferencedWarning(final Long id) {
        final Country country = countryRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        final Teams toCountryTeams = teamsRepository.findFirstByToCountry(country);
        if (toCountryTeams != null) {
            return WebUtils.getMessage("country.teams.toCountry.referenced", toCountryTeams.getId());
        }
        final Players toCountryPlayersPlayers = playersRepository.findFirstByToCountryPlayers(country);
        if (toCountryPlayersPlayers != null) {
            return WebUtils.getMessage("country.players.toCountryPlayers.referenced", toCountryPlayersPlayers.getId());
        }
        final League countryLeagueLeague = leagueRepository.findFirstByCountryLeague(country);
        if (countryLeagueLeague != null) {
            return WebUtils.getMessage("country.league.countryLeague.referenced", countryLeagueLeague.getId());
        }
        return null;
    }

}
