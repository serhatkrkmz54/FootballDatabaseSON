package db.footballdb.football_d_b_mongo.controller;

import db.footballdb.football_d_b_mongo.domain.Competitions;
import db.footballdb.football_d_b_mongo.domain.Teams;
import db.footballdb.football_d_b_mongo.model.*;
import db.footballdb.football_d_b_mongo.service.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.stream.Collectors;

@Controller
public class HomeController {

    private final PlayersService playersService;
    private final CompetitionsService competitionsService;
    private final TeamsService teamsService;
    private final LeagueService leagueService;
    private final CountryService countryService;

    public HomeController(PlayersService playersService, CompetitionsService competitionsService,
                          TeamsService teamsService, LeagueService leagueService, CountryService countryService) {
        this.playersService = playersService;
        this.competitionsService = competitionsService;
        this.teamsService= teamsService;
        this.leagueService = leagueService;
        this.countryService = countryService;
    }
    private <T> List<T> limitList(List<T> originalList, int limit) {
        return originalList.stream().limit(limit).collect(Collectors.toList());
    }
    @GetMapping("/")
    public String index(Model model) {
        //Anasayfada oyuncuları listele
        List<PlayersDTO> playersList = playersService.findAll();
        List<PlayersDTO> limitedPlayersList = limitList(playersList, 5);

        //Anasayfada takımları listele
        List<TeamsDTO> teamsListHome = teamsService.findAll();
        List<TeamsDTO> limitedTeamsList = limitList(teamsListHome, 5);

        //Anasayfada ligleri listele
        List<LeagueDTO> leagueListHome = leagueService.findAll();
        List<LeagueDTO> limitedLeagueList = limitList(leagueListHome, 5);

        //Anasayfada ülkeleri listele
        List<CountryDTO> countryListHome = countryService.findAll();
        List<CountryDTO> limitedCountryList = limitList(countryListHome, 5);

        //Anasayfada turnuvaları listele
        List<CompetitionsDTO> competitionsListHome = competitionsService.findAll();
        List<CompetitionsDTO> limitedCompetitionsList = limitList(competitionsListHome, 5);

        model.addAttribute("playerses", limitedPlayersList);
        model.addAttribute("teamses", limitedTeamsList);
        model.addAttribute("competitionses", limitedCompetitionsList);
        model.addAttribute("leagues", limitedLeagueList);
        model.addAttribute("countries", limitedCountryList);
        return "home/index";
    }

}