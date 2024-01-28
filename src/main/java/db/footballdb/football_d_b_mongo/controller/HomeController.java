package db.footballdb.football_d_b_mongo.controller;

import db.footballdb.football_d_b_mongo.service.CompetitionsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import db.footballdb.football_d_b_mongo.service.PlayersService; // Eğer bu import henuz ekli değilse ekleyin

@Controller
public class HomeController {

    private final PlayersService playersService;
    private final CompetitionsService competitionsService;

    public HomeController(PlayersService playersService, CompetitionsService competitionsService) {
        this.playersService = playersService;
        this.competitionsService = competitionsService;
    }

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("playerses", playersService.findAll());
        model.addAttribute("competitionses", competitionsService.findAll());
        return "home/index";
    }

}