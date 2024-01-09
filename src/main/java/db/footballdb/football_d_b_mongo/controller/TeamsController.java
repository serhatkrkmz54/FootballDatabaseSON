package db.footballdb.football_d_b_mongo.controller;

import db.footballdb.football_d_b_mongo.domain.Competitions;
import db.footballdb.football_d_b_mongo.domain.Country;
import db.footballdb.football_d_b_mongo.domain.League;
import db.footballdb.football_d_b_mongo.model.TeamsDTO;
import db.footballdb.football_d_b_mongo.repos.CompetitionsRepository;
import db.footballdb.football_d_b_mongo.repos.CountryRepository;
import db.footballdb.football_d_b_mongo.repos.LeagueRepository;
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


@Controller
@RequestMapping("/teamss")
public class TeamsController {

    private final TeamsService teamsService;
    private final CountryRepository countryRepository;
    private final LeagueRepository leagueRepository;
    private final CompetitionsRepository competitionsRepository;

    public TeamsController(final TeamsService teamsService,
            final CountryRepository countryRepository, final LeagueRepository leagueRepository,
            final CompetitionsRepository competitionsRepository) {
        this.teamsService = teamsService;
        this.countryRepository = countryRepository;
        this.leagueRepository = leagueRepository;
        this.competitionsRepository = competitionsRepository;
    }

    @ModelAttribute
    public void prepareContext(final Model model) {
        model.addAttribute("toCountryValues", countryRepository.findAll(Sort.by("id"))
                .stream()
                .collect(CustomCollectors.toSortedMap(Country::getId, Country::getCName)));
        model.addAttribute("leaguesssValues", leagueRepository.findAll(Sort.by("id"))
                .stream()
                .collect(CustomCollectors.toSortedMap(League::getId, League::getLeagueName)));
        model.addAttribute("toTeamstoCompetitionsValues", competitionsRepository.findAll(Sort.by("id"))
                .stream()
                .collect(CustomCollectors.toSortedMap(Competitions::getId, Competitions::getCompetitionName)));
    }

    @GetMapping
    public String list(final Model model) {
        model.addAttribute("teamses", teamsService.findAll());
        return "teams/list";
    }

    @GetMapping("/add")
    public String add(@ModelAttribute("teams") final TeamsDTO teamsDTO) {
        return "teams/add";
    }

    @PostMapping("/add")
    public String add(@ModelAttribute("teams") @Valid final TeamsDTO teamsDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "teams/add";
        }
        teamsService.create(teamsDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("teams.create.success"));
        return "redirect:/teamss";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable(name = "id") final Long id, final Model model) {
        model.addAttribute("teams", teamsService.get(id));
        return "teams/edit";
    }

    @PostMapping("/edit/{id}")
    public String edit(@PathVariable(name = "id") final Long id,
            @ModelAttribute("teams") @Valid final TeamsDTO teamsDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "teams/edit";
        }
        teamsService.update(id, teamsDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("teams.update.success"));
        return "redirect:/teamss";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable(name = "id") final Long id,
            final RedirectAttributes redirectAttributes) {
        final String referencedWarning = teamsService.getReferencedWarning(id);
        if (referencedWarning != null) {
            redirectAttributes.addFlashAttribute(WebUtils.MSG_ERROR, referencedWarning);
        } else {
            teamsService.delete(id);
            redirectAttributes.addFlashAttribute(WebUtils.MSG_INFO, WebUtils.getMessage("teams.delete.success"));
        }
        return "redirect:/teamss";
    }

}
