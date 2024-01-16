package db.footballdb.football_d_b_mongo.controller;

import db.footballdb.football_d_b_mongo.domain.Country;
import db.footballdb.football_d_b_mongo.domain.League;
import db.footballdb.football_d_b_mongo.domain.Teams;
import db.footballdb.football_d_b_mongo.model.LeagueDTO;
import db.footballdb.football_d_b_mongo.model.TeamsDTO;
import db.footballdb.football_d_b_mongo.repos.CountryRepository;
import db.footballdb.football_d_b_mongo.repos.LeagueRepository;
import db.footballdb.football_d_b_mongo.repos.TeamsRepository;
import db.footballdb.football_d_b_mongo.service.LeagueService;
import db.footballdb.football_d_b_mongo.service.TeamsService;
import db.footballdb.football_d_b_mongo.util.CustomCollectors;
import db.footballdb.football_d_b_mongo.util.WebUtils;
import jakarta.validation.Valid;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;


@Controller
@RequestMapping("/leagues")
public class LeagueController {

    private final LeagueService leagueService;
    private final TeamsService teamsService;
    private final CountryRepository countryRepository;
    private final TeamsRepository teamsRepository;

    private final LeagueRepository leagueRepository;

    public LeagueController(final LeagueService leagueService,
                            final CountryRepository countryRepository, final TeamsRepository teamsRepository, final TeamsService teamsService, LeagueRepository leagueRepository){
        this.leagueService = leagueService;
        this.countryRepository = countryRepository;
        this.teamsRepository = teamsRepository;
        this.teamsService =  teamsService;
        this.leagueRepository = leagueRepository;
    }

    @ModelAttribute
    public void prepareContext(final Model model) {
        model.addAttribute("countryLeagueValues", countryRepository.findAll(Sort.by("id"))
                .stream()
                .collect(CustomCollectors.toSortedMap(Country::getId, Country::getCName)));
    }

    @GetMapping
    public String list(final Model model) {
        model.addAttribute("leagues", leagueService.findAll());
        return "league/list";
    }
    // Lig-Takım eşleştirmesi burada

    @GetMapping("/teams/{id}")
    public String listTeams(@PathVariable(name = "id") final Long id, final Model model) {
        League league = leagueRepository.findById(id).get();
        List<Teams> teams = teamsRepository.findByLeaguesssId(id);
        List<TeamsDTO> list = teams.stream()
                .map(team -> {
                    TeamsDTO teamsDTO = teamsService.mapToDTO(team, new TeamsDTO());
                    teamsDTO.setLeagueAdi(team.getLeaguesss().getLeagueName());
                    return teamsDTO;
                })
                .toList();
        model.addAttribute("teams", list);
        model.addAttribute("ligAdi", league.getLeagueName());
        return "league/listele";
    }

    @GetMapping("/add")
    public String add(@ModelAttribute("league") final LeagueDTO leagueDTO) {
        return "league/add";
    }

    @PostMapping("/add")
    public String add(@ModelAttribute("league") @Valid final LeagueDTO leagueDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "league/add";
        }
        leagueService.create(leagueDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("league.create.success"));
        return "redirect:/leagues";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable(name = "id") final Long id, final Model model) {
        model.addAttribute("league", leagueService.get(id));
        return "league/edit";
    }

    @PostMapping("/edit/{id}")
    public String edit(@PathVariable(name = "id") final Long id,
            @ModelAttribute("league") @Valid final LeagueDTO leagueDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "league/edit";
        }
        leagueService.update(id, leagueDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("league.update.success"));
        return "redirect:/leagues";
    }


    @PostMapping("/delete/{id}")
    public String delete(@PathVariable(name = "id") final Long id,
            final RedirectAttributes redirectAttributes) {
        final String referencedWarning = leagueService.getReferencedWarning(id);
        if (referencedWarning != null) {
            redirectAttributes.addFlashAttribute(WebUtils.MSG_ERROR, referencedWarning);
        } else {
            leagueService.delete(id);
            redirectAttributes.addFlashAttribute(WebUtils.MSG_INFO, WebUtils.getMessage("league.delete.success"));
        }
        return "redirect:/leagues";
    }

}
