package db.footballdb.football_d_b_mongo.controller;

import db.footballdb.football_d_b_mongo.domain.*;
import db.footballdb.football_d_b_mongo.model.PlayersDTO;
import db.footballdb.football_d_b_mongo.model.TeamsDTO;
import db.footballdb.football_d_b_mongo.repos.*;
import db.footballdb.football_d_b_mongo.service.PlayersService;
import db.footballdb.football_d_b_mongo.service.TeamsService;
import db.footballdb.football_d_b_mongo.util.CustomCollectors;
import db.footballdb.football_d_b_mongo.util.WebUtils;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;


@Controller
@RequestMapping("/teams")
public class TeamsController {

    private final TeamsService teamsService;
    private final CountryRepository countryRepository;
    private final LeagueRepository leagueRepository;
    private final CompetitionsRepository competitionsRepository;
    private final TeamsRepository teamsRepository;
    private final PlayersRepository playersRepository;
    private final PlayersService playersService;

    public TeamsController(final TeamsService teamsService,
            final CountryRepository countryRepository, final LeagueRepository leagueRepository,
            final CompetitionsRepository competitionsRepository,final TeamsRepository teamsRepository,final PlayersRepository playersRepository, final PlayersService playersService) {
        this.teamsService = teamsService;
        this.countryRepository = countryRepository;
        this.leagueRepository = leagueRepository;
        this.competitionsRepository = competitionsRepository;
        this.teamsRepository = teamsRepository;
        this.playersRepository = playersRepository;
        this.playersService = playersService;
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
    public String list(final Model model, @Param("keyword") String keyword) {
        List<TeamsDTO> teamses = teamsService.getByKeyword(keyword);
        model.addAttribute("teamses", teamses);
        model.addAttribute("keyword", keyword);
        return getOnePage(model, 1);
    }

    @GetMapping("/list/page/{pageNumber}")
    public String getOnePage(final Model model,@PathVariable("pageNumber") int currentPage) {
        Page<TeamsDTO> page = teamsService.findPage(currentPage);
        int totalPages = page.getTotalPages();
        long totalItems = page.getTotalElements();
        List<TeamsDTO> teamses = page.getContent();
        if (model.getAttribute("keyword") != null) {
            teamses = (List<TeamsDTO>) model.getAttribute("teamses");
            if (teamses.size() < 10) {
                totalPages = 1;
            } else {
                totalPages = (int) Math.ceil((double) teamses.size() / 10);
            }
            totalItems = teamses.size();
        }
        BigDecimal toplamDeger = teamsService.getToplamTakimDegeri();
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("totalItems", totalItems);
        model.addAttribute("teamses", teamses);
        model.addAttribute("toplamDeger", toplamDeger);
        return "teams/list";
    }

    @GetMapping("/add")
    public String add(@ModelAttribute("teams") final TeamsDTO teamsDTO) {
        return "teams/add";
    }

    @PostMapping("/add")
    public String add(@ModelAttribute("teams") @Valid final TeamsDTO teamsDTO,
                      final BindingResult bindingResult, final RedirectAttributes redirectAttributes) throws IOException {
        if (bindingResult.hasErrors()) {
            return "teams/add";
        }
        teamsService.create(teamsDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("teams.create.success"));
        return "redirect:/teams";
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
        redirectAttributes.addFlashAttribute(WebUtils.MSG_INFO, WebUtils.getMessage("teams.update.success"));
        return "redirect:/teams";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable(name = "id") final Long id,
            final RedirectAttributes redirectAttributes) {
        final String referencedWarning = teamsService.getReferencedWarning(id);
        if (referencedWarning != null) {
            redirectAttributes.addFlashAttribute(WebUtils.MSG_ERROR, referencedWarning);
        } else {
            teamsService.delete(id);
            redirectAttributes.addFlashAttribute(WebUtils.MSG_ERROR, WebUtils.getMessage("teams.delete.success"));
        }
        return "redirect:/teams";
    }

}
