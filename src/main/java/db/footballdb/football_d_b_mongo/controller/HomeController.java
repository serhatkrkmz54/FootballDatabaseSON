package db.footballdb.football_d_b_mongo.controller;

import com.mongodb.client.MongoDatabase;
import db.footballdb.football_d_b_mongo.domain.Competitions;
import db.footballdb.football_d_b_mongo.domain.Teams;
import db.footballdb.football_d_b_mongo.model.*;
import db.footballdb.football_d_b_mongo.service.*;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Controller
public class HomeController {

    private final PlayersService playersService;
    private final CompetitionsService competitionsService;
    private final TeamsService teamsService;
    private final LeagueService leagueService;
    private final CountryService countryService;
    private final MongoTemplate mongoTemplate;
    public HomeController(PlayersService playersService, CompetitionsService competitionsService,
                          TeamsService teamsService, LeagueService leagueService, CountryService countryService,
                          MongoTemplate mongoTemplate) {
        this.playersService = playersService;
        this.competitionsService = competitionsService;
        this.teamsService= teamsService;
        this.leagueService = leagueService;
        this.countryService = countryService;
        this.mongoTemplate = mongoTemplate;
    }
    private <T> List<T> limitList(List<T> originalList, int limit) {
        return originalList.stream().limit(limit).collect(Collectors.toList());
    }


    @GetMapping("/")
    public String index(Model model) {
        //Günlük girilen veri miktarını saydıran kod
        OffsetDateTime today = OffsetDateTime.now();
        MongoDatabase database = mongoTemplate.getDb(); // mongoTemplate, MongoTemplate türünde bir nesne olmalıdır.
        Set<String> collectionNames = database.listCollectionNames().into(new HashSet<>());
        long totalDocumentsCreatedToday = 0;
        for (String collectionName : collectionNames) {
            Query todayQuery = new Query(Criteria.where("dateCreated")
                    .gte(today.truncatedTo(ChronoUnit.DAYS))
                    .lt(today.plusDays(1).truncatedTo(ChronoUnit.DAYS)));

            long documentsCreatedToday = mongoTemplate.count(todayQuery, collectionName);
            totalDocumentsCreatedToday += documentsCreatedToday;
        }

        // MongoDB'deki tüm koleksiyon adlarını al
        Iterable<String> tumKoleksiyonlar = mongoTemplate.getCollectionNames();

        // primarySequence koleksiyonunu hariç tut - otomatik son id tutma koleksiyonu
        Iterable<String> koleksiyonFiltrele = StreamSupport.stream(tumKoleksiyonlar.spliterator(), false)
                .filter(collectionName -> !collectionName.equals("primarySequence"))
                .collect(Collectors.toList());

        // Her koleksiyon için belge sayısını hesapla
        Map<String, Long> collectionDocumentCounts = StreamSupport.stream(koleksiyonFiltrele.spliterator(), false)
                .collect(Collectors.toMap(
                        collectionName -> collectionName,
                        collectionName -> mongoTemplate.count(new Query(), collectionName)
                ));
        // Toplam koleksiyon sayısını hesapla
        long totalDocumentCount = collectionDocumentCounts.values().stream().mapToLong(Long::longValue).sum();

        //Anasayfada oyuncuları listele
        List<PlayersDTO> playersList = playersService.findAll();
        List<PlayersDTO> limitedPlayersList = limitList(playersList, 5);
        long totalPlayers = playersList.size();
        //Anasayfada takımları listele
        List<TeamsDTO> teamsListHome = teamsService.findAll();
        List<TeamsDTO> limitedTeamsList = limitList(teamsListHome, 5);

        //Anasayfada ligleri listele
        List<LeagueDTO> leagueListHome = leagueService.findAll();
        List<LeagueDTO> limitedLeagueList = limitList(leagueListHome, 5);
        long totalLeagues = leagueListHome.size();

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
        model.addAttribute("totalDocumentCount", totalDocumentCount);
        model.addAttribute("totalLeagues", totalLeagues);
        model.addAttribute("totalPlayers", totalPlayers);
        model.addAttribute("totalDocumentsCreatedToday", totalDocumentsCreatedToday);
        model.addAttribute("today", today);
        return "home/index";
    }
}